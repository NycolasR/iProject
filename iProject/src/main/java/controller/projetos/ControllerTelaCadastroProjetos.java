package controller.projetos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import facades.casosdeuso.FacadeCasoDeUso4;
import facades.casosdeuso.FacadeCasoDeUso5;
import facades.casosdeuso.FacadeCasoDeUso6;
import model.autenticacao.Membro;
import model.autenticacao.RegistradorSessaoLogin;
import model.projetos.Edital;
import model.projetos.Projeto;
import util.ValidadoraDatas;

/**
 * 
 * @author NPG
 *
 *Essa classe é o controller como realiza operações do JPanel Tela cadastro Projetos
 */
public class ControllerTelaCadastroProjetos {

	private FacadeCasoDeUso4 facadeCasoDeUso4;
	private FacadeCasoDeUso5 facadeCasoDeUso5;
	private FacadeCasoDeUso6 facadeCasoDeUso6;
	private Membro[] membros;
	private Projeto[] projetos;
	private Edital[] editais;
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	private long matriculaDoMembro = registradorSessaoLogin.getMembroLogado().getMatricula();
	private long codigoProjeto;
	
	
	public ControllerTelaCadastroProjetos() {
		
		facadeCasoDeUso5 = new FacadeCasoDeUso5();
		facadeCasoDeUso6 = new FacadeCasoDeUso6();
		facadeCasoDeUso4 = new FacadeCasoDeUso4();
	}
	
	public void adicionarNovoProjeto(String nomeProjeto,String aporteCusteioReaisProjeto,
			String aporteCapitalReaisProjeto,String dataInicioProjeto,String dataTerminoProjeto,String aporteCusteioReaisMensalDoCoordenador) throws Exception{

		if( nomeProjeto.isEmpty() || dataInicioProjeto.equals("  /  /    ") || aporteCusteioReaisMensalDoCoordenador.isEmpty() ||
				dataTerminoProjeto.equals("  /  /    ") || aporteCusteioReaisProjeto.isEmpty() || aporteCapitalReaisProjeto.isEmpty()) {
			
			throw new Exception("Preencha todos os campos");
		}
		
		validarSeENumero(aporteCusteioReaisProjeto);
		validarSeENumero(aporteCapitalReaisProjeto);
		validarSeENumero(aporteCusteioReaisMensalDoCoordenador);
		
		LocalDate dataInicio = ValidadoraDatas.criarData(dataInicioProjeto);
		LocalDate dataTermino = ValidadoraDatas.criarData(dataTerminoProjeto);
		codigoProjeto = System.currentTimeMillis() % 1000000000;
		float aporteCusteioReais = Float.parseFloat(aporteCusteioReaisProjeto);
		float aporteCapitalReais = Float.parseFloat(aporteCapitalReaisProjeto);
		float aporteCusteioReaisMensal = Float.parseFloat(aporteCusteioReaisMensalDoCoordenador);
		
		facadeCasoDeUso5.criarProjeto(matriculaDoMembro, codigoProjeto, nomeProjeto, aporteCusteioReais, 
				aporteCapitalReais, dataInicio, dataTermino, aporteCusteioReaisMensal);
		
	}
	
	public void atualizarProjetoExistente(String nomeProjeto,String aporteCusteioReaisProjeto,String aporteCapitalReaisProjeto, int posicao) throws Exception{
		
		if(posicao == -1) {
			throw new Exception("Selecione um projeto para atualizá-lo");
		}else if(projetos[posicao].getCoordenador().getMatricula() != matriculaDoMembro) {
			
			throw new Exception("Você não tem permissão de atualizar esse projeto");
		}
		
		Projeto projeto = projetos[posicao];
		
		validarSeENumero(aporteCusteioReaisProjeto);
		validarSeENumero(aporteCapitalReaisProjeto);
		
		long codigoProjeto = projeto.getCodigo();
		float aporteCusteioReais = projeto.getAporteCusteioReais();
		float aporteCapitalReais = projeto.getAporteCapitalReais();
		
		if(!aporteCusteioReaisProjeto.isEmpty())
			
			aporteCusteioReais = Float.parseFloat(aporteCusteioReaisProjeto);
		
		if(!aporteCapitalReaisProjeto.isEmpty())
			
			aporteCapitalReais = Float.parseFloat(aporteCapitalReaisProjeto);
		
		facadeCasoDeUso5.atualizarProjeto(codigoProjeto, nomeProjeto, aporteCusteioReais, aporteCapitalReais);
		
	}
	
	public void removerProjetoExistente(int posicao) throws Exception{
		
		if(posicao == -1) {
			
			throw new Exception("Selecione um projeto para removê-lo");
		
		}else if(projetos[posicao].getCoordenador().getMatricula() != matriculaDoMembro) {
			
			throw new Exception("Você não tem permissão de remover esse projeto");
		}
		
		Projeto projeto = projetos[posicao];
				
		facadeCasoDeUso5.removerProjeto(projeto.getCodigo());
	}
	
