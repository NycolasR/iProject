package ponto.model.projetos;

import java.io.Serializable;

/**
 * 
 * @author NPG
 *
 */
public enum DiaSemana implements Serializable{

	SEG(1), TER(2), QUA(3), QUI(4), SEX(5), SAB(6),DOM(7);

	private final int valor;
	
	DiaSemana(int i) {
		valor = i;
	}
	/**
	 * 
	 * @return retorna o valor do dia da semana
	 */
	public int getValor() {	
		return valor;
	}

}
