package view.projetos;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import controller.projetos.ControllerTelaPonto;
import model.projetos.ItemProjeto;
import model.projetos.Projeto;
import util.FactorySwingComponents;

/**
 * @author NPG
 *
 *Essa tela é utilizada principalmente pelos usuários que possuem participações em um projeto,
 *e precisam bater ponto em um horário previsto.
 */
public class TelaPonto extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton btnDetalhes, btnCadastrarProjeto, btnJustificarPonto; // o btnDetalhes deve ser ativado se o ponto foi batido com sucesso
	private JComboBox<Projeto> cbxProjetos;
	
	private ControllerTelaPonto controllerTelaPonto;
	private Font fonteParaTextos = new Font("Arial", Font.PLAIN, 15);
	private TelaPonto telaPonto = this;
	
	public class OuvinteBtnBaterPonto implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			ItemProjeto projeto = (ItemProjeto) cbxProjetos.getSelectedItem();
			
			try {
				controllerTelaPonto.registrarPonto(projeto.getCodigo());
				
				JOptionPane.showMessageDialog(telaPonto, "Ponto batido");
				
				btnDetalhes.setEnabled(true);
				btnDetalhes.addActionListener(new OuvinteBtnMostrarDetalhes());
				
			} catch (Exception e1) {
				
				JOptionPane.showMessageDialog(telaPonto, e1.getMessage());
			}
		}
	}
	
	public class OuvinteBtnMostrarDetalhes implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				
				controllerTelaPonto.pegarDetalhes(LocalDateTime.now(), ((ItemProjeto) cbxProjetos.getSelectedItem()).getCodigo());
				
			} catch (Exception ex) {
				
				JOptionPane.showMessageDialog(telaPonto, ex.getMessage());
			}
		}
	}
	
	public class OuvinteBtnCadastrarProjeto implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			telaPonto.dispose();
			controllerTelaPonto.cadastrarProjeto();
		}
	}
	
	public class OuvinteBtnJustificarPonto implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			telaPonto.dispose();
			controllerTelaPonto.JustificarPonto();
		}
		
	}
	
	public TelaPonto() {
			
		try {
			controllerTelaPonto = new ControllerTelaPonto();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		setarPrincipaisConfiguracoes();
		addBotoes();
		addLabels();
		addCamposDeEntrada();
		iniciar();
	}
	

	private void setarPrincipaisConfiguracoes() {
		FactorySwingComponents.gerarLookAndFeelNimbus();
		
		setTitle("Pontos e Detalhes");
		setSize(400, 540);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
	}
	
	private void iniciar() {
		setVisible(true);
	}
	
	private void addLabels() {
		JLabel lblPrincipal = gerarLabel("Bata um ponto e Veja detalhes", 0, 50, 400, 30, JLabel.CENTER);
		lblPrincipal.setFont(new Font("Arial Narrow", Font.ITALIC, 23));
		add(lblPrincipal);
		
		add(gerarLabel("Projeto", 50, 120, 50, 30, JLabel.LEFT));

	}
	
	private JLabel gerarLabel(String texto, int x , int y, int width, int height, int alinhamento) {
		JLabel label = new JLabel(texto);
		label.setBounds(x, y, width, height);
		label.setHorizontalAlignment(alinhamento);
		label.setFont(fonteParaTextos);
		return label;
	}
	
	private void addBotoes() {
		Font fontBotoesComuns = new Font("Arial", Font.PLAIN, 15);
		
		JButton btnBaterPonto = gerarBotao("Bater Ponto", 125, 180, 150, 30, fontBotoesComuns);
		btnBaterPonto.addActionListener(new OuvinteBtnBaterPonto());
		add(btnBaterPonto);
		
		btnDetalhes = gerarBotao("Ver Detalhes", 125, 230, 150, 30, fontBotoesComuns);
		btnDetalhes.setEnabled(false); // Sera habilitado se o ponto for batido corretamente
		add(btnDetalhes);
		
		btnCadastrarProjeto = gerarBotao("Gerenciar Projetos", 125, 280, 150, 30, fontBotoesComuns);
		btnCadastrarProjeto.addActionListener(new OuvinteBtnCadastrarProjeto());
		add(btnCadastrarProjeto);
		
		btnJustificarPonto = gerarBotao("Justificar Ponto", 125, 330, 150, 30, fontBotoesComuns);
		btnJustificarPonto.addActionListener(new OuvinteBtnJustificarPonto());
		add(btnJustificarPonto);
		
	}

	private JButton gerarBotao(String texto, int x , int y, int width, int height, Font font) {
		JButton button = new JButton(texto);
		button.setBounds(x, y, width, height);
		button.setFont(font);
		return button;
	}
	
	private void addCamposDeEntrada() {
		
		// Acoplamento com Projeto somente para exibicao
		try {
			
			cbxProjetos = new JComboBox<Projeto>(controllerTelaPonto.getProjetos());
		}catch (Exception e) {
			e.printStackTrace();
		}
		cbxProjetos.setBounds(120, 120, 200, 30);
		cbxProjetos.setToolTipText("Selecione um projeto");
		add(cbxProjetos);
	}
	
}
