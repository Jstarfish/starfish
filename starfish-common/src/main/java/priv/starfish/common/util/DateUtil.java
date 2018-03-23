package priv.starfish.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.time.DateUtils;
import priv.starfish.common.base.DateFormats;


/**
 * 
 * @author Hu Changwei
 * @date 2013-06-11
 * 
 */
public class DateUtil {
	private DateUtil() {
		// prevent from being initialized
	}

	public static final SimpleDateFormat STD_DATE_FORMAT;
	public static final SimpleDateFormat STD_TIME_FORMAT;
	public static final SimpleDateFormat STD_DATE_TIME_FORMAT;

	public static final SimpleDateFormat STD_SHORT_TIME_FORMAT;
	public static final SimpleDateFormat STD_SHORT_DATE_TIME_FORMAT;
	public static final SimpleDateFormat STD_TIMESTAMP_FORMAT;
	//
	public static final SimpleDateFormat CMP_DATE_TIME_FORMAT;
	public static final SimpleDateFormat DATE_INT_FORMAT;
	public static final SimpleDateFormat TIME_INT_FORMAT;
	//
	public static final SimpleDateFormat DATE_DIR_FORMAT;
	public static final SimpleDateFormat DATE_FILE_FORMAT;
	public static final SimpleDateFormat DATE_TIME_FILE_FORMAT;
	public static final SimpleDateFormat TIMESTAMP_FILE_FORMAT;

	// 缓存日期格式
	private static Map<String, DateFormat> dateFormats = new ConcurrentHashMap<String, DateFormat>();

