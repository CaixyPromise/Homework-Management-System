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
      <el-table-column prop="title" label="作业标题"></el-table-column>
      <el-table-column prop="dwc" label="发布者"></el-table-column>

<el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>



      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">查看 <i class="el-icon-edit"></i></el-button>
          <el-button type="primary" @click="put(scope.row)">提交<i class="el-icon-circle-plus-outline"></i></el-button>
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

      <el-dialog title="作业详细" :visible.sync="dialogFormVisible" width="30%" >
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog title="提交作业" :visible.sync="dialogFormVisible1" width="30%" >
      <el-form label-width="80px" size="small"  ref="form1">
          <el-form-item label="发布者">
          <el-input v-model="form1.dwc" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="作业标题">
          <el-input v-model="form1.title" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="作业内容">
          <el-input v-model="form1.content" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="学生名字" required="true">
          <el-input v-model="form1.student" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="作答内容">
          <el-input type="textarea" v-model="form1.an1" ></el-input>
        </el-form-item>
        <el-upload
            class="upload-demo"
            ref="upload"
            action
            :http-request="httpRequest"
            :on-exceed="handleExceed"
            :file-list="fileList"
            :limit="3">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">每次更改需上传文件</div>
          </el-upload>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save1">确 定</el-button>
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
      dialogFormVisible1: false,
      form1:{}
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user-file/page", {
        params: { 
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })
     
    },
 
    reset() {
      this.name = ""
      this.load()
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
    save() {  
          this.dialogFormVisible = false
  
    },
      download(url) {
      window.open(url)
    },
    put(row){
        this.form1=JSON.parse(JSON.stringify(row))
        this.dialogFormVisible1 = true
    },
    save1() { 
       const params = new FormData()
        // 将上传文件数组依次添加到参数paramsData中
        this.fileList.forEach((x) => {
          params.append('file', x.file)
        });
        params.append('dwc',this.form1.dwc)
        params.append('title',this.form1.title)
        params.append('content',this.form1.content)
        params.append('student',this.form1.student)
        params.append('an1',this.form1.an1)
      this.request.post("/student-file/upload", params).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible1 = false
          this.load()
          this.$refs.upload.clearFiles()//清空上传列表
          this.fileList = []//集合清空
        } else {
          this.$message.error("保存失败")
          this.$refs.upload.clearFiles()//清空上传列表
        }
      })
       
          
          
    },
    httpRequest(option) {
        this.fileList.push(option)
      },
      handleExceed() {
        this.$message({ type: 'error', message: '最多支持3个附件上传' })
      },
      download(url) {
      window.open(url)
    },
  }
}
</script>

<style scoped>

</style>
