package view.projetos.relatorios;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import model.projetos.Edital;
import model.projetos.Grupo;
import model.projetos.ItemProjeto;
import model.projetos.Participacao;
import model.projetos.Projeto;
import util.CalculadoraCusteioCapitalTotal;

/**
 * 
 * @author NPG
 * Esta classe eh de um montador concreto (monta relatorios em HTML5 com CSS3) na implementacao do padrao builder.
 */
public class MontadorRelatorioHTML implements InterfacePartesMontagem {
	
	private File arquivoHTML;
	private StringBuffer arquivoStr;
	
	@Override
	public void reiniciar() {
		arquivoHTML = new File("relatorio.html");
		arquivoStr = new StringBuffer();		
	}
	
	private void iniciarHTML() {
		arquivoStr.append("<!DOCTYPE html>\n");
		arquivoStr.append("<html lang='pt-br'>\n");
		arquivoStr.append("<head>\n");
		arquivoStr.append("<title>Relatório de Itens de Projeto</title>\n");
		arquivoStr.append("<meta charset=\"utf-8\">");
		arquivoStr.append("<style>" + gerarCSS() +  "</style>");
		arquivoStr.append("</head>\n");
		arquivoStr.append("<body>\n");
	}
	

	/**
	 * Metodo responsavel pela estilizacao da pagina web com o relatorio. 
	 * @return Retorna uma string com toda a estilizacao em paginas de estilo CSS. 
	 */
	private String gerarCSS() {
		return "body {\r\n" + 
				"      font-family: Arial, Helvetica, sans-serif;\r\n" + 
				"      background-color: #f1f1f1;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    div {\r\n" + 
				"      border: 1px solid rgb(47, 165, 219);\r\n" + 
				"      border-radius: 10px;\r\n" + 
				"      box-shadow: 1px 2px 4px rgb(59, 10, 235);\r\n" + 
				"      backgound-color: rgb(255, 255, 255)" + 
				"      margin: 5px;\r\n" + 
				"      margin-top: 15px;\r\n" + 
				"      padding: 15px;\r\n" + 
				"      padding-top: 0px;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    div.projeto {\r\n" + 
				"      width: 90%;\r\n" + 
				"      margin: auto;\r\n" + 
				"      margin-top: 15px;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    h2 {\r\n" + 
				"      color: rgb(34, 0, 128);\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    h3.itemAtivo {\r\n" + 
				"      color: rgb(26, 206, 35);\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    h3.itemNaoAtivo {\r\n" + 
				"      color: rgb(255, 21, 21);\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    strong {\r\n" + 
				"      color: rgb(26, 184, 26);\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    h3.valor {\r\n" + 
				"      border: 2px solid rgb(102, 192, 0);\r\n" + 
				"      border-radius: 10px;\r\n" + 
				"      box-shadow: 1px 1px 4px rgb(139, 255, 128);\r\n" + 
				"      padding: 5px;\r\n" + 
				"    }";
	}
	
	@Override
	public File encerrar() {
		arquivoStr.append("</body>\n");
		arquivoStr.append("</html>");
		
		return gerarArquivo();
	}
	
