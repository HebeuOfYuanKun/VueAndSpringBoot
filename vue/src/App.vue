<template>
  <div id="app">
    <router-view></router-view>
  </div>  
</template>
<script>
  export default {
    name:'App',
    watch:{
      $route(to,from){
        if(to.path!='/'&&to.path!='/login'&&to.path!='/home/index'){
          let obj={
            path:to.meta.path,
            tabsName:to.meta.title
          }
          this.$store.commit('addTab',obj)
        }
      }   
    }
  }
</script>
<style>
	html, body, #app {
		font-family: 'Helvetica Neue','Hiragino Sans GB', 'WenQuanYi Micro Hei', 'Microsoft Yahei', 'sans-serif';
		height: 100%;
		padding: 0;
		margin: 0;
		font-size: 15px;
	}
  a{
    text-decoration:none
  }
</style>
