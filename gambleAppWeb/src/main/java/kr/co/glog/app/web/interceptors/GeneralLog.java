package kr.co.glog.app.web.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class GeneralLog implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
    	log.debug( "[General Log : preHandle] -----------------------------------------------------------------------------------------------" );
    	log.debug( "[General Log : preHandle] URL : " + request.getRequestURI() );
    	log.debug( "[General Log : preHandle] PARAM : " + mapper.writeValueAsString( request.getParameterMap() ) );
        log.debug( "[General Log : preHandle] -----------------------------------------------------------------------------------------------" );
    	//log.debug( "BODY : " + getBody(request.getInputStream()) );
		return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	// log.debug( "[General Log : postHandle]" );
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {
    	// log.debug( "[General Log : afterCompletion] --------------------------" );
    }
    
    /*
     * 	여기서 InputStream을 사용하면, 이 EOF가 나서 실제 서비스에서 객체 매핑할 때 오류가 납니다. 쓸 수 없습니다.
    private static String getBody( InputStream inputStream) throws IOException {
    	 
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
 
        try {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
 
        body = stringBuilder.toString();
        return body;
    }
	*/
}