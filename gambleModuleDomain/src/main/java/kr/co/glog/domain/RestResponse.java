package kr.co.glog.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class RestResponse {
	private RestResponseStatus status = RestResponseStatus.OK;			// http code
	private String responseCode = "";									// local response code
	private String responseMessage = "";								// local response message
	private Map<String, Object> response;								// local response data

	public void putData(String name, Object obj) {
		if( MapUtils.isEmpty(this.response) ) {
			this.response = new HashMap<String, Object>();
		}
		this.response.put(name, obj);
	}

}
