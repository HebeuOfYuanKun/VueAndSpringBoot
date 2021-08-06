<template>
  <div>
    <el-form :inline="true" class="demo-form-inline" v-model="searchForm">
      <el-form-item>
        <el-input
          v-model="searchForm.Name"
          placeholder="名称"
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
          @click="addDialogVisible = true"
          icon="el-icon-circle-plus-outline"
          >新增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定批量删除这些吗？" @confirm="deleteRoles">
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
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="name" label="名称" width="150"> </el-table-column>
      <el-table-column prop="code" label="唯一编码" width="150">
      </el-table-column>
      <el-table-column prop="remark" label="描述" width="450">
      </el-table-column>
      <el-table-column prop="statu" width="160" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.statu === '1'" type="success">正常</el-tag>
          <el-tag v-if="scope.row.statu === '0'" type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)"
            >编辑</el-button
          >
          <el-button size="mini" @click="disAuth(scope.row.id)"
            >分配权限</el-button
          >
          <el-popconfirm
            title="这是一段内容确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <el-button slot="reference" type="danger" size="small"
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
      title="编辑"
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
        <el-form-item label="用户角色" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="唯一编码" prop="code">
          <el-input v-model="addForm.code"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="remark">
          <el-input v-model="addForm.remark"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="statu">
          <el-radio-group v-model="addForm.statu">
            <el-radio label="0">禁用</el-radio>
            <el-radio label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="small"
            @click="submitForm('addForm')"
            :loading="loading"
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
      title="分配权限"
      :visible.sync="disDialogVisible"
      width="30%"
      
    >
    <el-form :model="TreeData">    
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
    <el-button @click="disDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="putData">确 定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
import { request } from "@/axios/request"
import qs from 'qs'
export default {
  name: "Role",
  data() {
    return {
      data: [],
      loading:false,
      TreeData:{}, 
      currentRoleId:null,
      addDialogVisible: false,
      disDialogVisible:false,
      searchForm: {
        Name: "",
      },
      addForm: {
        id:null,
        name: "",
        code: "",
        remark:"",
        statu: "",
      },
      page: {
        Size: 10,
        CurrentPage: 1,
        Total: null
      },
      selectIds:[],
      delButSta: true,
      tableData: [],
      rules: {
        name: [
          { required: true, message: "请输入角色名称", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
        ],
        code: [
          { required: true, message: "请输入唯一状态码", trigger: "blur" },
        ],
        statu: [{ required: true, message: "请输入状态", trigger: "blur" }],
      },
       defaultProps: {
          children: 'children',
          label: 'name'
        },
    };
  },
  
  methods: {
    submitForm(addForm) {
      this.$refs[addForm].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.addForm.id) {
            this.puttableData(this.addForm.id);
          } else {
            request({
              url: "/role",
              method: "post",
              data: this.addForm,
            }).then(res => {       
              this.addDialogVisible = false
              this.$message({
                message:res.data.msg,
                type:'success'
                })
            })
            this.loading = false
            this.resetForm('addForm')
            this.gettableData()
          }
        } else {
          alert("error submit!!");
          return false;
        }
      });
    },
    //分配权限
    putData(){
      request({
        url:'/role/'+this.currentRoleId,
        method:'put',
        data:{
          ids:this.$refs.AuthTree.getCheckedKeys()
        },
      })
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
    //获取所有数据
    gettableData() {
      request({
        url: "/role",
        method: "get",
        params:{
            Name:this.searchForm.Name,
            CurrentPage:this.page.CurrentPage,
            Size:this.page.Size
        }
      }).then(res => {
        this.tableData = res.data.object.data
        this.page.Total=res.data.object.TotalCount;
      });
    },
    resetForm(addForm) {
      this.$refs[addForm].resetFields();
    },
    closeDialog(done) {
      this.resetForm('addForm')
      this.loading=false
      done()
    },
    handleSizeChange(val) {
      this.page.Size=val
      this.gettableData()
    },
    handleCurrentChange(val) {
      this.page.CurrentPage=val
      this.gettableData()
    },
    //批量删除数据
    deleteRoles(){
      request({
        url:'/role',
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
    //查找符合条件的角色
    searchRole() {
      this.gettableData()
    },
    //修改角色信息
    puttableData(id) {
      this.loading = true
      request({
        url: "/role",
        method: "put",
        data: this.addForm,
      }).then(res => {   
        this.addDialogVisible = false
        this.$message({
          message:res.data.msg,
          type:'success'
          })      
      })    
      this.loading = false 
      this.resetForm('addForm')
      this.gettableData()
    },
    //根据id回写角色信息
    handleEdit(id) {
      request({
        url: "/role/" + id,
        method: "get",
      }).then((res) => {
        this.addForm = res.data.object;
        this.addDialogVisible = true;
      });
    },
    //分配按钮点击事件
    disAuth(id){
      this.currentRoleId=id
      this.disDialogVisible=true     
      request({
        url:'/role/menu/'+id,
        method:'get'
      }).then(res=>{
        this.$refs.AuthTree.setCheckedKeys(res.data.object)
      })
    },
    //分配权限树
    disAuthTree(){
      request({
        url:'/menu',
        method:'get'
      }).then(res=>{
        this.data=res.data.object
      })
    },
    //删除数据
    handleDelete(id) {
      request({
        url: "/role/" + id,
        method: "delete",
      }).then((res) => {
        this.$message({
          message:res.data.msg,
          type:'success'
          });
        this.gettableData();
      });
    },
  },
  created() {
    this.gettableData()
    this.disAuthTree()
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
