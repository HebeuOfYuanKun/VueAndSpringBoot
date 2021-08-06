import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router';
import store from './store/index'
//import { Input, Form, Row, Col, FormItem, Main } from 'element-ui';
Vue.use(ElementUI)
    // require('./mock')
Vue.config.productionTip = false
    // Vue.component(Input.name, Input)
    // Vue.component(Form.name, Form)
    // Vue.component(Row.name, Row)
    // Vue.component(Col.name, Col)
    // Vue.component(FormItem.name, FormItem)
    // Vue.component(Main.name, Main)
new Vue({
    router,
    store: store,
    render: h => h(App)
}).$mount('#app')