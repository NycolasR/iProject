package ponto.model.projetos;

import java.io.Serializable;

/**
 * 
 * @author NPG
 *
 */
public interface HorarioPrevistoFlyweigth extends Serializable{
	
	/**
	 * Esse método vai retornar a tolerância em minutos da parte intrínseca 
	 */
	public Object[] getToleranciaMinutosEDiaSemana();
}
