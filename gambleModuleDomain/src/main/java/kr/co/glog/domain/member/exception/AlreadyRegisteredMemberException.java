package kr.co.glog.domain.member.exception;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class AlreadyRegisteredMemberException extends ApplicationRuntimeException {

	String exception	= "AlreadyRegisteredMemberException";
	String message		= "이미 등록되어있는 사용자입니다.";

	public AlreadyRegisteredMemberException() {}

	public AlreadyRegisteredMemberException(String message ) {
		this.message = this.message + " [" + message + "]";
	}
	
}
