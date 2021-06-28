package ponto.model.projetos.etapastratadoras;

import java.util.ArrayList;

import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
/**
 * 
 * @author NPG
 *
 */
public class TratadorPontoInvalidoJustificativaCadastradaNegada extends TratadorEtapaPontoInvalido{

	/**
	 * Metodo da superclasse sobrescrito para tratar uma etapa separadamente.
	 * Essa tratadora valida se um ponto invalido possui a justificativa negada.
	 */
	public boolean tratarPontosInvalidos(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos, ArrayList<PontoTrabalhado> pontosTrabalhados) {

		if(pontoTrabalhado.getJustificativa() != null && pontoTrabalhado.isJustificativaAceita() == false) {
			pontoTrabalhado.setValido(false);
			return false;
		}
		
		return super.tratarPontosInvalidos(pontoTrabalhado, horariosPrevistos, pontosTrabalhados);
	}
}
