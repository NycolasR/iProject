package ponto.model.projetos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import persistencia.DAOXMLHorarioPrevisto;
/**
 * 
 * @author NPG
 *
 */
public class HorarioPrevisto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private  HorarioPrevistoParteExtrinseca horarioPrevistoFlyweigthParteExtrinseca;
	private  Set<HorarioPrevistoFlyweigth> horarioPrevistoPartesIntrinsecas = new LinkedHashSet<HorarioPrevistoFlyweigth>();
	
	/**
	 * @param diaSemana para adicionar no dia da semana da parte Extrinseca;
	 * @param horaInicio para adicionar ao horario inicio da parte extrinseca do horario previsto
	 * @param horaTermino para adicionar ao horario termino da parte extrinseca do horario previsto 
	 * @param toleranciaMinutos para bucar ou criar a parte intrinseca do horario previsto
	 * @return retorna o horario previsto que é a parte extrinseca junto com a parte intrinseca
	 */
	
	public  HorarioPrevistoFlyweigth getFlyweight(DiaSemana diaSemana , LocalDateTime horaInicio, LocalDateTime horaTermino , short toleranciaMinutos) {
		
		HorarioPrevistoFlyweigth parteIntrinseca = null;
		
		Iterator<HorarioPrevistoFlyweigth> iterador = horarioPrevistoPartesIntrinsecas.iterator();
		
		while(iterador.hasNext()) {
			
			HorarioPrevistoFlyweigth horarioPrevistoParteIntrinseca = iterador.next();
			
			short toleranciaMinutosParteIntrinseca = (short) horarioPrevistoParteIntrinseca.getToleranciaMinutosEDiaSemana()[0];
			DiaSemana diaSemanaParteIntrinseca = (DiaSemana) horarioPrevistoParteIntrinseca.getToleranciaMinutosEDiaSemana()[1];
			
			if(toleranciaMinutosParteIntrinseca == toleranciaMinutos && diaSemanaParteIntrinseca.getValor() == diaSemana.getValor()) {
				parteIntrinseca = horarioPrevistoParteIntrinseca;
			}
		}
		
		if(parteIntrinseca == null) {
			
			parteIntrinseca = new HorarioPrevistoParteIntrinseca(toleranciaMinutos,diaSemana);
			horarioPrevistoPartesIntrinsecas.add(parteIntrinseca);
		}
		
		horarioPrevistoFlyweigthParteExtrinseca = new HorarioPrevistoParteExtrinseca(horaInicio, horaTermino, parteIntrinseca);
		
		DAOXMLHorarioPrevisto daoxmlHorarioPrevisto = new DAOXMLHorarioPrevisto();
		daoxmlHorarioPrevisto.salvarHorarios(this);
		
		return horarioPrevistoFlyweigthParteExtrinseca;
	}
	
}
