package br.ucs.lasis.lasis1.model.comparator;

import java.util.Comparator;

import br.ucs.lasis.lasis1.model.entity.Rodada;

public class RodadaComparator implements Comparator<Rodada> {

	@Override
	public int compare(Rodada o1, Rodada o2) {
		if((o1!=null) && (o2!=null)) {
			Rodada r1 = (Rodada) o1;
			Rodada r2 = (Rodada) o2;
			return r1.getNumero().compareTo(r2.getNumero());
		} 
		return 0;
	}

}
