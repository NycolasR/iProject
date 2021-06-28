package view.projetos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import controller.projetos.ControllerTelaCadastroEditais;

/**
 * 
 * @author NPG
 * 
 * Essa classe é um produto concreto do padrão abstract factory
 *
 */
public class TelaCadastroEditaisSwing extends JPanelTelaCadastroSwing implements TelaCadastroEditais{

	private static final long serialVersionUID = 1L;
	
	private ControllerTelaCadastroEditais controllerTelaCadastroEditais;
	private TelaCadastroEditaisSwing telaCadastroEditaisSwing = this;
	
	public TelaCadastroEditaisSwing() {	
		controllerTelaCadastroEditais = new ControllerTelaCadastroEditais();
	}
	
	public class OuvinteDoBotaoDetalhes implements ActionListener{

		public void actionPerformed(ActionEvent evento) {
			
			if(telaCadastroEditaisSwing.getTabela().getSelectedRow() != -1) {
				
				int linha = telaCadastroEditaisSwing.getTabela().getSelectedRow();
				
				if(evento.getActionCommand().equals("Mostrar Detalhes no Navegador")) {
					
					controllerTelaCadastroEditais.mostrarDetalhesEmHTML(linha);
				
				}else {
					
					controllerTelaCadastroEditais.mostrarDetalhesEmTela(linha);
				}
				
				
			} else {
				
				JOptionPane.showMessageDialog(telaCadastroEditaisSwing, "Selecione um edital", null, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void mostrarEditaisUsuarioLogado() {
		
		adicionarLabels();	
		adicionarTabela();
		adicionarCampos();
		adicionarBotao();
	}
	
	private void adicionarBotao() {
		JButton btnDetalhesHTML = new JButton("Mostrar Detalhes no Navegador");
		JButton btnDetalhesSwing = new JButton("Mostrar Detalhes em Tela");
		
		btnDetalhesHTML.setBounds(150, 420, 215, 30);
		btnDetalhesSwing.setBounds(150, 460, 215, 30);
		
		btnDetalhesHTML.addActionListener(new OuvinteDoBotaoDetalhes());
		btnDetalhesSwing.addActionListener(new OuvinteDoBotaoDetalhes());
		
		add(btnDetalhesHTML);
		add(btnDetalhesSwing);
	}
	
	private void adicionarCampos() {
		
		super.adicionarCampos("Informe a matricula do membro que é o administrador");
		super.adicionarCamposDatas();

	}
	
	private void adicionarTabela() {
		
		super.adicionarTabela(controllerTelaCadastroEditais.getTodoOsEditaisDoSistema());
		
	}

		
	private void adicionarLabels() {
		
		super.adicionarLabels("EDITAIS");
		super.adicionarLabelsDatas();
		
	}
	
	private void atualizarTabela() {
		
		super.atualizarTabela(controllerTelaCadastroEditais.getTodoOsEditaisDoSistema()); 
	}
	
	public void preencherCampos() throws Exception{
		
		String[] campos = controllerTelaCadastroEditais.getCamposParaPreencher(super.getTabela().getSelectedRow());
				
		super.getCampoDataInicio().setText(campos[0]);
		super.getCampoDataTermino().setText(campos[1]);
		super.getCampoNome().setText(campos[2]);
	}
	
	
	public void adicionar() {
		
		try {
			
			controllerTelaCadastroEditais.adicionarNovoEdital( super.getCampoNome().getText().trim(),
					super.getCampoDataInicio().getText(), super.getCampoDataTermino().getText());
			
			atualizarTabela();
			
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void remover() {
		
		try {
			
			controllerTelaCadastroEditais.removerEditalExistente(super.getTabela().getSelectedRow());
			
			atualizarTabela();
			
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	public void atualizar() {
		
		try {
			
			controllerTelaCadastroEditais.atualizarEditalExistente( super.getCampoNome().getText().trim(),
					super.getCampoDataInicio().getText(), super.getCampoDataTermino().getText(), super.getTabela().getSelectedRow());
			
			atualizarTabela();
			
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	
}
