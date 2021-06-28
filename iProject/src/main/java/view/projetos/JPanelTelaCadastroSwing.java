package view.projetos;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import model.projetos.ItemProjeto;

/**
 * 
 * @author NPG
 *
 * Essa classe é o supertipo padronizador, utilizado pelas outras classes concretas,
 * que são produtos concretos do padrão abstract factory
 * 
 */

public abstract class JPanelTelaCadastroSwing extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFormattedTextField campoDataInicio;
	private JFormattedTextField campoDataTermino;
	private JTextField campoNome;
	private JTable tabela;
	private JScrollPane tabelaJScrollPane;
	private JPanelTelaCadastroSwing jPanelTelaCadastroSwing;
	
	public JPanelTelaCadastroSwing() {
		
		adicionarPrincipaisConfiguracoes();
		
	}
	
	public class OuvinteDoMouse implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			
			try {
				
				preencherCampos();
				
			}catch (Exception excessao) {
				
				JOptionPane.showMessageDialog(jPanelTelaCadastroSwing, excessao.getMessage());
			}
			
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
		
	}
	
	private void adicionarPrincipaisConfiguracoes() {
		setSize(500, 600);
		setLayout(null);
		this.setBounds(45, 50, 495, 510);
	}
	
	public void adicionarTabela(ItemProjeto[] itensProjetos) {	
		
		tabelaJScrollPane = new JScrollPane(criarTabela(itensProjetos));
		tabelaJScrollPane.setBounds(0, 60, 495, 200);
		
		add(tabelaJScrollPane);
		
	}
	
	private JTable criarTabela(ItemProjeto[] itensProjetos) {
		
		DefaultTableModel modeloDaTabela = new DefaultTableModel();
		
		modeloDaTabela.addColumn("Nome");
		modeloDaTabela.addColumn("Código");
		modeloDaTabela.addColumn("Situação");
		
		
		for(ItemProjeto itemProjeto : itensProjetos) {
			
			String situacao = "ATIVO";

			if(!itemProjeto.isAtivo()) {
				situacao = "FINALIZADO";
			}
			
			Object[] linha = new Object[] {itemProjeto,itemProjeto.getCodigo(),situacao};
			
			modeloDaTabela.addRow(linha);
		}
		tabela = new JTable(modeloDaTabela);
		tabela.addMouseListener(new OuvinteDoMouse());
		
		return tabela;	
	}
	
	public void adicionarCampos(String textoInformativoDaMatricula) {
		try {
			
			campoNome = new JTextField();
			
			campoNome.setBounds(120,370,260,30);
			
			add(campoNome);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void adicionarCamposDatas() {
		
		try {
			
			MaskFormatter formatoData = new MaskFormatter("##/##/####");
			
			campoDataInicio = new JFormattedTextField(formatoData);
			campoDataTermino = new JFormattedTextField(formatoData);
			
			campoDataInicio.setBounds(120,300,80,30);
			campoDataTermino.setBounds(300,300,80,30);
			
			add(campoDataInicio);
			add(campoDataTermino);
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void adicionarLabels(String tituloDoTitulo) {
		
		JLabel titulo = new JLabel(tituloDoTitulo);
		JLabel nome = new JLabel("Nome");
		
		titulo.setBounds(210, 10, 100, 30);
		nome.setBounds(230, 350, 100, 20);
		
		titulo.setFont(new Font("Arial", Font.PLAIN, 15));
		
		add(titulo);
		add(nome);
	}
	
	public void adicionarLabelsDatas() {
		
		JLabel dataInicio = new JLabel("Inicio");
		JLabel dataTermino = new JLabel("Término");
		
		dataInicio.setBounds(140, 280, 80, 20);
		dataTermino.setBounds(315, 280, 80, 20);
		
		add(dataInicio);
		add(dataTermino);
	}
	
	public void atualizarTabela(ItemProjeto[] itensProjetos) {
		
		remove(tabelaJScrollPane);
		adicionarTabela(itensProjetos);
	}
	
	public abstract void preencherCampos() throws Exception;
		
	
	public JTable getTabela() {
		return tabela;
	}

	public JFormattedTextField getCampoDataInicio() {
		return campoDataInicio;
	}

	public JFormattedTextField getCampoDataTermino() {
		return campoDataTermino;
	}

	public JTextField getCampoNome() {
		return campoNome;
	}
	
	
	
	
}
