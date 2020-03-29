<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                          clearable
                          @clear="initEmpSalary"
                          style="width: 350px;margin-right: 10px" v-model="keyword"
                          @keydown.enter.native="initEmpSalary" ></el-input>
                <el-button icon="el-icon-search" type="primary" @click="initEmpSalary">
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
                <!--<el-button type="primary" icon="el-icon-plus" @click="showAddEmpTrainView">
                    添加用户
                </el-button>-->

                <el-popover
                        placement="right"
                        width="800"
                        trigger="click">
                    <el-table :data="gridData">
                        <el-table-column width="150" property="employee.name" label="员工姓名"></el-table-column>
                        <el-table-column width="100" property="asdate" label="调薪日期"></el-table-column>
                        <el-table-column width="150" property="beforesalary" label="调前薪资"></el-table-column>
                        <el-table-column width="150" property="aftersalary" label="调后薪资"></el-table-column>
                        <el-table-column width="150" property="reason" label="调薪原因"></el-table-column>
                        <el-table-column width="150" property="remark" label="备注"></el-table-column>
                    </el-table>
                   <!-- <el-button slot="reference">click 激活</el-button>-->
                    <el-button slot="reference" type="primary"  >
                    调薪详情
                </el-button>
                    <!--<div style="display: flex;justify-content: flex-end">
                        <el-pagination
                                background
                                @current-change="currentChange"
                                @size-change="sizeChange"
                                layout="sizes, prev, pager, next, jumper, ->, total, slot"
                                :total="total">
                        </el-pagination>
                    </div>-->
                </el-popover>


            </div>
        </div>

        <div style="margin-top: 10px">
            <el-table
                    :data="empSalarys"
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
                        prop="name"
                        fixed
                        align="left"
                        label="姓名"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="工号"
                        align="left"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        label="性别"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="员工id"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        width="100"
                        align="left"
                        label="部门">
                </el-table-column>
                <el-table-column
                        prop="position.name"
                        width="150"
                        align="left"
                        label="职位">
                </el-table-column>
                <el-table-column
                        prop="salary.basicSalary"
                        width="120"
                        align="left"
                        label="基本薪资">
                </el-table-column>
                <el-table-column
                        prop="adjustSalary.aftersalary"
                        width="120"
                        align="left"
                        label="调后薪资">
                </el-table-column>

                <el-table-column
                        fixed="right"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpSalaryView(scope.row.workID)" style="padding: 3px" size="mini" type="primary">修改工资</el-button>
                        <el-button @click="showEmpSalaryDetail(scope.row)" style="padding: 3px" size="mini" type="info">修改详情</el-button>

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
                <el-form :model="emps" ref="empsForm">

                    <el-form-item label="工号:" prop="workID">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  @blur="searchEmp(emps.workID)"    v-model="emps.workID" placeholder="工号" ></el-input>
                    </el-form-item>

                    <el-form-item label="姓名:" prop="name">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="emps.name"
                                  placeholder="请输入员工姓名"></el-input>
                    </el-form-item>

                    <el-form-item label="员工ID:" prop="id">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="emps.id" placeholder="员工id" ></el-input>
                    </el-form-item>
                    <el-form-item label="性别:" prop="gender">
                        <el-radio-group v-model="emps.gender" >
                            <el-radio label="男">男</el-radio>
                            <el-radio label="女">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="部门:" prop="dname">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="emps.department.name" placeholder="所属部门" ></el-input>
                    </el-form-item>
                    <el-form-item label="职位:" prop="pname">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="emps.position.name" placeholder="职位"></el-input>
                    </el-form-item>

                    <el-form-item label="调前薪资:" prop="beforeSalary">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="emps.adjustSalary.beforesalary" placeholder="调前薪资"></el-input>
                    </el-form-item>

                    <el-form-item label="调后薪资:" prop="afterSalary">
                        <el-select v-model="emps.adjustSalary.aftersalary" placeholder="请选择" @change="getSid" size="mini" style="width: 150px;">
                            <el-option
                                    v-for="(item,index) in salarys"
                                    :key="item.id"
                                    :label="item.basicSalary"
                                    :value="item.basicSalary">

                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="调薪日期:" prop="asDate">
                        <el-date-picker
                                v-model="emps.adjustSalary.asdate"
                                size="mini"
                                type="date"
                                value-format="yyyy-MM-dd"
                                style="width: 150px;"
                                placeholder="调薪日期">
                        </el-date-picker>
                    </el-form-item>

                    <el-form-item label="调薪理由:" prop="reason">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="emps.adjustSalary.reason" placeholder="调薪理由"></el-input>
                    </el-form-item>
                    <el-form-item label="备注:" prop="remark">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="emps.adjustSalary.remark" placeholder="备注"></el-input>
                    </el-form-item>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="doAddEmpUpdateSalary()">确 定</el-button>
                </div>
            </div>
        </el-dialog>












    </div>
</template>

