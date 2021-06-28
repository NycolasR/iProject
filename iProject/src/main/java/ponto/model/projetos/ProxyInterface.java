package ponto.model.projetos;

import java.rmi.Remote;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import model.projetos.Projeto;
import ponto.model.projetos.etapastratadoras.TratadorEtapa;

/**
 * 
 * @author NPG
 * Interface usada para implementacao do padrao de responsabilidade PROXY.
 * Esta interface eh implementada pela classe de objeto remoto RegistradorPontoCentral.
 * Eh a esta interface de procuracao (supertipo) que o cliente ira se acoplar para usar o objeto remoto. 
 */
public interface ProxyInterface extends Remote {
	
	public boolean registrarPonto(long codProjeto, String login) throws Exception;
	
	public Set<Projeto> getProjetosAtivos(String login) throws Exception;
	
	public float horasTrabalhadasValidas(LocalDateTime dataInicio, LocalDateTime dataTermino, String login,long codProjeto) throws Exception;
	
	public float deficitHoras(LocalDateTime dataInicio , LocalDateTime dataTermino , String login,long codProjeto) throws Exception;
	
	public Set<PontoTrabalhado> getPontosInvalidos(String login,ArrayList<TratadorEtapa> etapas,long codProjeto) throws Exception;
	
	public void justificarPontoInvalido(long idPonto, String justificativa, String login,long codProjeto,ArrayList<TratadorEtapa> etapas) throws Exception;
	
	public void justificarPontoNaoBatido(long idPonto, String justificativa, String login,long codProjeto) throws Exception;
	
	public boolean fazerLogin(String email,String senha,boolean tipoProvedorInterno) throws Exception;
	
	public StringBuffer pegarDetalhes(LocalDateTime dataAgora ,String login,long codProjeto) throws Exception;
	
	public Projeto[] getTodosOsProjetos() throws RemoteException;
	
	public void teste() throws RemoteException;

}
