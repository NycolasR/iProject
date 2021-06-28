package ponto.model.projetos;

import java.time.Duration;
import java.time.LocalDateTime;
/**
 * 
 * @author NPG
 *
 */
public class HorarioPrevistoParteExtrinseca implements HorarioPrevistoFlyweigth{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalDateTime horaInicio;
	private LocalDateTime horaTermino;
	private HorarioPrevistoFlyweigth horarioPrevistoParteIntrinseca;
	
	/**
	 * Esse método é a implementacao do metodo da interface horarioPrevistoFlyweigth
	 * retorna a parte intrínseca que está dentro da parte intrinseca
	 */
	public Object[] getToleranciaMinutosEDiaSemana() {
		
		return horarioPrevistoParteIntrinseca.getToleranciaMinutosEDiaSemana();
	}

	/**
	 * @return retorna a expectativa da quantidade de horas trabalhadas entre um horário e outro
	 */
	public int getExpectativaHorasTrabalhadas() {
		
		Duration d = Duration.between(horaInicio, horaTermino);		
		int i = (int)d.toMinutes()/60;
		return i;
	}
	/**
	 * Esse método é o construtor sobreescrito dessa classe
	 * @param diaSemana dia da semana correspondente ao horario previsto
	 * @param horaInicio que se inicia o horario previsto
	 * @param horaTermino que se finaliza o horario previsto
	 * @param parteIntrinseca que estara fazendo parte do horario previsto para essa parte intrinseca
	 */
	public  HorarioPrevistoParteExtrinseca( LocalDateTime horaInicio , LocalDateTime horaTermino , HorarioPrevistoFlyweigth parteIntrinseca ) {
		
		this.horaInicio = horaInicio;
		this.horaTermino = horaTermino;
		this.horarioPrevistoParteIntrinseca = parteIntrinseca;
	}

	/**
	 * 
	 * @return retorna o horario inicial
	 */
	public LocalDateTime getHoraInicio() {
		return horaInicio;
	}

	/**
	 * 
	 * @param horaInicio a ser adicionado a parte extrinseca
	 */
	public void setHoraInicio(LocalDateTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * 
	 * @return retorna o horario termino
	 */
	public LocalDateTime getHoraTermino() {
		return horaTermino;
	}

	/**
	 * 
	 * @param horaTermino a ser adicionado a parte extrinseca
	 */
	public void setHoraTermino(LocalDateTime horaTermino) {
		this.horaTermino = horaTermino;
	}
	
	

}
