package kr.co.glog.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

	public static final String PATTERN_NORMAL = "yyyyMMdd";
	public static final String PATTERN_MONTH = "yyyyMM";
	public static final String PATTERN_YEAR = "yyyy";
	public static final String PATTERN_NORMAL_DASHED = "yyyy-MM-dd";
	public static final String PATTERN_NORMAL_KOREAN = "yyyy년 MM월 dd일";
	public static final String PATTERN_YY_KOREAN = "yy년 MM월 dd일";
	public static final String[] CAL_QUERTERS = {"1", "1", "1", "1", "2", "2", "2", "2", "3", "3", "3", "3", "4", "4", "4", "4"};
	
	public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public final static long DAY_MILLIS = HOUR_MILLIS * 24;
    public final static long YEAR_MILLIS = DAY_MILLIS * 365;

	/**
	 * 2000-01-01 을 Timestamp로 리턴
	 * @return
	 */
	public static Timestamp get20000101() {
		String date = "2000-01-01 00:00:00";
		Timestamp timestamp = Timestamp.valueOf( date );
		return timestamp;
	}

    /**
	  * Date 형식의 문자열 중에서 yyyymmdd 만 리턴
	  * @param date
	  * @return
	  */
	 public static String getYyyymmdd( String date ) {
		 if ( date == null ) return "";
		 
		 date = date.replaceAll("[^0-9]", "");

		 if ( date.length() > 8 ) date = date.substring(0,8);
	 
		 return date;
	 }
	 
	 /**
	  * 두날짜 사이의 일수를 리턴
	  * @param fromDate yyyyMMdd 형식의 시작일
	  * @param toDate yyyyMMdd 형식의 종료일
	  * @return 두날짜 사이의 일수
	  */
	 public static int getDiffDayCount(String fromDate, String toDate) {
		 fromDate = DateUtil.getYyyymmdd(fromDate);
		 toDate = DateUtil.getYyyymmdd(toDate);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
		 try {
			 return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
		 } catch (Exception e) {
			 return 0;
		 }
	 }
	 
	 /**
	  * 시작일부터 종료일까지 사이의 날짜를 배열에 담아 리턴
	  * ( 시작일과 종료일을 모두 포함한다 )
	  * @param fromDate yyyyMMdd 형식의 시작일
	  * @param toDate yyyyMMdd 형식의 종료일
	  * @return yyyyMMdd 형식의 날짜가 담긴 ArrayList<String> 
	  */
	 public static ArrayList<String> getDiffDays(String fromDate, String toDate) {
		 fromDate = DateUtil.getYyyymmdd(fromDate);
		 toDate = DateUtil.getYyyymmdd(toDate);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		 Calendar cal = Calendar.getInstance();

		 try {
			 cal.setTime(sdf.parse(fromDate));
		 } catch (Exception e) {
		 }

		 int count = getDiffDayCount(fromDate, toDate);

		 // 시작일부터
		 cal.add(Calendar.DATE, -1);

		 // 데이터 저장
		 ArrayList<String> list = new ArrayList<String>();

		 for (int i = 0; i <= count; i++) {
			 cal.add(Calendar.DATE, 1);
			 list.add(sdf.format(cal.getTime()));
		 }
		 
		 return list;
	 }
	 
	/**
	 * 
	 * @Method      getToday
	 * @Date          2013. 11. 5.
	 * @Author       Alicee
	 * @History       2013. 11. 5. | Alicee | Created
	 * @Method      지정된 포맷으로 오늘 날짜 구하기
	 * 
	 * @param format
	 * @return
	 */
	public static String getToday(String format) {
		Date today = new Date();
		return new SimpleDateFormat(format).format(today);
	}
	
	/**
	 * 현재년
	 * @return
	 */
	public static String getThisYear() {
		return getToday().substring(0, 4);
	}
	public static String getYear(String dateString) {
		if (dateString.length() >= 4) return dateString.substring(0, 4);
		return "";
	}
	
	/**
	 * 현재월
	 * @return
	 */
	public static String getThisMonth() {
		return getToday().substring(4, 6);
	}
	public static String getMonth(String dateString) {
		if (dateString.length() >= 6) return dateString.substring(4, 6);
		return "";
	}
	
	/**
	 * 오늘날짜
	 * @return
	 */
	public static String getThisDay() {
		return getToday().substring(6, 8);
	}
	public static String getDay(String dateString) {
		if (dateString.length() >= 8) return dateString.substring(6, 8);
		return "";
	}
	
	/**
	 * 
	 * @Method      getToday
	 * @Date          2013. 10. 23.
	 * @Author       Alicee
	 * @History       2013. 10. 23. | Alicee | Created
	 * @Method      오늘 날짜 구하기
	 * 
	 * @return	yyyyMMdd 형태로 반환
	 */
	public static String getToday() {
		return getToday("yyyyMMdd");
	}
	
	/**
	 * 
	 * @Method      getYesterday
	 * @Date          2013. 11. 15.
	 * @Author       Alicee
	 * @History       2013. 11. 15. | Alicee | Created
	 * @Method      지정된 포맷으로 어제 날짜 구하기
	 * 
	 * @param format
	 * @return
	 */
	public static String getYesterday(String format) {
		Date today = new Date();
		Date yesterday = new Date();
		yesterday.setTime(today.getTime() - ((long)1000 * 60 * 60 * 24));
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(yesterday);
	}

	/**
	 * 
	 * @Method      getYesterday
	 * @Date          2013. 10. 23.
	 * @Author       Alicee
	 * @History       2013. 10. 23. | Alicee | Created
	 * @Method      오늘 기준으로 어제일 구하기
	 * 
	 * @return	yyyyMMdd 형태로 반환
	 */
	public static String getYesterday() {
		return getYesterday("yyyyMMdd");
	}
	
	/**
	 * 
	 * @Method      getTomorrow
	 * @Date          2020. 10. 29.
	 * @Author       skkim
	 * @History       2013. 11. 15. | Alicee | Created
	 * @Method      지정된 포맷으로 내일 날짜 구하기
	 * 
	 * @param format
	 * @return
	 */
	public static String getTomorrow(String format) {
		Date today = new Date();
		Date tomorrow = new Date();
		tomorrow.setTime(today.getTime() + ((long)1000 * 60 * 60 * 24));
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(tomorrow);
	}
	
	/**
	 * 
	 * @Method      getTomorrow
	 * @Date          2020. 10. 29.
	 * @Author       skkim
	 * @History       2013. 10. 23. | Alicee | Created
	 * @Method      오늘 기준으로 내일 구하기
	 * 
	 * @return	yyyyMMdd 형태로 반환
	 */
	public static String getTomorrow() {
		return getTomorrow("yyyyMMdd");
	}
	

	// 현재 시간 조회
	public static String getCurrentTime() {		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(new Date());
	}
	
	/**
	 * 
	 * @Method      getLastMonth
	 * @Date          2013. 10. 23.
	 * @Author       Alicee
	 * @History       2013. 10. 23. | Alicee | Created
	 * @Method      오늘 기준으로 지난 달 구하기
	 * 
	 * @return yyyyMM 형태로 반환
	 */
	public static String getLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		
		StringBuffer sb = new StringBuffer();
		sb.append(year);
		if ( month < 10 ) sb.append("0");
		sb.append(month);
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @Method		: getDayCountOfYear
	 * @History		: 
	 * @param @return 
	 * @return int 
	 * @throws 
	 * @Description	: 한 해의 날짜 수 리턴
	 */
	public static int getDayCountOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, 11, 31);
		return cal.get( Calendar.DAY_OF_YEAR );
	}

	/**
	 *
	 * @Method		: changeTimeZone
	 * @History		:
	 * @param @return
	 * @return int
	 * @throws
	 * @Description	: 타임존 변경
	 */
	public static Timestamp changeTimeZone(Timestamp ts, int val) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		cal.add(Calendar.HOUR, val);
		ts.setTime(cal.getTime().getTime());
		return ts;
	}
	
	/**
	 * @Method Name  : getUserDefinedDay
	 * @작성일   : 2013. 10. 30. 
	 * @작성자   : nhkim@t1soft.co.kr
	 * @변경이력  :
	 * @Method 설명 :	현재날짜 기준으로 원하는 날짜 형식 출력
	 * @param field
	 * @param amount
	 * @param format
	 * @return
	 */
	public static String getUserDefinedDay(int field, int amount, String format) {
		Calendar c = Calendar.getInstance();
		c.add(field, amount);
		return new SimpleDateFormat(format).format(c.getTime());
	}
	/**
	 * @Method Name  : getUserDefinedDaySpec
	 * @Date              : 2014. 5. 2. 
	 * @Author           : nhkim@t1soft.co.kr
	 * @History           :
	 * @Method          : 특정날짜를 기준으로 계산한 날짜를 반환
	 *
	 * @param field
	 * @param amount
	 * @param format
	 * @param baseDate
	 * @return
	 * @throws Exception
	 */
	public static String getUserDefinedDaySpec(int field, int amount, String format, String baseDate) throws Exception {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(formatter.parse(baseDate));
		c.add(field, amount);
		return formatter.format(c.getTime());
	}
	
	
	/**
	 * @Method Name  : getUserDayOfWeek
	 * @Date              : 2014. 5. 2. 
	 * @Author           : nhkim@t1soft.co.kr
	 * @History           :
	 * @Method          : 특정날짜가 속한 주에서 입력받은 요일에 해당하는 날짜를 반환 (ex, 20140404, Calendar.MONDAY 입력시 20140404가 속한 주의 월요일에 해당하는 날짜를 반환)
	 *
	 * @param baseDate
	 * @param dayOfWeek
	 * @return
	 * @throws Exception
	 */
	public static String getUserDayOfWeek(String baseDate, int dayOfWeek) throws Exception{	//"20140101"
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date d = formatter.parse(baseDate);
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return formatter.format(c.getTime());
	}
	
	/**
	 * @Method Name  : getDayOfWeek
	 * @Date         : 2020. 3. 7. 
	 * @Author       : skkim
	 * @History      :
	 * @Method       : 특정 날짜의 요일 int 값 리턴
	 *
	 * @param yyyymmdd
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static int getDayOfWeek( String yyyymmdd ) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date = formatter.parse(yyyymmdd);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * @Method Name  : getDayOfWeekCode
	 * @Date         : 2020. 3. 7. 
	 * @Author       : skkim
	 * @History      :
	 * @Method       : 특정 날짜의 요일 3자리 코드 리턴, sun, mon, tue, wed, thu, fri, sat
	 *
	 * @param yyyymmdd
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static String getWeekTypeCode( String yyyymmdd ) throws ParseException {
		String [] weekTypeCode = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };
 		return weekTypeCode[DateUtil.getDayOfWeek(yyyymmdd)-1];
	}
	
	/**
	 * @Method Name  : getUserMonthMin
	 * @Date              : 2014. 5. 2. 
	 * @Author           : nhkim@t1soft.co.kr
	 * @History           : 
	 * @Method          : 특정날짜가 속한 달의 첫번째 날짜를 반환
	 *
	 * @param baseDate
	 * @return
	 * @throws Exception
	 */
	public static String getUserMonthMin(String baseDate) throws Exception{	//"20140101"
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date d = formatter.parse(baseDate);
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return formatter.format(c.getTime());
	}
	/**
	 * @Method Name  : getUserMonthMax
	 * @Date              : 2014. 5. 2. 
	 * @Author           : nhkim@t1soft.co.kr
	 * @History           :
	 * @Method          : 특정날짜가 속한 달의 첫번째 날짜를 반환
	 *
	 * @param baseDate
	 * @return
	 * @throws Exception
	 */
	public static String getUserMonthMax(String baseDate) throws Exception{	//"20140101"
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date d = formatter.parse(baseDate);
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return formatter.format(c.getTime());
	}
	
	/**
	 * 
	 * @Method Name  : getQuarterLable
	 * @Date              : 2014. 4. 3. 
	 * @Author           : Alicee
	 * @History           : 2014. 4. 3 | Alicee | Created
	 * @Method          : 분기 표기이름 조회
	 *
	 * @param quarter
	 * @return
	 */
	public static String getQuarterLable(int quarter) {
		String[] lable = {"1/4", "2/4", "3/4", "4/4"};
		return lable[quarter-1];
	}
	
	/**
	 * 
	 * @Method Name  : getDayOfWeekLable
	 * @Date              : 2014. 4. 3. 
	 * @Author           : Alicee
	 * @History           : 2014. 4. 3 | Alicee | Created
	 * @Method          : 요일 표기이름 조회
	 *
	 * @param day 일(1) ~ 토(7)
	 * @return
	 */
	public static String getDayOfWeekLable(int day) {
		String[] lable = {"일", "월", "화", "수", "목", "금", "토"};
		return lable[day-1];
	}
	
	public static String getDayOfWeekLableEng(int day) {
		String[] lable = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
		return lable[day-1];
	}
	
	/*public static Date StringToDate(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return new Date();
	}*/
	
	
	/**
	 * 
	 * @Method Name  	: monthsBetween
	 * @Date            : 2014. 4. 3. 
	 * @Author          : 이종호
	 * @History         : 2014. 4. 3 | 이종호 | Created
	 * @Method          : 두 날짜 사이의 개월수 차이
	 *
	 * @param
	 * @return
	 */
	public static int monthsBetween(String strFrom, String strTo) throws Exception {
		int result = 0;
		
		strFrom	= StrUtils.remainNumberString( strFrom );
		strTo	= StrUtils.remainNumberString( strTo );
		
		int fromYear = Integer.parseInt(strFrom.substring(0, 4));
		int toYear = Integer.parseInt(strTo.substring(0, 4));
		int fromMonth = Integer.parseInt(strFrom.substring(4, 6));
		int toMonth = Integer.parseInt(strTo.substring(4, 6));
		
		result = (toYear - fromYear) * 12 + (toMonth - fromMonth);

		return result;
	}
	
	/**
	 * 
	 * @Method Name  	: daysBetween
	 * @Date            : 2014. 4. 3. 
	 * @Author          : 이종호
	 * @History         : 2014. 4. 3 | 이종호 | Created
	 * @Method          : 두 날짜 사이의 일수 차이
	 *
	 * @param
	 * @return
	 */
	public static int daysBetween(String strFrom, String strTo, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date fromDate = formatter.parse(strFrom);
		Date toDate = formatter.parse(strTo);

		long duration = toDate.getTime() - fromDate.getTime();

		return (int) (duration / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * 
	 * @Method Name  : daysFromNow
	 * @Date              : 2015. 1. 8. 
	 * @Author           : skkim
	 * @History           :
	 * @Method          : 주어진 시간과 현재 시간과의 일수 차이
	 *
	 * @param strFrom
	 * @return
	 * @throws ParseException
	 */
	public static int daysFromNow( Timestamp strFrom ) throws ParseException {

		long diffMillis	= System.currentTimeMillis() - strFrom.getTime();
		
		return (int) ( diffMillis / ( 1000 * 60 * 60 * 24 ) );
	}
	
	/**
	 * 
	 * @Method Name  : getWeek
	 * @Date              : 2014. 5. 2. 
	 * @Author           : Alicee
	 * @History           : 2014. 5. 2 | Alicee | Created
	 * @Method          : 해당 날짜가 속한 주간 조회하기
	 *
	 * @param baseDate
	 * @param format
	 * @return
	 */
	public static String[] getWeek(String baseDate, String format) throws Exception {
		String[] period = new String[2];
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		cal.setTime(formatter.parse(baseDate));
		
		int week = cal.get(Calendar.DAY_OF_WEEK);
		if ( week == Calendar.SUNDAY ) {
			period[0] = DateUtil.getUserDefinedDaySpec(Calendar.DATE, -6, format, baseDate);
			period[1] = baseDate;
		} else {
			period[0] = DateUtil.getUserDefinedDaySpec(Calendar.DATE, (2-week), format, baseDate);
			period[1] = DateUtil.getUserDefinedDaySpec(Calendar.DATE, (8-week), format, baseDate);
		}
		
		return period;
	}
	
	/**
	 * 
	 * @Method Name  : getMonthLastDate
	 * @Date              : 2014. 5. 2. 
	 * @Author           : Alicee
	 * @History           : 2015. 5. 2 | Alicee | Created
	 * @Method          : 주어진 날짜가 포함된 달의 마지막 날 가져오기
	 *
	 * @param baseDate
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static int getMonthLastDate(String baseDate, String format) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		cal.setTime(formatter.parse(baseDate));
		
		return cal.getActualMaximum(Calendar.DATE);
	}
	
	/**
	 * 
	 * @Method Name  : getDaysPeriod
	 * @Date              : 2014. 6. 4. 
	 * @Author           : Alicee
	 * @History           : 2014.6.4 | Alicee | Created
	 * @Method          : 두 기간 사이의 날짜/요일/월/년도 구하기
	 *
	 * @param fromDate
	 * @param toDate
	 * @param format
	 * @param dayType
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getDaysPeriod(String fromDate, String toDate, String format, int dayType) throws Exception {
		LinkedHashSet<Object> hashset = new LinkedHashSet<Object>();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar fromCal = Calendar.getInstance();
		Calendar toCal = Calendar.getInstance();
		fromCal.setTime(formatter.parse(fromDate));
		toCal.setTime(formatter.parse(toDate));
		
		int temp = 0;
		while ( fromCal.compareTo(toCal) <= 0 ) {
			if ( dayType == Calendar.DATE ) {
				temp = fromCal.get(dayType);
				if ( temp < 10 ) {
					hashset.add("0"+temp);
				} else {
					hashset.add(""+temp);
				}
				fromCal.add(Calendar.DATE, 1);
			} else if ( dayType == Calendar.DAY_OF_WEEK ) {
				temp = fromCal.get(dayType);
				hashset.add(""+temp);
				fromCal.add(Calendar.DATE, 1);
			} else if ( dayType == Calendar.MONTH ) {
				temp = fromCal.get(dayType);
				temp++;
				if ( temp < 10 ) {
					hashset.add("0"+temp);
				} else {
					hashset.add(""+temp);
				}
				fromCal.add(Calendar.MONTH, 1);
			} else if ( dayType  == Calendar.YEAR ) {
				temp = fromCal.get(dayType);
				hashset.add(""+temp);
				fromCal.add(Calendar.YEAR, 1);
			} else { //Quarter
				temp = fromCal.get(Calendar.MONTH);
				hashset.add(CAL_QUERTERS[temp]);
				fromCal.add(Calendar.MONTH, 1);
			}
		}
		
		return new ArrayList<Object>(hashset);
	}
	
	
	public static int diffDays(Date earlierDate, Date laterDate) {
        if( earlierDate == null || laterDate == null ) return 0;        
        return (int) ((laterDate.getTime() / DAY_MILLIS) - (earlierDate.getTime() / DAY_MILLIS));
	}
	
	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	

	/**
	 * 
	 * @Method Name  : addDate
	 * @Date         : 2015. 5. 12. 
	 * @Author       : mtelo01
	 * @History      :
	 * @Method       : ex) addDate("M", "yyyy-MM-dd", 1)
	 *					gubun에 ("Y", "M", "D") 이외의 값을 넣으면 현재날짜 돌려줌
	 * @param gubun
	 * @param format
	 * @param val
	 * @return
	 */
	public static String addDate(String gubun, String format, int val) {
		Calendar cal = Calendar.getInstance();
		if ("Y".equals(gubun)) {
			cal.add(Calendar.YEAR, val);
		} else if ("M".equals(gubun)) {
			cal.add(Calendar.MONTH, val);
		} else if ("D".equals(gubun)) {
			cal.add(Calendar.DATE, val);
		}
		
		return setDateFormat(cal, format);
	}

	public static String addDate(String yyyymmdd, String gubun, String format, int val) {
		Calendar cal = Calendar.getInstance();
		yyyymmdd = yyyymmdd.replaceAll("-",  "");
		cal.set( Integer.parseInt(yyyymmdd.substring(0,4) ), Integer.parseInt(yyyymmdd.substring(4,6) )-1, Integer.parseInt(yyyymmdd.substring(6,8) ) );

		if ("Y".equals(gubun)) {
			cal.add(Calendar.YEAR, val);
		} else if ("M".equals(gubun)) {
			cal.add(Calendar.MONTH, val);
		} else if ("D".equals(gubun)) {
			cal.add(Calendar.DATE, val);
		}

		String calDate = setDateFormat(cal, format);
		return calDate;
	}

	/**
	 * Date 형태의 날짜를 받아서 string 형태로 더해서 리턴한다.
	 *
	 * @param date
	 * @param gubun
	 * @param format
	 * @param val
	 * @return
	 */
	public static String addDate(Date date, String gubun, String format, int val) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if ("Y".equals(gubun)) {
			cal.add(Calendar.YEAR, val);
		} else if ("M".equals(gubun)) {
			cal.add(Calendar.MONTH, val);
		} else if ("D".equals(gubun)) {
			cal.add(Calendar.DATE, val);
		}

		String calDate = setDateFormat(cal, format);
		return calDate;
	}


	
	public static String setDateFormat(Calendar cal, String format) {
		return new SimpleDateFormat(format).format(cal.getTimeInMillis());
	}


	/**
	 * 데이터 포맷 변경 메소드
	 * @param date 원본 데이터 스트링
	 * @param currPattern 원본 데이터의 포맷
	 * @param newPattern 변경할 데이터의 포맷
	 * @return
	 * @throws ParseException
	 */
	public static String changeDateFormat(String date, String currPattern, String newPattern) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(currPattern);
		cal.setTime(formatter.parse(date));

		return new SimpleDateFormat(newPattern).format(cal.getTimeInMillis());
	}

	/**
	 * 
	 * @Method		: getAge
	 * @History		: 
	 * @param @param pin
	 * @param @return 
	 * @return int 
	 * @throws 
	 * @Description	: 주민등록번호로 나이계산
	 */
	public static int getAge( String pin ) {
		
		if ( pin == null || pin.trim().equals("") ) return 0;
		
		int age = 0;
		int year	= Integer.parseInt(pin.substring(0,2));
		if ( year > 5 ) year = 1900+year;
		else year = 2000+year;
 		int month 	= Integer.parseInt(pin.substring(2,4));
		int day 	= Integer.parseInt(pin.substring(4,6));
		
		Calendar birthday	= Calendar.getInstance();
		birthday.set( Calendar.YEAR, year);
		birthday.set( Calendar.MONTH, month-1);
		birthday.set( Calendar.DATE, day);
		
		Calendar now		= Calendar.getInstance();
		age	= now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		if ( (birthday.get(Calendar.MONTH) > now.get(Calendar.MONTH)) || 
				( birthday.get(Calendar.MONTH) == now.get(Calendar.MONTH) && birthday.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH) ) )
		{
	        age--;
	    }
		
		return age;
	}


	/**
	 * 문자열을 데이트 형을 받아 와서 Date 형 객체로 변환해서 리턴한다.
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDate(String dateStr, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.parse(dateStr);
	}

	/**
	 * 타임스탬프를 받아 와서 String으로 변환해서 리턴한다.
	 * @param timestamp
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String timestampToStr(Timestamp timestamp, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format( new Date( timestamp.getTime() ) );
	}

	/**
	 * 타임스탬프를 받아 와서 Date 형 객체로 변환해서 리턴한다.
	 * @param timestamp
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date timestampToDate(Timestamp timestamp, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.parse( timestampToStr(timestamp, format) );
	}

	public static Date addTime(Date date, String gubun, int val) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if ("H".equals(gubun)) {
			cal.add(Calendar.HOUR, val);
		} else if ("m".equals(gubun)) {
			cal.add(Calendar.MINUTE, val);
		} else if ("s".equals(gubun)) {
			cal.add(Calendar.SECOND, val);
		}

		return cal.getTime();
	}

	public static void main( String [] args ) throws ParseException
	{
		System.out.println( DateUtil.get20000101() );
		System.out.println( DateUtil.daysFromNow( new Timestamp( System.currentTimeMillis() ) ) );
		System.out.println( getToday( "yyyy-MM-dd" ) );
		System.out.println( addDate( "20220131", "D", "yyyyMMdd", 1 ) );
		System.out.println( DateUtil.getDayCountOfYear() );
		
		System.out.println( DateUtil.getWeekTypeCode("20200307"));
		System.out.println( DateUtil.getDiffDays("20201029", "20201130"));
		System.out.println( DateUtil.daysBetween("20220129", "20220130", "yyyyMMdd") );
		System.out.println( DateUtil.getDayOfWeek( "20220127" ) + ", " + DateUtil.getDayOfWeekLableEng( DateUtil.getDayOfWeek( "20220127" ) ) );
	}

}
