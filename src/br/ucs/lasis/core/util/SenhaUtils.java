package br.ucs.lasis.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SenhaUtils {
	
	/**
	 * Gera uma senha com numeros (caracteres de 0-9), caracteres especiais (que não sejam letras ou números) e
	 * letras (caracteres de a - z ou A - Z)
	 * 
	 * @return senha
	 */
	public static String geradorSenha() {
		String letras = "QWERTYUIOPASDFGHJKLÇZXCVBNMqwertyuiopasdfghjklçzxcvbnm";

		String numeros = "0123456789";

		String caracteresEspeciais = "=(){}[];:!@_$%&*.+-#?";

		Random random = new Random();

		List<String> senha = new ArrayList<String>();

		// gera de 3 a 6 letras para a senha
		for (int i = 0; i < 3 + random.nextInt(5); i++) {
			senha.add(String.valueOf(letras.charAt(random.nextInt(54))));
		}

		// gera de 2 a 5 numeros para a senha
		for (int i = 0; i < 2 + random.nextInt(3); i++) {
			senha.add(String.valueOf(numeros.charAt(random.nextInt(10))));
		}

		// gera 2 caracteres especiais para a senha
		senha.add(String.valueOf(caracteresEspeciais.charAt(random.nextInt(21))));
		senha.add(String.valueOf(caracteresEspeciais.charAt(random.nextInt(21))));

		// embaralha a senha
		Collections.shuffle(senha);

		StringBuilder senhaGerada = new StringBuilder();

		// concatena a senha em uma string
		for (int i = 0; i < senha.size(); i++) {
			senhaGerada.append(senha.get(i));
		}

		return senhaGerada.toString().trim();

	}

}
