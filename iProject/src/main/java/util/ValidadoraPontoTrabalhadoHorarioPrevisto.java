package util;

import java.util.ArrayList;

import ponto.model.projetos.DiaSemana;
import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
/**
 * 
 * @author NPG
 *
 */
public class ValidadoraPontoTrabalhadoHorarioPrevisto {

	/**
	 * 
	 * @param pontoTrabalhado a ser analisado
	 * @param horariosPrevistos horarios previstos que o ponto deve atender
	 * @param usarTolerancia condição validadora para se utilizar ou não a tolerância em minutos
	 * @return retorna true caso o ponto trabalhado atenda a pelo menos um dos horarios previstos
	 */
	public static boolean validarHorarioPrevistoDePontoTrabalhado(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos, boolean usarTolerancia) {
					
		for(int i = 0; i < horariosPrevistos.size(); i++) {
			
			HorarioPrevistoParteExtrinseca horarioPrevisto = horariosPrevistos.get(i);
			
			short tolerancia = 0;
			
			if(usarTolerancia) {
				tolerancia = (short) horarioPrevisto.getToleranciaMinutosEDiaSemana()[0];
			}
			
			boolean diaDaSemanaIgual = ((DiaSemana)horarioPrevisto.getToleranciaMinutosEDiaSemana()[1]).getValor() == pontoTrabalhado.getDataHoraEntrada().getDayOfWeek().getValue();
			boolean dataInicioIgual = !pontoTrabalhado.getDataHoraEntrada().isAfter(horarioPrevisto.getHoraInicio().plusMinutes(tolerancia));
			boolean dataTerminoIgual = !pontoTrabalhado.getDataHoraSaida().isBefore(horarioPrevisto.getHoraTermino());
			
			if(diaDaSemanaIgual && dataInicioIgual && dataTerminoIgual) {
				
				return true;
			}
		}		
		
		return false;
	}
	
	
	
}
