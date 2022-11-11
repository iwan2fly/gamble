const CONTEXT_PATH = getContextPath();

function getContextPath() {
	var prefix = location.href.substring(0,location.href.indexOf(window.location.pathname));
	return prefix;
}

const AXIOS = {
	METHOD: {
		POST: "post",
		GET: "get",
		PUT: "put",
		DELETE: "delete",
		OPTIONS: "options"
	}
};

const RestResponseStatus = {
	OK: "OK",
	ILLEGAL_PARAMETER:"ILLEGAL_PARAMETER",
	LOGIN_REQUIRED:"LOGIN_REQUIRED",
	INTERNAL_SERVER_ERROR:"INTERNAL_SERVER_ERROR",
	INTERNAL_SERVER_WARN:"INTERNAL_SERVER_WARN",
	DATA_NOT_FOUND:"DATA_NOT_FOUND",
	DATABASE_ERROR:"DATABASE_ERROR",
	UNEXPECTED:"UNEXPECTED",
};

//API 참고 URL : https://github.com/axios/axios#axiospatchurl-data-config
axios.defaults.baseURL = CONTEXT_PATH+'/'; //'https://api.example.com';
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';


let __CONNECT_ERR = false;


//POST 일 경우, 무조건 body 에 파라미터 세팅
function _post( url, params, callback, args, object ) {
	sendCommandAxios(AXIOS.METHOD.POST,  url, params, callback, args, object );
}

//GET 일 경우, 무조건 query string 에 파라미터 세팅
function _get( url, params, callback, args ) {
	sendCommandAxios(AXIOS.METHOD.GET,  url, params, callback, args );
}

async function _aget( url, params ) {
	try {
		const response = await axios.get( url,  {params});
		return response.data;
	} catch ( error ) {
		console.log( '#error : ' + error );
	}

}

//Delete 일 경우, 무조건 body 에 파라미터 세팅
function _delete( url, params, callback, args ) {
	sendCommandAxios(AXIOS.METHOD.DELETE, url, params, callback, args );
}

function _options( url, params, callback, args ) {
	sendCommandAxios(AXIOS.METHOD.OPTIONS,  url, params, callback, args );
}

function _file( url, params, callback, args ) {
	sendCommandAxios('file',  url, params, callback, args );
}

function sendCommandAxios(method, url, params, callback, args, object ) {
	
    var timeout = 60 * 1000;
    
    var config = {
        method: method, // 'get', 'post', 'put', 'delete', 'options', 'patch'
        url: url,
        timeout: timeout,
        //headers: {'X-Requested-With': 'XMLHttpRequest'},
    };
    
    var appResult = {
    	status:'',
    	resultCode:'',
    	resultMessage:'',
    	resultMessageCode:'',
    	data:{},
    	gridParam:{},
    	errorMap:{}
    }

	config.headers = {};

    if (method == AXIOS.METHOD.GET) {
    	config.params = params;
    }
    else if (method == 'file') {
    	config.method = 'post';
		config.headers['Content-Type'] = 'multipart/form-data';
    	config.data = params;
    }
    else {
        // `data` is the data to be sent as the request body
        // Only applicable for request methods 'PUT', 'POST', and 'PATCH'
        config.data = params;
    }

	// 쿠키에 user-token 키 값이 있으면 헤더에 세팅합니다.

	let userToken = null;
	if( object != null && object.authToken != null && object.authToken != undefined) {
		userToken = object.authToken;
	} else {
		userToken = getCookie('user-token');
	}

	if (!!userToken) {
		config.headers['cpt-auth-token'] = userToken;
	}

    axios( config )
        .then( function( response ) {
        
            if (__CONNECT_ERR) {
                setTimeout(function() {
                    __CONNECT_ERR = false;
                    commonModalVue.modalData.commonModal = false;

                }, 1000);
            }

            if (callback) callback(response.data, args);
        })
        .catch( function( error ) {
        	if ( error.response ) {
    	      // The request was made and the server responded with a status code
    	      // that falls out of the range of 2xx
    	      //console.log(error.response.data);
    	      //console.log(error.response.status);
    	      //console.log(error.response.headers);
        		if ( error.response.status == '403' ) {
        			alert( '로그인이 필요한 서비스입니다.');
        		//	location.href="/";
        		}
    	    } else if ( error.request ) {
    	      // The request was made but no response was received
    	      // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
    	      // http.ClientRequest in node.js
    	      console.log(error.request);
    	      alert ( error.request );
    	    } else {
    	      // Something happened in setting up the request that triggered an Error
    	        console.log('Error', error.message);
    	    	alert( error.message );
    	    }
    	    // console.log(error.config);
            /*
            appResult.status = error.status;
            appResult.message = error.message;
            appResult.resultMessage = error.message;
                        
            const _NETWORK_ERROR = 'Network Error';
            if (error.message === _NETWORK_ERROR) {
                console.log("E1");
                vAlert('서버의 응답이 지연되어 재시도 중입니다. 잠시 기다려 주시기 바랍니다.');
                __CONNECT_ERR = true;

            } else if (error.response && error.response.status === 401) {
                console.log("E2");
                var code = error.response.data.logoutReason;
                var msg = '';
                if (code === '0') msg = '로그 아웃 되었습니다.\n다시 로그인 후 사용하시기 바랍니다.';
                else if (code === '1') msg = '동일 아이디 접속에 의해 로그아웃 되었습니다.';
                else if (code === '2') msg = '관리자에 의해 로그아웃 되었습니다.';
                else if (code === '3') msg = '세션 만료에 의해 로그아웃 되었습니다.';
                else msg = '알 수 없는 이유로 로그아웃 되었습니다.';
                vAlert(msg, '', function() {
                    goLogin();
                });
            } else {
                console.log("E3");
                // vAlert("시스템이 지연되고 있습니다.<br />잠시 후 다시 시도해 주시기 바랍니다.<br />code : "+jqXHR.status+"<br />"+"message : "+jqXHR.responseText+"<br />"+"error : "+errorThrown);

                if (error.message.indexOf("timeout") > -1) {
                    //vAlert("시스템이 지연되고 있습니다.<br />잠시 후 다시 시도해 주시기 바랍니다.<br />");
                    error.resultMessage = "시스템이 지연되고 있습니다.\n잠시 후 다시 시도해 주시기 바랍니다.";
                }
            }

            //if (callback) callback(error, args);
            if (callback) callback(appResult, args);
            
            */
        });
}

