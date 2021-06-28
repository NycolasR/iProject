package ponto.model.projetos;
/**
 * 
 * @author NPG
 *
 */
public class HorarioPrevistoParteIntrinseca implements HorarioPrevistoFlyweigth{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DiaSemana diaSemana;
	private short toleranciaMinutos;
	
	/**
	 * Esse método é o contrutor sobreescrito dessa classe
	 * @param toleranciaMinutos a ser adicionada a parte intrinseca
	 */
	public HorarioPrevistoParteIntrinseca(short toleranciaMinutos, DiaSemana diaSemana){
		
		this.toleranciaMinutos = toleranciaMinutos;
		this.diaSemana = diaSemana;
	}
	
	/**
	 * Esse método é a implementacao do metodo da interface horarioPrevistoFlyweigth
	 * que retorna a tolerancia em minutos
	 */
	public Object[] getToleranciaMinutosEDiaSemana() {
				
		return new Object[] {toleranciaMinutos,diaSemana};
	}
	/**
	 * 
	 * @return retorna o dia da semana da parte extrinseca
	 */
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}
	

}
