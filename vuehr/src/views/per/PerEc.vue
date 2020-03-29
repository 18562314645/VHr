<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                          clearable
                          @clear="initEmpECs"
                          style="width: 350px;margin-right: 10px" v-model="keyword"
                          @keydown.enter.native="initEmpECs" ></el-input>
                <el-button icon="el-icon-search" type="primary" @click="initEmpECs">
                    搜索
                </el-button>
            </div>
            <div>
                <el-button type="primary" icon="el-icon-plus" @click="showAddEmpECView">
                    添加用户
                </el-button>
            </div>
        </div>

        <div style="margin-top: 10px">
        <el-table
                :data="empECs"
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
                    prop="ename"
                    fixed
                    align="left"
                    label="姓名"
                    width="90">
            </el-table-column>
            <el-table-column
                    prop="workID"
                    label="工号"
                    align="left"
                    width="85">
            </el-table-column>
            <el-table-column
                    prop="gender"
                    label="性别"
                    align="left"
                    width="85">
            </el-table-column>
            <el-table-column
                    prop="dname"
                    width="95"
                    align="left"
                    label="部门">
            </el-table-column>
            <el-table-column
                    prop="ecdate"
                    width="150"
                    align="left"
                    label="奖罚日期">
            </el-table-column>
            <el-table-column
                    prop="ecreason"
                    width="150"
                    align="left"
                    label="奖罚原因">
            </el-table-column>
            <el-table-column
                    prop="ecpoint"
                    width="150"
                    align="left"
                    label="奖罚分数">
            </el-table-column>
            <el-table-column
                    prop="ectype"
                    :formatter="formatEctype"
                    width="150"
                    align="left"
                    label="奖罚类型">
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
                    <el-button @click="showEditEmpECView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                    <el-button @click="deleteEmpEC(scope.row)" style="padding: 3px" size="mini" type="danger">删除
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
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="80%">
            <div>
                <el-form :model="empec"  ref="empecForm">

                    <el-row>

                        <el-col :span="6">
                            <el-form-item label="工号:" prop="workID">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          @blur="searchEmp(empec.workID)"    v-model="empec.workID" placeholder="工号" ></el-input>
                            </el-form-item>
                        </el-col>


                        <el-col :span="5">
                            <el-form-item label="性别:" prop="gender">
                                <el-radio-group v-model="empec.gender" disabled>
                                    <el-radio label="男">男</el-radio>
                                    <el-radio label="女">女</el-radio>
                                   </el-radio-group>
                            </el-form-item>
                        </el-col>

                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="姓名:" prop="ename">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="empec.ename"
                                          placeholder="请输入员工姓名"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="所属部门:" prop="dname">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empec.dname" placeholder="所属部门" disabled></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>


                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="员工id:" prop="eid">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empec.eid" placeholder="员工id"></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="6">
                            <el-form-item label="奖罚理由:" prop="ecreason">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empec.ecreason" placeholder="奖罚理由"></el-input>
                            </el-form-item>
                        </el-col>

                    </el-row>


                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="奖罚分数:" prop="ecpoint">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empec.ecpoint" placeholder="奖罚分数" ></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="6">
                            <el-form-item label="奖罚日期:" prop="ecdate">
                                <el-date-picker
                                        v-model="empec.ecdate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;"
                                        placeholder="奖罚日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="奖罚类型:" prop="ectype">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empec.ectype" placeholder="奖罚类型" ></el-input>
                            </el-form-item>
                        </el-col>

                        <el-col :span="6">
                            <el-form-item label="奖罚备注:" prop="remark">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empec.remark" placeholder="奖罚备注" ></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <span v-show="showdata" slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button  type="primary" @click="doAddEmpEC">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PerEc",
        data(){
            return {
                title: '',
                empECs: [],
                loading: false,
                total: 0,
                page: 1,
                keyword: '',
                size: 10,
                showdata:true,
                dialogVisible: false,
                empec: {
                    ename: "",
                    workID: "",
                    gender: "",
                    dname: "",
                    ecdate: "",
                    ecreason: "",
                    ecpoint: "",
                    ectype: "",
                    remark: ""
                }
            }
        },
        mounted() {
            this.initEmpECs();

        },
        methods: {
            initEmpECs(){
                this.loading = true;
                let url = '/personnel/ec/?page=' + this.page + '&size=' + this.size + "&ename=" + this.keyword;

                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.empECs = resp.data;

                        this.total = resp.total;
                    }
                });
            },

            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEmpECs();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmpECs();
            },
            showAddEmpECView(){
                this.emptyEmpEC();
                this.title = '添加员工';
                this.dialogVisible = true;
            },
            showEditEmpECView(data){
                this.title='编辑员工信息';
                this.empec=data;
                this.dialogVisible = true;
            },
            deleteEmpEC(data){
                this.$confirm('此操作将永久删除【' + data.ename + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/personnel/ec/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmpECs();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            emptyEmpEC(){
                this.empec={
                    id: "",
                    ename: "",
                    workID: "",
                    gender: "",
                    dname: "",
                    ecdate: "",
                    ecreason: "",
                    ecpoint: "",
                    ectype: "",
                    remark: ""
                }

            },
            doAddEmpEC(){
                if(this.empec.id){
                    this.$refs['empecForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/personnel/ec/", this.empec).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpECs();
                                }
                            })
                        }
                    });
                }else{
                    this.$refs['empecForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/personnel/ec/", this.empec).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpECs();
                                }
                            })
                        }
                    });
                }
              },

            searchEmp(id){
                let url='/employee/basic/searchName?id='+id;
                this.getRequest(url).then(resp=>{
                    this.empec.gender=resp.gender;
                    this.empec.eid=resp.id;
                    this.empec.ename=resp.name;
                    this.empec.dname=resp.department.name;
                });

            },
            formatEctype(row,column){
                return row.ectype=='1'?'罚':'奖';
            },

        }
    }


</script>

<style scoped>

</style>
