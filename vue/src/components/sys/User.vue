<template>
  <div>
        <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input
          v-model="searchForm.Name"
          placeholder="用户名"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchRole" icon="el-icon-search"
          >查找</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="addUser"
          icon="el-icon-circle-plus-outline"
          >新增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定批量删除这些吗？" @confirm="deleteUsers">
          <el-button
            type="danger"
            slot="reference"
            icon="el-icon-delete-solid"
            :disabled="delButSta"
            >批量删除</el-button
          >
        </el-popconfirm>
      </el-form-item>
    </el-form>
      <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      border
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="30"> </el-table-column>
      <el-table-column  label="头像" width="100">
        <template slot-scope="scope">
         <el-avatar  :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="用户名" width="100"> </el-table-column>
      <el-table-column prop="userRole" label="角色" width="150">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.userRole" >{{item}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="150"></el-table-column>
      
      <el-table-column prop="created" label="创建时间" width="150"></el-table-column>
      <el-table-column prop="updated" label="上次更新时间" width="150"></el-table-column>
      <el-table-column prop="lastLogin" label="上次登录时间" width="150"></el-table-column>
      <el-table-column prop="statu" width="70" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.statu === 1" type="success">正常</el-tag>
          <el-tag v-if="scope.row.statu === 0" type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="360">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)"
            >编辑</el-button>
          <el-button size="mini" @click="disAuth(scope.row.id)"
            >分配角色</el-button
          >
          <el-popconfirm
            title="确定要删除?"
            @confirm="resetPassword(scope.row.id)"
          >
          <el-button size="mini" slot="reference"
            >重置密码</el-button
          >
          </el-popconfirm>
          <el-popconfirm
            title="确定要删除?"
            @confirm="handleDelete(scope.row.id)"
          >
            <el-button slot="reference" type="danger" size="mini"
              >删除</el-button
            >
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.CurrentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="page.Size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.Total"
    >
    </el-pagination>
     <el-dialog
      title="新增用户"
      :visible.sync="addDialogVisible"
      width="30%"
      :before-close="closeDialog"
    >
      <el-form
        :model="addForm"
        :rules="rules"
        ref="addForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="addForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="addForm.city"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="statu">
          <el-radio-group v-model="addForm.statu">
            <el-radio :label=0>禁用</el-radio>
            <el-radio :label=1>正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="small"
            @click="submitForm('addForm')"
            :loading="addFormloading"
            >立即创建</el-button
          >
          <el-button @click="resetForm('addForm')" size="small">重置</el-button>
          <el-button @click="addDialogVisible = false" size="small"
            >取 消</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
<el-dialog
      title="分配角色"
      :visible.sync="rolDialogVisible"
      width="30%"
      :before-close="closeRoleDialog"
    >
    <el-form >    
    <el-tree
      :data="data"
      show-checkbox
      node-key="id"
      ref="AuthTree"
      default-expand-all
      check-strictly
      :props="defaultProps">
    </el-tree>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="rolDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="putData">确 定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>

