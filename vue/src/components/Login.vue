<template>

    <el-container >  
        <el-main >
            
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <h1>管理员登录</h1>
                <p></p>
                <p></p>
                <p></p>
                <el-form-item label="账号：" prop="username">
                    <el-input v-model="ruleForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码：" prop="password">
                    <el-input v-model="ruleForm.password"></el-input>
                </el-form-item>
                <el-form-item label="验证码：" prop="code">
                    <el-input v-model="ruleForm.code" style="width:320px;float:left" ></el-input>
                    <el-image class="captchaImg" :src="captchaImg" @click="getcaptchaImg"></el-image>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
        </el-form>
        </el-main>
        <el-footer><h4>@DesigedBy HeBeiUniversityOfEngineer Yuankun</h4></el-footer>
    </el-container>
  
</template>

<script>
    import qs from 'qs'
    import axios from 'axios'
    import {request} from '../axios/request.js'
    export default {
        name:"Login",
        data(){
            return{
                ruleForm: {
                    username: '',
                    password: '',
                    code:'',
                    key:''
                },
                rules: {
                username: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    { min: 4, max: 11, message: '长度在 4 到 11 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                code: [
                    { required: true, message: '请输入验证码', trigger: 'blur' },
                    { min: 5, max: 5, message: '长度在 5 个字符', trigger: 'blur' }
                ],
                },
                captchaImg:null
            };
            },
            methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                if (valid) {
                    // axios.post('http://localhost:8081/login?'+qs.stringify(this.ruleForm)).then(res=>{
                    //     //const jwt=res.headers['Token']

                    //     this.$store.commit('setToken',res.data.object)
                    //     this.$router.push('/home/index')
                    //     this.$message({
                    //         message:'登录成功',
                    //         type: 'success'
                    //     });
                    // })
                    request({
                        url:'/login?'+qs.stringify(this.ruleForm),
                        method:'post'
                    }).then(res=>{
                        this.$store.commit('setToken',res.data.object)
                        this.$router.push('/home/index')
                        this.$message({
                            message:'登录成功',
                            type: 'success'
                        })
                    }).catch(error=>{
                          this.$message({
                            message:error.response.data.Msg,
                            type: 'error'
                        }) 
                        this.getcaptchaImg() 
                        })
                } else {
                    cthis.$message({
                            message:'非法输入',
                            type: 'error'
                        });
                    return false;
                }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            getcaptchaImg(){
                request({
                    url:'/captcha',
                    methods:'get'
                }).then(res=>{
                    this.captchaImg=res.data.object.Base64Image
                    this.ruleForm.key=res.data.object.key
                })
            }
        },
        created(){
            this.getcaptchaImg()
        }
    }
</script>

<style scoped>
.el-container{
    background-color: #fff;
    color: #333;
    text-align: center;
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
  }

  .el-main{
      padding: 0px;
      margin: 0px;
      height: 100%;
      display: flex;
      align-items: center;
  }
  .captchaImg{
    float:left;
    margin-left:20px
  }
</style>