package ponto.model.projetos.etapastratadoras;

/**
 * @author NPG
 * Enum de tipos de tratadoras existentes
 * 
 */
public enum TratadorEtapa {

	TratarPontosSemHorarioEntradaOuSaida(1),
	TratarPontoBatidoForaDaPrevisaoDeParticipacao(2),
	TratarPontoBatidoForaDaPrevisaoDeParticipacaoToleranciaMinutos(3),
	TratarPontoInvalidoJustificativaCadastradaNegada(4),
	TratarPontoIntervaloConflitantes(5);
	
	private final int valor;

	TratadorEtapa(int i) {
		valor = i;
	}
	
	public int getValor() {
		return valor;
	}
	
	
	
}
