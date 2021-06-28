package ponto.model.projetos.etapastratadoras;

import java.util.ArrayList;
/**
 * 
 * @author NPG
 *
 */
public class SimpleFactoryEtapasTratadoras {

	/**
	 * 
	 * @param etapas Lista de etapas para serem criadas/instânciadas
	 * @return Retorna o Primeiro tratador que possuirá em si, a sequencia de tratadores a serem chamados
	 */
	public static TratadorEtapaPontoInvalido criarEtapas(ArrayList<TratadorEtapa> etapas) {
		

		ArrayList<TratadorEtapaPontoInvalido> tratadores = new ArrayList<TratadorEtapaPontoInvalido>();
					
		for(int i = 0; i < etapas.size(); i++) {
			
			TratadorEtapaPontoInvalido tratadorEtapaPontoInvalido = null;
			
			if(etapas.get(i).getValor() == 1) {
				
				tratadorEtapaPontoInvalido = new TratadorPontosSemHorarioEntradaOuSaida();
				
			}else if(etapas.get(i).getValor() == 2) {
				
				tratadorEtapaPontoInvalido = new TratadorPontoBatidoForaDaPrevisaoDeParticipacao();
			
			}else if(etapas.get(i).getValor() == 3) {
				
				tratadorEtapaPontoInvalido = new TratadorPontoBatidoForaDaPrevisaoDeParticipacaoToleranciaMinutos();
				
			}else if(etapas.get(i).getValor() == 4) {
				
				tratadorEtapaPontoInvalido = new TratadorPontoInvalidoJustificativaCadastradaNegada();
				
			}else if(etapas.get(i).getValor() == 5) {
				
				tratadorEtapaPontoInvalido = new TratadorPontoIntervaloConflitantes();
				
			}
			tratadores.add(tratadorEtapaPontoInvalido);
		}
		
		
		
		return setarEtapas(tratadores);
	}
	
	/**
	 * 
	 * @param tratadores Lista de tratadores já instanciados
	 * @return Retorna o primeiro tratador já com a sequencia de tratadores setados
	 */
	private static TratadorEtapaPontoInvalido setarEtapas(ArrayList<TratadorEtapaPontoInvalido> tratadores){
		
		for(int i = 0; i < tratadores.size()-1; i++) {
			
			tratadores.get(i).setProximaEtapa(tratadores.get(i+1));
			
		}
		
		return tratadores.get(0);
	}
}
