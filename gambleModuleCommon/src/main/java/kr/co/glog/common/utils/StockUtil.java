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
public class StockUtil {

	public static String toStockCode7( String stockCode6 ) {
		String header = "A";                    // 일반 주식은 다음에서 A로 시작
		if ( stockCode6.charAt(0) == '5' || stockCode6.charAt(0) == '6' || stockCode6.charAt(0) == '7' ) header = "Q";         // ETN은 Q로 시작하고 500000번대..
		return header + stockCode6;
	}
	
}