import { request } from "@/axios/request"
import qs from 'qs'
  export default {
    name:'User',
    data(){
      return {
        searchForm:{
          Name:''
        },
        addForm: {
          id:null,
          userName: "",
          password:"",
          email: "",
          city:"",
          statu: null,
      },
      addFormloading:false,
      page: {
        Size: 10,
        CurrentPage: 1,
        Total: null,
      },
      data: [],  
      currentUserId:null,
      rolDialogVisible:false,
      addDialogVisible:false,
      delButSta: true,
      tableData: [],
      rules: {
        userName: [
          { required: true, message: "请输入用户名称", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 5, max: 11, message: "长度在 5 到 11 个字符", trigger: "blur" },
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
        ],
        statu: [{ required: true, message: "请输入状态", trigger: "blur" }],
      },
       defaultProps: {
          label: 'name'
        },
      }
    },
    methods: {
    submitForm(addForm) {
      this.$refs[addForm].validate((valid) => {
        if (valid) {
          this.addFormloading = true;
          if (this.addForm.id) {
            this.puttableData();
          } else {
            request({
              url: "/user",
              method: "post",
              data: this.addForm,
            }).then((res) => {
              this.addFormloading = false;
              this.addDialogVisible = false;
              this.$message({
                message:res.data.msg,
                type:'success'
                })
              this.gettableData()
            })
            this.addFormloading = false
            this.resetForm('addForm')    
          }
        } else {
          alert("error submit!!");
          return false;
        }
      });
    },
    //新增点击事件
    addUser(){
      this.addFormloading=false
      this.addDialogVisible=true      
    },
    //获取所有已经选择的id
    handleSelectionChange(val){
        this.selectIds=[]
        if(val.length!==0){
            val.forEach(element => {
            this.selectIds.push(element.id)
            })
            this.delButSta=false
        }else{
            this.delButSta=true
        }
    },
    //分配角色的回调
    closeRoleDialog(done) {
      this.$refs.AuthTree.setCheckedKeys([])
      done()
    },
    //获取所有数据
    gettableData() {
      request({
        url: "/users",
        method: "get",
        params:{
            Name:this.searchForm.Name,
            CurrentPage:this.page.CurrentPage,
            Size:this.page.Size
        }
      }).then((res) => {
        this.tableData = res.data.object.data
        this.page.Total=res.data.object.TotalCount
      });
    },
    resetForm(addForm) {
      this.$refs[addForm].resetFields()
    },
    closeDialog(done) {
      this.resetForm('addForm')
      done()
    },
    handleSizeChange(val) {
      this.page.Size=val
    },
    handleCurrentChange(val) {
      this.page.CurrentPage=val
    },

    //查找符合条件的角色
    searchRole() {
      this.gettableData()
      this.$message({
                message:'查询成功',
                type:'success'
                })
    },
    //修改用户信息
    puttableData() {
      this.addFormloading = true
      request({
        url: "/user",
        method: "put",
        data: this.addForm,
      }).then(res => {       
        this.addDialogVisible = false;
        this.$message({
                message:res.data.msg,
                type:'success'
                })
        
      })
      this.addFormloading = false;
      this.resetForm('addForm');
    },
    //批量删除
    deleteUsers(){
        request({
        url:'/user',
        method:'delete',
        params:{
          ids:this.selectIds
        },
         paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
          }
      }).then(res=>{
        this.delButSta=false
        this.gettableData()
        this.$message({
          message:res.data.msg,
          type:'success'
          })  
      })
    },
    //获取角色树
    getData(){
      request({
        url:'/role',
        method:'get',
      }).then(res=>{
        this.data=res.data.object.data
      })
    },
    //根据id回写角色信息
    handleEdit(id) {
      request({
        url: "/user/" + id,
        method: "get",
      }).then((res) => {
        this.addForm = res.data.object;
        this.addDialogVisible = true;
      });
    },
    //分配按钮点击事件
    disAuth(id){
      this.currentUserId=id
      this.rolDialogVisible=true
      this.loading=true
      request({
        url:'/user/role/'+id,
        method:'get'
      }).then(res=>{
        if(res.data.object!==null){
            this.$refs.AuthTree.setCheckedKeys(res.data.object)
        }else{
          this.$refs.AuthTree.setCheckedKeys([])
        }
        
        
      })
       
    },
    //重置用户密码
    resetPassword(id){
      request({
        url:'/user/password/'+id,
        method:'put'
      }).then(res=>{
        this.$message({
                message:res.data.msg,
                type:'success'
                })
      })
    },
    //修改用户的角色
    putData(){
      this.loading=true
      request({
        url:'/user/'+this.currentUserId,
        method:'put',
        data:{
          ids:this.$refs.AuthTree.getCheckedKeys()
        }
      }).then(res => {
        this.$message({
                message:res.data.msg,
                type:'success'
                })
      })
      this.gettableData()
      this.loading=false
      this.rolDialogVisible=false
    },
    //删除数据
    handleDelete(id) {
      request({
        url: "/user/" + id,
        method: "delete",
      }).then(res => {
        this.$message({
                message:res.data.msg,
                type:'success'
                })
        this.gettableData();
      });
    },
  },
  created() {
    this.gettableData()
    this.getData()
  },   
}
</script>

<style scoped>
.el-pagination {
  float: right;
}
.el-form {
  padding-right: 40px;
}
.el-button{
  margin-right: 7px;
  margin-left: 10px;
}
</style>