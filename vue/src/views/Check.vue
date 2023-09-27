<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
       <el-table-column prop="student" label="学生姓名"></el-table-column>
      <el-table-column prop="title" label="作业标题"></el-table-column>
      <el-table-column prop="dwc" label="发布者"></el-table-column>
       <el-table-column prop="flag" label="作业状态">
         <template slot-scope="scope">
           <div>{{scope.row.flag?'已阅':'审核中'}}</div>
        </template>
       </el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">查看 <i class="el-icon-edit"></i></el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

      <el-dialog title="用户文件" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small"  ref="form">
        <el-form-item label="作业标题">
          <el-input v-model="form.title" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="发布者">
          <el-input v-model="form.dwc" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="作业内容">
          <el-input type="textarea" v-model="form.content" disabled></el-input>
        </el-form-item>
        <el-form-item label="学生姓名">
          <el-input v-model="form.student" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="作答内容">
          <el-input v-model="form.an1" autocomplete="off" disabled></el-input>
        </el-form-item>
      </el-form>
        
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">确 定</el-button>
        <el-button type="danger" @click="cancel">取消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Publish",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      fileList:[],
      form:{},
      dialogFormVisible: false,
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/student-file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          
        }
      }).then(res => {
        this.tableData = res.data.records
        console.log(this.tableData)
        this.total = res.data.total
      })
     
    },
    reset() {
      this.name = ""
      this.load()
    },
     handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  
      handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    cancel() {  
          this.dialogFormVisible = false

    },
    save(){
       this.request.post("/student-file",this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          
        } else {
          this.$message.error("保存失败")
        }
       })
    },
      download(url) {
      window.open(url)
    },

  }
}
</script>

<style scoped>

</style>
