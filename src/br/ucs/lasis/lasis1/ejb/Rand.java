package br.ucs.lasis.lasis1.ejb;

import java.util.Random;

import net.bootsfaces.render.A;

public class Rand {

	public static void main(String[] args) {

		// instância um objeto da classe Random usando o construtor padrão
		Random gerador = new Random();

		// imprime sequência de 10 números inteiros aleatórios
		for (int i = 0; i < 100; i++) {
			int randomico = gerador.nextInt(21);
			randomico = 10 - randomico;
			float fator = (100f + randomico) / 100f;
//			System.out.println(i + " -> " + randomico + " -> " + fator);
				//	+ String.format("%.2f", fator));
		}

	}

}
