package ponto.model.projetos.etapastratadoras;

import java.util.ArrayList;

import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
import util.ValidadoraPontoTrabalhadoHorarioPrevisto;
/**
 * 
 * @author NPG
 *
 */
public class TratadorPontoBatidoForaDaPrevisaoDeParticipacao extends TratadorEtapaPontoInvalido{

	/**
	 * Metodo da superclasse sobreescrito para tratar uma etapa separadamente.
	 * Essa tratadora valida se um ponto batido esta fora dos horarios previstos para 
	 * uma certa participacao.
	 */
	public boolean tratarPontosInvalidos(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos,ArrayList<PontoTrabalhado> pontosTrabalhados) {
		
		
		if(!ValidadoraPontoTrabalhadoHorarioPrevisto.validarHorarioPrevistoDePontoTrabalhado(pontoTrabalhado, horariosPrevistos, false)) {
			pontoTrabalhado.setValido(false);
			return false;
		}
				
		return super.tratarPontosInvalidos(pontoTrabalhado, horariosPrevistos, pontosTrabalhados);
	}
	
}
