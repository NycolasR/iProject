package ponto.model.projetos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * 
 * @author NPG
 * Classe usada para testes de acesso ao objeto remoto atraves do metodo Naming.lookup(str).
 */
public class AppCliente {
	public static void main(String[] args) {
		try {
			ProxyInterface proxyInterface = (ProxyInterface) Naming.lookup("RegistradorPontoCentral");
			
			System.out.println("Hi");
			System.err.println("AAAAAAAAAA");
			proxyInterface.teste();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
