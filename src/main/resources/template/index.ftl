<template>
  <div>
    <div class="filter-container">
      <el-input class="filter-item" style="width: 200px;" v-model="listQuery.id" placeholder="ID"></el-input>
      <el-button class="filter-item" style="width: 100px;" type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
      <el-button class="filter-item" style="width: 100px;" type="primary" icon="el-icon-edit" @click="handleCreate">新建</el-button>
    </div>

    <el-table :data="list" v-loading.body="listLoading" element-loading-text="加载中" border fit highlight-current-row>
      <el-table-column label="序号" type="index" align="center" width="50px">
      </el-table-column>
<#list propertyList! as property>
      <el-table-column label="${property.columnComment!}" align="center" <#if property.propertyType=="Date"> width="160px" </#if>>
        <template slot-scope="scope">
          {{scope.row.${property.propertyName!}<#if property.propertyType=="Date"> | dateFormat </#if>}}
        </template>
      </el-table-column>
</#list>
      <el-table-column label="操作" align="center" width="200px">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page" :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :close-on-click-modal="false" width="50%">
      <el-form ref="dataForm" :model="entity" :rules="rules" label-position="right" label-width="100px">
<#list propertyList! as property>
        <el-form-item label="${property.columnComment!}:" prop="${property.propertyName!}">
          <el-input v-model="entity.${property.propertyName!}"></el-input>
        </el-form-item>
</#list>
      </el-form>
      <div slot="footer">
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">新建</el-button>
        <el-button v-else type="primary" @click="updateData">修改</el-button>
        <el-button @click="dialogFormVisible=false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getList, update${entityName}, add${entityName}, delete${entityName} } from '@/api/${entityName}'
import { getBusinessInfo } from '@/api/common'
import { parseTime } from '@/utils/index.js'

export default {
  data() {
    return {
      list: [],
      listLoading: true,
      total: null,
      listQuery: {
        page: 1,
        limit: 20,
        id: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改',
        create: '新建'
      },
      entity: {
<#list propertyList! as property>
        ${property.propertyName!}: undefined<#if property_has_next>,<#else></#if>

</#list>
      },
      loadingOptions: {
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      },
      rules: {
      }
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        Y: "success",
        N: "danger"
      };
      return statusMap[status];
    },
    statusDesc(s) {
      const statusMap = {
        Y: "显示",
        N: "隐藏"
      };
      return statusMap[s];
    },
    dateFormat(date) {
      if (date) {
        return parseTime(date, '{y}-{m}-{d} {h}:{i}:{s}');
      }
    }
  },
  computed: {
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      getList(this.listQuery).then(response => {
        this.list = response.data.list;
        this.listLoading = false;
        this.total = response.data.count;
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.fetchData();
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.fetchData();
    },
    resetEntity() {
      this.entity = {
<#list propertyList! as property>
        ${property.propertyName!}: undefined<#if property_has_next>,<#else></#if>

</#list>
      }
    },
    handleCreate() {
      this.resetEntity();
      this.dialogStatus = 'create';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate();
      });
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const loading = this.$loading(this.loadingOptions);
          add${entityName}(this.entity).then(response => {
            // 根据状态判断是否成功
            if (response.code === 0) {
              this.fetchData();
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              });
            } else {
              this.$notify({
                title: '失败',
                message: '操作失败',
                type: 'error',
                duration: 2000
              });
            }
            loading.close();
          }).catch(e => {
            console.dir(e);
            this.$notify({
              title: "失败",
              message: e.message,
              type: "error",
              duration: 2000
            });
            loading.close();
            console.log("操作失败", e);
        });
          this.dialogFormVisible = false;
        }
      });
    },
    handleUpdate(row) {
      this.entity = Object.assign({}, row);
      this.dialogStatus = 'update';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate();
      });
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const loading = this.$loading(this.loadingOptions);
          update${entityName}(this.entity).then(response => {
            // 根据状态判断是否成功
            if (response.code === 0) {
              this.fetchData();
              this.$notify({
                title: '成功',
                message: '操作成功',
                type: 'success',
                duration: 2000
              });
            } else {
              this.$notify({
                title: '失败',
                message: '操作失败',
                type: 'error',
                duration: 2000
              });
            }
            loading.close();
          }).catch(e => {
            console.dir(e);
            this.$notify({
              title: "失败",
              message: e.message,
              type: "error",
              duration: 2000
            });
            loading.close();
            console.log("操作失败", e);
          });
          this.dialogFormVisible = false;
        }
      });
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading(this.loadingOptions);
        delete${entityName}(row.id).then(response => {
          // 根据状态判断是否成功
          if (response.code === 0) {
            var delIndex = '';
            this.list.map((item, index) => {
              if (item.id == row.id) {
                delIndex = index;
              }
            });
            this.list.splice(delIndex, 1);
            this.$notify({
              title: '成功',
              message: '操作成功',
              type: 'success',
              duration: 2000
            });
          } else {
            this.$notify({
              title: '失败',
              message: '操作失败',
              type: 'error',
              duration: 2000
            });
          }
          loading.close();
        }).catch(e => {
          console.dir(e);
          this.$notify({
             title: "失败",
             message: e.message,
             type: "error",
             duration: 2000
          });
          loading.close();
          console.log("操作失败", e);
        });
      });
    }
  }
}
</script>
