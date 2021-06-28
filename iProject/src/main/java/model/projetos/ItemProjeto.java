
package model.projetos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import model.autenticacao.Membro;

/**
 * @author NPG
 *
 * Essa classe organiza o agrupamento de componentes, simples e compostos
 *tratando-os de maneira distinta perante as classes cliente e entre eles mesmos, 
 *ou seja, as classes dos objetos simples e compostos herdam dessa classe, e 
 *implementam os métodos que fazem parte das suas estruturas, ou lançam
 *excessão caso os métodos não façam parte, sendo assim, proporciona 
 *transparência as classes clientes.
 *
 */
public abstract class ItemProjeto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long codigo;
	private String nome;
	private boolean ativo;
	private TiposItemProjeto tipoItemProjeto;
	
	/*
	 * 		Os m�todos abstratos abaixo, est�o dessa forma, pois alguns deles v�o ter propaga��o de chama [ ativar() ; 
	 * desativar(); getCustoTotal(); getCusteioReaisNaoGastoTotal(); getCapitalReaisNaoGastoTotal()], e por isso
	 * sao executados em cascata.
	 * 		O restante dos m�todos lan�am excess�o, pois s�o implementados apenas por algumas classes compostas,
	 * dessa forma somente as classes interessadas � que ir�o implementar como necessitam que os m�todos sejam
	 * implementados
	 */
	
	public abstract void ativar();
	
	public abstract void desativar();
	
	public abstract float getCustoTotal();
	
	public abstract float getCusteioReaisNaoGastoTotal();
	
	public abstract float getCapitalReaisNaoGastoTotal();
	
	public void adicionarItemProjeto(ItemProjeto itemProjeto)throws Exception {
		
		throw new UnsupportedOperationException();
	}
		
	public void removerItemProjeto(ItemProjeto itemProjeto)throws Exception{
		
		throw new UnsupportedOperationException();
	}

	public void adicionarMembro(Membro membro, LocalDate dataInicio, LocalDate dataTermino, boolean isCordenador,
										float aporteCusteioReaisMensal)throws Exception{
		
		throw new UnsupportedOperationException();
	}

	public void adicionarMembro(Membro membro) throws Exception{
		
		throw new UnsupportedOperationException();
	}
	
	public void removerMembro(Membro membro) throws Exception{
		
		throw new UnsupportedOperationException();
	}

	public void mover(ItemProjeto itemProjeto) throws Exception{
		
		throw new UnsupportedOperationException();
	}
	
	public LocalDate getDataInicio() throws Exception {
		
		throw new UnsupportedOperationException();
	}
	
	public LocalDate getDataTermino() throws Exception {
		
		throw new UnsupportedOperationException();
	}										
	
	public ArrayList<Membro> getMembros() throws Exception{		
																		
		throw new UnsupportedOperationException();					
	}													
	
	public ItemProjeto getGrupo() throws Exception{		
														
		throw new UnsupportedOperationException();		
	}
	
	public boolean isCoordenador() throws Exception{
		
		throw new UnsupportedOperationException();
	}
	
	public Membro getCoordenador() throws Exception{
		
		throw new UnsupportedOperationException();
	}
	
	public ArrayList<ItemProjeto> getProjetos() throws Exception{
		
		throw new UnsupportedOperationException();
	}
	
	public float getAporteCusteioReais() throws Exception{
		
		throw new UnsupportedOperationException();
	}
	
	public float getAporteCapitalReais()throws Exception {
		
		throw new UnsupportedOperationException();
	}
	
	public String getNome() {
		
		return this.nome;
	}
	
	public void setNome(String nome) {
		
		this.nome = nome;
	}
	
	public boolean isAtivo() {
		
		return this.ativo ;
	}
	
	protected void setAtivo(boolean ativo) {
		
		this.ativo = ativo;
	}
	
	public long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * TODO FEITO [UML] os métodos a baixo devem ser adicionados ao uml, pois eles setam um tipo de item projeto a um item projeto
	 * e retornam um tipo de item projeto 
	 */
	
	public TiposItemProjeto getTipoItemProjeto() {
		return tipoItemProjeto;
	}

	public void setTipoItemProjeto(TiposItemProjeto tipoItemProjeto) {
		this.tipoItemProjeto = tipoItemProjeto;
	}

	@Override
	public String toString() {
		return getNome();
	}
		
	/*
	 * Os m�todos que n�o s�o abstratos, nem lan�am excess�o, ou seja, j estao sendo implementados, 
	 * sao aqueles comuns a todas as classes compostas ou simples. (Embora a classe Participa��o n�o possua
	 * o atributo nome, implementou-se os m�todos de recuperar e adicionar (get e set) na super classe,
	 * e sobrescreveu-se os mesmos na classe Participa��o retornando null, e n�o adicionando
	 * nenhum nome).
	 */
	
}
