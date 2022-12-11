const log = console.log;

/*function goLoginPage() {
	var prefix = location.href.substring(0, location.href.indexOf('/'+CONTEXT_ROOT+'/'));
	location.href = prefix + CONTEXT_ROOT + '/login';	
}*/


// Reload 방지
function preventReload() { 
    if (event.keyCode == 116) {
    	alert("이 화면에서 새로고침은 불가능합니다.");
        event.keyCode = 2;
        return false;
    } else if (event.ctrlKey && (event.keyCode == 78 || event.keyCode == 82)) {
        return false;
    }
}

// 문자열 바이트 수
function byteLengthStr( str ) {
	
	var size = 0;
	
	if (str != null && str.length != 0) { 
		for (var i = 0; i < str.length; i++) {
			size += byteSizeChar(str.charAt(i));
		}
	}
	
	return size;
}

// 문자 바이트 수
function byteSizeChar( ch ) {
	
	if (ch == null || ch.length == 0) return 0;

	var charCode	= ch.charCodeAt(0);
	if ( charCode <= 0x00007F ) return 1;
	else if ( charCode <= 0x00007F ) return 2;
	else if ( charCode <= 0x00FFFF ) return 3;
	else return 4;
}

//한글 2바이트 처리 문자 갯수
function hangulLength( str ) {
	var size = 0;
	
	if (str == null || str.length == 0) return size;

	for (var i = 0; i < str.length; i++) {
		c	= str.charAt(i);
		if ( escape(c).length > 4 ) {
			size += 2;
		} else {
			size += 1;
		}
	}
	
	return size;
}

// 쓰지 마세요.. 위에거 쓰세요
function ms949LengthStr( str ) {
	return hangulLEngth(str);
}


// 문자열 컷
function cutStr( str, length ) {
	
	if ( str == null ) return str;
	
	var returnStr = "";
	var size = 0;
	var strLength	= hangulLength(str);

	for ( var i = 0; i < str.length; i++ ) {
		c = str.charAt(i);
		if ( escape(c).length > 4 ) {
			size+=2;
		} else {
			size+=1;
		}
		
		if ( size < length ) {
			returnStr += c;
		}
	}
	
	if ( strLength > length ) returnStr += "...";
	
	return returnStr;
}

/**
 * 퍼센트 -> 소수 (90% -> 0.9)
 * ex) percentToDecimal(90) -> return 0.9
 */
function percentToDecimal(percent) {
	return (100 - percent) / 100;
}

/**
 * 원단위 절사 (일단은 100, 10, 1원 만 지원함)
 * ex) cutPrice(100) 	12,300 -> 12,000 (100원 단위 절사)
 * ex) cutPrice(10) 	12,340 -> 12,300 (10원 단위 절사)
 * ex) cutPrice(1) 		12,345 -> 12,340 (1원 단위 절사)
 */
function cutPrice(price, unit) {
	let n = unit * 10;
	if (n === 1000 || n === 100 || n === 10) {
		return Math.floor(price / n) * n;
	}
	return price;
}

// 3자리마다 ,
function toCurrency(value) {

	// if(isNaN(value)) return value;
	if('-' == value || '' == value) return value;

	try {
		let number;
		if (isNaN(value)) {

			const onlyNumber = /[^0-9\.\-]/g; // 숫자와 점만 허용
			number = value.toString().replace(onlyNumber, '');

			/*let negertive = value.startWith('-')>0? 1:0;

			const firstPointPosition = number.indexOf('.') + 1;
			console.log('# 1. firstPointPosition: ', str)
			const str = number.substring(0, firstPointPosition); // // 첫번째 . 이전 문자열 저장

			console.log('# 2. str: ', str)
			const pointlessNumber = number.substring(firstPointPosition, number.length).replace(/[^0-9\-]/g, ''); // 첫번째 . 이후 문자열에 또 . 이 있다면 모두 삭제함
			console.log('# 3. pointlessNumber: ', str)
			number = Number(str + pointlessNumber); // 합체
			console.log('# 4. number: ', number)
			if (negertive) number='-' + number;*/

			if (isNaN(number)) return value; // 위의 치환과정을 거쳤는데도 number 포맷이 아니면 그냥 그대로 리턴

		} else {
			number = value;
		}

		return new Intl.NumberFormat('ko-KR').format(number);
	} catch {
		return value;
	}
}

