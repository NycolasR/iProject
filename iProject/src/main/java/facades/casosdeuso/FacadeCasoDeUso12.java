package facades.casosdeuso;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import model.projetos.Projeto;
import ponto.model.projetos.PontoTrabalhado;
import ponto.model.projetos.ProxyInterface;
import ponto.model.projetos.etapastratadoras.TratadorEtapa;
import util.MyLogger;
/**
 * 
 * @author NPG
 *
 */
public class FacadeCasoDeUso12 {

	private ProxyInterface registradorProxy;
	private ArrayList<TratadorEtapa> etapas = new ArrayList<TratadorEtapa>();
	private Logger logger = MyLogger.getInstance();
	
	public FacadeCasoDeUso12() throws Exception{
		
		 registradorProxy = (ProxyInterface) Naming.lookup("RegistradorPontoCentral");
			addEtapas();
	}
	
	/**
	 * 
	 * @param idPonto Codigo do ponto trabalhado
	 * @param login Email do membro 
	 * @param justificativa Justificativa do ponto inválido ou não batido;
	 * @param codProjeto Projeto no qual vai se buscar esses pontos invalidos do membro
	 * @return retorna true se tudo ocorreu bem, caso contrario lanca uma excessao
	 */
	public boolean justificarPontoInvalido(long idPonto, String login,String justificativa, long codProjeto) {
		
		try {
						
			registradorProxy.justificarPontoInvalido(idPonto, justificativa, login, codProjeto, etapas);
			logger.info("Ponto justificado com sucesso");
			
		}catch (Exception e) {
			
			logger.severe(e.getMessage());
			return false;
		}

		return true;
	}
	
	public PontoTrabalhado[] getPontosInvalidosDoMembro(String login, long codProjeto) throws Exception{
		
					
		Set<PontoTrabalhado> pontosTrabalhados = registradorProxy.getPontosInvalidos(login, etapas, codProjeto);
		return pontosTrabalhados.toArray(new PontoTrabalhado[pontosTrabalhados.size()]);
			
		
	}
	
	public Projeto[] getProjetosAtivosDoMembro(String login) throws Exception{
		

		Set<Projeto> projetos = registradorProxy.getProjetosAtivos(login);
		return projetos.toArray(new Projeto[projetos.size()]);
							
	}
	
	private void addEtapas() {
		
		etapas.add(TratadorEtapa.TratarPontosSemHorarioEntradaOuSaida);
		etapas.add(TratadorEtapa.TratarPontoBatidoForaDaPrevisaoDeParticipacaoToleranciaMinutos);
		etapas.add(TratadorEtapa.TratarPontoBatidoForaDaPrevisaoDeParticipacao);
		etapas.add(TratadorEtapa.TratarPontoIntervaloConflitantes);
	}
	
}
