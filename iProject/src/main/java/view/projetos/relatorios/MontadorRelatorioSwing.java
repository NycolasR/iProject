package view.projetos.relatorios;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.projetos.Edital;
import model.projetos.Grupo;
import model.projetos.ItemProjeto;
import model.projetos.Participacao;
import model.projetos.Projeto;
import util.CalculadoraCusteioCapitalTotal;
import util.FactorySwingComponents;

/**
 * 
 * @author NPG
 * Esta classe eh de um montador concreto (monta relatorios em JPanels com barra de rolagem) na implementacao do padrao builder.
 */
public class MontadorRelatorioSwing implements InterfacePartesMontagem {

	private FactorySwingComponents factory = new FactorySwingComponents();
	private JPanel panel;
	
	private Font fontEditalGrupo = new Font("Arial", Font.PLAIN, 16);
	private Color colorEditalGrupo = new Color(230, 230, 230);
	
	private Font fontProjetoParticipacao = new Font("Arial", Font.PLAIN, 14);
	private Color colorProjeto = new Color(215, 215, 215);
	
	private Color colorParticipacao = new Color(200, 200, 200);
	private Color colorValores = new Color(26, 206, 35);
	
	@Override
	public void gerarRelatorioEdital(Edital edital) throws Exception {
		iniciar();
		panel.add(factory.gerarJPanelComLabel("Dados do Edital: " + edital.getNome(), fontEditalGrupo, colorEditalGrupo));
		panel.add(factory.gerarJPanelComLabel("Código: " + edital.getCodigo(), fontEditalGrupo, colorEditalGrupo));
		panel.add(factory.gerarLabelIsAtivo(edital.isAtivo(), fontEditalGrupo));
		panel.add(factory.gerarJPanelComLabel("Vigência: " + edital.getDataInicio() + " --- " + edital.getDataTermino(), fontEditalGrupo, colorEditalGrupo));
		
		float[] valoresTotais = CalculadoraCusteioCapitalTotal.calcularCusteioCapitalTotal(edital.getProjetos());
		float valorTotalCusteioReais = valoresTotais[0];
		float valorTotalCapitalReais = valoresTotais[1];
		
		//valores totais de custeio e capital de um edital respectivamente
		float naoGastoCusteio = edital.getCusteioReaisNaoGastoTotal();
		float naoGastoCapital = edital.getCapitalReaisNaoGastoTotal();
		valoresTotaisENaoGastos(edital);
		panel.add(factory.gerarJPanelComLabel("Valor gasto de custeio: " + (valorTotalCusteioReais - naoGastoCusteio), fontEditalGrupo, colorValores));
		panel.add(factory.gerarJPanelComLabel("Valor gasto de capital: " + (valorTotalCapitalReais - naoGastoCapital), fontEditalGrupo, colorValores));
		panel.add(factory.gerarJPanelComLabel("Projetos deste Edital:", fontEditalGrupo, colorEditalGrupo));
	}

	@Override
	public void gerarRelatorioGrupo(Grupo grupo) throws Exception {
		iniciar();
		panel.add(factory.gerarJPanelComLabel("Dados do Grupo: " + grupo.getNome(), fontEditalGrupo, colorEditalGrupo));
		panel.add(factory.gerarJPanelComLabel("Código: " + grupo.getCodigo(), fontEditalGrupo, colorEditalGrupo));
		panel.add(factory.gerarLabelIsAtivo(grupo.isAtivo(), fontEditalGrupo));
		panel.add(factory.gerarJPanelComLabel("Data de criação: " + grupo.getDataCriacao(), fontEditalGrupo, colorEditalGrupo));
		panel.add(factory.gerarJPanelComLabel("Link CNPQ: " + grupo.getLinkCNPq(), fontEditalGrupo, colorEditalGrupo));
		valoresTotaisENaoGastos(grupo);
		panel.add(factory.gerarJPanelComLabel("Projetos deste Grupo:", fontEditalGrupo, colorEditalGrupo));
	}
	
	private void valoresTotaisENaoGastos(ItemProjeto itemProjeto) throws Exception {
		//valores totais de custeio e capital de um edital respectivamente
		float[] valoresTotais = CalculadoraCusteioCapitalTotal.calcularCusteioCapitalTotal(itemProjeto.getProjetos());
		float valorTotalCusteioReais = valoresTotais[0];
		float valorTotalCapitalReais = valoresTotais[1];
		
		panel.add(factory.gerarJPanelComLabel("Total custeio em reais: " + valorTotalCusteioReais, fontEditalGrupo, colorValores));
		panel.add(factory.gerarJPanelComLabel("Total capital em reais: " + valorTotalCapitalReais, fontEditalGrupo, colorValores));
		
		valoresNaoGastos(itemProjeto);
	}
	
	private void valoresNaoGastos(ItemProjeto itemProjeto) {
		panel.add(factory.gerarJPanelComLabel("Valor diponível de custeio: " + itemProjeto.getCusteioReaisNaoGastoTotal(), fontEditalGrupo, colorValores));
		panel.add(factory.gerarJPanelComLabel("Valor diponível de capital: " + itemProjeto.getCapitalReaisNaoGastoTotal(), fontEditalGrupo, colorValores));
	}

