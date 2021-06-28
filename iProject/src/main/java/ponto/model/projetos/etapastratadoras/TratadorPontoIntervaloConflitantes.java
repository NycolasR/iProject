package ponto.model.projetos.etapastratadoras;

import java.util.ArrayList;

import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
/**
 * 
 * @author NPG
 *
 */
public class TratadorPontoIntervaloConflitantes extends TratadorEtapaPontoInvalido{

	/**
	 * Metodo da superclasse sobrescrito para tratar uma etapa separadamente.
	 * Essa tratadora valida se as horas/datas de um ponto batido estão em conflito
	 * com outros pontos.
	 */
	public boolean tratarPontosInvalidos(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos, ArrayList<PontoTrabalhado> pontosTrabalhados) {

		for(int i = 0; i < pontosTrabalhados.size(); i++) {
			
			
			PontoTrabalhado ponto = pontosTrabalhados.get(i);
			
			if(ponto.getId() != pontoTrabalhado.getId() && 
					pontoTrabalhado.getDataHoraEntrada().getDayOfWeek().getValue() == ponto.getDataHoraEntrada().getDayOfWeek().getValue()) {
				
				if(!pontoTrabalhado.getDataHoraEntrada().isAfter(ponto.getDataHoraSaida()) || !pontoTrabalhado.getDataHoraSaida().isBefore(ponto.getDataHoraEntrada())) {
					
					pontoTrabalhado.setValido(false);
					return false;
					
				}
			}
			
		}
		
		return super.tratarPontosInvalidos(pontoTrabalhado,horariosPrevistos, pontosTrabalhados);
	}
}
