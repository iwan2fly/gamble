package kr.co.glog.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @ToString
public class NetworkCommunicationFailureException extends ApplicationRuntimeException {

	String exception = "NetworkCommunicationFailureException";
	String message = "네트워크 통신 중 오류가 발생했습니다.";

	public NetworkCommunicationFailureException() {
	}

	public NetworkCommunicationFailureException(String message) {
		super.setMessage( message );
	}

}
