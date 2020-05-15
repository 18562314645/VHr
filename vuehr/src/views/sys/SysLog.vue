<template>
   <div>
       <div style="display: flex;justify-content: space-between">
           <div>
               <el-input placeholder="请输入员工名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                         clearable
                         @clear="initSysLog"
                         style="width: 350px;margin-right: 10px" v-model="keyword"
                         @keydown.enter.native="" ></el-input>
               <el-button icon="el-icon-search" type="primary" @click="initSysLog">
                   搜索
               </el-button>
           </div>

       </div>
        <el-table
                :data="tableData"
                style="width: 100%"
                :row-class-name="tableRowClassName">
            <el-table-column
                    prop="adddate"
                    label="操作日期"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="operate"
                    label="操作内容"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="hr1.name"
                    label="操作员">
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
</template>

<script>
    export default {
        name: "SysLog",
        mounted() {
            this.initSysLog();

        },
        methods: {
            tableRowClassName({row, rowIndex}) {
                if (rowIndex === 1) {
                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';
            },
            initSysLog(){
                this.loading=true;
                let url='/sys/log/?page='+ this.page+ '&size='+ this.size+'&hr.name='+this.keyword;
                this.getRequest(url).then(resp=>{
                    this.loading=false;
                    this.tableData=resp.data;
                    this.total=resp.total;
                })
            },
            sizeChange(currentSize) {
                this.size = currentSize;
                this.initSysLog();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initSysLog();
            }
        },
        data() {
            return {
                tableData: [],
                page:1,
                size:10,
                total:0,
                keyword:'',
                loading:false
            }
        }
    }
</script>

<style>
    .el-table .warning-row {
        background: oldlace;
    }

    .el-table .success-row {
        background: #f0f9eb;
    }
</style>