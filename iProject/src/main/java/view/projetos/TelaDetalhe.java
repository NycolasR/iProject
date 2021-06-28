package view.projetos;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import util.FactorySwingComponents;
/**
 * 
 * @author NPG
 *
 *Essa classe é responsável por mostrar os detalhes de uma participação 
 */
public class TelaDetalhe extends JFrame{
	

	private static final long serialVersionUID = 1L;

	public TelaDetalhe(StringBuffer detalhes) {
		
		setarConfiguracoes();
		gerarArea(detalhes);
		this.setVisible(true);

	}
	
	private void setarConfiguracoes() {
		FactorySwingComponents.gerarLookAndFeelNimbus();
		
		setTitle("Detalhes");
		setSize(400, 530);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
	}
	
	private void gerarArea(StringBuffer detalhes) {
		
		String detalhe = String.valueOf(detalhes);
		
		JTextArea area = new JTextArea(detalhe);
		
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setEditable(false);
		
		JScrollPane jScrollPane = new JScrollPane(area);
		jScrollPane.setBounds(20,20,340,400);
		
		add(jScrollPane);
	}
	

}
