/**
 * 회원 API
 */

// 회원 목록 조회
const getUserList = async (params) => {
    return await instance.post(`/rest/users`, {params});
}
