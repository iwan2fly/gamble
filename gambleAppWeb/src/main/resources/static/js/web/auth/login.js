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
            console.log('# loginResponse: ', loginResponse);

            return false;
        }
    }
}
