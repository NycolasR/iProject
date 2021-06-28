package view.autenticacao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import controller.autenticacao.ControllerTelaAutenticacao;
import view.projetos.OuvinteFocoJTextField;
/**
 * 
 * @author NPG
 *
 * Essa classe é um produto concreto do padrão abstract factory
 *
 */
public class TelaAutenticacaoSwing extends JPanel implements TelaAutenticacao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ControllerTelaAutenticacao controllerTelaAutenticacao;
	private JRadioButton jrbProvInterno;
	private JRadioButton jrbProvSMTP;
	private JTextField emailField;
	private JPasswordField passwordField;
	
	private Font fonteParaTextos = new Font("Arial", Font.PLAIN, 15);
	
	
	public class OuvinteBtnIniciar implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(emailField.getText().isEmpty() || String.valueOf(passwordField.getPassword()).isEmpty()) {
				JOptionPane.showMessageDialog(null, "Preencha todos os campos", null, JOptionPane.WARNING_MESSAGE);
			} else {
				
				try {
					boolean cond = controllerTelaAutenticacao.fazerLogin(emailField.getText(),
							String.valueOf(passwordField.getPassword()), jrbProvInterno.isSelected());
					
					if(cond) {
						controllerTelaAutenticacao.proximaTela();
					} else {
						JOptionPane.showMessageDialog(null, "[ERRO] Verifique suas credenciais", null, JOptionPane.ERROR_MESSAGE);
					}
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public TelaAutenticacaoSwing() {
		controllerTelaAutenticacao = new ControllerTelaAutenticacao();
		adicionarPrincipaisConfiguracoes();
		addCamposDeEntradaTelaAutenticacao();
		addLabelsTelaAutenticacao();
		addRadioButtonsTelaAutenticacao();
		addBotoesTelaAutenticacao();
		setVisible(true);
	}
	
	@Override
	public void addCamposDeEntradaTelaAutenticacao() {
		emailField = new JTextField();
		emailField.setToolTipText("exemplo@exemplo.com");
		emailField.setBounds(120, 100, 200, 30);
		emailField.addFocusListener(new OuvinteFocoJTextField(emailField));
		add(emailField);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Sua senha");
		passwordField.setBounds(120, 150, 200, 30);
		passwordField.addFocusListener(new OuvinteFocoJTextField(passwordField));
		add(passwordField);
	}
	
	@Override
	public void addRadioButtonsTelaAutenticacao() {
		jrbProvInterno = gerarRadioButton("Provedor Interno", 30, 250, 130, 30);
		jrbProvInterno.setSelected(true);
		
		jrbProvSMTP = gerarRadioButton("Provedor SMTP", 30, 285, 130, 30);
		
		add(jrbProvInterno);
		add(jrbProvSMTP);
		
		ButtonGroup opcoesProvedores = new ButtonGroup(); 
		opcoesProvedores.add(jrbProvInterno);
		opcoesProvedores.add(jrbProvSMTP);
	}
	
	private JRadioButton gerarRadioButton(String texto, int x , int y, int width, int height) {
		JRadioButton jrb = new JRadioButton(texto);
		jrb.setBounds(x, y, width, height);
		jrb.setFont(fonteParaTextos);
		return jrb;
	}
	
	@Override
	public void addLabelsTelaAutenticacao() {
		JLabel lblPrincipal = gerarLabel("Faça Login", 0, 30, 400, 30, JLabel.CENTER);
		lblPrincipal.setFont(new Font("Verdana", Font.ITALIC,25));
		add(lblPrincipal);
		
		add(gerarLabel("E-mail", 50, 100, 50, 30, JLabel.LEFT));
		add(gerarLabel("Senha", 50, 150, 50, 30, JLabel.LEFT));
		add(gerarLabel("Como deseja fazer login?", 15, 210, 200, 30, JLabel.LEFT));
		add(gerarLabel("Não tem uma conta?", 40 , 370, 200, 30, JLabel.LEFT));
		
	}
	
	private JLabel gerarLabel(String texto, int x , int y, int width, int height, int alinhamento) {
		JLabel label = new JLabel(texto);
		label.setBounds(x, y, width, height);
		label.setHorizontalAlignment(alinhamento);
		label.setFont(fonteParaTextos);
		return label;
	}
	
	@Override
	public void addBotoesTelaAutenticacao() {
		Font fontBotoesComuns = new Font("Arial", Font.PLAIN, 15);
		
		JButton btnEntrar = gerarBotao("Entrar", 210,210, 150, 30, fontBotoesComuns);
		btnEntrar.addActionListener(new OuvinteBtnIniciar());
		add(btnEntrar);
		
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
		this.setBounds(0, 0, 385, 450);
	}	
}
