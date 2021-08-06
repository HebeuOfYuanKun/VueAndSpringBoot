<template>
  <div>
      <h1 >您的数据：{{$store.state.num}}</h1>
      <p>点击进行加<button @click="Increase">加</button></p>
  </div>
</template>

<script>
    export default {
        name:'test',
        data(){
            return {
            }
        },
        methods:{
            Increase(){
                this.$store.dispatch('increa',1)
            }
        }
    }
</script>

<style>

</style>