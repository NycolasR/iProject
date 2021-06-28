package ponto.model.projetos;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import facades.casosdeuso.FacadeCasoDeUso2;
import model.autenticacao.Membro;
import model.autenticacao.TipoProvedorAutenticacao;
import model.projetos.Participacao;
import model.projetos.Projeto;
import persistencia.DAOXMLMembroConta;
import persistencia.DAOXMLProjetoParticipacao;
import ponto.model.projetos.etapastratadoras.SimpleFactoryEtapasTratadoras;
import ponto.model.projetos.etapastratadoras.TratadorEtapa;
import ponto.model.projetos.etapastratadoras.TratadorEtapaPontoInvalido;
import util.ValidadoraDatas;
import util.ValidadoraMembros;

/**
 * 
 * @author NPG
 * Classe de objeto remoto na implementacao do padrao PROXY.
 * Possui os metodos que devem poder ser acessados remotamente pela implementacao da interface ProxyInterface.
 * Extende UnicastRemoteObject para poder ser acessivel pela rede pelo uso do pacote java.rmi. 
 */
public class RegistradorPontoCentral extends UnicastRemoteObject implements ProxyInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DAOXMLMembroConta daoxmlMembroConta = new DAOXMLMembroConta();
	
	
	public RegistradorPontoCentral() throws RemoteException{
		
	}
	
	/**
	 * O metodo a abixo registra um ponto trabalhado em um projeto que será encontrado atraves do codigo passado
	 * como parametro, e o login para se encontrar o membro que irá registrar esse ponto
	 */
	public boolean registrarPonto(long codProjeto,String login) throws Exception{
		
		DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		
		Projeto projeto = daoxmlProjetoParticipacao.consultarPorID(codProjeto);

		Membro membroDoRegistrador = daoxmlMembroConta.getMembroPeloLogin(login);
		
		Membro membro = ValidadoraMembros.validarExistenciaDeMembro(projeto.getMembros(), membroDoRegistrador);
		
		if(membro == null) {
			throw new Exception("Membro não faz parte desse projeto");
		}
		
		LocalDateTime dataHoraAtualParaMarcar = LocalDateTime.now();
		
		for(int i = 0; i < projeto.getParticipacoes().size(); i++) {
			
		
			Participacao participacao = (Participacao) projeto.getParticipacoes().get(i);
			
			if(participacao.getMembro().getMatricula() == membro.getMatricula() && participacao.isAtivo()) {
				
				ArrayList<PontoTrabalhado> pontosTrabalhados = participacao.getPontosTrabalhados();

				PontoTrabalhado pontoTrabalhado = null;
				
				for(int j = 0; j < pontosTrabalhados.size(); j++) {
					
					if(ValidadoraDatas.dataIsVazia(pontosTrabalhados.get(j).getDataHoraSaida()) && 
						ValidadoraDatas.dataEntradaisIgualDataSaidaDePontoTrabalhado(pontosTrabalhados.get(j).getDataHoraEntrada(), dataHoraAtualParaMarcar)) {
						
						 pontoTrabalhado = pontosTrabalhados.get(j);
					}
				}
				
				if(pontoTrabalhado == null) {
					
					pontoTrabalhado = new PontoTrabalhado();
					pontoTrabalhado.setDataHoraEntrada(dataHoraAtualParaMarcar);
					participacao.addPontoTrabalhado(pontoTrabalhado);
					
					
				}else {
					
					pontoTrabalhado.setDataHoraSaida(dataHoraAtualParaMarcar);
					
				}
				daoxmlMembroConta.atualizar(membro.getMatricula(), membro);
				daoxmlProjetoParticipacao.atualizar(codProjeto, projeto);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * O metodo a abaixo busca os projetos em que o membo, que será encontrado atraves do login passado por parametro,
	 * esta participando e que estao ativos
	 */
	public Set<Projeto> getProjetosAtivos(String login) throws Exception { 
		
		Membro membro = daoxmlMembroConta.getMembroPeloLogin(login);
		
		Set<Projeto> projetosAtivos = new LinkedHashSet<Projeto>();
		
		DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		
		Iterator<Projeto> projetos = daoxmlProjetoParticipacao.getTodosRegistros().iterator();
				
		while(projetos.hasNext()) {
			
			Projeto projeto = projetos.next();
			ArrayList<Membro> membros = projeto.getMembros();
			
			for(int i = 0; i < membros.size(); i++) {
				
				if(membros.get(i).getMatricula() == membro.getMatricula() && projeto.isAtivo()) {
					
					projetosAtivos.add(projeto);
				}
			}
		}
		return projetosAtivos;
	}

	/**
	 * O metodo a abaixo retorna as horas trabalhadas de um membro em determinado projeto
	 */
	public float horasTrabalhadasValidas(LocalDateTime dataInicio , LocalDateTime dataTermino ,String login,long codProjeto) throws Exception{
		
		Membro membro = daoxmlMembroConta.getMembroPeloLogin(login);
		
		float quantidadeDeHorasTrabalhadas = pegarQuantHorasTrabalhadasEQuantPontosTrabalhados(dataInicio, dataTermino,membro,codProjeto, true)[0];
		
		return quantidadeDeHorasTrabalhadas;
	}
	
	/**
	 * O metodo a abixo retorna as quntidade de horas que faltaram para um membro
	 * pagar em um determinado projeto
	 */
	public float deficitHoras(LocalDateTime dataInicio , LocalDateTime dataTermino , String login, long codProjeto) throws Exception{
		
		Membro membro = daoxmlMembroConta.getMembroPeloLogin(login);
		
		float[] quantHorasEPontos = pegarQuantHorasTrabalhadasEQuantPontosTrabalhados(dataInicio, dataTermino,membro, codProjeto, false);

		return (quantHorasEPontos[1]*4) - quantHorasEPontos[0];
		
	}
	
	/**
	 * O metodo a baixo retorna os pontos invalidos de um membro em um determinado 
	 * projeto
	 */
	public Set<PontoTrabalhado> getPontosInvalidos(String login,ArrayList<TratadorEtapa> etapas, long codProjeto) throws Exception{
		
		Membro membro = daoxmlMembroConta.getMembroPeloLogin(login);
				
		return pegarPontosValidoOuInvalidosComLoginEEtapas(membro, etapas, false, codProjeto);
	}
	
	/**
	 * O metodo a baixo justifica os pontos invalidos de um membro em
	 * determinado projeto
	 */
	public void justificarPontoInvalido(long idPonto, String justificativa, String login,long codProjeto,ArrayList<TratadorEtapa> etapas) throws Exception {
		
		
		Set<PontoTrabalhado> pontosTrabalhadosInvalidos = getPontosInvalidos(login, etapas,codProjeto);
		
		Iterator<PontoTrabalhado> pontosTrabalhados = pontosTrabalhadosInvalidos.iterator();
		
		PontoTrabalhado pontoTrabalhado = null;
		
		while(pontosTrabalhados.hasNext()) {
			
			PontoTrabalhado pontoTrabalhadoIterado = pontosTrabalhados.next();
			
			if(pontoTrabalhadoIterado.getId() == idPonto) {
				pontoTrabalhado = pontoTrabalhadoIterado;
			}
		}
		
		if(pontoTrabalhado == null) {
			
			throw new Exception("Nenhum Ponto Trabalhado Encontrado");
			
		}else {
			
			pontoTrabalhado.setJustificativa(justificativa);
		}
		
	}
	
	/**
	 * O metodo a abaixo justifica os pontos nao batidos de um 
	 * membro em determinado projeto
	 */
	public void justificarPontoNaoBatido(long idPonto, String justificativa, String login,long codProjeto)throws Exception {
		
		ArrayList<TratadorEtapa> etapas = new ArrayList<TratadorEtapa>();

		etapas.add(TratadorEtapa.TratarPontosSemHorarioEntradaOuSaida);
		
		justificarPontoInvalido( idPonto, justificativa, login,codProjeto, etapas);
		
		
	}
	
	/**
	 * O metodo a baixo realiza o login no sistema atraves do login e senha de determinado membro
	 */
	public boolean fazerLogin(String email,String senha,boolean tipoProvedorInterno) throws Exception {
		
		FacadeCasoDeUso2 casoDeUso2 = new FacadeCasoDeUso2();
		
		TipoProvedorAutenticacao tipoProvedor = TipoProvedorAutenticacao.INTERNO;
		
		if(!tipoProvedorInterno) {
			
			tipoProvedor = TipoProvedorAutenticacao.EMAIL_SMTP;
		}
		
		return casoDeUso2.fazerLogin(email, senha, tipoProvedor);
	}
	
	/**
	 * O metodo a abaixo retorna todos os projetos do sistemas, estando eles ativos ou nao
	 */
	public Projeto[] getTodosOsProjetos() {
		
		DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		Set<Projeto> projetosRegistrados = daoxmlProjetoParticipacao.getTodosRegistros();
		return projetosRegistrados.toArray(new Projeto[projetosRegistrados.size()]);
	}
	
	/**
	 * Esse metodo pega os detalhes para serem mostrados no JFrame
	 */
	public StringBuffer pegarDetalhes(LocalDateTime dataAgora ,String login,long codProjeto) throws Exception{
		
		StringBuffer detalhes = new StringBuffer();
		
		detalhes.append("Horas trabalhadas válidas: ");
		detalhes.append(horasTrabalhadasValidas(dataAgora.plusDays(-30), dataAgora, login, codProjeto)+"\n");
		
		detalhes.append("Déficit de horas: ");
		detalhes.append(deficitHoras(dataAgora.plusDays(-30), dataAgora, login, codProjeto)+"\n");
		
		detalhes.append("PONTOS INVÁLIDOS:\n");
		
		ArrayList<TratadorEtapa> etapas = new ArrayList<TratadorEtapa>();
		
		etapas.add(TratadorEtapa.TratarPontosSemHorarioEntradaOuSaida);
		etapas.add(TratadorEtapa.TratarPontoBatidoForaDaPrevisaoDeParticipacaoToleranciaMinutos);
		etapas.add(TratadorEtapa.TratarPontoIntervaloConflitantes);
		
		Iterator<PontoTrabalhado> pontos = getPontosInvalidos(login, etapas, codProjeto).iterator();
		
		
		while(pontos.hasNext()) {
			
			detalhes.append("> "+pontos.next().getId()+"\n");
		}
		
		return detalhes;
	}
	

	public void teste() throws RemoteException {
		
	}
	
	/**
	 *Esse método pega os pontos válidos ou inválidos (fica a escolha do cliente)
	 *que ele trabalhou em um determinado projeto 
	 *
	 */
	private Set<PontoTrabalhado> pegarPontosValidoOuInvalidosComLoginEEtapas(Membro membro,ArrayList<TratadorEtapa> etapas, boolean isValidos, long codProjeto) throws Exception{
		
		DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		Projeto projeto = daoxmlProjetoParticipacao.consultarPorID(codProjeto);
		
		Membro membroRetornado = ValidadoraMembros.validarExistenciaDeMembro(projeto.getMembros(), membro);

		if(membroRetornado == null) {
			throw new Exception("Membro não pertence a esse edital");
		}
		
		Set<PontoTrabalhado> pontosTrabalhadosInvalidos = new LinkedHashSet<PontoTrabalhado>();
		
		
		for(int i = 0; i < projeto.getParticipacoes().size(); i++) {
			
			Participacao participacao = (Participacao) projeto.getParticipacoes().get(i);
			
			
			if(participacao.getMembro().getMatricula() == membroRetornado.getMatricula()) {
				
				ArrayList<PontoTrabalhado> pontosTrabalhados = participacao.getPontosTrabalhados();
				
				
				for(int j = 0; j < pontosTrabalhados.size(); j++) {
					
					PontoTrabalhado pontoTrabalhado = pontosTrabalhados.get(j);
					
					TratadorEtapaPontoInvalido tratador = SimpleFactoryEtapasTratadoras.criarEtapas(etapas);
					
					if(isValidos) {
						
						if(tratador.tratarPontosInvalidos(pontoTrabalhado, participacao.getHorariosPrevistos(), participacao.getPontosTrabalhados())) {
							
							
							pontosTrabalhadosInvalidos.add(pontoTrabalhado);
						}
						
					} else {
						
					
						if(!tratador.tratarPontosInvalidos(pontoTrabalhado, participacao.getHorariosPrevistos(), participacao.getPontosTrabalhados())) {
							
							pontosTrabalhadosInvalidos.add(pontoTrabalhado);
						}
					}
				}
			}
			
			
		}
		return pontosTrabalhadosInvalidos;
		
	}
	
	/**
	 * Esse método calcula as horas trabalhadas dos pontos válidos ou do pontos inválidos
	 * e retorna a quanidade de pontos que foram calculadas nessas horas, e tambem
	 * a propria quantidade de horas
	 */
	private float[] pegarQuantHorasTrabalhadasEQuantPontosTrabalhados(LocalDateTime dataInicio , LocalDateTime dataTermino, 
																		Membro membro, long codProjeto, boolean isValidos) throws Exception{
		
		float[] quantHorasTrabalhadasEQuantPontosTrabalhados = new float[2];
		
		ArrayList<TratadorEtapa> etapas = new ArrayList<TratadorEtapa>();

		etapas.add(TratadorEtapa.TratarPontosSemHorarioEntradaOuSaida);
		etapas.add(TratadorEtapa.TratarPontoBatidoForaDaPrevisaoDeParticipacaoToleranciaMinutos);
		etapas.add(TratadorEtapa.TratarPontoIntervaloConflitantes);

		float quantidadeDePontos = 0;
		float quantidadeDeHorasTrabalhadas = 0;
		
		Iterator<PontoTrabalhado> pontosTrabalhados = pegarPontosValidoOuInvalidosComLoginEEtapas(membro, etapas, isValidos,codProjeto).iterator();	
		
		
		while(pontosTrabalhados.hasNext()) {
			
			PontoTrabalhado pontoTrabalhado = pontosTrabalhados.next();
			
			if(pontoTrabalhado.getDataHoraSaida() != null) {
				
				if(!pontoTrabalhado.getDataHoraEntrada().isBefore(dataInicio) && !pontoTrabalhado.getDataHoraSaida().isAfter(dataTermino)) {
					
					quantidadeDeHorasTrabalhadas += pontoTrabalhado.getHorasTrabalhadas();
					quantidadeDePontos += 1;
				}
			}
			
		}
		
		
		quantHorasTrabalhadasEQuantPontosTrabalhados[0] = quantidadeDeHorasTrabalhadas;
		quantHorasTrabalhadasEQuantPontosTrabalhados[1] = quantidadeDePontos;
		
		return quantHorasTrabalhadasEQuantPontosTrabalhados;
	}


	
	
}
