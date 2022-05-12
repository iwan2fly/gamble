package kr.co.glog.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class ApplicationRuntimeException extends RuntimeException {

	String exception	= "ApplicationRuntimeException";
	String message		= "예외 상황";

	public ApplicationRuntimeException() {}

	public ApplicationRuntimeException(String message ) {
		this.message = this.message + " [" + message + "]";
	}
	
}