// 3자리마다 ,
function _toCurrency( value ) {

	if ( value != null && value != "" ) {
		var nnp = '';
		value = "" + value;
		if ( value.charAt(0) == '-' ) {
			nnp = '-';
			value = value.substring(1, value.length);
		}
	
		value = toNumberStr(value);
		value = nnp + Number(value).toLocaleString().split(".")[0];
	}
	
	return value;
}

// 숫자와 부호만 리턴
function toNumberStr( value ) {
	
	// 문자열화
	var nnp = '';
	value = "" + value;
	if ( value.charAt(0) == '-' ) nnp = '-';
	
	var str	= "";
	for ( var i = 0; i < value.length; i++ ) {
		if ( value[i] >= '0' && value[i] <= '9' ) str += value[i];
	}
	
	return nnp + String(Number(str));
	
}

// 생년월일
function yymmdd( value, delimeter ) {
	if ( delimeter == null ) delimeter = '.';
	if ( value != null && value != "" ) {
		if ( value.length == 6 || value.length == 13 ) {
			value = value.substring(0,2) + delimeter + value.substring(2,4) + delimeter + value.substring(4,6);
		}
		if ( value.length == 8 || value.length == 13 ) {
			value = value.substring(2,4) + delimeter + value.substring(4,6) + delimeter + value.substring(6,8);
		}
	}
	return value;
}

// 날짜
function yyyymmdd( value, delimeter ) {
	if ( delimeter == undefined || delimeter == null ) delimeter = '.';
	if ( value != null && value != "" ) {
		if ( value.length == 8 || value.length == 13 ) {
			value = value.substring(0,4) + delimeter + value.substring(4,6) + delimeter + value.substring(6,8);
		}
	}
	return value;
}

function mmdd( value, delimeter ) {
	if ( delimeter == undefined || delimeter == null ) delimeter = ".";
	if ( value != null && value != "" ) {
		if ( value.length == 8 || value.length == 13 ) {
			value = value.substring(4,6) + delimeter + value.substring(6,8);
		}
	}
	return value;
}

// yyyymmdd를 javascript Date 형식으로 리턴
function toDate( yyyymmdd ) {
	let value	= toNumberStr(yyyymmdd);
	if ( value.length != 8 ) return value;
	
	let year	= value.substring(0,4);
	let month	= value.substring(4,6) - 1;
	let day		= value.substring(6,8);
	
	let date = new Date( year, month, day);
	return date;
}

// Date object 를  문자열로
function dateToStr( date, delimeter ) {
	if ( date == null || date == '' ) return "";
	if ( delimeter == null ) delimeter = '';
	dateStr = moment(date).format('YYYY' + delimeter + 'MM' + delimeter + 'DD');
	return dateStr;
}

// 두 날짜 차이
function dateDiff( start, end ) {
	return Math.abs( ( start.getTime() - end.getTime() ) / 1000 / 60 / 60 / 24 );
}

// 다음날 구하기
function nextDay( yyyymmdd ) {
	//console.log( "yyyymmdd : " + yyyymmdd );
	let date = toDate( yyyymmdd );
	//console.log( "date : " + date );

	date.setDate( date.getDate() + 1  );
	let dateStr = dateToStr( date, '' );
	return dateStr;
}

// 전날 구하기
function prevDay( yyyymmdd ) {
	//console.log( "yyyymmdd : " + yyyymmdd );
	let date = toDate( yyyymmdd );
	//console.log( "date : " + date );

	date.setDate( date.getDate() - 1  );
	let dateStr = dateToStr( date, '' );
	return dateStr;
}


function timestampToDateAndTime ( timestamp ) {
	let date = timestamp.substring(0, 10);
	let time = timestamp.substring(11, 19);
	return date + " " + time;
}

