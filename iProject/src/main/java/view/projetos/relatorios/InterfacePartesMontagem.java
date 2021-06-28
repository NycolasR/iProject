package view.projetos.relatorios;

import model.projetos.Edital;
import model.projetos.Grupo;
import model.projetos.Projeto;

/**
 * 
 * @author NPG
 * Esta interface representa o supertipo para os montadores concretos da implementacao do padrao builder.
 */
public interface InterfacePartesMontagem {
	public void gerarRelatorioEdital(Edital edital) throws Exception;
	public void gerarRelatorioGrupo(Grupo grupo) throws Exception;
	public void gerarRelatorioProjeto(Projeto projeto) throws Exception;
	public void reiniciar();
	public Object encerrar();
	public void mostrarProduto();
}
