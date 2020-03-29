<template>

    <div >
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                          clearable
                          @clear="initEmps"
                          style="width: 350px;margin-right: 10px" v-model="keyword"
                          @keydown.enter.native="initEmps" ></el-input>
                <el-button icon="el-icon-search" type="primary" @click="initEmps">
                    搜索
                </el-button>
            </div>
            <div>
                <el-button type="primary" icon="el-icon-plus" @click="showAddEmpECView">
                    添加用户
                </el-button>
            </div>
        </div>
        <div>
            <el-table :data="emps" border stripe size="mini">
                <el-table-column type="selection" align="left" width="55"></el-table-column>
                <el-table-column prop="name" label="姓名" fixed width="120" align="left"></el-table-column>
                <el-table-column prop="workID" label="工号" width="120" align="left"></el-table-column>
                <el-table-column prop="email" label="电子邮件" width="160" align="left"></el-table-column>
                <el-table-column prop="phone" label="电话号码" width="120" align="left"></el-table-column>
                <el-table-column prop="department.name" label="所属部门" width="120" align="left"></el-table-column>
                <el-table-column prop="jobLevel.name" label="所属职称" width="120" align="left"></el-table-column>
                <el-table-column label="部门套账" align="center">
                    <template slot-scope="scope">
                        <el-tooltip placement="right" v-if="scope.row.salary">
                            <div slot="content">
                                <table>
                                    <tr>
                                        <td>基本工资</td>
                                        <td>{{scope.row.salary.basicSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>交通补助</td>
                                        <td>{{scope.row.salary.trafficSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>午餐补助</td>
                                        <td>{{scope.row.salary.lunchSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>奖金</td>
                                        <td>{{scope.row.salary.bonus}}</td>
                                    </tr>
                                    <tr>
                                        <td>养老金比率</td>
                                        <td>{{scope.row.salary.pensionPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>养老金基数</td>
                                        <td>{{scope.row.salary.pensionBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>医疗保险比率</td>
                                        <td>{{scope.row.salary.medicalPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>医疗保险基数</td>
                                        <td>{{scope.row.salary.medicalBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>公积金比率</td>
                                        <td>{{scope.row.salary.accumulationFundPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>公积金基数</td>
                                        <td>{{scope.row.salary.accumulationFundBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>启用时间</td>
                                        <td>{{scope.row.salary.createDate}}</td>
                                    </tr>
                                </table>
                            </div>
                            <el-tag>{{scope.row.salary.name}}</el-tag>
                        </el-tooltip>
                        <el-tag v-else>暂未设置</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="150">
                    <template slot-scope="scope">
                        <el-popover
                                placement="left"
                                title="修改工资账套"
                                @show="showPop(scope.row.salary)"
                                @hide="hidePop(scope.row)"
                                width="200"
                                trigger="click">
                            <div>
                                <el-select v-model="currentSalary" placeholder="请选择" size="mini">
                                    <el-option
                                            v-for="item in salaries"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </div>
                            <el-button slot="reference" type="warning" >编辑</el-button>
                            <el-button slot="reference" type="danger" @click="deleEmpSarlary(scope.row)">删除</el-button>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @size-change="sizeChange"
                        @current-change="currentChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>


        <el-dialog :title="title"
                   :visible.sync="dialogFormVisible"
                   width="50%" >
            <div align="center">
                <el-form :model="empsalary" ref="empsalaryForm">

                    <el-form-item label="工号:" prop="employee.workID">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  @blur="searchEmp(empsalary.employee.workID)"    v-model="empsalary.employee.workID" placeholder="工号" ></el-input>
                    </el-form-item>

                    <el-form-item label="姓名:" prop="employee.name">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="empsalary.employee.name"
                                  placeholder="请输入员工姓名"></el-input>
                    </el-form-item>

                    <el-form-item label="员工ID:" prop="empsalary.eid">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="empsalary.eid" placeholder="员工id" ></el-input>
                    </el-form-item>
                    <el-form-item label="性别:" prop="gender">
                        <el-radio-group v-model="empsalary.employee.gender" >
                            <el-radio label="男">男</el-radio>
                            <el-radio label="女">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="所属部门:" prop="dname">
                        <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                  v-model="empsalary.department.name" placeholder="所属部门" ></el-input>
                    </el-form-item>
                    <el-form-item label="薪资待遇:" prop="sid">
                        <el-select v-model="empsalary.sid" placeholder="请选择" @change="getSid" size="mini" style="width: 150px;">
                            <el-option
                                    v-for="(item,index) in salarys"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">

                            </el-option>
                        </el-select>
                    </el-form-item>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="doAddEmploySalary">确 定</el-button>
                </div>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: "SalTable",
        data() {
            return {
                emps: [],
                title: '',
                keyword:'',
                total: 0,
                currentPage: 1,
                currentSize: 10,
                currentSalary: null,
                salaries: [],
                salarys: [],
                empsalary: {
                    id: '',
                    eid: '',
                    sid: '',
                    department: {},
                    position: {},
                    employee: {},


                },

                dialogFormVisible: false,
            }
        },
        mounted() {
            this.initEmps();
            this.initSalaries();
            this.initSalarys();
        },
        methods: {
            sizeChange(size) {
                this.currentSize = size;
                this.initEmps();
            },
            currentChange(page) {
                this.currentPage = page;
                this.initEmps();
            },
            hidePop(data) {
                if (this.currentSalary) {
                    this.putRequest('/salary/sobcfg/?eid=' + data.id + '&sid=' + this.currentSalary).then(resp => {
                        if (resp) {
                            this.initEmps();
                        }
                    });
                }
            },
            /*获取员工工资套账*/
            initSalarys() {
                this.getRequest("/salary/sob/").then(resp => {
                    if (resp) {

                        this.salarys = resp;
                    }
                })
            },
            getSid(name) {
                let obj = {};
                obj = this.salarys.find((item) => { //这里的carslist就是上面遍历的数据源
                    return item.basicSalary === name;//筛选出匹配数据
                });
                this.sid = obj.id;


            },
            showPop(data) {
                if (data) {
                    this.currentSalary = data.id;
                } else {
                    this.currentSalary = null;
                }
            },
            showAddEmpSalaryView() {

                this.title = '添加员工';
                this.dialogFormVisible = true;
            },
            deleEmpSarlary(data) {
                alert(data.id);
                this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/emp/salary/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmps();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            initSalaries() {
                this.getRequest("/salary/sobcfg/salaries").then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                })
            },

            /*添加员工工资表*/
            doAddEmploySalary() {
                this.$refs['empsalaryForm'].validate(valid => {
                    if (valid) {
                        alert(valid);
                        this.postRequest("/emp/salary/", this.empsalary).then(resp => {
                            this.dialogFormVisible = false;
                            this.initEmps();
                        })
                    }
                });
            },


        searchEmp(id) {
            let url = '/employee/basic/searchName?id=' + id;
            this.getRequest(url).then(resp => {
                this.empsalary.employee.name = resp.name;
                this.empsalary.eid = resp.id;
                this.empsalary.employee.gender = resp.gender;
                this.empsalary.department.name = resp.department.name;

            });
        },
        initEmps() {
            this.getRequest("/emp/salary/?page=" + this.currentPage + '&size=' + this.currentSize+'&name='+this.keyword).then(resp => {
                if (resp) {
                    this.emps = resp.data;
                    this.total = resp.total;
                }
            })
        }
      }
    }
</script>

<style>
    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }

    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>