//전화번호 -
function toPhone( value ) {
	
	if ( value != null && value != "" ) {
		value = value.replace(/-/gi, "");
		
		// 맨 앞자리 0 제거한 문자열, 그냥 원래 0이라면 제거 안됨
		value = toNumberStr(value);
		
		if ( value == 0 ) {
			// 첫 0에는 반응 안함
		}
		// 서울
		else if ( value.charAt(0) == '2' ) {

			if ( value.length < 2 ) value = "0" + value;
			else if ( value.length < 6 ) value = "0" + value.substring(0,1) + "-" + value.substring(1,value.length);
			else value = "0" + value.substring(0,1) + "-" + value.substring(1,value.length-4) + "-" + value.substring(value.length-4,value.length);
		// 휴대폰
		/*
		} else if ( value.charAt(0) == '1' ) {
			value = "0" + value.substring(0,2) + "-" + value.substring(2,value.length-4) + "-" + value.substring(value.length-4,value.length);
		*/
		// 기타
		} else {
			if ( value.length < 3 ) value = "0" + value;
			else if ( value.length < 7 ) value = "0" + value.substring(0,2) + "-" + value.substring(2,value.length);
			else value = "0" + value.substring(0,2) + "-" + value.substring(2,value.length-4) + "-" + value.substring(value.length-4,value.length);
		}
	}
	return value;
}


// 주민등록번호
function toPin( value ) {
	if ( value != null && value != "" ) {
		value = value.replace(/-/gi, "");
		value = value.substring(0,6) + "-" + value.substring(6,value.length);
	}
	return value;
}

function toMaskedPin( value ) {
	value = toPin(value);
	return value.substring(0,8) + "******";
}

// Timestamp to date
function timestampToDate( timestamp ){
	
	// 값이 없으면 공백을 출력
	if( timestamp == null || timestamp == "" ){
		return "";
	}
	
	var d = new Date( timestamp );
	var result 	= d.getFullYear() 
				+ "-" + leadingZeros((d.getMonth() + 1),2) 
				+ "-" + leadingZeros(d.getDate(),2) 
				+ " " + leadingZeros(d.getHours(),2) 
				+ ":" + leadingZeros(d.getMinutes(),2) 
				+ ":" + leadingZeros(d.getSeconds(),2);

	return result;
}


// 원래 값과 자리수를 파라미터로 받아 원하는 자리수 만큼 앞에 0을 채워주는 함수
function leadingZeros(n, digits) {
	
	var zero = '';
	n = n.toString();

	if (n.length < digits) {
		for (var i = 0; i < digits - n.length; i++)	zero += '0';
	}
	
	return zero + n;
}


function toBirthday( pin, debtorType ) {
	if ( debtorType == '개인') {
		return yymmdd(pin);
	} else {
		return pin;
	}
}

// email 체크
function isEmail( email ) {
	var regExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
	var result	= false;
	if ( email != null && email.length != 0 ) {
		if ( regExp.test(email) ) {
			result = true;
		}
	}
	return result;
} 

function plainToHtml( str ) {
	if ( str != null ) {
		str = str.replace(/\n/g, "<br />");
	} else {
		str	= "";
	}
	return str;
}

// 상품명 다이어트
function dietResocName( resocName ) {
	
	if (  typeof(resocName) == 'string' ) {
		// [ xxx ] 제거
		if ( resocName.includes("]") ) {
			resocName	= resocName.substring( resocName.indexOf(']')+1, resocName.length );
		}
		
		// * 제거
		if ( resocName.includes("*") ) {
			resocName	= resocName.substring( resocName.indexOf('*')+1, resocName.length );
		}
		if ( resocName.includes("*") ) {
			resocName	= resocName.substring( 0, resocName.indexOf('*') );
		}
	}
	
	return resocName;
}

// 상품명에서 부가정보 뽑아내기
function getInfoFromName( resocName ) {

	let info = '';

	if (  typeof(resocName) == 'string' ) {
		// [ xxx ] 제거
		if ( resocName.includes("]") ) {
			info	= resocName.substring( resocName.indexOf(']')+1, resocName.length );
		}

		// * 제거
		if ( info.includes("*") ) {
			info	= info.substring( info.indexOf('*')+1, info.length );
		}
		if ( info.includes("*") ) {
			info	= info.substring( info.indexOf('*')+1, info.length );
		}
	}

	return info;
}

function getWeek( date ) {
	let dayOfWeek = '';
	try {
		let week = ['일', '월', '화', '수', '목', '금', '토'];
		let dateObject = new Date(yyyymmdd(date, '-'));
		dayOfWeek = week[ dateObject.getDay() ];
	} catch ( e ) {
		console.log( e );
	}
	return dayOfWeek;
}

