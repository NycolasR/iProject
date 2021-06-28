package view.projetos;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import controller.projetos.ControllerTelaCadastroGrupos;

/**
 * 
 * @author NPG
 *
 *Essa classe é um produto concreto do padrão abstract factory
 *
 */
public class TelaCadastroGruposSwing extends JPanelTelaCadastroSwing implements TelaCadastroGrupos {

	private static final long serialVersionUID = 1L;
	private ControllerTelaCadastroGrupos controllerTelaCadastroGrupos;
	private JTextField campoLink;
	private JFormattedTextField campoDataCriacao;
	
	public TelaCadastroGruposSwing() {
		
		controllerTelaCadastroGrupos = new ControllerTelaCadastroGrupos();
	}

	public void mostrarGruposDoUsuarioLogado() {
		
		adicionarLabels();
		adicionarTabela();
		adicionarCampos();
	}
	
	private void adicionarCampos() {
		
		super.adicionarCampos("Informe a matricula do membro que é o administrador");
		
		try {
			
			MaskFormatter formatoData = new MaskFormatter("##/##/####");
			
			campoLink = new JTextField();
			campoDataCriacao = new JFormattedTextField(formatoData);
			
			campoLink.setBounds(120,300,260,30);
			campoDataCriacao.setBounds(210,430,80,30);

			add(campoLink);
			add(campoDataCriacao);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void adicionarTabela() {
		
		super.adicionarTabela(controllerTelaCadastroGrupos.getTodoOsGruposDoSistema());
		
	}
	
	private void adicionarLabels() {
		
		super.adicionarLabels("GRUPOS");
		
		JLabel link = new JLabel("Link");
		JLabel dataCriacao = new JLabel("Data Criação");
		
		link.setBounds(133, 280, 80, 20);
		dataCriacao.setBounds(120,430,100,30);
		
		add(link);
		add(dataCriacao);
	}
	
	private void atualizarTabela() {
		
		super.atualizarTabela(controllerTelaCadastroGrupos.getTodoOsGruposDoSistema()); 
	}
	
	public void preencherCampos() throws Exception{

		String[] campos = controllerTelaCadastroGrupos.getCamposParaPreencher(super.getTabela().getSelectedRow());
		
		campoDataCriacao.setText(campos[0]);
		campoLink.setText(campos[1]);
		super.getCampoNome().setText(campos[2]);

	}	
	
	public void adicionar() {
		
		try {
			
			controllerTelaCadastroGrupos.adicionarNovoGrupo( super.getCampoNome().getText().trim(),
					campoDataCriacao.getText(), campoLink.getText().trim());
		
			atualizarTabela();
			
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void remover() {
		
		try {
			
			controllerTelaCadastroGrupos.removerGrupoExistente(super.getTabela().getSelectedRow());
		
			atualizarTabela();
			
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void atualizar() {
		
		try {
			
			controllerTelaCadastroGrupos.atualizarGrupoExistente( super.getCampoNome().getText().trim(),
					campoDataCriacao.getText(), campoLink.getText().trim(),super.getTabela().getSelectedRow() );
			
			atualizarTabela();
			
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

}
