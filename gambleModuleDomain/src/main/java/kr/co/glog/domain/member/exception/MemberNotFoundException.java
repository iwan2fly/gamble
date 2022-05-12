package kr.co.glog.domain.member.exception;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class MemberNotFoundException extends ApplicationRuntimeException {

	String exception	= "MemberNotFoundException";
	String message		= "예외 상황";

	public MemberNotFoundException() {}

	public MemberNotFoundException(String message ) {
		this.message = this.message + " [" + message + "]";
	}
	
}
