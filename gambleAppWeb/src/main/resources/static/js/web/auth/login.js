let v;
document.addEventListener("DOMContentLoaded", function () {
    v = new Vue(obj);
});

const obj = {
    el: '#v',
    data: {
        email: 'iwan2fly@gmail.com',
        password: '123456789',
    },
    methods: {
        async submit() {
            console.log("submit");

            let param = {
                email: this.email,
                pwd: this.password
            };

            /*_post( '/rest/auth/login', param, function(json, args) {
                console.log( json );
            }, null );*/

            const loginResponse = await axios.post(`/rest/auth/login`, {
                email: this.email,
                pwd: this.password,
            });

            const authorization = loginResponse.headers.authorization;
            let accessToken = authorization.substring('Bearer '.length, authorization.length);
            console.log('# accessToken: ', accessToken);
            console.log('# responseBody: ', loginResponse.data);

            /**
             * TODO 토큰을 일일이 세팅해야하므로
             * TODO Axios 인터셉터로 토큰을 저장하는 로직을 넣어야 함
             */
            localStorage.gambleAt = accessToken;

            if (loginResponse.data.httpStatus === RestResponseStatus.OK) {
                //window.location.href = 'https://google.com';
                console.log('# 메인페이지로 ㄱㄱ');
            }
        }
    }
}
