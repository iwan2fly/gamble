const instance = (() => setInterceptors(axios.create({
    //baseURL: `http://localhost:8888`,
    timeout: 10 * 1000, // 10 초
})))();
