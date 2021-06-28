package view.projetos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import controller.projetos.ControllerTelaPrincipal;
import view.autenticacao.TelaConfiguracaoAdmin;
/**
 * 
 * @author NPG
 * 
 * Essa é a tela principal do projeto, que ficará visível apenas para um membro
 * administrador, possibilitando o cadastro de membros, grupos, editais e projetos.
 * No entanto, a equipe descidiu possibilitar a qualquer usuário a possibilidade
 * dele criar um projeto caso ele queira, sem necessariamente ele ser um administrador,
 * dessa forma posiibilitando a esse usuário acesso a uma parte da tela principal.
 *
 */
public class TelaPrincipal extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ControllerTelaPrincipal controllerTelaPrincipal;
	private TelaPrincipal telaPrincipal = this;
	private JButton btnFazerLogoff, btnBaterPonto, btnCadastrarEdital, btnCadastrarProjeto, btnCadastrarGrupo, btnGerenciarMembro, btnVoltar, btnAdicionar, btnRemover, btnAtualizar;
	private FabricaTela fabricaTelaCadastro;
	private TelaCadastroEditais telaCadastroEditais;
	private TelaCadastroProjetos telaCadastroProjetos;
	private TelaCadastroGrupos telaCadastroGrupos;
	private TelaConfiguracaoAdmin telaConfiguracaoAdmin;
	private boolean isAdmin;
	
	public TelaPrincipal() {
		controllerTelaPrincipal = new ControllerTelaPrincipal(telaPrincipal);
		adicionarConfiguracoesBasicas();
		adicionarBotoes();
	}
	
	public class OuvinteFazerLogoff implements ActionListener {
		
		public void actionPerformed(ActionEvent evento) {
			controllerTelaPrincipal.fazerLogoff();
	
		}
	}
	
	public class OuvinteAdicionar implements ActionListener {
		
		public void actionPerformed(ActionEvent evento) {
			
			if(telaCadastroEditais != null) {
				
				telaCadastroEditais.adicionar();

			
			}else if(telaCadastroProjetos != null) {
				
				telaCadastroProjetos.adicionar();
				
			}else {
				
				telaCadastroGrupos.adicionar();
			}
		}
	}
	
	public class OuvinteRemover implements ActionListener{
		
		public void actionPerformed(ActionEvent evento) {
			
			if(telaCadastroEditais != null) {
				
				telaCadastroEditais.remover();
			
			}else if(telaCadastroProjetos != null) {
				
				telaCadastroProjetos.remover();
				
			}else {
				
				telaCadastroGrupos.remover();
			}
			
		}
	}
	
	public class OuvinteAtualizar implements ActionListener{
		
		public void actionPerformed(ActionEvent evento) {
			
			if(telaCadastroEditais != null) {
				
				telaCadastroEditais.atualizar();
			
			}else if(telaCadastroProjetos != null) {
				
				telaCadastroProjetos.atualizar();
				
			}else {
				
				telaCadastroGrupos.atualizar();
			}
			
			
			
		}
	}
	
	public class OuvinteGerenciarMembro implements ActionListener{
		
		public void actionPerformed(ActionEvent evento) {
			
			fabricaTelaCadastro = new FabricaTelaSwing();
			telaConfiguracaoAdmin = fabricaTelaCadastro.fabricarTelaConfiguracaoAdmin();
			adicionarJPanel(telaConfiguracaoAdmin);
			
			modificarTamanho(400, 490);
			btnVoltar.setBounds(290,415,90,35);
			mostrarBotoes(false, false, true);
			
		}
	}
	
	public class OuvinteDosBotoes implements ActionListener{

		public void actionPerformed(ActionEvent evento) {

			if(evento.getActionCommand().equals("VOLTAR")) {
				
				if(!isAdmin) {
					
					telaPrincipal.dispose();
					controllerTelaPrincipal.voltaPraTelaPonto();
					
				}else {
					modificarTamanho(300, 450);
					mostrarBotoes(true, false, false);
	
					if(telaCadastroEditais != null) {
						removerJPanel(telaCadastroEditais);
						
					} else if(telaCadastroProjetos != null) {
						removerJPanel(telaCadastroProjetos);
						
					} else if(telaCadastroGrupos != null) {	
						removerJPanel(telaCadastroGrupos);
					}else {
						removerJPanel(telaConfiguracaoAdmin);
						btnVoltar.setBounds(440,590,90,35);
					}
				}
				
			} else {

				fabricaTelaCadastro = new FabricaTelaSwing();
				
				if(evento.getActionCommand().equals("GERENCIAR EDITAL")) {
					
					telaCadastroEditais = (fabricaTelaCadastro.fabricarTelaCadastroEditais());
					telaCadastroEditais.mostrarEditaisUsuarioLogado();
					
					adicionarJPanel(telaCadastroEditais);
					
					
				} else if(evento.getActionCommand().equals("GERENCIAR PROJETO")){
					
					criarTelaCadastroProjetos();
//					mostrarBotoes(false, true, true);
					
				} else if(evento.getActionCommand().equals("Bater Ponto")){
					
					telaPrincipal.dispose();
					controllerTelaPrincipal.voltaPraTelaPonto();
					
				} else {
					
					telaCadastroGrupos = fabricaTelaCadastro.fabricarTelaCadastroGrupos();
					telaCadastroGrupos.mostrarGruposDoUsuarioLogado();
					
					adicionarJPanel(telaCadastroGrupos);
				}

				modificarTamanho(600, 700);
				mostrarBotoes(false, true, true);
			}
		}
	}
	
	public void validarAdminCoordenador(boolean isAdmin) {
		
		this.isAdmin = isAdmin;
		
		if(!isAdmin) {
			criarTelaCadastroProjetos();
//			mostrarBotoes(false, true, false);
		}
		
		setVisible(true);	
	}
	
	private void criarTelaCadastroProjetos() {
		
		fabricaTelaCadastro = new FabricaTelaSwing();
		telaCadastroProjetos = fabricaTelaCadastro.fabricarTelaCadastroProjetos();
		telaCadastroProjetos.mostrarProjetosDoUsuarioLogado();
		
		adicionarJPanel(telaCadastroProjetos);
		
		modificarTamanho(600, 700);
		mostrarBotoes(false, true, true);
		
	}
	
	private void adicionarBotoes() {
		
		btnCadastrarEdital = new JButton("GERENCIAR EDITAL");
		btnCadastrarProjeto = new JButton("GERENCIAR PROJETO");
		btnCadastrarGrupo = new JButton("GERENCIAR GRUPO");
		btnGerenciarMembro = new JButton("GERENCIAR MEMBRO");
		btnFazerLogoff = new JButton("Fazer Logoff");
		btnBaterPonto = new JButton("Bater Ponto");
		btnVoltar = new JButton("VOLTAR");
		btnAdicionar = new JButton("ADICIONAR");
		btnRemover = new JButton("REMOVER");
		btnAtualizar = new JButton("ATUALIZAR");
		
		btnCadastrarEdital.setBounds(60, 50, 170, 50);
		btnCadastrarProjeto.setBounds(60, 130, 170, 50);
		btnCadastrarGrupo.setBounds(60, 210, 170, 50);
		btnGerenciarMembro.setBounds(60, 290, 170, 50);
		btnFazerLogoff.setBounds(160, 375, 120, 30);
		btnBaterPonto.setBounds(20, 375, 120, 30);
		btnVoltar.setBounds(440,590,90,35);
		btnAdicionar.setBounds(50,590,100,35);
		btnRemover.setBounds(180,590,100,35);
		btnAtualizar.setBounds(310,590,100,35);
		
		btnCadastrarEdital.addActionListener(new OuvinteDosBotoes());
		btnCadastrarProjeto.addActionListener(new OuvinteDosBotoes());
		btnCadastrarGrupo.addActionListener(new OuvinteDosBotoes());
		btnGerenciarMembro.addActionListener(new OuvinteGerenciarMembro());
		btnFazerLogoff.addActionListener(new OuvinteFazerLogoff());
		btnBaterPonto.addActionListener(new OuvinteDosBotoes());
		btnVoltar.addActionListener(new OuvinteDosBotoes());
		btnAdicionar.addActionListener(new OuvinteAdicionar());
		btnRemover.addActionListener(new OuvinteRemover());
		btnAtualizar.addActionListener(new OuvinteAtualizar());
		
		add(btnCadastrarEdital);
		add(btnCadastrarProjeto);
		add(btnCadastrarGrupo);
		add(btnGerenciarMembro);
		add(btnFazerLogoff);
		add(btnVoltar);
		add(btnAdicionar);
		add(btnRemover);
		add(btnAtualizar);
		add(btnBaterPonto);
		
		btnVoltar.setVisible(false);
		btnAdicionar.setVisible(false);
		btnRemover.setVisible(false);
		btnAtualizar.setVisible(false);
	}
	
	private void mostrarBotoes(boolean primarios, boolean secundarios, boolean voltar) {
		
		btnCadastrarEdital.setVisible(primarios);
		btnCadastrarProjeto.setVisible(primarios);
		btnCadastrarGrupo.setVisible(primarios);
		btnGerenciarMembro.setVisible(primarios);
		btnFazerLogoff.setVisible(primarios);
		btnBaterPonto.setVisible(primarios);
		
		btnAdicionar.setVisible(secundarios);
		btnRemover.setVisible(secundarios);
		btnAtualizar.setVisible(secundarios);
		
		btnVoltar.setVisible(voltar);
	}
	
	private void modificarTamanho(int x, int y) {
		
		setSize(x, y);
		setLocationRelativeTo(null);
	}
	
	private void adicionarJPanel(Object jpanel) {
		
		add((JPanel)jpanel);
	}
	
	private void removerJPanel(Object jpanel) {
		
		this.remove((JPanel)jpanel);
		telaCadastroEditais = null;
		telaCadastroProjetos = null;
		telaCadastroGrupos = null;
	}
		
	private void adicionarConfiguracoesBasicas() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		modificarTamanho(300, 450);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.DARK_GRAY);
	}
		
}
