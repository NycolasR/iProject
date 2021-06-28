package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author NPG
 * Classe utilitaria usada para validar formatos de emails
 * usando regex.
 */
public class ValidadoraFormatoEmailLogin {
	
	public static boolean validarLoginComum(String login) { // Para quando a autenticacao for ContaEmailLivre
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return validar(regex, login);
	}
	
	public static boolean validarLoginIFPB(String login) { // Para quando a autenticacao for ContaEmailIFPB
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "academico.ifpb.edu.br";
		return validar(regex, login);
	}
	
	private static boolean validar(String regex, String login) {
		Pattern pattern = Pattern.compile(regex); 
		Matcher matcher = pattern.matcher(login);	
		return matcher.find() && matcher.group().equals(login);
	}
}
