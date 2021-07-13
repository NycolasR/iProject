package facades.casosdeuso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import model.autenticacao.Membro;
import model.projetos.ItemProjeto;
import model.projetos.Projeto;
import persistencia.DAOXMLMembroConta;
import persistencia.DAOXMLProjetoParticipacao;
import util.CentralAtualizacaoItemProjeto;
import util.MyLogger;
import util.ValidadoraItemProjeto;

/**
 * @author NPG
 *
 *  A implementacao do padrao facade facilita aos usuÃ¡rios o uso de funcoes que exigiriam um esforco e conhecimento desnecessario sobre os mÃ©todos
 * 	disponibilizados no programa. Para o caso de uso 1, Ã© simplificado a relacao de criar um projeto, remover um projeto, e atualizar suas informacoes de forma simples e direta,
 * 	sem a necessidade de um conhecimento previo ou amplo sobre os metodos e eliminando a necessidade de ter contado direto com todos os objetos envolvidos.
 *
 */
public class FacadeCasoDeUso5 {
	
//	Um membro pode criar, remover e atualizar dados de seus projetos, sendo ele
//	o criador, automaticamente o sistema o coloca como o coordenador do projeto.
	
	private DAOXMLProjetoParticipacao daoxmlProjetoParticipacao;
	private DAOXMLMembroConta daoxmlMembroConta;
	private Logger logger = MyLogger.getInstance();
	
	public FacadeCasoDeUso5() {
		// Só inicializa a instância quando o objeto for criado
		daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		daoxmlMembroConta = new DAOXMLMembroConta();
	}
	
	// Método testado
	public void criarProjeto(
			long matriculaMembro,
			long codigoProjeto,
			String nomeProjeto,
			float aporteCusteioReais,
			float aporteCapitalReais,
			LocalDate dataInicio,
			LocalDate dataTermino,
			float aporteCusteioReaisMensal) throws Exception{
		Projeto projeto = instanciarProjeto(nomeProjeto, aporteCusteioReais, aporteCapitalReais, codigoProjeto);
		
		Membro membro = null;
		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matriculaMembro);
			projeto.adicionarMembro(membro, dataInicio, dataTermino, true, aporteCusteioReaisMensal);
			
			if(daoxmlProjetoParticipacao.criar(projeto)) {
				logger.info("Projeto: " + projeto.getNome() + " criado com sucesso.");
				daoxmlMembroConta.atualizar(matriculaMembro, membro);
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	// Método testado
	public void removerProjeto(long codigo) throws Exception{
		try {
			Projeto projeto = daoxmlProjetoParticipacao.consultarPorID(codigo);
			
			ArrayList<Membro> membros = projeto.getMembros();
			
			for (int i = 0; i < membros.size(); i++) {
				Membro membro = membros.get(i);
				
				projeto.removerMembro(membro);
				daoxmlMembroConta.atualizar(membro.getMatricula(), membro);
			}
			
			if(daoxmlProjetoParticipacao.remover(codigo))
				logger.info("Projeto: " + projeto.getNome() + " removido com sucesso.");
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	// Método testado
	public void atualizarProjeto(
			long codigoProjeto,
			String nomeProjeto,
			float aporteCusteioReais,
			float aporteCapitalReais) throws Exception{
		
		Projeto projeto = null;
		try {
			projeto = daoxmlProjetoParticipacao.consultarPorID(codigoProjeto);
			
			ArrayList<ItemProjeto> projetos = new ArrayList<ItemProjeto>(daoxmlProjetoParticipacao.getTodosRegistros());
			
			if(ValidadoraItemProjeto.validarExistenciaDeItemProjeto(projetos, nomeProjeto) != null) {
				
				throw new Exception("Projeto já existente com esse nome");
			}
			
			
			setarAtributos(projeto, nomeProjeto, aporteCusteioReais, aporteCapitalReais, codigoProjeto);
			
			CentralAtualizacaoItemProjeto.atualizarProjeto(codigoProjeto, projeto);
			logger.info("Projeto: " + projeto.getNome() + " atualizado com sucesso.");
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	// Método utilitário
	private Projeto instanciarProjeto(
			String nomeProjeto,
			float aporteCusteioReais,
			float aporteCapitalReais,
			long codigoProjeto) {
		
		Projeto projeto = new Projeto();
		setarAtributos(projeto, nomeProjeto, aporteCusteioReais, aporteCapitalReais, codigoProjeto);
		projeto.ativar();
		
		return projeto;
	}
	
	// Método utilitário
	private void setarAtributos(
			Projeto projeto,
			String nomeProjeto,
			float aporteCusteioReais,
			float aporteCapitalReais,
			long codigoProjeto) {
		
		projeto.setNome(nomeProjeto);
		projeto.setAporteCusteioReais(aporteCusteioReais);
		projeto.setAporteCapitalReais(aporteCapitalReais);
		projeto.setCodigo(codigoProjeto);
	}
	
	public Projeto[] getTodosOsProjetos() {	
		Set<Projeto> projetosRegistrados = daoxmlProjetoParticipacao.getTodosRegistros();
		
		Iterator<Projeto> projetos = projetosRegistrados.iterator();
		try {
			while(projetos.hasNext()) {
				
				Projeto projeto = projetos.next();
				
				if(projeto.getParticipacoes().get(0).getDataTermino().isBefore(LocalDate.now())) {
					
					projeto.desativar();
				
				}else {
					
					projeto.ativar();
				}
				
				CentralAtualizacaoItemProjeto.atualizarProjeto(projeto.getCodigo(), projeto);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return projetosRegistrados.toArray(new Projeto[projetosRegistrados.size()]);
	}
}
















