package view.autenticacao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.autenticacao.ControllerTelaConfiguracaoAdmin;
import model.autenticacao.Membro;
/**
 * 
 * @author NPG
 *
 * Essa classe é um produto concreto do padrão abstract factory
 * 
 */
public class TelaConfiguracaoAdminSwing extends JPanel implements TelaConfiguracaoAdmin {


	private static final long serialVersionUID = 1L;
	
	private ControllerTelaConfiguracaoAdmin controllerTelaConfiguracaoAdmin;
	private JScrollPane tabelaJScrollPane;
	private JTable tabela;
	
	private Font fonteParaTextos = new Font("Arial", Font.PLAIN, 15);
	
	public class OuvinteDoBotaoHabilitar implements ActionListener{

		public void actionPerformed(ActionEvent evento) {
			
			if(tabela.getSelectedRow() != -1) {
				
				controllerTelaConfiguracaoAdmin.tornarOuDesabilitarMembroAdministrador( (long) tabela.getValueAt(tabela.getSelectedRow(), 1), (String) tabela.getValueAt(tabela.getSelectedRow(), 2));
			
				controllerTelaConfiguracaoAdmin = new ControllerTelaConfiguracaoAdmin();
				
				atualizarTabela();
			
			} else {
				
				JOptionPane.showMessageDialog(null, "Selecione um membro", null, JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	
	public TelaConfiguracaoAdminSwing() {
		controllerTelaConfiguracaoAdmin = new ControllerTelaConfiguracaoAdmin();
		adicionarPrincipaisConfiguracoes();
		addTabelaDeMembrosCadastradosTelaConfiguracaoAdmin();
		addBotoesTelaConfiguracaoAdmin();
		addLabelsTelaConfiguracaoAdmin();
		setVisible(true);
	}
	
	
	@Override
	public void addLabelsTelaConfiguracaoAdmin() {
		JLabel lblPrincipal = gerarLabel("Membros Cadastrados", 0, 20, 400, 30, JLabel.CENTER);
		lblPrincipal.setFont(new Font("Arial Narrow", Font.ITALIC, 23));
		add(lblPrincipal);
		
		
	}
	
	private JLabel gerarLabel(String texto, int x , int y, int width, int height, int alinhamento) {
		JLabel label = new JLabel(texto);
		label.setBounds(x, y, width, height);
		label.setHorizontalAlignment(alinhamento);
		label.setFont(fonteParaTextos);
		return label;
	}
	
	@Override
	public void addBotoesTelaConfiguracaoAdmin() {
		JButton btnHabilitar = new JButton("Habilitar/Desabilitar");
		btnHabilitar.setBounds(117, 350, 150, 30);
		btnHabilitar.addActionListener(new OuvinteDoBotaoHabilitar());
		
		add(btnHabilitar);
	}
	
	@Override
	public void addTabelaDeMembrosCadastradosTelaConfiguracaoAdmin() {	
		Membro[] membros = controllerTelaConfiguracaoAdmin.getTodosOsMembros();
		tabelaJScrollPane = new JScrollPane(criarTabela(membros));
		tabelaJScrollPane.setBounds(0, 60, 385, 285);
		
		add(tabelaJScrollPane);
	}

	private JTable criarTabela(Membro[] membros) {
		
		DefaultTableModel modeloDaTabela = new DefaultTableModel();
		
		modeloDaTabela.addColumn("Nome");
		modeloDaTabela.addColumn("Matricula");
		modeloDaTabela.addColumn("Administrador");
		
		
		for(Membro membro : membros) {
			
			String situacao = "SIM";

			if(!membro.isAdministrador()) {
				situacao = "NAO";
			}
			
			Object[] linha = new Object[] {membro.getNome(),membro.getMatricula(),situacao};
			
			modeloDaTabela.addRow(linha);
		}
		tabela = new JTable(modeloDaTabela);
		
		return tabela;	
	}
	
	public void atualizarTabela() {
		
		remove(tabelaJScrollPane);
		addTabelaDeMembrosCadastradosTelaConfiguracaoAdmin();
		
	}
	
	private void adicionarPrincipaisConfiguracoes() {
		setSize(500, 600);
		setLayout(null);
		this.setBounds(0, 0, 385, 490);
	}


}