	private File gerarArquivo() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(arquivoHTML));
			writer.write(arquivoStr.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return arquivoHTML;
	}
	
	/**
	 * Esse metodo gera um relatório dos dados do Edital com tags HTML5
	 * @param edital Edital do qual se deseja montar o relatorio com as informacoes.
	 * @return Retorna uma StringBuffer com o relatorio formatado em tags HTML5.
	 * @throws Exception Lanca excecao generica em caso de erro ocorrido em tempo de
	 * execucao. O erro eh especificado na mensagem inerente a excecao.
	 */
	@Override
	public void gerarRelatorioEdital(Edital edital) throws Exception {
		iniciarHTML();
		
		arquivoStr.append("<div class=\"edital\">");
		arquivoStr.append("<h2>Dados do Edital: " + edital.getNome() + " :</h2>\n");
		arquivoStr.append("<h3>Código: " + edital.getCodigo() + "</h3>\n");
		arquivoStr.append(tagIsAtivo(edital.isAtivo()));
		arquivoStr.append("<h3>Vigência: " + edital.getDataInicio() + " <---> " + edital.getDataTermino() + "</h3>\n");
		arquivoStr.append(valoresTotaisENaoGastos(edital));
		
		float[] valoresTotais = CalculadoraCusteioCapitalTotal.calcularCusteioCapitalTotal(edital.getProjetos());
		float valorTotalCusteioReais = valoresTotais[0];
		float valorTotalCapitalReais = valoresTotais[1];
		
		float naoGastoCusteio = edital.getCusteioReaisNaoGastoTotal();
		float naoGastoCapital = edital.getCapitalReaisNaoGastoTotal();
		arquivoStr.append("<h3 class=\"valor\">Valor gasto de custeio: " + (valorTotalCusteioReais - naoGastoCusteio) + "</h3>\n");
		arquivoStr.append("<h3 class=\"valor\">Valor gasto de capital: " + (valorTotalCapitalReais - naoGastoCapital) + "</h3>\n");
		arquivoStr.append("<h3>Dados dos Projetos deste Edital: </h3>\n");
		arquivoStr.append("</div>");
	}
	
	private StringBuffer valoresTotaisENaoGastos(ItemProjeto itemProjeto) throws Exception {
		StringBuffer subDivisao = new StringBuffer();
		
		//valores totais de custeio e capital de um edital respectivamente
		float[] valoresTotais = CalculadoraCusteioCapitalTotal.calcularCusteioCapitalTotal(itemProjeto.getProjetos());
		float valorTotalCusteioReais = valoresTotais[0];
		float valorTotalCapitalReais = valoresTotais[1];
		
		subDivisao.append("<h3 class=\"valor\">Total custeio em reais: " + valorTotalCusteioReais + "</h3>\n");
		subDivisao.append("<h3 class=\"valor\">Total capital em reais: " + valorTotalCapitalReais+ "</h3>\n");
		subDivisao.append(valoresNaoGastos(itemProjeto));
		
		return subDivisao;
	}
	
	private StringBuffer valoresNaoGastos(ItemProjeto itemProjeto) {
		StringBuffer subDivisao = new StringBuffer();
		
		subDivisao.append("<h3 class=\"valor\">Valor diponível de custeio (não gasto): " + itemProjeto.getCusteioReaisNaoGastoTotal() + "</h3>\n");
		subDivisao.append("<h3 class=\"valor\">Valor diponível de capital (não gasto): " + itemProjeto.getCapitalReaisNaoGastoTotal() + "</h3>\n");
		
		return subDivisao;
	}
	
	/**
	 * Esse método gera um relatório dos dados do Grupo em formato HTML
	 * @throws Exception Exception Lanca excecao generica em caso de erro ocorrido em tempo de
	 * execucao. O erro eh especificado na mensagem inerente a excecao.
	 */
	@Override
	public void gerarRelatorioGrupo(Grupo grupo) throws Exception {
		iniciarHTML();
		
		arquivoStr.append("<div class=\"grupo\">");
		arquivoStr.append("<h2>Dados do Grupo: " + grupo.getNome() + " :</h2>\n");
		arquivoStr.append("<h3>Código: " + grupo.getCodigo() + "</h3>\n");
		arquivoStr.append(tagIsAtivo(grupo.isAtivo()));
		arquivoStr.append("<h3>Data de criação: " + grupo.getDataCriacao() + "</h3>\n");
		arquivoStr.append("<h3>Link CNPQ: " + grupo.getLinkCNPq() + "</h3>\n");
		arquivoStr.append(valoresTotaisENaoGastos(grupo));
		arquivoStr.append("<h3>Dados dos Projetos deste Grupo: </h3>\n");
		arquivoStr.append("</div>");
	}
	
	/**
	 * Esse metodo gera um relatorio dos dados do projeto com tags de HTML5.
	 * @param projeto Projeto do qual se deseja montar o relatorio com as informacoes.
	 * @return Retorna uma StringBuffer com o relatorio formatado em tags HTML5.
	 * @throws Exception Exception Lanca excecao generica em caso de erro ocorrido em tempo de
	 * execucao. O erro eh especificado na mensagem inerente a excecao.
	 */
	@Override
	public void gerarRelatorioProjeto(Projeto projeto) throws Exception {
		if(arquivoStr == null) {
			arquivoStr = new StringBuffer();
			reiniciar();
			iniciarHTML();
		}
		
		arquivoStr.append(gerarRelatorioProjetoPrivado(projeto));
	}
	
	private StringBuffer gerarRelatorioProjetoPrivado(Projeto projeto) {
		
		StringBuffer projetoRelatorio = new StringBuffer();
		
		projetoRelatorio.append("<div class=\"projeto\">");
		projetoRelatorio.append("<h2>Dados do Projeto: " + projeto.getNome() + " :</h2>\n");
		projetoRelatorio.append("<h3>Código: " + projeto.getCodigo() + "</h3>\n");
		projetoRelatorio.append(tagIsAtivo(projeto.isAtivo()));
		
		try {
			//data inicio e termino do projeto equivalem as datas da primeira participacao, pois foi criada essa regra de negócio
			//essa participação equivale ao do coordenador, já que é a primeira participacao
			projetoRelatorio.append("<h3>Vigência: "+ projeto.getParticipacoes().get(0).getDataInicio() + " <---> " + projeto.getParticipacoes().get(0).getDataTermino() + "</h3>");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		projetoRelatorio.append("<h3 class=\"valor\">Aporte custeio reais: " + projeto.getAporteCusteioReais() + "</h3>\n");
		projetoRelatorio.append("<h3 class=\"valor\">Aporte capital reais: " + projeto.getAporteCapitalReais() + "</h3>\n");
		projetoRelatorio.append(valoresNaoGastos(projeto));
		projetoRelatorio.append("<h3>Dados das Participações deste Projeto: </h3>\n");
		
		Participacao[] participacoes = projeto.getParticipacoes().toArray(new Participacao[projeto.getParticipacoes().size()]);
		Arrays.sort(participacoes);
		for (int i = 0; i < participacoes.length; i++) {
			projetoRelatorio.append(gerarRelatorioParticipacao(participacoes[i]));
		}
		return projetoRelatorio;
	}
	
	/**
	 * Esse metodo gera um relatório dos dados da participacao com tags HTML5.
	 * @param participacao Participacao doa qual se deseja montar o relatorio com as informacoes.
	 * @return Retorna uma StringBuffer com o relatorio formatado em tags HTML5.
	 */
	private StringBuffer gerarRelatorioParticipacao(Participacao participacao) {
		StringBuffer relatorioDeParticipacao = new StringBuffer();
		relatorioDeParticipacao.append("<div class=\"participacao\">");
		
		relatorioDeParticipacao.append("<h2>Dados do membro participante: " + participacao.getMembro().getNome() + "</h2>\n");
		
		if(participacao.isCoordenador())
			relatorioDeParticipacao.append("<strong><h3>Esta participação pertence ao coordenador do projeto!</h3></strong>\n");
			
		relatorioDeParticipacao.append("<h3>Código do membro participante: " + participacao.getCodigo() + "</h3>\n");
		relatorioDeParticipacao.append("<h3>Período da participação: " + participacao.getDataInicio() + " <---> " + participacao.getDataTermino() + "</h3>\n");
		relatorioDeParticipacao.append("<h3 class=\"valor\">Custeio mensal (Bolsa): " + participacao.getAporteCusteioMensalReais() + "</h3>\n");
		
		relatorioDeParticipacao.append(tagIsAtivo(participacao.isAtivo()));
		
		relatorioDeParticipacao.append("<h3>Quantidade de meses custeados: " + participacao.getQtdMesesCusteados() + "</h3>\n");
		relatorioDeParticipacao.append("<h3>Quntidade de meses pagos: " + participacao.getQtdMesesPagos() + "</h3>\n");

		relatorioDeParticipacao.append("</div>");
		return relatorioDeParticipacao;
	}
	
	private String tagIsAtivo(boolean isAtivo) {
		if(isAtivo)
			return "<h3 class=\"itemAtivo\">Este item está ativo!</h3>\n";
		else
			return "<h3 class=\"itemNaoAtivo\">Este item não está ativo!</h3>\n";
	}
	
	@Override
	public void mostrarProduto() {
		try {
			Desktop.getDesktop().open((File) arquivoHTML);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
