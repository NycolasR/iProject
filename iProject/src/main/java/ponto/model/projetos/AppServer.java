package ponto.model.projetos;

import java.net.InetAddress;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author NPG
 * Classe usada para iniciar o servidor do objeto remoto RegistradorPontoCentral,
 * atribuindo-lhe um nome em registro, iniciando a aplicacao servidora na porta 1099.
 * Para que aplicacoes clientes funcionem corretamente, este servidor deve estar ativado.
 */
public class AppServer {
	
	public static void main(String[] args) {
		Registry registry = null;
		
		try {
			// Para tentar iniciar o registro
			registry = LocateRegistry.createRegistry(1099);
			System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
			RegistradorPontoCentral remoto = new RegistradorPontoCentral();
			registry.bind("RegistradorPontoCentral", remoto);
			
//			UnicastRemoteObject.unexportObject(registry, true);
//			registry.unbind("RegistradorPontoCentral");
			
		} catch(Exception e1) {
			
			try {
				// Para ver se está rodando
				e1.printStackTrace();
				registry = LocateRegistry.getRegistry();
				
			} catch(RemoteException e2) {
				// Se nao foi possivel criar nem encontrar algum rodando, encerra a execucao
				System.err.println("Não foi possível iniciar o servidor");
				System.exit(0);
			}
		}
	}
}
