package kr.co.glog.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class UnexpectedDataException extends ApplicationRuntimeException {

	String exception = "UnexpectedDataException";
	String message = "예상치 못한 결과를 받았습니다.";

	public UnexpectedDataException() {
	}

	public UnexpectedDataException(String message) {
		super.setMessage( message );
	}

}
