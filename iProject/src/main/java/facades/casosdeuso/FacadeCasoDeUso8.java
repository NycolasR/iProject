package facades.casosdeuso;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JScrollPane;

import model.projetos.Edital;
import model.projetos.Grupo;
import model.projetos.Projeto;
import model.projetos.TiposItemProjeto;
import persistencia.DAOXMLEdital;
import persistencia.DAOXMLGrupo;
import persistencia.DAOXMLProjetoParticipacao;
import util.MyLogger;
import view.projetos.relatorios.DiretorMontagem;
import view.projetos.relatorios.MontadorRelatorioHTML;
import view.projetos.relatorios.MontadorRelatorioSwing;

/**
 * @author NPG
 *
 * 	A implementacao do padrao facade facilita aos usuarios o uso de funcoµes que exigiriam um esforcoo e conhecimento desnecessario sobre os metodos
 * 	disponibilizados no programa. Para o caso de uso 1, Ã© simplificado a relacao de gerar relatorio, sendo esse o unico metodo que o usuario precissara utilizar, 
 * 	de forma simples e direta, sem a necessidade de um conhecimento previo ou amplo sobre os metodos e eliminando a necessidade de ter contado direto com todos os 
 * 	objetos envolvidos.
 * 
 */
public class FacadeCasoDeUso8 {
	
	private static DiretorMontagem diretorMontagem;
	private Logger logger = MyLogger.getInstance();
	 
	/**
	 * O metodo abaixo retorna um relatorio com tags HTML5, que disponibiliza
	 * as informações sobre editais, grupos, projetos e participacoes do sistema.
	 * @param codItemProjeto Codigo identificador do ItemProjeto que se deseja gerar um relatorio.
	 * @param tipoItemProjeto Enum identificador do tipo concreto do itemProjeto.
	 * @return Retorna um arquivo de exetensao .html do relatorio.
	 * @throws Exception No caso de algum erro generico (especificado pela exception.getMessage()) ocorrer.
	 */
	public File gerarRelatorioHTML(long codItemProjeto, TiposItemProjeto tipoItemProjeto) throws Exception {
		diretorMontagem = new DiretorMontagem(new MontadorRelatorioHTML());
		logger.info("Foi gerado um relatório em HTML de um " + tipoItemProjeto + " com código: " + codItemProjeto);
		return (File) gerarProduto(codItemProjeto, tipoItemProjeto);
	}
	
	/**
	 * O metodo abaixo retorna um relatorio em JScrollPane com os paineis, que disponibiliza
	 * as informações sobre editais, grupos, projetos e participacoes do sistema.
	 * @param codItemProjeto Codigo identificador do ItemProjeto que se deseja gerar um relatorio.
	 * @param tipoItemProjeto Enum identificador do tipo concreto do itemProjeto.
	 * @return JScrollPane com os paineis do relatorio pronto para ser exibido em um JFrame.
	 * @throws Exception No caso de algum erro generico (especificado pela exception.getMessage()) ocorrer.
	 */
	public JScrollPane gerarRelatorioSwing(long codItemProjeto, TiposItemProjeto tipoItemProjeto) throws Exception {
		diretorMontagem = new DiretorMontagem(new MontadorRelatorioSwing());
		logger.info("Foi gerado um relatório em Swing de um " + tipoItemProjeto + " com código: " + codItemProjeto);
		return (JScrollPane) gerarProduto(codItemProjeto, tipoItemProjeto);
	}
	
	/**
	 * O metodo abaixo retorna o relatorio pronto visto pelo supertipo object.
	 * @param codItemProjeto Codigo identificador do ItemProjeto que se deseja gerar um relatorio.
	 * @param tipoItemProjeto Enum identificador do tipo concreto do itemProjeto.
	 * @return Retorna um relatorio pronto, cujo tipo eh especificado pelo montador concreto setado no diretor de montagem.
	 * @throws Exception No caso de algum erro generico (especificado pela exception.getMessage()) ocorrer.
	 */
	private Object gerarProduto(long codItemProjeto, TiposItemProjeto tipoItemProjeto) throws Exception {
		switch (tipoItemProjeto) {
		case EDITAL:
			DAOXMLEdital daoxmlEdital = new DAOXMLEdital();
			Edital edital = daoxmlEdital.consultarPorID(codItemProjeto);
			diretorMontagem.gerarRelatorioEdital(edital);
			break;
			
		case GRUPO:
			DAOXMLGrupo daoxmlGrupo = new DAOXMLGrupo();
			Grupo grupo = daoxmlGrupo.consultarPorID(codItemProjeto);
			diretorMontagem.gerarRelatorioGrupo(grupo);
			break;
			
		case PROJETO:
			DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
			Projeto projeto = daoxmlProjetoParticipacao.consultarPorID(codItemProjeto);
			diretorMontagem.gerarRelatorioProjeto(projeto);
			break;
			
		default:
			throw new UnsupportedOperationException("Não foi possível gerar um relatório deste item. Verifique se o item escolhido é uma opção válida.");
		}
		
		return diretorMontagem.getProduto();
	}
}
