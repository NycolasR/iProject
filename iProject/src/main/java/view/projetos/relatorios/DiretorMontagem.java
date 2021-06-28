package view.projetos.relatorios;

import model.projetos.Edital;
import model.projetos.Grupo;
import model.projetos.Projeto;

/**
 * 
 * @author NPG
 * Esta classe representa o Diretor de montagem na implementacao do padrao builder. 
 */
public class DiretorMontagem {
	private InterfacePartesMontagem montadorFormato;
	private Object produto;
	
	public DiretorMontagem(InterfacePartesMontagem montadorFormato) {
		this.montadorFormato = montadorFormato;
	}
	
	public Object getProduto() throws Exception {
		if(produto == null)
			throw new Exception("Nenhum produto foi gerado.");
		
		return produto;
	}
	
	private void setProduto(Object obj) {
		this.produto = obj;
	}
	
	/**
	 * Forma de montagem 1: Gera um relatorio de um edital com seus projetos.
	 * @param codigo Codigo do edital no arquivo XML.
	 */
	public void gerarRelatorioEdital(Edital edital) {
		try {
			// Para garantir que um relatorio totalmente novo sera gerado, chama-se o metodo reiniciar
			montadorFormato.reiniciar();
			montadorFormato.gerarRelatorioEdital(edital);
			
			// Para cada projeto deste edital, haverá um espaço com os dados deste projeto.
			// É responsabilidade do diretor de montagem coordenar este aspecto de montagem.
			for (int i = 0; i < edital.getProjetos().size(); i++) {
				montadorFormato.gerarRelatorioProjeto((Projeto) edital.getProjetos().get(i));
			}
			
			encerrarEMostrar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Forma de montagem 3: Gera um relatorio de um grupo com seus projetos.
	 * @param codigo Codigo do grupo no arquivo XML.
	 */
	public void gerarRelatorioGrupo(Grupo grupo) {
		try {
			// Para garantir que um relatorio totalmente novo sera gerado, chama-se o metodo reiniciar
			montadorFormato.reiniciar();
			montadorFormato.gerarRelatorioGrupo(grupo);
			
			// Para cada projeto deste edital, haverá um espaço com os dados deste projeto
			// É responsabilidade do diretor de montagem coordenar este aspecto de montagem.
			for (int i = 0; i < grupo.getProjetos().size(); i++) {
				montadorFormato.gerarRelatorioProjeto((Projeto) grupo.getProjetos().get(i));
			}
			
			encerrarEMostrar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Forma de montagem 1: Gera um relatorio de um projeto.
	 * @param codigo Codigo do projeto no arquivo XML.
	 */
	public void gerarRelatorioProjeto(Projeto projeto) {
		try {
			montadorFormato.gerarRelatorioProjeto(projeto);
			encerrarEMostrar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void encerrarEMostrar() {
		setProduto(montadorFormato.encerrar());
		montadorFormato.mostrarProduto();
	}
}
