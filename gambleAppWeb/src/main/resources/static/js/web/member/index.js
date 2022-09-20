let v;
document.addEventListener("DOMContentLoaded", function () {
    v = new Vue(obj);
});

const obj = {
    el: '#v',
    data: {
        jsGreeting: 'js greeting',
        greetings: 'duplicated',
    }
}
