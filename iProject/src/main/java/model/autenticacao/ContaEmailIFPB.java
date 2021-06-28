package model.autenticacao;

import util.ValidadoraFormatoEmailLogin;

/**
 * @author NPG
 * Esta classe é um tipo concreto de abstração.
 * Faz parte da implementação do padrão Bridge.
 */
public class ContaEmailIFPB extends Conta {
	
	@Override
	public boolean autenticar(String login, String senha, boolean isSenhaEncriptada) {
		System.out.println("Autenticando com ContaEmailIFPB");
		
		if(validarLogin(login)) {
			return super.autenticar(login, senha, false); // Esta abstração não deve trabalhar com senhas encriptadas
		}
		return false;
	}
	
	@Override
	public boolean validarLogin(String login) {
		return ValidadoraFormatoEmailLogin.validarLoginIFPB(login);
	}
}
