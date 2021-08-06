<template>
  <el-menu
    :default-active="this.$store.state.editableTabsValue"
    class="el-menu-vertical-demo"
    background-color="#d3dce6"
  >
    <router-link to="/home/index">
      <el-menu-item index="index">
        <i class="el-icon-menu"></i>
        <span slot="title">首页</span>
      </el-menu-item>
    </router-link>

    <el-submenu :index="menu.path" v-for="menu in menuList">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span>{{menu.tabsName}}</span>
      </template>

        <router-link  v-for="child in menu.children" :to="child.path">
          <el-menu-item :index="child.path" @click="addTabs(child)">
            <template slot="title">
              <i :class="child.icon"></i>
              <span>{{child.tabsName}}</span>
            </template>
          </el-menu-item>
        </router-link>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "SideMenu",
  data(){
    return {
      // menuList:''
    }
  },
  computed:{
    menuList:{
      get(){
        return this.$store.state.menuList
      }
    }
  },
  methods:{
    addTabs(child){
      this.$store.commit('addTab',child)
    }
  }
};
</script>

<style scoped>
/* el-menu-item{
  background-color:"#d3dcee"
} */
</style>
