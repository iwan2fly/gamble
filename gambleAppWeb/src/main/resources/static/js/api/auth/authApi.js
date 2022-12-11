/**
 * 인증 API
 */

// 로그인 (인증토큰)
const getAuth = async (params) => {
    return await instance.post(`/rest/auth/login`, params);
}

// admin 테스트 호출
const getOnlyAdmin = async (params) => {
    return await instance.get(`/rest/auth/admin`, {params});
}

// manager 테스트 호출
const getOnlyManager = async () => {
    return await instance.get(`/rest/auth/manager`);
}

// user 테스트 호출
const getOnlyUser = async () => {
    return await instance.get(`/rest/auth/user`);
}
