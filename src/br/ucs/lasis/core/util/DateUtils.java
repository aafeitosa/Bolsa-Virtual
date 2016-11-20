package br.ucs.lasis.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static final String FORMATO_BRASILEIRO = "dd/MM/yyyy HH:mm:ss";
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			FORMATO_BRASILEIRO);

	/**
	 * Retorna o primeiro dia do mês passado dado pela quantidade informada em
	 * n.
	 * 
	 * Ex.: Passando-se 3, tem-se o primeiro dia do mês de três meses atrás.
	 * 
	 * @param n
	 *            O numero de meses a retroceder
	 * 
	 * @return Um objeto <code>Date</code> contendo o primeiro dia do mês de n
	 *         meses atrás
	 */
	public static Date getPrimeiroDiaMesesAtras(int n) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, n * -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	/**
	 * Formata uma data no padrão brasileiro dd/MM/yyyy HH:mm:ss
	 * 
	 * @param date
	 *            A data a ser formatada
	 * @return Um <code>String</code> contendo a data formatada.
	 */
	public String formata(Date date) {
		if (null == date) {
			return "";
		}
		return formatter.format(date);
	}

	public Date getFimDia(Date date) {

		if (null == date) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();

	}

}
