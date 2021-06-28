package model.projetos;

public enum TiposItemProjeto {

	PROJETO(1),EDITAL(2),GRUPO(3),PARTICIPACAO(4);
	
	
	private final int valor;

	TiposItemProjeto(int i) {
		
		this.valor = i;
	}
	
	public int getValor() {
		return valor;
	}
	
}
