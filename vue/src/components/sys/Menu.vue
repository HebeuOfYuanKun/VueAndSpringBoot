<template>
  <div>
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true" icon="el-icon-circle-plus-outline">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="tableData"
      style="width: 100%;margin-bottom: 20px;"
      row-key="id"
      border
      stripe
      default-expand-all
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="名称" sortable width="180">
      </el-table-column>
      <el-table-column prop="perms" label="权限编码" sortable width="180">
      </el-table-column>
      <el-table-column prop="icon" label="图标" sortable width="180">
      </el-table-column>
      <el-table-column prop="type" label="类型" sortable width="180">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" type="success">目录</el-tag>
          <el-tag v-if="scope.row.type === 1">菜单</el-tag>
          <el-tag v-if="scope.row.type === 2" type="info">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="菜单URL"> </el-table-column>
      <el-table-column prop="component" label="菜单组件"> </el-table-column>
      <el-table-column prop="orderNum" label="排序号"> </el-table-column>
      <el-table-column prop="statu" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.statu === 1" type="success">正常</el-tag>
          <el-tag v-if="scope.row.statu === 0" type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)"
            >编辑</el-button>
          <el-popconfirm title="这是一段内容确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <el-button slot="reference" type="danger" size="mini" >删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="新增菜单"
      :visible.sync="dialogVisible"
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
        <el-form-item label="上级菜单" prop="parentId">
          <el-select v-model="addForm.parentId" placeholder="请选择上级菜单">
            <template v-for="item in tableData">
              <el-option :label="item.name" :value="item.id"> </el-option>
              <template v-for="child in item.children">
                <el-option :label="child.name" :value="child.id">
                  {{ "-" + child.name }}
                </el-option>
              </template>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="权限编码" prop="perms">
          <el-input v-model="addForm.perms"></el-input>
        </el-form-item>
        <el-form-item label="菜单URL" prop="path">
          <el-input v-model="addForm.path"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="addForm.icon"></el-input>
        </el-form-item>
        <el-form-item label="菜单组件" prop="component">
          <el-input v-model="addForm.component"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="addForm.type">
            <el-radio :label="0">菜单</el-radio>
            <el-radio :label="1">目录</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="statu">
          <el-radio-group v-model="addForm.statu">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number
            v-model="addForm.orderNum"
            :min="1"
            :max="100"
            label="序号"
          ></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="small"
            @click="submitForm('addForm')"
            :loading="addForm.loading"
            >立即创建</el-button>
          <el-button @click="resetForm('addForm')" size="small">重置</el-button>
          <el-button @click="dialogVisible = false" size="small"
            >取 消</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { request } from "@/axios/request";
export default {
  name: "Menu",
  data() {
    return {
      addForm: {
        id: null,
        parentId: "",
        name: "",
        perms: "",
        path: "",
        component: "",
        type: null,
        statu: null,
        icon:null,
        orderNum: null,
        loading: false,
      },
      rules: {
        name: [
          { required: true, message: "请输入菜单名称", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
        ],
        parentId: [
          { required: true, message: "请选择上级菜单", trigger: "blur" },
        ],
        perms: [
          { required: true, message: "请输入权限编码", trigger: "blur" },
        ],
        type: [{ required: true, message: "请输入类型", trigger: "blur" }],
        statu: [{ required: true, message: "请输入状态", trigger: "blur" }],
        orderNum: [{ required: true, message: "请输入序号", trigger: "blur" }],
      },
      dialogVisible: false,
      tableData: [],
    };
  },
  methods: {
    //提交对话框表单
    submitForm(addForm) {
      this.$refs[addForm].validate((valid) => {
        if (valid) {
          this.addForm.loading = true;
          
          if (this.addForm.id) {
            this.putaddForm(this.addForm.id);
          } else {
            request({
              url: "/menu",
              method: "post",
              data: this.addForm,
            }).then((res) => {
              this.addForm.loading = false;
              this.dialogVisible = false;
              this.$message({
                message:res.data.msg,
                type:'success'
                });
              this.gettableData();
            });
            
          }
        } else {
          alert("error submit!!");
          return false;
        }
      });
    },
    resetForm(addForm) {
      this.$refs[addForm].resetFields();
    },
    closeDialog(done) {
      this.resetForm('addForm')
      done()
    },
    //获取所有菜单树数据
    gettableData() {
      request({
        url: "/menu",
        method: "get",
      }).then((res) => {
        this.tableData=res.data.object
      });
    },
    //修改数据
    putaddForm(id) {
      this.addForm.loading = true;
      request({
        url: "/menu/",
        method: "put",
        data: this.addForm,
      }).then((res) => {        
        this.dialogVisible = false;
        this.$message({
          message:res.data.msg,
          type:'success'
          });       
        this.gettableData()
      })
      this.resetForm('addForm')
      this.addForm.loading = false
    },
    //根据id获取数据回写对话框
    handleEdit(id) {
      request({
        url: "/menu/" + id,
        method: "get",
      }).then((res) => {
        this.addForm = res.data.object;
        
      });
      this.dialogVisible = true;
    },
    //删除数据
    handleDelete(id) {      
      request({
        url: "/menu/" + id,
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
    this.gettableData();
  },
};
</script>

<style scoped>
.el-form {
  padding-right: 40px;
}
.el-button{
  margin-right: 7px;
  margin-left: 10px;
}
</style>
