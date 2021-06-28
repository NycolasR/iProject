package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author NPG
 * Classe utilitaria usada para encriptar Strings. 
 */
public class Encriptador {
	
	public static String criptografarSenha(String senha) {
		// Codigo obtido da pratica FACADE
		String hash = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] textBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < textBytes.length; i++) {
             buffer.append(Integer.toString((textBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
                hash = buffer.toString();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
	}
}
