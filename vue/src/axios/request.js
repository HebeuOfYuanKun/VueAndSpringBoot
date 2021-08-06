import axios from 'axios'
import router from '../router'
import Element from 'element-ui';
export function request(config) {
    const instance = axios.create({
        baseURL: 'http://127.0.0.1:8081',
        //baseURL: 'http:localhost:8080',
        timeout: 5000,
        headers: {
            'Content-Type': 'application/json; charset=utf-8',
            //'Token': localStorage.getItem('Token'),
            'Access-Control-Allow-Origin': '*' //cors错误是因为请求头没加Access-Control-Allow-XXX信息
        }
    })
    instance.interceptors.request.use(res => {
        // Do something before request is sent
        //window.localStorage.getItem("Token") 获取token的value
        let token = localStorage.getItem("Token")
        if (token) {
            //将token放到请求头发送给服务器,将tokenkey放在请求头中
            res.headers['Token'] = token;
            //也可以这种写法 config.headers['sToken'] = Token;     
        }
        return res;
    }, error => {
        // Do something with request error
        return Promise.reject(error);
    });
    instance.interceptors.response.use(response => {

            return response;
        },
        error => {
            console.log(error)
            if (error.response.data.StateCode === 401) {
                localStorage.removeItem("Token")
                Element.Message.error(!error.response.data.Msg ? '请登录' : error.response.data.Msg)
                router.push("/")
            }
            if (error.response.data.StateCode === 403) {
                Element.Message.error(!error.response.data.Msg ? '权限不足' : error.response.data.Msg)
            }
            return Promise.reject(error);
        });
    return instance(config)
}