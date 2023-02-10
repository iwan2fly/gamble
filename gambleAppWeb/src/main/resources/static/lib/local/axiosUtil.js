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
        });
}