// 주간 영문코드를 한글로
function getWeekNameKor( endCode ) {
	if ( endCode == null ) return '';
	else if ( endCode == 'mon' ) return '월';
	else if ( endCode == 'tue' ) return '화';
	else if ( endCode == 'wed' ) return '수';
	else if ( endCode == 'thu' ) return '목';
	else if ( endCode == 'fri' ) return '금';
	else if ( endCode == 'sat' ) return '토';
	else if ( endCode == 'sun' ) return '일';
	else return '';
}

function fieldFromArray( array, field ) {
	return array.map(function(item) {
		return item[field];
	});
}

/**
 * 쿼리 스트링 객체로 가져오기
 */
function getQueryString() {
	var params = {};
	window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
	return params;
}

/**
 * 객체를 쿼리스트링으로 만들어 링크 반환하기
 */
function _linkTo(url, obj) {
	let returnUrl = url;

	if (!url.startsWith("http")) {
		//console.log('# window.location: ', window.location);
		//console.log('# window.parent.location: ', window.parent.location);
		//console.log('# document.referrer: ', document.referrer);
		//console.log('# document.location: ', document.location);

		// 베네피아
		const BENEPIA_ROOT_PATH = '/mall/benepia';
		if (document.location.pathname.startsWith(BENEPIA_ROOT_PATH)) {
			returnUrl = BENEPIA_ROOT_PATH + url;
		}
	}

	// 객체 -> QueryString
	const params = [];
	if (!!obj) {
		for (let i in obj) {
			if (obj.hasOwnProperty(i)) {
				params.push(encodeURIComponent(i) + "=" + encodeURIComponent(obj[i]));
			}
		}
	}
	return returnUrl + (params.length ? '?' + params.join("&") : '');
}

/**
 * 유효성검사 정규식
 */
// 올바른 이메일 포맷만
function regExpEmail() {
	return new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
}

// 10 ~ 11 자리의 숫자만
function regExpPhoneNumber() {
	return new RegExp(/^(?:(010-?\d{4})|(01[1|6|7|8|9]-?\d{3,4}))-?(\d{4})$/);
}

/**
 * 빈 객체 확인
 */
function isEmptyObject(obj) {
	return Object.keys(obj).length === 0 && obj.constructor === Object;
}

function toHangulDay(value) {
	if (value === 0) return '일';
	else if (value === 1) return '월';
	else if (value === 2) return '화';
	else if (value === 3) return '수';
	else if (value === 4) return '목';
	else if (value === 5) return '금';
	else if (value === 6) return '토';
}

/**
 * setCookie(변수명, 변수값, 기간(분단위));
 * setCookie("token", "cghnupctnh89pt3", 10);
 */
/*function setCookie (key, value, minutes) {
	var date = new Date();
	date.setTime(date.getTime() + 1000 * 60 * minutes);
	document.cookie = key + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};*/

/*function getCookie (key) {
	var value = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
	return value ? value[2] : null;
};*/

/*function deleteCookie (key) {
	document.cookie = key + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
}*/

/**
 * javascript function 주기적으로 호출하기 (특정 url에서만 동작)
 */
function scheduledFunction(url, func, minutes) {
	if (include(window.location.pathname, url)) {
		const delay = !!minutes ? 6000 * minutes : 6000; // 분 단위 (6000)
		let f = setTimeout(function schedule() {
			func();
			f = setTimeout(schedule, delay);
		}, delay);
	}
}

/**
 * array에 값이 일치하는 것이 있는지 확인
 */
function include(array, str) {
	return array.indexOf(str) !== -1;
}

/**
 *  서울 표준 시(UTC + 9) 구하기
 */
function seoul_time() {
    // 1. 현재 시간(Locale)
    const curr = new Date();

    // 2. UTC 시간 계산
    const utc = curr.getTime() +  (curr.getTimezoneOffset() * 60 * 1000);

    // 3. UTC to KST (UTC + 9시간)
    const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
    const kr_curr = new Date(utc + (KR_TIME_DIFF));

    return kr_curr;
}

/**
 *  알림메시지 표시시간 계산용 함수
 */
function timeString(time) {
    hours = time.getHours();
    minutes = time.getMinutes();

    if (hours < 10) hours = '0' + hours;
    if (minutes < 10) minutes = '0' +minutes;

    return hours + '' + minutes;
}

