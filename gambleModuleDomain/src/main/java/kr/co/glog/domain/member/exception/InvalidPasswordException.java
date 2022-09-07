package kr.co.glog.domain.member.exception;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class InvalidPasswordException extends ApplicationRuntimeException {

	String exception	= "InvalidPasswordException";
	String message		= "입력하신 정보로 인증에 실패했습니다.";

	public InvalidPasswordException() {}

	public InvalidPasswordException(String message) {
		super.setMessage( message );
	}


}
