package facades.casosdeuso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import model.autenticacao.Membro;
import model.projetos.Edital;
import model.projetos.ItemProjeto;
import model.projetos.Projeto;
import persistencia.DAOXMLEdital;
import persistencia.DAOXMLMembroConta;
import util.CentralAtualizacaoItemProjeto;
import util.MyLogger;
import util.ValidadoraItemProjeto;

/**
 * @author NPG
 *
 *	 A implementacao do padrao facade facilita aos usuarios o uso de funcoes que exigiriam um esforco e conhecimento desnecessario sobre os metodos
 * 	disponibilizados no programa. Para o caso de uso 4, Ã© simplificado a relacao de criar um edital , remover um edital, e atualizar suas informacoes de forma simples e direta,
 * 	sem a necessidade de um conhecimento previo ou amplo sobre os mÃ©todos e eliminando a necessidade de ter contado direto com todos os objetos envolvidos.
 * 
 */
public class FacadeCasoDeUso4 {
	
//	Membros administradores podem adicionar, remover ou atualizar editais (de pesquisa).
//	Remoções de editais de pesquisa que possuem projetos ainda vinculados são ilegais.
	
	private DAOXMLEdital daoxmlEdital;
	private DAOXMLMembroConta daoxmlMembroConta;
	private Logger logger = MyLogger.getInstance();
	
	public FacadeCasoDeUso4() {
		// Só inicializa a instância quando o objeto for criado
		daoxmlEdital = new DAOXMLEdital();
		daoxmlMembroConta = new DAOXMLMembroConta();
	}
	
	// Método testado
	public boolean adicionarEdital(long matriculaMembro, String nomeEdital, LocalDate dataInicio, LocalDate dataTermino, long codigo) throws Exception{
		
		Membro membro = null;
		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matriculaMembro);
			// Vê se o membro tem permissão para adicionar um Edital ao sistema
			if(membro.isAdministrador()) {
				// Instancia de Edital
				Edital edital = new Edital();
				setarAtributos(edital, nomeEdital, dataInicio, dataTermino, codigo);
				edital.ativar();
				
				boolean editalCriado = daoxmlEdital.criar(edital);
				
				if(editalCriado)
					logger.info("Edital: " + nomeEdital + " criado com sucesso.");
				return editalCriado;
			}
			logger.warning("Membro não adminstrador tentou adicionar um edital.");
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}		
		return false;
	}
	
	// Método testado
	public boolean removerEdital(long matriculaMembro, long id) throws Exception{
		Membro membro = null;

		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matriculaMembro);

			// Ve se o membro recuperado tem permissao para remover um Edital do sistema
			if(membro.isAdministrador()) {
				// Recupera edital para ver se ainda possue projetos vinculados
				Edital edital = daoxmlEdital.consultarPorID(id);

				// Se nao houverem projetos vinculados, o edital pode ser removido
				if(edital.getProjetos().isEmpty()) {
					boolean editalRemovido = daoxmlEdital.remover(id); 
					
					if(editalRemovido)
						logger.info("Edital: " + edital.getNome() + " removido com sucesso.");
					
					return editalRemovido;
				}
			}
			logger.warning("Membro não adminstrador tentou remover um edital.");
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return false;
	}
	
	public boolean atualizarEdital(long matriculaMembro, long codigoEdital,  String nomeEdital, LocalDate dataInicio, LocalDate dataTermino) throws Exception{
		Membro membro = null;
		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matriculaMembro);
			
			// Vê se o membro tem permissão para remover um Edital do sistema
			if(membro.isAdministrador()) {
				// Instancia de Edital
				Edital edital = daoxmlEdital.consultarPorID(codigoEdital);
				
				ArrayList<ItemProjeto> editais = new ArrayList<ItemProjeto>(daoxmlEdital.getTodosRegistros());
				
				if(ValidadoraItemProjeto.validarExistenciaDeItemProjeto(editais, nomeEdital) != null) {
					
					throw new Exception("Edital já existente com esse nome");
				}
				
				setarAtributos(edital, nomeEdital, dataInicio, dataTermino, codigoEdital);
				
				boolean editalAtualizado = CentralAtualizacaoItemProjeto.atualizarEdital(codigoEdital, edital);
				
				if(editalAtualizado)
					logger.info("Edital: " + edital.getNome() + " atualizado com sucesso.");
				
				return editalAtualizado;
			}
			logger.warning("Membro não adminstrador tentou atualizar um edital.");
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return false;
	}
	
	
	private void setarAtributos(Edital edital, String nomeEdital, LocalDate dataInicio, LocalDate dataTermino, long codigoEdital) {
		edital.setNome(nomeEdital);
		edital.setDataInicio(dataInicio);
		edital.setDataTermino(dataTermino);
		edital.setCodigo(codigoEdital);
	}
	
	/**
	 * 
	 * @return retorna todos os editais do sistema
	 */
	public Edital[] getTodosOsEditais() {
		Set<Edital> editaisRegistrados = daoxmlEdital.getTodosRegistros();

		Iterator<Edital> editais = editaisRegistrados.iterator();
		try {
			while(editais.hasNext()) {
				
				Edital edital = editais.next();
				
				if(edital.getDataTermino().isBefore(LocalDate.now())) {
					
					edital.desativar();
					
				}else {
					edital.ativar();
				}
				
				CentralAtualizacaoItemProjeto.atualizarEdital(edital.getCodigo(), edital);
				logger.info("[INFO]: editais atualizados");
			}
		}catch (Exception e) {
			logger.severe("[ERRO]: erro ao atualizar os editais");
			e.printStackTrace();
		}
		
		return editaisRegistrados.toArray(new Edital[editaisRegistrados.size()]);
	}
	
	public void adicionarProjetoAUmEdital( Edital edital, Projeto projeto) throws Exception{
		
		try {
			
			edital.adicionarItemProjeto(projeto);
			CentralAtualizacaoItemProjeto.atualizarProjeto(projeto.getCodigo(), projeto);
			
			logger.info("[INFO]: projeto adicionado a um edital");
			
		}catch (Exception e) {
			
			logger.severe("[ERRO]: erro adicionar um projeto a um edital");
			throw new Exception(e.getMessage());
		}

	}
}
