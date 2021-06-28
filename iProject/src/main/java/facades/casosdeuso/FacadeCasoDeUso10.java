package facades.casosdeuso;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;

import model.autenticacao.Membro;
import model.projetos.Participacao;
import model.projetos.Projeto;
import persistencia.DAOXMLHorarioPrevisto;
import persistencia.DAOXMLMembroConta;
import persistencia.DAOXMLProjetoParticipacao;
import ponto.model.projetos.DiaSemana;
import ponto.model.projetos.HorarioPrevisto;
import ponto.model.projetos.HorarioPrevistoFlyweigth;
import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import util.MyLogger;
import util.ValidadoraDatas;
import util.ValidadoraMembros;

/**
 * 
 * @author NPG
 *
 */
public class FacadeCasoDeUso10 {
	
	private Logger logger = MyLogger.getInstance();

	/**
	 * 
	 * @param matriculaDoMembro Matricula do membro que quer se buscar para se adicionar um novo horario previsto
	 * @param matriculaDoCoordenador Matricula do cordenador que vai adicionar o horario previsto
	 * @param codProjeto Codigo do projeto que vai buscar o projeto em que o membro esta ou nao
	 * @param horaEntrada hora entrada do horarrio previsto
	 * @param horaSaida Hora saida do horario previsto
	 * @param diaDaSemana Dia da semana do horario previsto
	 * @param tolerancia Tolerancia em minutos do horario previsto
	 * @return Retorna true caso tudo ocorra bem, caso contrario retorna false
	 * @throws Exception Lanca excessao caso o membro nao exista no projeto, nem a matricula seja do coordenador
	 */
	public boolean adicionarNovoHoararioPrevisto(long matriculaDoMembro,long matriculaDoCoordenador, long codProjeto, 
			LocalDateTime horaEntrada, LocalDateTime horaSaida, DiaSemana diaDaSemana, short tolerancia) throws Exception{
	
		try {
			
			DAOXMLMembroConta daoxmlMembroConta = new DAOXMLMembroConta();
			DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
			
			Membro membroCoordenador = daoxmlMembroConta.consultarPorID(matriculaDoCoordenador);
			
			Projeto projeto = daoxmlProjetoParticipacao.consultarPorID(codProjeto);
			
			if(projeto.getCoordenador().getMatricula() == membroCoordenador.getMatricula()) {
				
				Membro membro = ValidadoraMembros.validarExistenciaDeMembro(projeto.getMembros(), daoxmlMembroConta.consultarPorID(matriculaDoMembro));
				
				if(membro == null) {
					
					String mensagem = "Membro não existe neste projeto";
					
					logger.severe(mensagem);
					throw new Exception(mensagem);
				}
				
				for(int i = 0; i < projeto.getParticipacoes().size(); i++) {
					
					Participacao participacao = (Participacao) projeto.getParticipacoes().get(i);
					
					if(participacao.getMembro().getMatricula() == membro.getMatricula() && participacao.isAtivo()) {
						
						
						//antes de adicionar, tenho q validar se o hora data está no meio das datas de inico e termino do projeto
						//que por ventura são as mesmas datas de uma participacao
						
						validarDataEntreDuasDatas(participacao.getDataInicio(), horaEntrada, participacao.getDataTermino());
						validarDataEntreDuasDatas(participacao.getDataInicio(), horaSaida, participacao.getDataTermino());
						
						participacao.addHorarioPrevisto(criarHorarioPrevisto(horaEntrada, horaSaida, diaDaSemana, tolerancia, participacao.getHorariosPrevistos()));
						
						logger.info("Horário previsto adicionado");
						
						daoxmlProjetoParticipacao.atualizar(codProjeto, projeto);
						
						logger.info("[DAOXMLProjetoParticipacao] - Projetos atualizados no sistema");
						
						return true;
					}
				}
				
				logger.warning("Membro com participação ativa não encontrado");
				return false;
				
			} else {
				
				String mensagem = "A matrícula não pertence ao cordenador";
				
				logger.severe(mensagem);
				throw new Exception(mensagem);
				
			}
		}catch (Exception e) {
			
			logger.severe(e.getMessage());
			throw new Exception(e.getMessage());
		}
		
		
		
		
	}
	
