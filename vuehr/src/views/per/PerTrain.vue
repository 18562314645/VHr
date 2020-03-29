
<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                          clearable
                          @clear="initEmpTrains"
                          style="width: 350px;margin-right: 10px" v-model="keyword"
                          @keydown.enter.native="" ></el-input>
                <el-button icon="el-icon-search" type="primary" @click="initEmpTrains">
                    搜索
                </el-button>
            </div>
            <div>
                <el-upload
                        :show-file-list="false"
                        :before-upload="beforeUpload"
                        :on-success="onSuccess"
                        :on-error="onError"
                        :disabled="importDataDisabled"
                        style="display: inline-flex;margin-right: 8px"
                        action="/personnel/train/import">
                    <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
                        {{importDataBtnText}}
                    </el-button>
                </el-upload>
                <el-button type="success" @click="exportData" icon="el-icon-download">
                    导出数据
                </el-button>
                <el-button type="primary" icon="el-icon-plus" @click="showAddEmpTrainView">
                    添加用户
                </el-button>
            </div>
        </div>

        <div style="margin-top: 10px">
            <el-table
                    :data="empTrains"
                    stripe
                    border
                    v-loading="loading"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="employee.name"
                        fixed
                        align="left"
                        label="姓名"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="employee.workID"
                        label="工号"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="employee.gender"
                        label="性别"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        width="95"
                        align="left"
                        label="部门">
                </el-table-column>
                <el-table-column
                        prop="traindate"
                        width="150"
                        align="left"
                        label="培训日期">
                </el-table-column>
                <el-table-column
                        prop="traincontent"
                        width="150"
                        align="left"
                        label="培训内容">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        width="150"
                        align="left"
                        label="备注">
                </el-table-column>

                <el-table-column
                        fixed="right"
                        width="150"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpTrainView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button @click="deleteEmpTrain(scope.row)" style="padding: 3px" size="mini" type="danger">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @current-change="currentChange"
                        @size-change="sizeChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>


        <el-dialog :title="title"
                   :visible.sync="dialogFormVisible"
                    width="50%" >
            <div align="center">
            <el-form :model="empt" ref="emptForm">

                <el-form-item label="工号:" prop="employee.workID">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                              @blur="searchEmp(empt.employee.workID)"    v-model="empt.employee.workID" placeholder="工号" ></el-input>
                </el-form-item>

                <el-form-item label="姓名:" prop="employee.name">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="empt.employee.name"
                              placeholder="请输入员工姓名"></el-input>
                </el-form-item>

                <el-form-item label="员工ID:" prop="empt.eid">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                               v-model="empt.eid" placeholder="员工id" ></el-input>
                </el-form-item>
                <el-form-item label="性别:" prop="gender">
                    <el-radio-group v-model="empt.employee.gender" >
                        <el-radio label="男">男</el-radio>
                        <el-radio label="女">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="所属部门:" prop="dname">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                              v-model="empt.department.name" placeholder="所属部门" ></el-input>
                </el-form-item>
                <el-form-item label="培训日期:" prop="traindate">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                              v-model="empt.traindate" placeholder="培训日期"></el-input>
                </el-form-item>
                <el-form-item label="培训内容:" prop="traincontent">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                              v-model="empt.traincontent" placeholder="培训内容"></el-input>
                </el-form-item>
                <el-form-item label="备注:" prop="remark">
                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                              v-model="empt.remark" placeholder="备注"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="doAddEmployTrain">确 定</el-button>
            </div>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: "PerTrain",
        data() {
            return {
                empTrains: [],
                title: '',
                loading: false,
                dialogFormVisible: false,
                page:1,
                size:10,
                total:0,
                keyword:'',
                importDataBtnText:'导入数据',
                importDataBtnIcon: 'el-icon-upload2',
                importDataDisabled: false,
                empt: {
                    eid: '',
                    traindate: '',
                    traincontent: '',
                    remark: '',
                    employee:{

                    },
                    department:{}
                },

            };
        },
        mounted() {
            this.initEmpTrains();

        },
        methods:{
            showAddEmpTrainView(){
                this.emptyEmpt();
                this.title='添加员工培训';
                this.dialogFormVisible=true;
            },
            showEditEmpTrainView(data){
                this.title='编辑员工培训';
                this.empt=data;
                this.dialogFormVisible=true;
            },
            initEmpTrains(){
                this.loading = true;
                let url = '/personnel/train/?page=' + this.page + '&size=' + this.size + "&employee.name=" + this.keyword;

                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.empTrains = resp.data;

                        this.total = resp.total;
                    }
                });
            },

            onError(err, file, fileList) {
                this.importDataBtnText = '导入数据';
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataDisabled = false;
            },
            onSuccess(response, file, fileList) {
                this.importDataBtnText = '导入数据';
                this.importDataBtnIcon = 'el-icon-upload2';
                this.importDataDisabled = false;
                this.initEmps();
            },
            beforeUpload() {
                this.importDataBtnText = '正在导入';
                this.importDataBtnIcon = 'el-icon-loading';
                this.importDataDisabled = true;
            },
            exportData() {
                window.open('/personnel/train/export', '_parent');
            },

            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEmpTrains();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmpTrains();
            },

            emptyEmpt(){
              this.empt={
                  eid: '',
                  traindate: '',
                  traincontent: '',
                  remark: '',
                  employee:{
                        name:'',
                        gender:'',
                        workID:'',
                  },
                  department:{

                  }
              }
            },

            doAddEmployTrain(){
                if(this.empt.id){

                    this.$refs['emptForm'].validate(valid=>{
                        if(valid){
                            this.putRequest("/personnel/train/",this.empt).then(resp=>{
                                if(resp){
                                    this.dialogFormVisible=false;
                                    this.initEmpTrains();
                                }
                            })
                        }
                    });
                }else{
                    this.$refs['emptForm'].validate(valid=>{
                        if(valid){
                            this.postRequest("/personnel/train/",this.empt).then(resp=>{
                                this.dialogFormVisible=false;
                                this.initEmpTrains();
                            })
                        }
                    });
                }
            },

            deleteEmpTrain(data){
                this.$confirm('此操作将永久删除【' + data.employee.name + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/personnel/train/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmpTrains();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

            searchEmp(id){
                let url='/employee/basic/searchName?id='+id;
                this.getRequest(url).then(resp=>{
                    this.empt.employee.name=resp.name;
                    this.empt.eid=resp.id;
                    this.empt.employee.gender=resp.gender;
                    this.empt.department.name=resp.department.name;

                });
            }

        }
    };
</script>

<style scoped>

</style>

