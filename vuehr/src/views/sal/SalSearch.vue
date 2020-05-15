<template>
    <div>
        <div>
            <el-table :data="emps" border stripe size="mini">
                <el-table-column type="selection" align="left" width="55"></el-table-column>
                <el-table-column prop="name" label="姓名" fixed width="120" align="left"></el-table-column>
                <el-table-column prop="workID" label="工号" width="120" align="left"></el-table-column>
                <el-table-column prop="gender" label="性别" width="200" align="left"></el-table-column>
                <el-table-column prop="position.name" label="职位" width="200" align="left"></el-table-column>
                <el-table-column prop="salary.basicSalary" label="基本工资" width="120" align="left"></el-table-column>
                <el-table-column prop="salary.bonus" label="奖金" width="120" align="left"></el-table-column>
                <el-table-column prop="salary.lunchSalary" label="午饭补贴" width="120" align="left"></el-table-column>
                <el-table-column prop="salary.trafficSalary" label="交通补贴" width="120" align="left"></el-table-column>
                <el-table-column prop="salary.allSalary" label="应发工资" width="120" align="left"></el-table-column>

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
    </div>
</template>

<script>
    export default {
        name: "SalSearch",
        data() {
            return {
                emps: [],
                total: 0,
                currentPage: 1,
                currentSize: 10,
                currentSalary: null,
                salaries: [],
                allSalary:[]
            }
        },
        mounted() {
            this.initEmps();
            this.initSalaries();
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

            initEmps() {
                this.getRequest("/salary/sobcfg/?page=" + this.currentPage + '&size=' + this.currentSize).then(resp => {
                    if (resp) {
                        this.emps = resp.data;
                        this.emps.forEach(item=>{

                            this.allSalary=item.salary.basicSalary+item.salary.bonus+item.salary.lunchSalary+item.salary.trafficSalary;


                        })
                        this.total = resp.total;
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>