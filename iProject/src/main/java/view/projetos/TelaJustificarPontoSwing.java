package view.projetos;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import controller.projetos.ControllerTelaJustificarPonto;
import model.projetos.ItemProjeto;
import ponto.model.projetos.PontoTrabalhado;
import util.FactorySwingComponents;

/**
 * 
 * @author NPG
 *
 *Essa classe é um produto concreto do padrão abstract factory
 */
public class TelaJustificarPontoSwing extends JFrame implements TelaJustificarPonto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton btnProximo, btnVoltar;
	private JLabel labelTitulo;
	private JTable tabela;
	private JScrollPane tabelaJScrollPane;
	private JTextArea areaJustificativa;
	private JComboBox<PontoTrabalhado> pontosTrabalhados;
	private ControllerTelaJustificarPonto controllerTelaJustificarPonto;
	private TelaJustificarPontoSwing telaJustificarPonto = this;
	
	public TelaJustificarPontoSwing() {
		controllerTelaJustificarPonto = new ControllerTelaJustificarPonto();
	}
	
	public void justificarPonto() {
		
		adicionarConfiguracoesBasicas();
		adicionarBotoes();
		adicionarLabel();
		adicionarArea();
		gerarPrimeiraVersao();
		setVisible(true);
	}
	
	public class OuvinteBotaoProximo implements ActionListener{

		public void actionPerformed(ActionEvent evento) {

			if(tabelaJScrollPane.isVisible()) {
				try {
					
					gerarSegundaVersao();
					
				}catch (Exception e) {

					JOptionPane.showMessageDialog(telaJustificarPonto, e.getMessage());
					gerarPrimeiraVersao();
				}

			}else if(evento.getActionCommand().equals("JUSTIFICAR")){
				
				try {
					
					
					if(pontosTrabalhados.getSelectedItem() != null) {
						
						controllerTelaJustificarPonto.justificarPonto(pontosTrabalhados.getSelectedIndex(),
								areaJustificativa.getText(), tabela.getSelectedRow());
						
						JOptionPane.showMessageDialog(telaJustificarPonto, "Ponto Justificado");
						
						areaJustificativa.setText("");
						telaJustificarPonto.repaint();
						
					}else {
						JOptionPane.showMessageDialog(telaJustificarPonto, "Nenhum ponto inválido escolhido");
					}
					
				}catch (Exception e) {
					
					JOptionPane.showMessageDialog(telaJustificarPonto, e.getMessage());

				}
				
				
			}
		}
	}
	
	public class OuvinteBotaoVoltar implements ActionListener{
		
		public void actionPerformed(ActionEvent evento) {
			
			if(areaJustificativa.isVisible()) {
				
				tabelaJScrollPane.setVisible(false);
				pontosTrabalhados.setVisible(false);
				
				gerarPrimeiraVersao();
								
			}else {
				
				telaJustificarPonto.dispose();
				controllerTelaJustificarPonto.gerarTelaPonto();
				
			}
			
		}
	}
	
	private void adicionarConfiguracoesBasicas() {
		
		setSize(300,400);
		setLocationRelativeTo(null);
		FactorySwingComponents.gerarLookAndFeelNimbus();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		

	}
	
	
	private void adicionarBotoes() {		
		btnProximo = new JButton("PRÓXIMO");
		btnVoltar = new JButton("VOLTAR");
			
		btnProximo.addActionListener(new OuvinteBotaoProximo());
		btnVoltar.addActionListener(new OuvinteBotaoVoltar());
		
		add(btnProximo);
		add(btnVoltar);
		
		btnVoltar.setVisible(false);
		btnProximo.setVisible(false);
		
	}
	
	
	private void adicionarLabel() {
		
		
		labelTitulo = new JLabel();
		
		labelTitulo.setFont(new Font("Arial", Font.PLAIN, 16));
		
		labelTitulo.setBackground(Color.WHITE);
		
		add(labelTitulo);
				
	}
	
	private void adicionarTabela(ItemProjeto[] projetos) {
		
		DefaultTableModel modeloDaTabela = new DefaultTableModel();
				
		modeloDaTabela.addColumn("Nome");
		modeloDaTabela.addColumn("Código");
		modeloDaTabela.addColumn("Situação");
		
		
		for(ItemProjeto itemProjeto : projetos) {
			
			String situacao = "ATIVO";

			if(!itemProjeto.isAtivo()) {
				situacao = "FINALIZADO";
			}
			
			Object[] linha = new Object[] {itemProjeto,itemProjeto.getCodigo(),situacao};
			
			modeloDaTabela.addRow(linha);
		}
		
		tabela = new JTable(modeloDaTabela);
		
		tabelaJScrollPane = new JScrollPane(tabela);
		tabelaJScrollPane.setBounds(20,70,242,200);
		
		add(tabelaJScrollPane);
		
		tabelaJScrollPane.setVisible(false);
	}
	
	private void adicionarArea() {
		areaJustificativa = new JTextArea();
		areaJustificativa.setToolTipText("Escreva sua justificativa aqui");
		
		add(areaJustificativa);
		
		areaJustificativa.setVisible(false);
	}
	
	private void adicionarCombo() {
		add(pontosTrabalhados);
	}
		
	
	private void modificarLocalizacaoBotoes(int x1, int y1, int z1, int w1, int x2, int y2, int z2, int w2) {
		
		btnProximo.setBounds(x1,y1,z1,w1);
		btnVoltar.setBounds(x2,y2,z2,w2);
	}
	
	private void modificarLabel(String titulo, int x, int y, int z, int w) {
		
		labelTitulo.setText(titulo);
		labelTitulo.setBounds(x,y,z,w);
	}
	
	private void modificarArea(int x, int y, int z, int w) {
		areaJustificativa.setBounds(x,y,z,w);
	}
	
	private void modificarCombo(int x, int y, int z, int w) {
		
		pontosTrabalhados.setBounds(x,y,z,w);
	}
	
	private void gerarPrimeiraVersao() {
		
			try {
			deixarTudoInvisivel();
			
			modificarLabel("PROJETOS",100,20,100,30);
			btnProximo.setText("PRÓXIMO");
			adicionarTabela(controllerTelaJustificarPonto.getProjetos());
			modificarLocalizacaoBotoes(162,290,100,40, 20,290,100,40);
			
			labelTitulo.setVisible(true);
			tabelaJScrollPane.setVisible(true);
			btnProximo.setVisible(true);
			btnVoltar.setVisible(true);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("deu erro ao gerar a primeira versão");
		}
		
	}
	
	private void gerarSegundaVersao() throws Exception{
		
		deixarTudoInvisivel();
		tabelaJScrollPane.setVisible(false);
		labelTitulo.setVisible(false);
		btnProximo.setText("JUSTIFICAR");
		int posicao = tabela.getSelectedRow();
		pontosTrabalhados = new JComboBox<>(controllerTelaJustificarPonto.getPontosTrabalhados(posicao));
		modificarCombo(20,30,242,30);
		modificarArea(20,70,242,200);
		
		adicionarCombo();
			
		pontosTrabalhados.setVisible(true);
		areaJustificativa.setVisible(true);
		btnProximo.setVisible(true);
		btnVoltar.setVisible(true);
	}
	
	private void deixarTudoInvisivel() {
		
		btnProximo.setVisible(false);
		btnVoltar.setVisible(false);
		areaJustificativa.setVisible(false);
		
	}	
}
