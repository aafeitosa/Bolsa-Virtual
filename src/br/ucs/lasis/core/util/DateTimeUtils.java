package br.ucs.lasis.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class DateTimeUtils {

	private DateTimeUtils() {
	}

	public static List<Date> getDaysDateRange(Date first, Date second) {

		if (isSameYear(first, second)) {
			return getDateDaysSameYear(toCalendar(first), toCalendar(second));
		}

		return getDateDaysDifferentYears(toCalendar(first), toCalendar(second));

	}

	public static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static boolean isSameYear(Date first, Date second) {
		Calendar firstCal = Calendar.getInstance();
		firstCal.setTime(first);
		Calendar secondCal = Calendar.getInstance();
		secondCal.setTime(second);
		return firstCal.get(Calendar.YEAR) == secondCal.get(Calendar.YEAR);
	}

	public static List<Date> getDateDaysSameYear(Calendar initialCal, Calendar finalCal) {

		int firstDayPeriod = initialCal.get(Calendar.DAY_OF_YEAR);
		int lastDayPeriod = finalCal.get(Calendar.DAY_OF_YEAR);

		List<Date> dates = new ArrayList<>();
		dates.add(initialCal.getTime());

		Calendar calToCount = (Calendar) initialCal.clone();
		for (int i = firstDayPeriod; i < lastDayPeriod; i++) {
			calToCount.add(Calendar.DAY_OF_YEAR, 1);
			dates.add(calToCount.getTime());
		}

		return dates;

	}

	public static List<Date> getDateDaysDifferentYears(Calendar initialCal, Calendar finalCal) {

		List<Integer> yearRange = getYearRange(initialCal, finalCal);

		List<Date> retorno = new ArrayList<>();
		Calendar cal1;
		Calendar cal2;
		for (int i = 0; i < yearRange.size(); i++) {

			if (0 == i) {
				cal1 = Calendar.getInstance();
				cal1.setTime(DateTimeUtils.getEndOfYear(yearRange.get(i)));
				retorno.addAll(getDateDaysSameYear(initialCal, cal1));
				continue;
			}

			if (i == (yearRange.size() - 1)) {
				cal1 = Calendar.getInstance();
				cal1.setTime(DateTimeUtils.getBeginningOfYear(yearRange.get(i)));
				retorno.addAll(getDateDaysSameYear(cal1, finalCal));
				continue;
			}

			cal1 = Calendar.getInstance();
			cal1.setTime(DateTimeUtils.getBeginningOfYear(yearRange.get(i)));
			cal2 = Calendar.getInstance();
			cal2.setTime(DateTimeUtils.getEndOfYear(yearRange.get(i)));

			retorno.addAll(getDateDaysSameYear(cal1, cal2));

		}

		return retorno;

	}

	public static List<Integer> getYearRange(Calendar initialCal, Calendar finalCal) {

		int fistYearPeriod = initialCal.get(Calendar.YEAR);
		int lastYearPeriod = finalCal.get(Calendar.YEAR);

		List<Integer> retorno = new ArrayList<>();
		for (int i = fistYearPeriod; i <= lastYearPeriod; i++) {
			retorno.add(i);
		}

		return retorno;

	}

	public static boolean isSameDate(Date first, Date second) {
		return 0 == DateTimeUtils.getBeginingOfDay(first).compareTo(DateTimeUtils.getBeginingOfDay(second));
	}

	public static Date getEndOfDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	public static Date getBeginingOfDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getBeginningOfYear(int year) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		return getBeginingOfDay(calendar.getTime());
	}

	public static Date getEndOfYear(int year) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);

		return getEndOfDay(calendar.getTime());
	}

	public static Long getMinutes(Date dateInitial, Date dateFinal) {
		Calendar calInitial = Calendar.getInstance();
		Calendar calFinal = Calendar.getInstance();

		calInitial.setTime(dateInitial);
		calFinal.setTime(dateFinal);

		return ((calFinal.getTimeInMillis() - calInitial.getTimeInMillis())) / 60000;
	}

	public static Date formatParseDate(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}

	public static boolean isFirstDateBefore(Date first, Date second) {
		return getBeginingOfDay(first).before(getBeginingOfDay(second));
	}

	public static boolean isFirstDateAfter(Date first, Date second) {
		return getBeginingOfDay(first).after(getBeginingOfDay(second));
	}

	public static Integer daysBetweenDates(Date dateInitial, Date dateFinal) {
		Long days = (dateFinal.getTime() - dateInitial.getTime() + 3600000L) / 86400000L;
		return days.intValue();
	}

}
