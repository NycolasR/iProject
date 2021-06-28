package view.projetos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import controller.projetos.ControllerTelaCadastroProjetos;
import model.autenticacao.Membro;
import model.projetos.Edital;

/**
 * 
 * @author NPG
 * 
 * Essa classe é um produto concreto do padrão abstract factory
 *
 */
public class TelaCadastroProjetosSwing extends JPanelTelaCadastroSwing implements TelaCadastroProjetos {

	private static final long serialVersionUID = 1L;

	private ControllerTelaCadastroProjetos controllerTelaCadastroProjetos;
	private JFormattedTextField campoCusteio;
	private JFormattedTextField campoCapital;
	private JFormattedTextField campoCusteioMensal;
	private JComboBox<Membro> comboMembros;
	private JButton btnAdicionarMembro, btnRemoverMembro;
	private JLabel membros, capital, custeio, custeioMensal;
	private TelaCadastroProjetosSwing telaCadastroProjetosSwing = this;
	
	
	public  TelaCadastroProjetosSwing() {

		controllerTelaCadastroProjetos = new ControllerTelaCadastroProjetos();
	}
	
	public class OuvinteDosBotoesAdiconarERemover implements ActionListener{

		public void actionPerformed(ActionEvent evento) {
			
			try {
				
				if(evento.getActionCommand().equals("Adicionar Membro")) {
					
					controllerTelaCadastroProjetos.adicionarMembro(comboMembros.getSelectedIndex(), getTabela().getSelectedRow(),
							getCampoDataInicio().getText(), getCampoDataTermino().getText(), campoCusteioMensal.getText().trim());
				
					JOptionPane.showMessageDialog(telaCadastroProjetosSwing, "Membro adicionado com sucesso");
					
				}else if(evento.getActionCommand().equals("Remover Membro")){
					
					controllerTelaCadastroProjetos.removerMembro(comboMembros.getSelectedIndex(), getTabela().getSelectedRow());
				
					JOptionPane.showMessageDialog(telaCadastroProjetosSwing, "Membro removido com sucesso");
				
				}
				
			}catch (Exception e) {
				
				JOptionPane.showMessageDialog(telaCadastroProjetosSwing, e.getMessage());
			}

		}
		
	}
	
	public void mostrarProjetosDoUsuarioLogado() {
		
		adicionarLabels();
		adicionarTabela();
		adicionarCampos();
		adicionarCombo();
		adicionarBotao();
		setIsCordenador(controllerTelaCadastroProjetos.isCoordenadorDeAlgumProjeto());
	}
	