	@Override
	public void gerarRelatorioProjeto(Projeto projeto) throws Exception {
		if(panel == null)
			panel = factory.gerarJPanel("Relatório de Projeto", colorProjeto);
		
		panel.add(gerarRelatorioProjetoPrivado(projeto));
	}
	
	private JScrollPane gerarRelatorioProjetoPrivado(Projeto projeto) {
		JPanel relatorioDeProjeto = factory.gerarJPanel("Relatório de Projeto", colorProjeto);
		relatorioDeProjeto.setSize(450, 600);
		
		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Dados do Projeto: " + projeto.getNome(), fontProjetoParticipacao, colorProjeto));
		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Código: " + projeto.getCodigo(), fontProjetoParticipacao, colorProjeto));
		relatorioDeProjeto.add(factory.gerarLabelIsAtivo(projeto.isAtivo(), fontProjetoParticipacao));

		try {
			relatorioDeProjeto.add(factory.gerarJPanelComLabel("Vigência: "+ projeto.getParticipacoes().get(0).getDataInicio() + " --- " + projeto.getParticipacoes().get(0).getDataTermino(), fontProjetoParticipacao, colorProjeto));
		} catch (Exception e) {
			e.printStackTrace();
		}

		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Aporte custeio reais: " + projeto.getAporteCusteioReais(), fontProjetoParticipacao, colorValores));
		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Aporte capital reais: " + projeto.getAporteCapitalReais(), fontProjetoParticipacao, colorValores));
		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Valor diponível de custeio: " + projeto.getCusteioReaisNaoGastoTotal(), fontProjetoParticipacao, colorValores));
		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Valor diponível de capital: " + projeto.getCapitalReaisNaoGastoTotal(), fontProjetoParticipacao, colorValores));
		relatorioDeProjeto.add(factory.gerarJPanelComLabel("Dados das Participações deste Projeto:", fontProjetoParticipacao, colorProjeto));

		JPanel pnlParticipacoes = new JPanel();
		pnlParticipacoes.setLayout(new BoxLayout(pnlParticipacoes, BoxLayout.Y_AXIS));

		Participacao[] participacoesArr = projeto.getParticipacoes().toArray(new Participacao[projeto.getParticipacoes().size()]);
		Arrays.sort(participacoesArr);
		for(int contador = 0; contador < participacoesArr.length; contador++) {
			pnlParticipacoes.add(gerarRelatorioParticipacao((Participacao) projeto.getParticipacoes().get(contador)));
		}
		
		JScrollPane scrollParticipacoes = new JScrollPane(pnlParticipacoes);
		relatorioDeProjeto.add(scrollParticipacoes);
		JScrollPane scrollProjetos = new JScrollPane(relatorioDeProjeto);
		
		return scrollProjetos;
	}
	
	private JScrollPane gerarRelatorioParticipacao(Participacao participacao) {
		JPanel relatorioDeParticipacao = null;
		relatorioDeParticipacao = factory.gerarJPanel( "Relatório de Participação", colorParticipacao);
		relatorioDeParticipacao.setSize(400, 400);

		relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Dados do membro participante: " + participacao.getMembro().getNome(), fontProjetoParticipacao, colorParticipacao));

		if(participacao.isCoordenador())
			relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Esta participação pertence ao coordenador do projeto!", fontProjetoParticipacao, colorParticipacao));


		relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Código do membro participante: " + participacao.getCodigo(), fontProjetoParticipacao, colorParticipacao));

		relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Período da participação: " + participacao.getDataInicio() + " <---> " + participacao.getDataTermino(), fontProjetoParticipacao, colorParticipacao));
		relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Custeio mensal (Bolsa): " + participacao.getAporteCusteioMensalReais(), fontProjetoParticipacao, colorValores));

		relatorioDeParticipacao.add(factory.gerarLabelIsAtivo(participacao.isAtivo(), fontProjetoParticipacao));
		relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Quantidade de meses custeados: " + participacao.getQtdMesesCusteados(), fontProjetoParticipacao, colorValores));
		relatorioDeParticipacao.add(factory.gerarJPanelComLabel("Quntidade de meses pagos: " + participacao.getQtdMesesPagos(), fontProjetoParticipacao, colorValores));

		JScrollPane scrollPane = new JScrollPane(relatorioDeParticipacao);
		return scrollPane;
	}
	

	@Override
	public JScrollPane encerrar() {
		JScrollPane scrollPane = new JScrollPane(panel);
		return scrollPane;
	}
	
	@Override
	public void reiniciar() {
		panel = factory.gerarJPanel("Relatório de ItemProjeto", colorEditalGrupo);
	}
	
	public void iniciar() {
		FactorySwingComponents.gerarLookAndFeelNimbus();
		reiniciar();
		panel.setSize(550, 450);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	}
	
	@Override
	public void mostrarProduto() {
		try {			
			JFrame frame = new JFrame();
			FactorySwingComponents.gerarLookAndFeelNimbus();
			frame.setTitle("Relatório Final");
			frame.setSize(500, 600);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.add((JScrollPane) new JScrollPane(panel));
			frame.setVisible(true);		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