/**
 * 텍스트필드 검증용 함수
 * 사용법 : validationField (검사할 택스트, 필수여부(true or false), 최소 글자수, 최대 글자수);
 * 기본적으로 네이버에서 안받아주는 특수문자는 다 에러처리 함.
 */

function validationField (text, required, minLength, maxLength) {
	// required 타입 검사
	if(text == null || text == undefined || text.length < 1) {
		// 비었을 때 필수이면 에러
		if (required == true) {
			const isValid = false;
			return {
				isValid: isValid,
				showErrorMessage: !isValid,
				message: '필수값입니다.',
				formValid: isValid ? 'is-valid' : 'is-invalid',
				required: required,
				currentLength: 0,
				minLength: minLength,
				maxLength: maxLength
			};
		}
		// 비었을 때 선택이면 검증해도 의미없으니 통과
		else {
			const isValid = true;
			return {
				isValid: isValid,
				showErrorMessage: !isValid,
				message: '',
				formValid: isValid ? 'is-valid' : 'is-invalid',
				required: required,
				currentLength: 0,
				minLength: minLength,
				maxLength: maxLength
			};
		}
	}

	// filterType 검사
	let allowPattern = "^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9一-鿕぀-ヿㇰ-ㇿ\\s\\(\\)\\_\\-\\:\\&\\!\\[\\]\\%\\.\\,\\+\\~\\@\\*\\^\\'\\/\\?\\²\\℃\\※\\<\\>]*$"; // 텍스트필드 검사용 정규식 패턴 (네이버 허용문자 기준)
	let allowExpression = new RegExp( allowPattern ); // 텍스트필드 검사용 정규식

	if ( allowExpression.test(text) == false ) {
		const isValid = false;
		return {
			isValid: isValid,
			showErrorMessage: !isValid,
			message: '한글, 영문, 숫자, 특수문자(-_():&![],.%+~@*^\'/?²℃※<>)만 입력가능합니다.',
			formValid: isValid ? 'is-valid' : 'is-invalid',
			required: required,
			currentLength: text.length,
			minLength: minLength,
			maxLength: maxLength
		};
	}

	// minLength 검사
	if (minLength > 0 && text.length < minLength) {
		const isValid = false;
		return {
			isValid: isValid,
			showErrorMessage: !isValid,
			message: '최소 '+minLength+'자 이상이여아 합니다. 현재 글자 수 : ' + text.length + '자',
			formValid: isValid ? 'is-valid' : 'is-invalid',
			required: required,
			currentLength: text.length,
			minLength: minLength,
			maxLength: maxLength
		};
	}

	// maxLength 검사
	if (text.length > maxLength) {
		const isValid = false;
		return {
			isValid: isValid,
			showErrorMessage: !isValid,
			message: '최대 '+maxLength+'자 이하여아 합니다. 현재 글자 수 : ' + text.length + '자',
			formValid: isValid ? 'is-valid' : 'is-invalid',
			required: required,
			currentLength: text.length,
			minLength: minLength,
			maxLength: maxLength
		};
	}

	// 검사 다 끝났는데 문제 없으면 정상 처리
	const isValid = true;
	return {
		isValid: isValid,
		showErrorMessage: !isValid,
		message: '',
		formValid: isValid ? 'is-valid' : 'is-invalid',
		required: required,
		currentLength: text.length,
		minLength: minLength,
		maxLength: maxLength
	};
}

/**
 * 숫자 필드 필터 함수
 * type="tel" 도 대응함.
 * 사용법 : <input style="ime-mode:disabled;" onKeyDown="filterNumber(this, event);" onChange="filterNum(this);">
 */

function filterNumber(elem, event){
	elem.value=elem.value.replace(/[^0-9]/g,"");
	var code = event.keyCode;
	if((code>47&&code<58)||event.ctrlKey||event.altKey||code==8||code==9||code==46){
		return;
	}
	event.preventDefault();
	return false;
}

function filterNum(elem){
	elem.value=elem.value.replace(/[^0-9]/g,"");
}



// GET 파라비터 값을 리턴하는 함수
// URL 이 test.com?user_name=test&id=tt 일때, getParameter("id") / getParameter("user_name")
function getParameter(name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		results = regex.exec(location.search);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

