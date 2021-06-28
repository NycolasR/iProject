package facades.casosdeuso;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import persistencia.DAOXMLProjetoParticipacao;
import ponto.model.projetos.ProxyInterface;
import util.MyLogger;

/**
 * @author NPG
 * 
 */
public class FacadeCasoDeUso13 {

	private ProxyInterface registradorPontoCentral;
	private Logger logger = MyLogger.getInstance();
	
	public FacadeCasoDeUso13() {
		
		try {
			registradorPontoCentral = (ProxyInterface) Naming.lookup("RegistradorPontoCentral");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public String verDetalhesGerais(long codProjeto,long matriculaCoordenador, String loginMembro, LocalDateTime desde, LocalDateTime ate) {
		StringBuffer detaldetalhesGerais = new StringBuffer();
		
		detaldetalhesGerais.append("Horas trabalhadas válidas: " + verHorasTrabalhadasValidas(codProjeto,matriculaCoordenador, loginMembro, desde, ate));
		detaldetalhesGerais.append("\nDéficit de horas: " + verDeficitHoras(codProjeto,matriculaCoordenador,loginMembro, desde, ate));
		
		logger.info("Gerado detalhes gerais");
		return detaldetalhesGerais.toString();
	}
	
	public float verHorasTrabalhadasValidas(long codProjeto,long matriculaCoordenador, String loginMembro, LocalDateTime desde, LocalDateTime ate) {
		try {
			
			if(!verificarMatriculaDeCoordenador(matriculaCoordenador, codProjeto)) {
				
				String mensagem = "A matricula não pertence ao cordenador do projeto";
				logger.severe(mensagem);
				throw new Exception(mensagem);
			}
			
			logger.info("Foram pegas as horas trabalhadas válidas de um membro em um projeto");
			
			return registradorPontoCentral.horasTrabalhadasValidas(desde, ate, loginMembro, codProjeto);
			
		} catch (Exception e) {
			
			logger.severe(e.getMessage());
			System.out.println(e.getMessage());
		}
		
		return -1;
	}
	
	public float verDeficitHoras(long codProjeto,long matriculaCoordenador,String loginMembro, LocalDateTime desde, LocalDateTime ate) {
		try {
			
			if(!verificarMatriculaDeCoordenador(matriculaCoordenador, codProjeto)) {
				
				String mensagem = "A matricula não pertence ao cordenador do projeto";
				logger.severe(mensagem);
				throw new Exception(mensagem);
			}
			
			logger.info("Foi pego o déficit de horas que um membro deve em um projeto");
			
			return registradorPontoCentral.deficitHoras(desde, ate, loginMembro,codProjeto);
			
		} catch (Exception e) {
			
			logger.severe(e.getMessage());
			System.out.println(e.getMessage());
		}
		
		return -1;
	}
	
	private boolean verificarMatriculaDeCoordenador(long matriculaCoordenador, long codProjeto) throws Exception{
		
		DAOXMLProjetoParticipacao daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
				
		if(matriculaCoordenador == -1 ||
				daoxmlProjetoParticipacao.consultarPorID(codProjeto).getCoordenador().getMatricula() == matriculaCoordenador) {
			
			return true;
			
		}
			
		return false;
		
	}
	
}










