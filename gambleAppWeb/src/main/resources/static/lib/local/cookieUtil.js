const getCookie = (name) => {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
};

const setCookie = (name, value, options = {}) => {
    options = {
        path: '/',
        // 필요한 옵션은 여기에,,
        ...options
    };

    if (options.expires instanceof Date) {
        options.expires = options.expires.toUTCString();
    }

    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) {
            updatedCookie += "=" + optionValue;
        }
    }
    document.cookie = updatedCookie;
};

const deleteCookie = (name) => {
    setCookie(name, "", {
        'max-age': -1
    });
};

const saveAuthTokenToCookie = (token) => setCookie('gamble_auth_token', token);
const getAuthTokenFromCookie = _ => getCookie('gamble_auth_token');
const saveUserToCookie = (user) => setCookie('gamble_user', user);
const getUserFromCookie = _ => setCookie('gamble_user');