	static {
		STD_DATE_FORMAT = new SimpleDateFormat(DateFormats.STD_DATE);
		STD_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_TIME);
		STD_DATE_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_DATE_TIME);
		STD_SHORT_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_SHORT_TIME);
		STD_SHORT_DATE_TIME_FORMAT = new SimpleDateFormat(DateFormats.STD_SHORT_DATE_TIME);
		STD_TIMESTAMP_FORMAT = new SimpleDateFormat(DateFormats.STD_TIMESTAMP);
		//
		CMP_DATE_TIME_FORMAT = new SimpleDateFormat(DateFormats.CMP_DATE_TIME);
		DATE_INT_FORMAT = new SimpleDateFormat(DateFormats.INT_DATE);
		TIME_INT_FORMAT = new SimpleDateFormat(DateFormats.INT_TIME);
		//
		DATE_DIR_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
		DATE_FILE_FORMAT = new SimpleDateFormat("yyyyMMdd");
		DATE_TIME_FILE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
		TIMESTAMP_FILE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

		// 常见日期格式
		dateFormats.put(DateFormats.STD_DATE, STD_DATE_FORMAT);
		dateFormats.put(DateFormats.STD_TIME, STD_TIME_FORMAT);
		dateFormats.put(DateFormats.STD_DATE_TIME, STD_DATE_TIME_FORMAT);
		//
		dateFormats.put(DateFormats.CMP_DATE_TIME, CMP_DATE_TIME_FORMAT);
		dateFormats.put(DateFormats.INT_DATE, DATE_INT_FORMAT);
		dateFormats.put(DateFormats.INT_TIME, TIME_INT_FORMAT);
		//
		dateFormats.put(DateFormats.STD_SHORT_TIME, STD_SHORT_TIME_FORMAT);
		dateFormats.put(DateFormats.STD_SHORT_DATE_TIME, STD_SHORT_DATE_TIME_FORMAT);
		dateFormats.put(DateFormats.STD_TIMESTAMP, STD_TIMESTAMP_FORMAT);
	}

	public static DateFormat getDateFormat(String format) {
		DateFormat retFormat = dateFormats.get(format);
		if (retFormat == null) {
			retFormat = new SimpleDateFormat(format);
			dateFormats.put(format, retFormat);
		}
		return retFormat;
	}

	// 默认时区"GMT+08:00"
	public final static TimeZone defaultTimezone = TimeZone.getTimeZone("GMT+08:00");

	public static Calendar getCalendar(TimeZone timeZone) {
		if (timeZone == null) {
			timeZone = defaultTimezone;
		}
		return Calendar.getInstance(defaultTimezone);
	}

	public static Calendar getCalendar() {
		return getCalendar(null);
	}

	public static boolean isLeapYear(int year) {
		return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
	}

	public static Date get(int year, int month, int date) {
		return get(year, month, date, 0, 0, 0, 0);
	}

	public static Date get(int year, int month, int date, int hourOfDay, int minute) {
		return get(year, month, date, hourOfDay, minute, 0, 0);
	}

	public static Date get(int year, int month, int date, int hourOfDay, int minute, int second) {
		return get(year, month, date, hourOfDay, minute, second, 0);
	}

	public static Date get(int year, int month, int date, int hourOfDay, int minute, int second, int millSecond) {
		year = NumUtil.narrow(year, 0, 9999);
		month = NumUtil.narrow(month, 1, 12);
		switch (month) {
		case 2:
			date = isLeapYear(year) ? NumUtil.narrow(date, 1, 29) : NumUtil.narrow(date, 1, 28);
			break;
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			date = NumUtil.narrow(date, 1, 31);
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			date = NumUtil.narrow(date, 1, 30);
			break;
		default:
		}
		hourOfDay = NumUtil.narrow(hourOfDay, 0, 23);
		minute = NumUtil.narrow(minute, 0, 59);
		second = NumUtil.narrow(second, 0, 59);
		millSecond = NumUtil.narrow(millSecond, 0, 999);
		//
		Calendar cal = getCalendar();
		month = month - 1;
		cal.set(year, month, date, hourOfDay, minute, second);
		cal.set(Calendar.MILLISECOND, millSecond);
		return cal.getTime();
	}

	public static Date castTo_yyyyMMdd(Date xDate) {
		Calendar cal = getCalendar();
		cal.setTime(xDate);
		//
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	//
	public static String toStdDateStr(Date date) {
		return date == null ? null : STD_DATE_FORMAT.format(date);
	}

	public static Date fromStdDateStr(String dtStr) throws ParseException {
		return STD_DATE_FORMAT.parse(dtStr);
	}

	public static String toStdTimeStr(Date date) {
		return date == null ? null : STD_TIME_FORMAT.format(date);
	}

	public static Date fromStdTimeStr(String dtStr) throws ParseException {
		return STD_TIME_FORMAT.parse(dtStr);
	}

	public static String toStdDateTimeStr(Date date) {
		return date == null ? null : STD_DATE_TIME_FORMAT.format(date);
	}

	public static Date fromStdDateTimeStr(String dtStr) throws ParseException {
		return STD_DATE_TIME_FORMAT.parse(dtStr);
	}

	public static String toStdShortTimeStr(Date date) {
		return date == null ? null : STD_SHORT_TIME_FORMAT.format(date);
	}

	public static Date fromStdShortTimeStr(String dtStr) throws ParseException {
		return STD_SHORT_TIME_FORMAT.parse(dtStr);
	}

	public static String toStdShortDateTimeStr(Date date) {
		return date == null ? null : STD_SHORT_DATE_TIME_FORMAT.format(date);
	}

	public static Date fromStdShortDateTimeStr(String dtStr) throws ParseException {
		return STD_SHORT_DATE_TIME_FORMAT.parse(dtStr);
	}

	public static String toStdTimestampStr(Date date) {
		return date == null ? null : STD_TIMESTAMP_FORMAT.format(date);
	}

	public static Date fromStdTimestampStr(String dtStr) throws ParseException {
		return STD_TIMESTAMP_FORMAT.parse(dtStr);
	}

	public static String toCmpDateTimeStr(Date date) {
		return date == null ? null : CMP_DATE_TIME_FORMAT.format(date);
	}

	public static Date fromCmpDateTimeStr(String dtStr) throws ParseException {
		return CMP_DATE_TIME_FORMAT.parse(dtStr);
	}

	// yyyyMMddHHmmss => yyyy-MM-dd HH:mm:ss
	public static String fromCmpDateTimeToStdDateTimeStr(String dtStr) {
		try {
			Date date = fromCmpDateTimeStr(dtStr);
			return toStdDateTimeStr(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	//

	public static Integer toDateInteger(Date date) {
		return date == null ? null : Integer.valueOf(DATE_INT_FORMAT.format(date));
	}

	public static Integer toDateInteger() {
		return toDateInteger(new Date());
	}

	public static Integer toTimeInteger(Date date) {
		return date == null ? null : Integer.valueOf(TIME_INT_FORMAT.format(date));
	}

	public static Integer toTimeInteger() {
		return toTimeInteger(new Date());
	}

	public static String toDateDirStr(Date date) {
		return date == null ? null : DATE_DIR_FORMAT.format(date);
	}

	public static String toDateDirStr() {
		return toDateFileStr(new Date());
	}

	public static String toDateFileStr(Date date) {
		return date == null ? null : DATE_FILE_FORMAT.format(date);
	}

	public static String toDateFileStr() {
		return toDateFileStr(new Date());
	}

	public static String toDateTimeFileStr(Date date) {
		return date == null ? null : DATE_TIME_FILE_FORMAT.format(date);
	}

	public static String toDateTimeFileStr() {
		return toDateTimeFileStr(new Date());
	}

	public static String toTimestampFileStr(Date date) {
		return date == null ? null : TIMESTAMP_FILE_FORMAT.format(date);
	}

	public static String toTimestampFileStr() {
		return toTimestampFileStr(new Date());
	}

	//

	/**
	 * 日期计算
	 * 
	 * @param refDate
	 * @param amount
	 * @return
	 */
	public static Date addYears(Date refDate, int amount) {
		return DateUtils.addYears(refDate, amount);
	}

	public static Date addYears(int amount) {
		Date refDate = new Date();
		return DateUtils.addYears(refDate, amount);
	}

	public static Date addMonths(Date refDate, int amount) {
		return DateUtils.addMonths(refDate, amount);
	}

	public static Date addMonths(int amount) {
		Date refDate = new Date();
		return DateUtils.addMonths(refDate, amount);
	}

	public static Date addDays(Date refDate, int amount) {
		return DateUtils.addDays(refDate, amount);
	}

	public static Date addDays(int amount) {
		Date refDate = new Date();
		return DateUtils.addDays(refDate, amount);
	}

	public static Date addHours(Date refDate, int amount) {
		return DateUtils.addHours(refDate, amount);
	}

	public static Date addHours(int amount) {
		Date refDate = new Date();
		return DateUtils.addHours(refDate, amount);
	}

	public static Date addMinutes(Date refDate, int amount) {
		return DateUtils.addMinutes(refDate, amount);
	}

	public static Date addMinutes(int amount) {
		Date refDate = new Date();
		return DateUtils.addMinutes(refDate, amount);
	}

	public static Date addSeconds(Date refDate, int amount) {
		return DateUtils.addSeconds(refDate, amount);
	}

	public static Date addSeconds(int amount) {
		Date refDate = new Date();
		return DateUtils.addSeconds(refDate, amount);
	}

	public static Date addWeeks(Date refDate, int amount) {
		return DateUtils.addWeeks(refDate, amount);
	}

	public static Date addWeeks(int amount) {
		Date refDate = new Date();
		return DateUtils.addWeeks(refDate, amount);
	}

	/** 返回最新近的1秒（比如用于解决浏览器的Last-Modified只能精确到秒的问题） */
	public static long ceilToSecond(long timeInMs) {
		int remain = (int) (timeInMs % 1000);
		if (remain > 0) {
			return timeInMs + 1000 - remain;
		} else {
			return timeInMs;
		}
	}
}
