<template>
<div>
  <el-form
    :model="ruleForm"
    :rules="rules"
    ref="ruleForm"
    label-width="100px"
    class="demo-ruleForm"
  >
  <el-form-item>
      <h1>修改资料</h1>
    </el-form-item>
    <el-form-item label="旧密码" prop="currentpassword">
      <el-input v-model="ruleForm.currentpassword"></el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="password">
      <el-input v-model="ruleForm.password"></el-input>
    </el-form-item>
    <el-form-item label="确认密码" prop="checkpassword">
      <el-input v-model="ruleForm.checkpassword"></el-input>
    </el-form-item>
    <el-form-item>
    <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
    <el-button @click="resetForm('ruleForm')">重置</el-button>
  </el-form-item>
  </el-form>
  </div>
</template>

<script>
import { request } from '../../axios/request';
export default {
  name: "ChangeInfo",
  data() {
    var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
    return {
      ruleForm: {
        currentpassword:'',
        password:'',
        checkpassword:'',
      },
      rules: {
        currentpassword: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入新密码", trigger: "blur" },
        ],
        checkpassword:[
          {required: true,validator:validatePass, trigger: "blur" },
        ]
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          
          request({
            url:'/user/newpassword',
            method:'put',
            params:{
              currentpassword:this.ruleForm.currentpassword,
              password:this.ruleForm.password
            }
          }).then(res=>{
          this.$message({
                message:res.data.msg,
                type:'success'
                })          })
        } else {
          this.openMessage('请正确输入')  
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    openMessage(Msg){
      this.$message({
            showClose: true,
            message: Msg
          })
    }
  },
};
</script>

<style scoped>
div{
  /* display: flex;
  align-items: center; */
  text-align: center;
  /* justify-content: center; */
}
.el-form {
	width: 450px;
	margin: 50px auto;
}
</style>
