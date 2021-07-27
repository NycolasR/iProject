package facades.casosdeuso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import model.autenticacao.Membro;
import model.projetos.Grupo;
import model.projetos.ItemProjeto;
import persistencia.DAOXMLGrupo;
import persistencia.DAOXMLMembroConta;
import util.CentralAtualizacaoItemProjeto;
import util.MyLogger;
import util.ValidadoraItemProjeto;

/**
 * 
 * @author NPG
 *
 * 	A implementação do padrão facade facilita aos usuários o uso de funções que exigiriam um esforço e conhecimento desnecessário sobre os métodos
 * 	disponibilizados no programa. Para o caso de uso 3, é simplificado a relação de criar um grupo, remover um grupo e atualizar suas informações de forma simples e direta,
 * 	sem a necessidade de um conhecimento prévio ou amplo sobre os métodos e eliminando a necessidade de ter contado direto com todos os objetos envolvidos.
 */
public class FacadeCasoDeUso3 {
	
	//	Membros administradores podem adicionar, remover ou atualizar Grupos (de pesquisa).
	//	Remoções de Grupos de pesquisa que possuem projetos ainda vinculados são ilegais.
	private DAOXMLGrupo daoxmlGrupo;
	private DAOXMLMembroConta daoxmlMembroConta;
	
	private Logger myLogger = MyLogger.getInstance();
	
	//A sobrescrita do construtor vai criar as instâncias dos atributos dessa fachada.
	public FacadeCasoDeUso3() {
		daoxmlGrupo = new DAOXMLGrupo();
		daoxmlMembroConta = new DAOXMLMembroConta();
	}
		
	
	public boolean adicionar(long matricula, String nome, LocalDate dataCriacao, String linkCNPq , long codigo) throws Exception{
		
		Membro membro = null;
		
		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matricula);
			// Vê se o membro tem permissão para adicionar um Edital ao sistema.
			if(membro.isAdministrador()) {
				
				Grupo grupo = new Grupo();
				setarAtributos(grupo, nome, dataCriacao, linkCNPq, codigo);
				
				if(daoxmlGrupo.criar(grupo)) {
					myLogger.info(this.getClass() + " " + grupo.getClass() +" "+ grupo.getNome() + " foi criado e gravado com sucesso!");
					return true;
				}
				
				myLogger.warning(this.getClass() + " " + grupo.getClass() + " Grupo Já existente");
				
			}
		} catch (Exception e) {
			myLogger.severe(e.toString());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return false;
	}
	
	public boolean remover(long matricula, long ID) throws Exception{
		
		Membro membro = null;
		
		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matricula);
			// Vê se o membro tem permissão para adicionar um Edital ao sistema
			if(membro.isAdministrador()) {
				// Recupera edital para ver se ainda possue projetos vinculados.
				Grupo grupo  = daoxmlGrupo.consultarPorID(ID);
				
				// Se nao houverem projetos vinculados, o edital pode ser removido.
				if(grupo.getProjetos().isEmpty()) {		
					myLogger.info(this.getClass() + " " + grupo.getClass() +" "+ grupo.getNome() + " foi removido com sucesso!");
					return daoxmlGrupo.remover(ID);
					
				}
				
				myLogger.warning(this.getClass() + " Este grupo tem projetos vinculados e não pode ser removido");
				
			}
			myLogger.warning(this.getClass() +" "+ membro.getNome() + " Não é um administrador. Apenas Administradores podem remover membros de um grupo");
			
		} catch (Exception e1) {
			myLogger.severe(e1.toString());
			e1.printStackTrace();
			throw new Exception(e1.getMessage());
		}
		
		return false;
	}
	
	public boolean atualizar(long matricula, String nome, LocalDate dataCriacao, String linkCNPq , long codigo) throws Exception{
		
		Membro membro = null;
		
		try {
			membro = daoxmlMembroConta.consultarPorMatricula(matricula);
			// Vê se o membro tem permissão para adicionar um Edital ao sistema
			if(membro.isAdministrador()) {
				Grupo grupo = null;
				
				grupo = daoxmlGrupo.consultarPorID(codigo);
				
				ArrayList<ItemProjeto> grupos = new ArrayList<ItemProjeto>(daoxmlGrupo.getTodosRegistros());
				
				if(ValidadoraItemProjeto.validarExistenciaDeItemProjeto(grupos, nome) != null) {
					
					throw new Exception("Grupo já existente com esse nome");
				}
				
				setarAtributos(grupo, nome, dataCriacao, linkCNPq, codigo);
				
				if(CentralAtualizacaoItemProjeto.atualizarGrupo(codigo, grupo)) {
					myLogger.info(this.getClass() + " " + grupo.getClass() +" "+ grupo.getNome() + " foi atualizado com sucesso!");
					return true;
				}
				
				myLogger.warning(this.getClass() + "Não foi possível atualizar este grupo. grupo inexistente");
			}
			
			myLogger.warning(this.getClass() +" "+ membro.getNome() + " Não é um administrador. Apenas Administradores podem remover membros de um grupo");
			
		} catch (Exception e) {
			myLogger.severe(e.toString());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return false;
	}
	
	private void setarAtributos(Grupo grupo , String nome, LocalDate dataCriacao, String linkCNPq , long codigo) {
		grupo.setCodigo(codigo);
		grupo.setNome(nome);
		grupo.setDataCriacao(dataCriacao);
		grupo.setLinkCNPq(linkCNPq);	
	}
	
	/**
	 * 
	 * @return retorna todos os grupos do sistema
	 */
	public Grupo[] getTodosOsGrupos() {
		Set<Grupo> gruposRegistrados = daoxmlGrupo.getTodosRegistros();
		return gruposRegistrados.toArray(new Grupo[gruposRegistrados.size()]);
	}
}
