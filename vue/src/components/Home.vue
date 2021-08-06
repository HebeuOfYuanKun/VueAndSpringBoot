<template>
  <el-container>
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <Header></Header>
      </el-header>
      <el-main>
        <Tabs></Tabs>
        <div style="padding:0 20px">
          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideMenu from './include/SideMenu'
import Header from './include/Header'
import Tabs from '@/components/include/Tabs'
  export default { 
    name: "Home",
    components:{
        SideMenu:SideMenu,
        Header:Header,
        Tabs:Tabs
      }
  };
</script>

<style scoped>
.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  /* text-align: center; */
  /* line-height: 160px; */
  padding: 0px;
}

body > .el-container {
  margin-bottom: 40px;
}
.el-container {
  height: 100%;
}
.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

</style>