	private void adicionarCampos() {
		
		super.adicionarCampos("Informe a matricula do membro que irá se tornar, ou já é, o coordenador de um projeto");
		super.adicionarCamposDatas();
		
		try {
			
			campoCusteio = new JFormattedTextField();
			campoCapital = new JFormattedTextField();
			campoCusteioMensal = new JFormattedTextField();
			
			campoCusteio.setBounds(30,430,80,30);
			campoCapital.setBounds(130,430,80,30);
			campoCusteioMensal.setBounds(240,430,80,30);
			
			campoCusteioMensal.setToolTipText("Informe o valor da bolsa que o coordenador vai receber por mês");
			
			add(campoCusteio);
			add(campoCapital);
			add(campoCusteioMensal);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void adicionarBotao() {
		
		btnAdicionarMembro = new JButton("Adicionar Membro");
		btnRemoverMembro = new JButton("Remover Membro");
		
		btnAdicionarMembro.setBounds(30, 470, 140, 30);
		btnRemoverMembro.setBounds(320, 470, 140, 30);
		
		btnAdicionarMembro.addActionListener(new OuvinteDosBotoesAdiconarERemover());
		btnRemoverMembro.addActionListener(new OuvinteDosBotoesAdiconarERemover());
		
		add(btnAdicionarMembro);
		add(btnRemoverMembro);
		
	}
	
	private void adicionarTabela() {
		
		super.adicionarTabela(controllerTelaCadastroProjetos.getTodoOsProjetosDoSistema());
	}
	
	private void adicionarLabels() {
		
		super.adicionarLabels("PROJETOS");
		super.adicionarLabelsDatas();
		
		custeio = new JLabel("Custeio");
		capital = new JLabel("Capital");
		custeioMensal = new JLabel("Custeio Mensal");
		membros = new JLabel("Membros");
		
		custeio.setBounds(50, 410, 80, 20);
		capital.setBounds(145, 410, 80, 20);
		custeioMensal.setBounds(235, 410, 100, 20);
		membros.setBounds(380, 410, 100, 20);

		add(custeio);
		add(capital);
		add(custeioMensal);
		add(membros);
	}
	
	private void adicionarCombo() {

		comboMembros = new JComboBox<Membro>(controllerTelaCadastroProjetos.getTodosOsMembros());
		
		comboMembros.setBounds(360,430,100,30);
		
		add(comboMembros);
	}
	
	private void setIsCordenador(boolean isCordenador) {
		
		comboMembros.setVisible(isCordenador);
		btnAdicionarMembro.setVisible(isCordenador);
		btnRemoverMembro.setVisible(isCordenador);
		membros.setVisible(isCordenador);
		
		if(!isCordenador) {
			
			campoCapital.setBounds(210,430,80,30);
			campoCusteioMensal.setBounds(380,430,80,30);
			
			capital.setBounds(227, 410, 80, 20);
			custeioMensal.setBounds(375, 410, 100, 20);
			
			
		}
		
		
	}
	
	private void atualizarTabela() {
		
		super.atualizarTabela(controllerTelaCadastroProjetos.getTodoOsProjetosDoSistema()); 
	}
	
	
	/**
	 * 
	 * Metodo que adiciona um projeto a um edital
	 */
	private void adicionarProjetoAUmEdital() throws Exception{
		
		Edital[] posicoes = controllerTelaCadastroProjetos.getTodosOsEditaisDoSistema();
		
		if(posicoes.length == 0) {
			
			throw new Exception("Nenhum edital no sistema para adicionar o projeto");
		}
		
		Edital resposta = null;
		
		while(resposta == null) {
			
		
			resposta = (Edital) JOptionPane.showInputDialog(this, "Escolha um Edita para adicionar seu projeto", null,
					JOptionPane.PLAIN_MESSAGE, null, posicoes,posicoes[0]);
		}
				
		controllerTelaCadastroProjetos.adicionarProjetoAUmEdital(controllerTelaCadastroProjetos.getPosicaoDoUltimoProjetoAdicionado(), resposta.getCodigo());
		
		JOptionPane.showMessageDialog(this, "Pojeto adicionado a um edital com sucesso");
	}
	
	
	
	public void preencherCampos() throws Exception{
		
		String[] campos = controllerTelaCadastroProjetos.getCamposParaPreencher(super.getTabela().getSelectedRow());
				
		super.getCampoDataInicio().setText(campos[0]);
		super.getCampoDataTermino().setText(campos[1]);
		super.getCampoNome().setText(campos[2]);
		
		campoCusteio.setText(campos[3]);
		campoCapital.setText(campos[4]);
				
	}
	
	
	public void adicionar() {
		
		try {
			
			controllerTelaCadastroProjetos.adicionarNovoProjeto(super.getCampoNome().getText().trim(), campoCusteio.getText().trim(), campoCapital.getText().trim(),
					super.getCampoDataInicio().getText(), super.getCampoDataTermino().getText(), campoCusteioMensal.getText().trim());
			
			atualizarTabela();
			adicionarProjetoAUmEdital();
			
			
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void remover() {
		
		try {
			
			controllerTelaCadastroProjetos.removerProjetoExistente(super.getTabela().getSelectedRow());
			
			atualizarTabela();
			
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void atualizar() {
		
		try {
			
			controllerTelaCadastroProjetos.atualizarProjetoExistente(super.getCampoNome().getText().trim(), campoCusteio.getText().trim(),
					campoCapital.getText().trim(), super.getTabela().getSelectedRow());
			
			atualizarTabela();
			
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
}
