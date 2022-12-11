const setInterceptors = (axiosInstance) => {
    axiosInstance.interceptors.request.use(
        (config) => {
            const accessToken = getAuthTokenFromCookie()
            if (!!accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
            //console.log('# config: ', config);
            return config;
        },
        (error) => {
            console.log('# request error: ', error.request);
            return Promise.reject(error);
        },
    );

    axiosInstance.interceptors.response.use(
        (response) => {
            const accessToken = response.headers.access_token;
            if (!!accessToken) saveAuthTokenToCookie(accessToken);
            //console.log('# response: ', response);
            return response;
        },
        // ({response: errorResponse}) => {
        (error) => {
            console.log('# response error: ', error.response);
            //return Promise.reject(error);

            // status 401 리턴 시 로그인페이지로
            if (error.response.status === 401) {
                goLoginPage();
            }

            return Promise.resolve(error.response);
        },
    );
    return axiosInstance;
};

let once = true;
const goLoginPage = () => {
    if (once) {
        once = !once;
        alert('접속시간이 초과되어 로그인페이지로 이동합니다.');
        window.location.href = '/auth/login';
        return;
    }
}
