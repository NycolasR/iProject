package view.autenticacao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import controller.autenticacao.ControllerTelaCriarConta;
import view.projetos.OuvinteFocoJTextField;
/**
 * 
 * @author NPG
 *
 * Essa classe é um produto concreto do padrão abstract factory
 * 
 */
public class TelaCriarContaSwing extends JPanel implements TelaCriarConta{

	
	private static final long serialVersionUID = 1L;

	private ControllerTelaCriarConta controllerTelaCriarConta;
	private JTextField nomeField, emailField;
	private JFormattedTextField matriculaField;
	private JPasswordField passwordField;
	private Font fonteParaTextos = new Font("Arial", Font.PLAIN, 15);
	
	public class OuvinteBtnCadastrar implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if(nomeField.getText().isEmpty() || emailField.getText().isEmpty() || String.valueOf(passwordField.getPassword()).isEmpty()) {
				JOptionPane.showMessageDialog(null, "Preencha todos os campos", null, JOptionPane.WARNING_MESSAGE);
				
			}else if(matriculaField.getText().trim().length() < 9 ) {
				JOptionPane.showMessageDialog(null, "Matricula deve conter 9 digitos", null, JOptionPane.WARNING_MESSAGE);
			}else {
				try {
					controllerTelaCriarConta.cadastrarConta(nomeField.getText(), Integer.parseInt(matriculaField.getText()),
							emailField.getText(), String.valueOf(passwordField.getPassword()));
					JOptionPane.showMessageDialog(null, "Conta Cadastrada com Sucesso", null, JOptionPane.WARNING_MESSAGE);
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}
	}
	
	
	public TelaCriarContaSwing() {
		controllerTelaCriarConta = new ControllerTelaCriarConta();
		adicionarPrincipaisConfiguracoes();
		addCamposDeEntradaTelaCriarConta();
		addLabelsTelaCriarConta();
		addBotoesTelaCriarConta();
		setVisible(true);
	}
	
	
	@Override
	public void addCamposDeEntradaTelaCriarConta() {
		nomeField = new JTextField();
		nomeField.setToolTipText("digite o seu nome");
		nomeField.setBounds(150, 100, 200, 30);
		nomeField.addFocusListener(new OuvinteFocoJTextField(nomeField));
		add(nomeField);
		
		emailField = new JTextField();
		emailField.setToolTipText("exemplo@exemplo.com");
		emailField.setBounds(150,200, 200, 30);
		emailField.addFocusListener(new OuvinteFocoJTextField(emailField));
		add(emailField);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Sua senha");
		passwordField.setBounds(150, 250, 200, 30);
		passwordField.addFocusListener(new OuvinteFocoJTextField(passwordField));
		add(passwordField);
		
		try {
			
			MaskFormatter formatoMatriculaECodigo = new MaskFormatter("#########");
			matriculaField = new JFormattedTextField(formatoMatriculaECodigo);
			matriculaField.setToolTipText("000000000");
			matriculaField.setBounds(150, 150, 200, 30);
			add(matriculaField);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addLabelsTelaCriarConta() {
		JLabel lblPrincipal = gerarLabel("CADASTRAR CONTA", 0, 35, 400, 30, JLabel.CENTER);
		lblPrincipal.setFont(new Font("Arial Narrow", Font.ITALIC, 23));
		add(lblPrincipal);
		
		add(gerarLabel("Nome:", 50, 100, 100, 30, JLabel.LEFT));
		add(gerarLabel("Matricula:", 50, 150, 100, 30, JLabel.LEFT));
		add(gerarLabel("E-mail:", 50, 200, 100, 30, JLabel.LEFT));
		add(gerarLabel("Senha:", 50, 250, 100, 30, JLabel.LEFT));
		add(gerarLabel("Já tem uma conta?", 40 , 370, 200, 30, JLabel.LEFT));
	}
	
	private JLabel gerarLabel(String texto, int x , int y, int width, int height, int alinhamento) {
		JLabel label = new JLabel(texto);
		label.setBounds(x, y, width, height);
		label.setHorizontalAlignment(alinhamento);
		label.setFont(fonteParaTextos);
		return label;
	}
	
	@Override
	public void addBotoesTelaCriarConta() {
		Font fontBotoesComuns = new Font("Arial", Font.PLAIN, 15);
		
		JButton btnCadastrar = gerarBotao("Cadastrar", 210, 300, 150, 30, fontBotoesComuns);
		btnCadastrar.addActionListener(new OuvinteBtnCadastrar());
		add(btnCadastrar);
		
	}

	private JButton gerarBotao(String texto, int x , int y, int width, int height, Font font) {
		JButton button = new JButton(texto);
		button.setBounds(x, y, width, height);
		button.setFont(font);
		return button;
	}
	private void adicionarPrincipaisConfiguracoes() {
		setSize(350,400);
		setLayout(null);
		this.setBounds(0, 0, 385, 490);	}
}
