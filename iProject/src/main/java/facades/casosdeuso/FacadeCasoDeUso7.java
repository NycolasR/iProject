package facades.casosdeuso;

import java.util.logging.Logger;

import model.autenticacao.Membro;
import persistencia.DAOXMLMembroConta;
import util.MyLogger;

/**
 * 
 * @author NPG
 *
 * 	A implementacao do padrao facade facilita aos usuarios o uso de funcoes que exigiriam um esforcoo e conhecimento desnecessario sobre os metodos
 * 	disponibilizados no programa. Para o caso de uso 1, Ã© simplificado a relacao de tornar ou desabilitar um membro como administrador de forma simples e direta,
 * 	sem a necessidade de um conhecimento previo ou amplo sobre os metodos e eliminando a necessidade de ter contado direto com todos os objetos envolvidos.
 *
 */
public class FacadeCasoDeUso7 {

	private DAOXMLMembroConta daoXmlMembroConta;	//centraliza o objeto instânciado em um atributo globalizado
	private Logger logger = MyLogger.getInstance();
	
	//A instância do atributo daoXmlMembroConta só é criada com a instânciação da classe casoDeUso7
	public FacadeCasoDeUso7() {
		daoXmlMembroConta = new DAOXMLMembroConta();
	}
	
	/*
	 * O método a baixo habilita ou desabilita um membro como administrador, caso seja passado
	 * como parâmetro de entrada matriculas diferentes de 0, e uma condição true ou false.
	 * Caso a matricula passada para o administrador seja realmente do administrador, e ela
	 * seja diferente da matrícula do membro a ser mudado(pois um administrador não pode se 
	 * auto desabilitar), então utiliza-se o daoXmlMmembroConta para encontrar o ID a partir
	 * da matrícula no membro a ser mudado, e para pegar um membro a partir do ID passado como 
	 * parâmetro de entrada, logo após setamos o atributo boleando ao setAdministrador(), e por 
	 * fim atualizamos o membro utilizando o daoXmlMmebroConta.
	 */
	public boolean tornarOuDesabilitarMembroAdministrador(long matriculaMembroAdministrador, long matriculaMembroASerMudado, boolean isAdministrador) throws Exception {

		try {
			Membro membroAdministrador = daoXmlMembroConta.consultarPorID(matriculaMembroAdministrador);

			if(matriculaMembroAdministrador != 0 && matriculaMembroASerMudado != 0 && membroAdministrador.isAdministrador() && 
					matriculaMembroAdministrador != matriculaMembroASerMudado) {

				Membro membroASerMudado = daoXmlMembroConta.consultarPorID(matriculaMembroASerMudado);

				membroASerMudado.setAdministrador(isAdministrador);
				
				logger.info("Membro " + matriculaMembroASerMudado + ": tornou-se administrador");
				
				daoXmlMembroConta.atualizar(membroASerMudado.getMatricula(), membroASerMudado);
				
				logger.info("[XMLMembroConta] - Dados dos membros atualizados ");
				
				return true;
				
			}
			
			logger.warning("Dados para tornar membro administrador, inválidos");
			
		}catch (Exception e) {
			
			logger.severe(e.getMessage());
			throw new Exception(e.getMessage());
			
		}
		
		return false;
			
			
	}

}
