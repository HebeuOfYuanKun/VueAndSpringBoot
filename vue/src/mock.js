// const Mock = require('mockjs')

// const Random = Mock.Random

// let Result = {
//     code: 200,
//     msg: '操作成功',
//     data: null
// }

// Mock.mock('/captcha', 'get', () => {

//     Result.data = {
//         token: Random.string(32),
//         captcha: Random.dataImage('80x40', 'p7n5w')
//     }
//     return Result
// })

// Mock.mock('/menuList', 'get', () => {

//     Result.data = {
//         menuList: [
//             // {
//             //         path: '/home/index',
//             //         index: 'index',
//             //         icon: 'el-icon-menu',
//             //         name: '首页',
//             //         MenuComponent: 'Index',
//             //     },
//             {
//                 index: 'SystemManager',
//                 icon: 'el-icon-s-operation',
//                 name: '系统管理',
//                 component: '',
//                 children: [{
//                         path: '/home/user',
//                         index: 'user',
//                         icon: 'el-icon-user',
//                         name: '用户管理',
//                         component: 'User',
//                     },
//                     {
//                         path: '/home/role',
//                         index: 'role',
//                         icon: 'el-icon-collection-tag',
//                         name: '角色管理',
//                         component: 'Role',
//                     },
//                     {
//                         path: '/home/menu',
//                         index: 'menu',
//                         icon: 'el-icon-notebook-2',
//                         name: '菜单管理',
//                         component: 'Menu',
//                     },
//                 ]
//             },
//             // {
//             //     path: '/home/utills',
//             //     index: 'utils',
//             //     icon: 'el-icon-document',
//             //     name: '系统工具',
//             //     component: ''
//             // }
//         ]
//     }
//     return Result
// })
// Mock.mock('/menu', 'get', () => {

//     Result.data = [{
//             "id": 1,
//             "created": "2021-01-15T18:58:18",
//             "updated": "2021-01-15T18:58:20",
//             "State": 1,
//             "parentId": 0,
//             "Name": "系统管理",
//             "MenuUrl": "",
//             "AuthCode": "sys:manage",
//             "MenuComponent": "",
//             "Type": 0,
//             "Icon": "el-icon-s-operation",
//             "Sort": 1,
//             "children": [{
//                     "id": 2,
//                     "created": "2021-01-15T19:03:45",
//                     "updated": "2021-01-15T19:03:48",
//                     "statu": 1,
//                     "parentId": 1,
//                     "Name": "用户管理",
//                     "path": "/sys/users",
//                     "AuthCode": "sys:user:list",
//                     "MenuComponent": "sys/User",
//                     "Type": 1,
//                     "Icon": "el-icon-s-custom",
//                     "Sort": 1,
//                     "children": [{
//                             "id": 9,
//                             "created": "2021-01-17T21:48:32",
//                             "updated": null,
//                             "statu": 1,
//                             "parentId": 2,
//                             "Name": "添加用户",
//                             "MenuUrl": null,
//                             "AuthCode": "sys:user:save",
//                             "MenuComponent": null,
//                             "Type": 2,
//                             "Icon": null,
//                             "Sort": 1,
//                             "children": []
//                         },
//                         {
//                             "id": 10,
//                             "created": "2021-01-17T21:49:03",
//                             "updated": "2021-01-17T21:53:04",
//                             "statu": 1,
//                             "parentId": 2,
//                             "Name": "修改用户",
//                             "MenuUrl": null,
//                             "AuthCode": "sys:user:update",
//                             "MenuComponent": null,
//                             "Type": 2,
//                             "Icon": null,
//                             "Sort": 2,
//                             "children": []
//                         },
//                         {
//                             "id": 11,
//                             "created": "2021-01-17T21:49:21",
//                             "updated": null,
//                             "statu": 1,
//                             "parentId": 2,
//                             "Name": "删除用户",
//                             "MenuUrl": null,
//                             "AuthCode": "sys:user:delete",
//                             "MenuComponent": null,
//                             "Type": 2,
//                             "Icon": null,
//                             "Sort": 3,
//                             "children": []
//                         },
//                         {
//                             "id": 12,
//                             "created": "2021-01-17T21:49:58",
//                             "updated": null,
//                             "statu": 1,
//                             "parentId": 2,
//                             "Name": "分配角色",
//                             "MenuUrl": null,
//                             "AuthCode": "sys:user:role",
//                             "MenuComponent": null,
//                             "Type": 2,
//                             "Icon": null,
//                             "Sort": 4,
//                             "children": []
//                         },
//                         {
//                             "id": 13,
//                             "created": "2021-01-17T21:50:36",
//                             "updated": null,
//                             "statu": 1,
//                             "parentId": 2,
//                             "Name": "重置密码",
//                             "MenuUrl": null,
//                             "AuthCode": "sys:user:repass",
//                             "MenuComponent": null,
//                             "Type": 2,
//                             "Icon": null,
//                             "Sort": 5,
//                             "children": []
//                         }
//                     ]
//                 },
//                 {
//                     "id": 3,
//                     "created": "2021-01-15T19:03:45",
//                     "updated": "2021-01-15T19:03:48",
//                     "statu": 1,
//                     "parentId": 1,
//                     "Name": "角色管理",
//                     "MenuUrl": "/sys/roles",
//                     "AuthCode": "sys:role:list",
//                     "MenuComponent": "sys/Role",
//                     "Type": 1,
//                     "Icon": "el-icon-rank",
//                     "Sort": 2,
//                     "children": []
//                 },

//             ]
//         },
//         {
//             "id": 5,
//             "created": "2021-01-15T19:06:11",
//             "updated": null,
//             "statu": 1,
//             "parentId": 0,
//             "Name": "系统工具",
//             "MenuUrl": "",
//             "AuthCode": "sys:tools",
//             "MenuComponent": null,
//             "Type": 0,
//             "Icon": "el-icon-s-tools",
//             "Sort": 2,
//             "children": [{
//                 "id": 6,
//                 "created": "2021-01-15T19:07:18",
//                 "updated": "2021-01-18T16:32:13",
//                 "statu": 1,
//                 "parentId": 5,
//                 "Name": "数字字典",
//                 "MenuUrl": "/sys/dicts",
//                 "AuthCode": "sys:dict:list",
//                 "MenuComponent": "sys/Dict",
//                 "Type": 1,
//                 "Icon": "el-icon-s-order",
//                 "Sort": 1,
//                 "children": []
//             }]
//         }
//     ]

//     return Result
// })
// Mock.mock(('/role'), 'get', () => {

//     Result.data = {
//         "tableData": [{
//                 "id": 3,
//                 "created": "2021-01-04T10:09:14",
//                 "updated": "2021-01-30T08:19:52",
//                 "State": 1,
//                 "Name": "普通用户",
//                 "Code": "normal",
//                 "Description": "只有基本查看功能",
//                 "menuIds": []
//             },
//             {
//                 "id": 6,
//                 "created": "2021-01-16T13:29:03",
//                 "updated": "2021-01-17T15:50:45",
//                 "State": 1,
//                 "Name": "超级管理员",
//                 "code": "admin",
//                 "Description": "系统默认最高权限，不可以编辑和任意修改",
//                 "menuIds": []
//             }
//         ],
//         "total": 2,
//         "size": 10,
//         "current": 1,
//         "orders": [],
//         "optimizeCountSql": true,
//         "hitCount": false,
//         "countId": null,
//         "maxLimit": null,
//         "searchCount": true,
//         "pages": 1
//     }

//     return Result

// })