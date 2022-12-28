package kr.co.glog.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class ParameterMissingException extends ApplicationRuntimeException {

	String exception = "ParameterMissingException";
	String message = "파라미터가 전달되지 않았습니다.";

	public ParameterMissingException() {
	}

	public ParameterMissingException( String message ) {
		super.setMessage( message );
	}

}
