package kr.co.glog.domain.member.exception;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class UnregisteredEmailException extends ApplicationRuntimeException {

	String exception	= "UnregisteredEmailException";
	String message		= "등록되지 않은 이메일입니다.";

	public UnregisteredEmailException() {}

	public UnregisteredEmailException(String message) {
		super.setMessage( message );
	}


}
