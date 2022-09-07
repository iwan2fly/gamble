package kr.co.glog.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StrUtils {

	
	// Byte to Hex String
	public static String byteToHexString( byte [] bytes ) {
		StringBuffer sb = new StringBuffer();
		for ( byte b : bytes ) {
			sb.append( Integer.toString((b &0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
	
	/**
     * Private constructor to prevent instantiation of the class
     */
	private StrUtils()
	{
		
	}
	
	private static StrUtils str = new StrUtils();
	
	public static StrUtils getInstance()
	{
		return str;
	}
	
	public static boolean isNumber( char c )
	{
		if ( c >= '0' && c <= '9' )
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isAlpha( char c )
	{
		if ( c >= 'A' && c <= 'Z' )
		{
			return true;
		}
		else if ( c >= 'a' && c <= 'z' )
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isNumber( String str )
	{
		for ( int i = 0; i < str.length(); i++ )
		{
			if ( !isNumber( str.charAt(i) ) )
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAlpha( String str )
	{
		for ( int i = 0; i < str.length(); i++ )
		{
			if ( !isAlpha( str.charAt(i) ) )
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	public static boolean isAlphaNumeric( String str )
	{
		for ( int i = 0; i < str.length(); i++ )
		{
			if ( !isNumber( str.charAt(i) ) && !isAlpha( str.charAt(i) ) )
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static String removeChar( String str, char c )
	{
		String newStr	= "";
		
		if ( str == null ) return str;
		
		for ( int i = 0; i < str.length(); i++ )
		{
			if ( str.charAt(i) != c )
			{
				newStr	+= str.charAt(i);
			}
		}
		
		return newStr;
	}
	
	
	public static String replaceChar( String str, char source, char target )
	{
		String newStr	= "";
		
		if ( str == null ) return str;
		
		for ( int i = 0; i < str.length(); i++ )
		{
			if ( str.charAt(i) == source )
			{
				newStr	+= target;
			}
			else 
			{
				newStr	+= str.charAt(i);
			}
		}
		
		return newStr;
	}
	
	public static String replace( String str, int from, int length, char target )
	{
		String newStr	= "";
		
		// 문자열 길이보다 치환길이가 길면.. 길이 내에서만 치환
		if ( str.length() < from + length )
		{
			length = str.length() - from;
		}
		
		// 치환부분 치환
		newStr	= str.substring( 0, from );
		for ( int i = 0; i < length; i++ )
		{
			newStr += target;
		}
		
		// 나머지 부분 붙임
		newStr	= newStr + str.substring( from+length, str.length() );
		
		return newStr;
			
	}
	

	public static boolean isEmailAddr( String str )
	{
		if ( str == null || str.trim().equals("") ) return false;
		
		Pattern pattern = Pattern.compile("\\w+[@]\\w+\\.\\w+");
        Matcher match = pattern.matcher(str);
        return match.find();
	}
	
	
	public static boolean isContainHangul( String str )
	{
		byte [] strByte = str.getBytes();
		for( int i = 0; i < strByte.length; i++ )
		{
		    if ( Integer.parseInt(Integer.toString(strByte[i]) ) < 0 )
		    {
		    	return true;
		    }
		}
		
		return false;
	}
	
	public static String toCurrency( String str )
	{
		Long value	= 0L;
		
		try
		{
			value =	Long.parseLong( str );
		}
		catch ( Exception e )
		{
			return str;
		}
		
		return toCurrency( value );
	}
	
	
	public static String toCurrency( int value )
	{
		String sign	= "";
		String returnStr	= "";
		
		int absValue	= Math.abs( value );
		if ( value < 0 )
		{
			sign	= "-";
		}
		
		String str	= "" + absValue;
		int count	= 0;
		for ( int i = str.length()-1; i >= 0; i-- )
		{
			if ( count != 0 && count % 3 == 0 )
			{
				returnStr	= "," + returnStr;
			}
			
			returnStr	= str.charAt(i) + returnStr;
			//System.out.println( count + ":" + returnStr );
			count++;
		}
		
		return sign + returnStr;
	}
	

	public static String toCurrency( long value )
	{
		String sign	= "";
		String returnStr	= "";
		
		long absValue	= Math.abs( value );
		if ( value < 0 )
		{
			sign	= "-";
		}
		
		String str	= "" + absValue;
		int count	= 0;
		for ( int i = str.length()-1; i >= 0; i-- )
		{
			if ( count != 0 && count % 3 == 0 )
			{
				returnStr	= "," + returnStr;
			}
			
			returnStr	= str.charAt(i) + returnStr;
			//System.out.println( count + ":" + returnStr );
			count++;
		}
		
		return sign + returnStr;
	}
	
	// 주민등록번호, 사업자등록번호, 법인번호에 - 붙이기..
	public static String toPin( String pin )
	{
		if ( pin != null ) pin = pin.replaceAll("-","");
		
		if ( pin == null || pin.equals("") ) pin = "";
		else if ( pin.length() == 13 ) pin = pin.substring(0,6) + "-" + pin.substring(6, 13);
		else if ( pin.length() == 10 ) pin = pin.substring(0,3) + "-" + pin.substring(3,5) + "-" + pin.substring(5,10);
		
		return pin;
	}
	
	public static String toBirthday( String day ) {
		if ( day == null ) return "";
		day = StrUtils.remainNumberString(day);
		if ( day.length() == 13 ) {
			return day.substring(0,2) + "-" + day.substring(2,4) + "-" + day.substring(4,6);
		} else if ( day.length() == 8 ) {
			return day.substring(0,4) + "년 " + day.substring(4,6) + "월 " + day.substring(6,8) + "일";
		} else if ( day.length() == 6 ) {
			return day.substring(0,2) + "-" + day.substring(2,4) + "-" + day.substring(4,6);
		} else {
			return day;
		}
	}
	
	public static String toBirthday( String day, String delimeter ) {
		if ( day == null ) return "";
		day = StrUtils.remainNumberString(day);
		if ( day.length() == 13 ) {
			return day.substring(0,2) + delimeter + day.substring(2,4) + delimeter + day.substring(4,6);
		} else if ( day.length() == 8 ) {
			return day.substring(0,4) + delimeter + day.substring(4,6) + delimeter + day.substring(6,8);
		} else if ( day.length() == 6 ) {
			return day.substring(0,2) + delimeter + day.substring(2,4) + delimeter + day.substring(4,6);
		} else {
			return day;
		}
	}
	
	public static String toPhone( String phone ) {
		return toPhone( phone, "-" );
	}
	public static String toPhone( String phone, String delimeter ) {
		
		if ( phone != null && phone != "" && phone.length() >= 9 ) {
			phone = phone.replaceAll("-", "");
			
			if ( phone.charAt(0) == '0' ) phone = phone.substring( 1, phone.length() );
			int length = phone.length();
			// 서울
			if ( phone.charAt(0) == '2' ) {
				phone = "0" + phone.substring(0,1) + "-" + phone.substring(1,length-4) + "-" + phone.substring(length-4,length);
			// 휴대폰
			} else if ( phone.charAt(0) == '1' ) {
				phone = "0" + phone.substring(0,2) + "-" + phone.substring(2,length-4) + "-" + phone.substring(length-4,length);
			// 기타
			} else {
				phone = "0" + phone.substring(0,2) + "-" + phone.substring(2,length-4) + "-" + phone.substring(length-4,length);
			}
		}
		return phone;
	}
	
	public static String remainNumberString( String str ) {
		String result	 = "";
		for ( int i = 0; i < str.length(); i++ ) {
			if ( StrUtils.isNumber( str.charAt(i))) {
				result += str.charAt(i);
			}
		}
		return result;
	}
	
	// 한글 2바이트 계산 길이
	public static int lengthMS949( String str ) throws UnsupportedEncodingException {
		byte [] bytes = str.getBytes("ms949");
		return bytes.length;
	}
	
	// 한글 3바이트 계산 길이
	public static int lengthUTF8( String str ) throws UnsupportedEncodingException {
		byte [] bytes = str.getBytes("utf-8");
		return bytes.length;
	}
	
	// 주민등록번호에서 성별코드(1: 남성, 2: 여성) 가져오기
	public static String pinToGenderCode(String pin) {
		String key = "";
		String genderCode = "";
		
		// 주민등록번호 7번째 자리의 숫자가
		if (pin != null && pin.length() > 7) {
			key = pin.substring(6, 7);			
		}
		
		String[] male = {"1", "3", "5", "7"}; // 홀수면 남성
		String[] female = {"2", "4", "6", "8"}; // 짝수면 여성
		
		// 주민번호로 성별구하기	
		if (Arrays.stream(male).anyMatch(key::equals)) {
			genderCode = "1"; // 남성
		} else if (Arrays.stream(female).anyMatch(key::equals)) {
			genderCode = "2"; // 여성
		}
		
		return genderCode;
	}
	
	// 주민등록번호를 통해 생년월일 yyyymmdd 포맷으로 가져오기
	public static String pinToBirthDay(String pin) {
		String key = "";
		String yearPrefix = "";
		
		// 주민등록번호 7번째 자리의 숫자가
		if (pin != null && pin.length() > 7) {
			key = pin.substring(6, 7);			
		}
		
		String[] year1900 = {"1", "2", "5", "6"}; // 이면 1900년대 출생
		String[] year2000 = {"3", "4", "7", "8"}; // 이면 2000년대 출생
		
		if (Arrays.stream(year1900).anyMatch(key::equals)) {
			yearPrefix = "19" + pin.substring(0, 6);
		} else if (Arrays.stream(year2000).anyMatch(key::equals)) {
			yearPrefix = "20" + pin.substring(0, 6);
		}
		
		return yearPrefix;
	}

	// 날짜 문자열에 구분기호 넣기
	public static String toFormatDate( String date, String delimiter ) {
		if ( date == null || date.length() < 8 ) return date;
		else {
			return date.substring(0,4) + delimiter + date.substring(4,6) + delimiter + date.substring(6,8);
		}
	}
	
	public static String toFormatDate( String date ) {
		return toFormatDate( date, "-" ); 
	}
	
	/*public static String printJSONObjectPretty(Object obj) {
		String resultString = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			resultString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			log.error("", e);
		} catch (JsonMappingException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return resultString;
	}*/

	public static String printObjectString(Object obj) {
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
	}

	public static String printObjectString(Object obj, String[] fieldArray) {
		ToStringBuilder tb = new ToStringBuilder(obj, ToStringStyle.MULTI_LINE_STYLE);

		Class < ? > klass = obj.getClass();
		Class < ? > [] parameterTypes = {};
		for (String getterName: fieldArray) {
			try {
				Method method = klass.getMethod("get" + org.apache.commons.lang3.StringUtils.capitalize(getterName), parameterTypes);
				tb.append(getterName, method.invoke(obj));

			} catch (NoSuchMethodException e) {
				log.error("", e);
			} catch (SecurityException e) {
				log.error("", e);
			} catch (IllegalAccessException e) {
				log.error("", e);
			} catch (IllegalArgumentException e) {
				log.error("", e);
			} catch (InvocationTargetException e) {
				log.error("", e);
			}
		}

		return tb.toString();
	}

	/*// XML 텍스트를 JSON 텍스트로 변환합니다.
	public static String xmlStringToJsonString( String xmlString ) throws JSONException {

		JSONObject xmlJSONObj = XML.toJSONObject( xmlString );
		String jsonString = xmlJSONObj.toString();
		System.out.println(jsonString);

		return jsonString;
	}*/

	
	public static void main( String [] args ) throws Exception {
		SimpleDateFormat sdf	= new SimpleDateFormat("yyyyMMddHHmmssS");
//		for ( int i = 0; i < 10000; i++ ) {
//			System.out.println(System.currentTimeMillis());
//			System.out.println(sdf.format(System.currentTimeMillis()));
//			Thread.sleep( 1L );
//		}

		int ARRAY_SIZE = 9000000;
		Random ran = new Random();
		long startMill = System.currentTimeMillis();
		int[] a = new int[ARRAY_SIZE];
		int tmpNum;
		int tmpRandomNum;
		for (int i=0;i<a.length;i++ ) a[i] = i+1;
		for (int i=0;i<a.length;i++){
			tmpRandomNum = ran.nextInt(ARRAY_SIZE);
			tmpNum = a[i];
			a[i] = a[tmpRandomNum];
			a[tmpRandomNum] = tmpNum;
		}
		long endMill = System.currentTimeMillis();
		System.out.println(" "+(endMill-startMill) );
	}
	
}
