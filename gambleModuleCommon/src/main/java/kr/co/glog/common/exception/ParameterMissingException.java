package kr.co.glog.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class ParameterMissingException extends ApplicationRuntimeException {

	public ParameterMissingException() {
		this.setException( "ParameterMissingException" );
		this.setMessage( "파라미터가 전달되지 않았습니다." );
	}

	public ParameterMissingException( String message ) {
		this();
		this.setMessage( message );
	}

}
