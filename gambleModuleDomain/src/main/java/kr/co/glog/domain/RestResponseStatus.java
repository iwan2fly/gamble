package kr.co.glog.domain;

import java.util.HashMap;
import java.util.Map;

public enum RestResponseStatus {
	OK(200),

	ILLEGAL_PARAMETER(301),

    BAD_REQUEST(400), // ERROR_400
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),

	INTERNAL_SERVER_ERROR(500),

	UNKNOWN(999);
	
    private int result;

    private static final Map<Integer, RestResponseStatus> intToTypeMap = new HashMap<Integer, RestResponseStatus>();

    static {
        for (RestResponseStatus type : RestResponseStatus.values()) {
            intToTypeMap.put(type.getValue(), type);
        }
    }

    public static RestResponseStatus fromInt(int i) {
    	RestResponseStatus type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null)
            return RestResponseStatus.UNKNOWN;
        return type;
    }

    private RestResponseStatus(int result) {
        this.result = result;
    }

    public int getValue() {
        return this.result;
    }
}
