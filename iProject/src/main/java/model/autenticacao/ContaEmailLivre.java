package model.autenticacao;

import util.Encriptador;
import util.ValidadoraFormatoEmailLogin;

/**
 * @author NPG
 * Esta classe é um tipo concreto de abstração.
 * Faz parte da implementação do padrão Bridge.
 */
public class ContaEmailLivre extends Conta {

	@Override
	public boolean autenticar(String login, String senha, boolean isSenhaEncriptada) {
		System.out.println("Autenticando com ContaEmailLive");
		
		String senhaCriptografada = "";
		if(isSenhaEncriptada) {
			senhaCriptografada = Encriptador.criptografarSenha(senha);
			senha = senhaCriptografada;
		}
		
		if(validarLogin(login)) {
			return super.autenticar(login, senha, isSenhaEncriptada);
		}
		return false;
	}
	
	@Override
	public boolean validarLogin(String login) {
		return ValidadoraFormatoEmailLogin.validarLoginComum(login);
	}
}
