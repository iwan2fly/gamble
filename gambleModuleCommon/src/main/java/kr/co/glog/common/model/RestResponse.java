package kr.co.glog.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class RestResponse {

	// HTTP ResponseCdoe
	private HttpStatus httpStatus = HttpStatus.OK;

	// Service Response Code
	private String responseCode = "";

	// Service Response Message
	private String responseMessage = "";

	// Service Debug Message
	private String debugMessage = "";

	// Service Response Data
	private Map<String, Object> object;


	public void putData(String name, Object obj) {
		if( MapUtils.isEmpty(this.object) ) {
			this.object = new HashMap<String, Object>();
		}
		this.object.put(name, obj);
	}

}
