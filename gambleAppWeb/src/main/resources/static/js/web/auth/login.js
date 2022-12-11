let authVue;
document.addEventListener("DOMContentLoaded", function () {
    authVue = new Vue(auth);
});

const auth = {
    el: '#el-auth',
    data() {
        return {
            email: 'iwan2fly@gmail.com',
            password: '123456789',
        }
    },
    created() {
        log('# created auth vue');
    },
    methods: {
        async submitAuthForm() {
            const getAuthResponse = await getAuth({
                email: this.email,
                pwd: this.password
            });
            log('# getAuthResponse: ', getAuthResponse);

            if (getAuthResponse.data.success) {
                window.location.href = `/`;
            }
        }
    }
};