<script>
    export default {
        name: "PerSalary",

        data() {
            return {
                empSalarys: [],
                title:'',


                gridData: [],



                loading: false,
                dialogFormVisible: false,
                page:1,
                size:10,
                total:0,
                keyword:'',
                sid: '',
                salarys:[],
                importDataBtnText:'导入数据',
                importDataBtnIcon: 'el-icon-upload2',
                importDataDisabled: false,
                emps: {
                    workID: '',
                    name: '',
                    id: '',
                    gender: '',
                    department:{},
                    position:{},
                    adjustSalary:{
                        asdate:'',
                    },
                    salary:{}

                },

            };
        },
        mounted() {
            this.initEmpSalary();
            this.initSalarys();
            this.initAdjustSalarys();

        },
        methods:{
            showAddEmpSalaryView(){
                this.emptyEmps();
                this.title='添加员工培训';
                this.dialogFormVisible=true;
            },
            showEditEmpSalaryView(data){

                this.title='修改员工工资';
                this.emps.workID=data;
                this.searchEmp(this.emps.workID);
                this.dialogFormVisible=true;
            },
            showEmpSalaryDetail(data){
                this.title='修改工资详情';
                this.searchAdjustSalary(data);
                this.dialogFormVisible=true;
            },
            initEmpSalary(){
                this.loading = true;
                let url = '/employee/basic/adjustSalary/?page=' + this.page + '&size=' + this.size + "&name=" + this.keyword;

                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.empSalarys = resp.data;

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
                this.initEmpSalary();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmpSalary();
            },
            initAdjustSalarys(){
                this.getRequest("/employee/basic/getAllAdjustSalary/").then(resp => {
                    if (resp) {

                        this.gridData = resp;
                    }
                })
            },

            emptyEmps(){
                this.emps={
                    id: '',
                    name: '',
                    workID: '',
                    gender: '',
                    department:{},
                    position:{},
                    adjustsalary:{}
                }
            },

            /*获取员工工资套账*/
            initSalarys(){
                this.getRequest("/salary/sob/").then(resp => {
                    if (resp) {

                        this.salarys = resp;
                    }
                })
            },

            doAddEmpUpdateSalary(){
                this.$refs['empsForm'].validate(valid=>{
                    if(valid){
                        this.postRequest("/employee/basic/addEmpAdjustSalary/",this.emps).then(resp=>{
                            if(resp){

                               let eid=this.emps.id;

                                 this.updateEmpSalary(eid,this.sid);
                                this.dialogFormVisible=false;
                                this.initEmpSalary();
                            }
                        })
                    }
                });
            },

            getSid(name){
                let obj = {};
                obj = this.salarys.find((item)=>{ //这里的carslist就是上面遍历的数据源
                    return item.basicSalary === name;//筛选出匹配数据
                });
                this.sid=obj.id;


            },

            updateEmpSalary(eid,sid){
                this.putRequest("/employee/basic/updateEmpSalarys/?eid="+eid+"&sid="+sid).then(resp=>{
                    if(resp){
                        this.initEmpSalary();
                    }
                })
            },

            doAddEmploySalary(){
                if(this.emps.id){

                    this.$refs['empsForm'].validate(valid=>{
                        if(valid){
                            this.putRequest("/employee/basic/addEmpAdjustSalary/",this.emps).then(resp=>{
                                if(resp){
                                   /* this.updateEmpSalary()*/
                                    this.dialogFormVisible=false;
                                    this.initEmpTrains();
                                }
                            })
                        }
                    });
                }else{
                    this.$refs['empsForm'].validate(valid=>{
                        if(valid){
                            this.postRequest("/employee/basic/updataSalary/",this.emps).then(resp=>{
                                this.dialogFormVisible=false;
                                this.initEmpTrains();
                            })
                        }
                    });
                }
            },

           /* deleteEmpTrain(data){
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
            },*/

            searchEmp(id){
                let url='/employee/basic/searchName?id='+id;
                this.getRequest(url).then(resp=>{
                    this.emps.name=resp.name;
                    this.emps.id=resp.id;
                    this.emps.gender=resp.gender;
                    this.emps.department.name=resp.department.name;
                    this.emps.position.name=resp.position.name;
                    this.emps.adjustSalary.beforesalary=resp.salary.basicSalary;
                });
            },

            searchAdjustSalary(data){
                let id=data.id;
                let url='/employee/basic/getAdjustSalaryById?id='+id;
                this.emps.workID=data.workID;
                this.emps.name=data.name;
                this.emps.id=data.id;
                this.emps.gender=data.gender;
                this.emps.department.name=data.department.name;
                this.emps.position.name=data.position.name;
                this.getRequest(url).then(resp=>{
                    this.emps.adjustSalary.beforesalary=resp.beforesalary;
                    this.emps.adjustSalary.aftersalary=resp.aftersalary;
                    this.emps.adjustSalary.asdate=resp.asdate;
                    this.emps.adjustSalary.reason=resp.reason;
                    this.emps.adjustSalary.remark=resp.remark;
                });
            },

        }
    }
</script>

<style scoped>

</style>