	public void adicionarMembro( int posicaoMembro, int posicaoProjeto, String dataInicioParticipacao,
			String dataTerminoParticipacao, String aporteCusteioDaParticipacao) throws Exception{
		
		if(posicaoProjeto == -1 || posicaoMembro == -1 ) {
			
			throw new Exception("Escolha um projeto e escolha um membro");
			
		}else if(projetos[posicaoProjeto].getCoordenador().getMatricula() != matriculaDoMembro) {
			
			throw new Exception("Você não tem permissão de adicionar um membro nesse projeto");
			
		}else if( dataInicioParticipacao.equals("  /  /    ") || dataTerminoParticipacao.equals("  /  /    ") || aporteCusteioDaParticipacao.isEmpty()) {
			
			throw new Exception("informe a data de início e término da participação do membro e quanto irá ganhar por mês");
		}
		
		validarSeENumero(aporteCusteioDaParticipacao);
				
		long matriculaCandidato = membros[posicaoMembro].getMatricula();
		long codigoProjeto = projetos[posicaoProjeto].getCodigo();
		LocalDate dataInicio = ValidadoraDatas.criarData(dataInicioParticipacao);
		LocalDate dataTermino = ValidadoraDatas.criarData(dataTerminoParticipacao);
		float aporteCusteioReaisMensal = Float.parseFloat(aporteCusteioDaParticipacao);
		
		if(projetos[posicaoProjeto].getAporteCusteioReais() < (aporteCusteioReaisMensal*ChronoUnit.MONTHS.between(dataInicio, dataTermino))) {
			
			throw new Exception("Mensalidade excede o valor de custeio");
		}
		
		facadeCasoDeUso6.adicionarParticipante(matriculaDoMembro, matriculaCandidato, codigoProjeto, dataInicio, dataTermino, aporteCusteioReaisMensal);
	}
	
	public void removerMembro( int posicaoMembro, int posicaoProjeto) throws Exception{
		
		if(posicaoProjeto == -1 || posicaoMembro == -1 ) {
			
			throw new Exception("Escolha um projeto e escolha um membro");
			
		}else if(projetos[posicaoProjeto].getCoordenador().getMatricula() != matriculaDoMembro) {
			
			throw new Exception("Você não tem permissão de remover um membro desse projeto");
		}
		
		long matriculaCandidatoRemovivel = membros[posicaoMembro].getMatricula();
		long codigoProjeto = projetos[posicaoProjeto].getCodigo();
		
		facadeCasoDeUso6.removerParticipante(matriculaDoMembro, matriculaCandidatoRemovivel, codigoProjeto);
		
	}
	
	public String[] getCamposParaPreencher(int posicao) throws Exception{
		
		/*
		 * Esse método retorna os valores de um projeto para seus respectivos campos, onde...
		 * posicao 0 = nome do projeto
		 * posicao 1 = aporte custeio
		 * posicao 2 = aporte capital
		 */
		if(posicao != -1) {
			
			Projeto projeto = projetos[posicao];
			
			String[] campos = new String[5];
			
			LocalDate dataInicioProjeto = projeto.getParticipacoes().get(0).getDataInicio();
			LocalDate dataTerminoProjeto = projeto.getParticipacoes().get(0).getDataTermino();
			
			campos[0] = String.valueOf(ValidadoraDatas.transformarDataEmString(dataInicioProjeto));
			campos[1] = String.valueOf(ValidadoraDatas.transformarDataEmString(dataTerminoProjeto));
			campos[2] = String.valueOf(projeto.getNome());
			campos[3] = String.valueOf(projeto.getAporteCusteioReais());
			campos[4] = String.valueOf(projeto.getAporteCapitalReais());
			
			return campos;
			
		}
		
		return  null ;
	}
	
	public void adicionarProjetoAUmEdital(int posicaoDoProjeto, long codigoEdital) throws Exception{
		
		if(posicaoDoProjeto == -1 ) {
			
			throw new Exception("Escolha um projeto");
			
		}else if(projetos[posicaoDoProjeto].getCoordenador().getMatricula() != matriculaDoMembro) {
			
			throw new Exception("Você não tem permissão de adicionar esse projeto a um edital");
		}
		
		int posicaoEdital = 0;
		
		for(int i = 0; i < editais.length; i++) {
			
			if(editais[i].getCodigo() == codigoEdital) {
				
				posicaoEdital = i;
				break;
			}
		}
		
		
		facadeCasoDeUso4.adicionarProjetoAUmEdital(editais[posicaoEdital], projetos[posicaoDoProjeto]);

	}
	
	public int getPosicaoDoUltimoProjetoAdicionado() {
		
		Projeto[] projetosDoSistema = getTodoOsProjetosDoSistema();
		
		for(int i = 0; i < projetosDoSistema.length; i++) {
			
			if(projetosDoSistema[i].getCodigo() == codigoProjeto) {
				
				return i;
			}
			
		}
		
		return 0;
	}
	
		
	public Projeto[] getTodoOsProjetosDoSistema() {
		
		projetos = facadeCasoDeUso5.getTodosOsProjetos();
		return projetos;
	}
	
	public Membro[] getTodosOsMembros() {
		
		membros = facadeCasoDeUso6.pegarTodosOsMembros();
		return membros;
	}
	
	public Edital[] getTodosOsEditaisDoSistema() {
		
		editais = facadeCasoDeUso4.getTodosOsEditais();
		return editais;
	}
	
	public boolean isCoordenadorDeAlgumProjeto() {
		
		Membro membro = registradorSessaoLogin.getMembroLogado();
		
		for(int i = 0; i < membro.getParticipacoes().size(); i++) {
			
			if(membro.getParticipacoes().get(i).isCoordenador()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean possuiEdital(int posicaoDoProjeto) throws Exception{
		
		if(posicaoDoProjeto == -1 ) {
			
			throw new Exception("Escolha um projeto");
			
		}else if(projetos[posicaoDoProjeto].getCoordenador().getMatricula() == matriculaDoMembro) {
			
			if(projetos[posicaoDoProjeto].getEdital() == null) {
				
				return false;
			}
		}
		
		return true;
		
	}
	
	private void validarSeENumero(String valor) throws Exception {
		if(!valor.matches("^[0-9]*[.]{0,1}[0-9]*$")) {
			throw new Exception("Valores incorretos");
		}
	}
}
