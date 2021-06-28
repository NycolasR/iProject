package util;

import model.projetos.Edital;
import model.projetos.Grupo;
import model.projetos.Projeto;
import persistencia.DAOXMLEdital;
import persistencia.DAOXMLGrupo;
import persistencia.DAOXMLProjetoParticipacao;

/**
 * Classe utilitaria desenvolvida para facilitar a atualizacao 
 * dos registros em XML dos tipos concretos de ItemProjeto.
 * @author NPG
 * 
 */
public class CentralAtualizacaoItemProjeto {
	public static void atualizarProjeto(long codigo, Projeto projeto) {
		DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		daoxmlProjetoParticipacao.atualizar(codigo, projeto);
		
		if(projeto.getGrupo() != null)
			atualizarGrupo(projeto.getGrupo().getCodigo(), (Grupo) projeto.getGrupo());
		if(projeto.getEdital() != null)
			atualizarEdital(projeto.getEdital().getCodigo(), (Edital) projeto.getEdital());
	}
	
	public static boolean atualizarEdital(long codigo, Edital edital) {
		DAOXMLEdital daoxmlEdital = new DAOXMLEdital();
		return daoxmlEdital.atualizar(codigo, edital);
	}

	public static boolean atualizarGrupo(long codigo, Grupo grupo) {
		DAOXMLGrupo daoxmlGrupo = new DAOXMLGrupo();
		return daoxmlGrupo.atualizar(codigo, grupo);
	}
}
