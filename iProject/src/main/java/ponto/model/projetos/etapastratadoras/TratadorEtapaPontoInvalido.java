package ponto.model.projetos.etapastratadoras;

import java.util.ArrayList;

import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
/**
 * 
 * @author NPG
 *
 */
public abstract class TratadorEtapaPontoInvalido {

	protected TratadorEtapaPontoInvalido proximaEtapa;
	
	/**
	 * Essa classe é o supertipo que vai ser extendida pelas outras classes tratadoras
	 * @param pontoTrabalhado ponto trabalhado a ser analisado
	 * @param horariosPrevistos horarios previstos para serem comparados
	 * @param pontosTrabalhados pontos trabalhados para serem comparados com o ponto trabalhado
	 * @return retorna true ou false, caso sejam analisado todos os tratadores e nenhum deles erro
	 */
	public boolean tratarPontosInvalidos(PontoTrabalhado pontoTrabalhado, ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos,ArrayList<PontoTrabalhado> pontosTrabalhados) {
		
		if(proximaEtapa != null) {
			
			return proximaEtapa.tratarPontosInvalidos(pontoTrabalhado,horariosPrevistos,pontosTrabalhados);
		}
		return true;
	}
	
	
	public TratadorEtapaPontoInvalido getProximaEtapa() {
		
		return proximaEtapa;
	}
	
	public void setProximaEtapa(TratadorEtapaPontoInvalido proximaEtapa) {
		
		this.proximaEtapa = proximaEtapa;
	}
	
}
