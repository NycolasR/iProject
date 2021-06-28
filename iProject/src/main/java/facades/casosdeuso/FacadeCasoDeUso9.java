package facades.casosdeuso;

import java.util.logging.Logger;

import model.autenticacao.RegistradorSessaoLogin;
import util.MyLogger;

/**
 * @author NPG
 *
 */
public class FacadeCasoDeUso9 {
	// Caso de Uso 9
	public boolean fazerLogout(String login) {
		RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
		
		Logger logger = MyLogger.getInstance();
		logger.info("Membro de login " + login + " fez logout.");
		
		return registradorSessaoLogin.registrarOffline(login); 
	}
}