	/**
	 * Esse método cria o horario previsto recebendo como parametro a hora entrada, hora saida,
	 * o dia da semana, a tolerancia em minutos, e os horarios previstos já existentes para
	 * serem feitas as validações necessárias
	 */
	private HorarioPrevistoParteExtrinseca criarHorarioPrevisto(LocalDateTime horaEntrada, LocalDateTime horaSaida, DiaSemana diaDaSemana,short tolerancia,
										ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos) throws Exception{
		
		HorarioPrevisto horarioPrevistoFabrica = new HorarioPrevisto();
		
		DAOXMLHorarioPrevisto daoxmlHorarioPrevisto = new DAOXMLHorarioPrevisto();
		horarioPrevistoFabrica = daoxmlHorarioPrevisto.recuperarHorarios();
		
		if(!ValidadoraDatas.dataEntradaisIgualDataSaidaDePontoTrabalhado(horaEntrada, horaSaida) || !horaEntrada.isBefore(horaSaida) ||
				horaEntrada.getDayOfWeek().getValue() != diaDaSemana.getValor() || !validarTempoEmHorasATrabalhar(horaEntrada, horaSaida)) {
			
			throw new Exception("As horas estão incorretas");
		}
		
		for(int i = 0; i < horariosPrevistos.size(); i++) {
			
			HorarioPrevistoParteExtrinseca horarioPrevisto = horariosPrevistos.get(i);

			boolean isDiaSemana = false;
			
			if(diaDaSemana.getValor() == horarioPrevisto.getHoraInicio().getDayOfWeek().getValue()) {
				isDiaSemana = true;
			}
			
			if (!validarDatasDeHoararioPrevisto(horaEntrada, horarioPrevisto.getHoraInicio(), horaSaida, horarioPrevisto.getHoraTermino(), isDiaSemana)) {
				
				throw new Exception("Horários conflitantes");
			}
			
		}
		
		HorarioPrevistoFlyweigth horarioPrevistoParteExtrinseca = horarioPrevistoFabrica.getFlyweight(diaDaSemana, horaEntrada, horaSaida, tolerancia);
		
		return (HorarioPrevistoParteExtrinseca) horarioPrevistoParteExtrinseca;
	}
	
	/**
	 * Esse método faz validações com as datas recebidas como parametro e o dia da semana
	 * 
	 * @return retorna true caso as horas_datas sejam validas, senão retorna false
	 */
	private boolean validarDatasDeHoararioPrevisto(LocalDateTime dataHora1, LocalDateTime dataHora2,LocalDateTime dataHora3, 
													LocalDateTime dataHora4, boolean isDiaSemana) {
		if(!isDiaSemana) {
			return true;
			
		} else if(dataHora1.isAfter(dataHora4) || dataHora3.isBefore(dataHora2)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Esse método calcula quanto tempo um membro no projeto vai trabalhar, e
	 * se o tempo for 4 horas o método retorna true, caso contrário retorna false
	 */
	private boolean validarTempoEmHorasATrabalhar(LocalDateTime dataHora1, LocalDateTime dataHora2){
		
		Duration duracao = Duration.between(dataHora1, dataHora2);		
		long horasParaTrabalhar = duracao.toHours();
		
		if(horasParaTrabalhar == 4) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Esse métod valida se uma data está entre outras duas datas, se estiver retorna true,
	 * senão retorna false
	 */
	private boolean validarDataEntreDuasDatas(LocalDate dataHora1,LocalDateTime dataHoraMeio, LocalDate dataHora2) throws Exception{
		LocalDate dataHora = LocalDate.of(dataHoraMeio.getYear(), dataHoraMeio.getMonthValue(), dataHoraMeio.getDayOfMonth());
		
		if(!dataHora1.isAfter(dataHora) && !dataHora2.isBefore(dataHora)) {
			return true;
		} else {
			throw new Exception("A hora não está para um dia válido");
		}	
	}
}
