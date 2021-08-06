<template>
  <div>
    <strong>后台管理系统</strong>
    <div class="header-right">
      <el-avatar
        :src="UserInfo.portrait"
      ></el-avatar>
      <el-menu
        class="el-menu-demo"
        mode="horizontal"
      >
        <el-menu-item index="1">{{UserInfo.username}}</el-menu-item>
        <el-submenu index="2">
          <template slot="title">个人中心</template>
          <router-link to="/home/changeinfo">
            <el-menu-item index="2-1">修改资料</el-menu-item>
          </router-link>          
          <el-menu-item index="2-2" @click="logout">退出</el-menu-item>
        </el-submenu>
        <el-menu-item index="3">消息中心</el-menu-item>
        <el-menu-item index="4">
          <router-link to="/home/author">作者信息</router-link>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script>
import {request} from '@/axios/request'
export default {
  name: "Header",
  data(){
      return {
          UserInfo:{
            id:0,
            username:null,
            portrait:null
          }        
      }
  },
  methods:{
      getUserInfo(){
          request({
              url:'/user',
              method:'get'
          }).then(res=>{
              this.UserInfo.id=res.data.object.id;
              this.UserInfo.username=res.data.object.username;
              this.UserInfo.portrait=res.data.object.avatar
          })
      },
      logout(){
        request({
          url:'logout',
          method:'post',
        }).then(res=>{
          this.$message({
                message:res.data.Msg,
                type:'success'
                })
          localStorage.clear()
					sessionStorage.clear()

					this.$store.commit("resetState")

					this.$router.push("/login")
        })
			
  }},
  created(){
      this.getUserInfo()
  },
};
</script>

<style scoped>
.el-menu-demo {
  background-color: #b3c0d1;
}
.header-right {
  float: right;
  width: 450px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
</style>
