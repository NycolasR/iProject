package ponto.model.projetos;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
/**
 * 
 * @author NPG
 *
 */
public class PontoTrabalhado implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalDateTime dataHoraEntrada;
	private LocalDateTime dataHoraSaida;
	private String justificativa;
	private boolean justificativaAceita;
	private boolean valido;
	private long id;

	public PontoTrabalhado() {
		this.id = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @return retorna as horas trabalhadas entre a data hora inicio e a data hora saida
	 * 
	 * TODO FEITO [UML] modificar tipo do retorno desse método
	 * 
	 */
	public float getHorasTrabalhadas() {
		
		Duration d = Duration.between(dataHoraEntrada, dataHoraSaida);		
		float i = d.toMinutes()/60;
		
		return i;
	}

	public LocalDateTime getDataHoraEntrada() {
		return dataHoraEntrada;
	}

	public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
		this.dataHoraEntrada = dataHoraEntrada;
	}

	public LocalDateTime getDataHoraSaida() {
		return dataHoraSaida;
	}

	public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public boolean isJustificativaAceita() {
		return justificativaAceita;
	}

	public void setJustificativaAceita(boolean justificativaAceita) {
		this.justificativaAceita = justificativaAceita;
	}
	
	public long getId() {
		return id;
	}
		
	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public String toString() {
		
		return "Ponto Trabalhado - " + dataHoraEntrada.getDayOfWeek();
	}

}
