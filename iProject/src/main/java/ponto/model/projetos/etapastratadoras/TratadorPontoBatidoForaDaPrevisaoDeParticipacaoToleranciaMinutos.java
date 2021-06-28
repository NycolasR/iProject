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
public class TratadorPontoBatidoForaDaPrevisaoDeParticipacaoToleranciaMinutos extends TratadorEtapaPontoInvalido{
	
	/**
	 * Metodo da superclasse sobrescrito para tratar uma etapa separadamente.
	 * Essa tratadora valida se um ponto batido esta fora dos horarios previstos para 
	 * uma certa participacao levando em consideracao a tolerancia em minutos.
	 */
	public boolean tratarPontosInvalidos(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos, ArrayList<PontoTrabalhado> pontosTrabalhados) {

		
		if(!ValidadoraPontoTrabalhadoHorarioPrevisto.validarHorarioPrevistoDePontoTrabalhado(pontoTrabalhado, horariosPrevistos, true)) {
			pontoTrabalhado.setValido(false);
			return false;
		}
				
		return super.tratarPontosInvalidos(pontoTrabalhado, horariosPrevistos, pontosTrabalhados);
	}
}
