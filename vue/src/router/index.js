import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login'
import Test from '../components/test'
import Home from '../components/Home'
import Index from '@/components/include/Index'
import Author from '@/components/header/Author'
import ChangeInfo from '@/components/header/ChangeInfo'
import { request } from '../axios/request'
import store from '../store'
import Element from 'element-ui';
Vue.use(VueRouter)

const routes = [{
        path: '/',
        alias: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/test',
        name: 'test',
        component: Test
    },
    {
        path: '/home',
        name: 'home',
        component: Home,
        children: [{
                path: '/home/index',
                name: 'index',
                component: Index
            },
            {
                path: '/home/changeinfo',
                name: 'changeinfo',
                meta: {
                    title: '更改资料',
                    path: '/home/changeinfo',
                },
                component: ChangeInfo
            },
            {
                path: '/home/author',
                name: 'author',
                meta: {
                    title: '作者信息',
                    path: '/home/author',
                },
                component: Author
            },
        ]
    }
]

const router = new VueRouter({
    routes
})
router.beforeEach((to, from, next) => {
    let Token = localStorage.getItem("Token")
    if (to.path === '/login' || to.path === '/') {
        next()
    } else if (!Token) {
        Element.Message.error("登录过期，请重新登录")
        next({ path: '/' })
    } else if (store.state.hasRoute) {
        next()
    } else {
        request({
            url: '/nav',
            method: 'get',
        }).then(res => {
            store.commit('setMenuList', res.data.object.Nav)
            store.commit('setAuthList', res.data.object.Authority)
                //动态路由绑定
            res.data.object.Nav.forEach(element => {
                if (element.children) {
                    element.children.forEach(child => {
                        let toRouter = menuToRoute(child)
                        router.addRoute('home', toRouter)
                    })
                }
            })
            store.commit('setRouteState', true)
        })
        next()
    }
})
const menuToRoute = (menu) => {
    if (!menu.component) {
        return null
    }
    return {
        path: menu.path,
        meta: {
            path: menu.path,
            title: menu.tabsName,
            icon: menu.icon
        },
        component: () =>
            import ('../components/sys/' + menu.component + '.vue')
    }
}

export default router