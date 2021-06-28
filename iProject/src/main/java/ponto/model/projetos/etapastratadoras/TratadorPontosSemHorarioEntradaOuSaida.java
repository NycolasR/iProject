package ponto.model.projetos.etapastratadoras;

import java.util.ArrayList;

import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
/**
 * 
 * @author NPG
 *
 */
public class TratadorPontosSemHorarioEntradaOuSaida extends TratadorEtapaPontoInvalido{

	/**
	 * Metodo da superclasse sobrescrito para tratar uma etapa separadamente.
	 * Essa tratadora valida se um ponto não possui horario de entrada ou horario de saida cadastrados.
	 */
	public boolean tratarPontosInvalidos(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos, ArrayList<PontoTrabalhado> pontosTrabalhados) {
				
		if(pontoTrabalhado.getDataHoraEntrada() == null || pontoTrabalhado.getDataHoraSaida() == null) {
			pontoTrabalhado.setValido(false);
			return false;
		} else {
			return super.tratarPontosInvalidos(pontoTrabalhado,horariosPrevistos, pontosTrabalhados);
		}
	}
}
