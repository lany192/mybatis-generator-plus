<template>
    <div class="app-container">
        <!-- 查询和其他操作 -->
        <div class="filter-container">
            <el-input v-model="query.keyword" clearable class="filter-item" style="width: 200px;" placeholder="请输入关键字"/>
            <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
            <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
        </div>
        <!-- 查询结果 -->
        <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>
        <#list columns as item>
            <#if item.type == "BIGINT">
                <#if item.name == "id">
                <el-table-column align="center" label="${nameZh}ID" prop="${item.nameLower}" sortable/>
                <#else>
                <el-table-column align="center" label="${item.nameUpper}" prop="${item.nameLower}" sortable/>
                </#if>
            </#if>
            <#if item.type == "INT">
                <el-table-column align="center" label="${item.nameUpper}" prop="${item.nameLower}"/>
            </#if>
            <#if item.type == "FLOAT">
                <el-table-column align="center" label="${item.nameUpper}" prop="${item.nameLower}"/>
            </#if>
            <#if item.type == "VARCHAR" || item.type == "CHAR">
                <el-table-column align="center" label="${item.nameUpper}" prop="${item.nameLower}"/>
            </#if>
            <#if item.type == "BIT">
                <el-table-column align="center" label="${item.nameUpper}" prop="${item.nameLower}">
                    <template slot-scope="scope">
                        {{ scope.row.${item.nameLower} ? '是' : '否' }}
                    </template>
                </el-table-column>
            </#if>
            <#if item.type == "TIMESTAMP">
                <#if item.name == "create_time">
                <el-table-column :formatter="$timestampFormat" align="center" label="创建时间" prop="${item.nameLower}" sortable/>
                <#else>
                <el-table-column :formatter="$timestampFormat" align="center" label="${item.nameUpper}" prop="${item.nameLower}" sortable/>
                </#if>
            </#if>
        </#list>
        </el-table>
        <!-- 分页菜单 -->
        <pagination v-show="total>0" :total="total" :page.sync="query.page" :limit.sync="query.limit" @pagination="getList" />
    </div>
</template>

<script>
import { get${nameUpper}List } from '@/api/admin/${nameLower}Api'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
    name: '${nameUpper}',
    components: { Pagination },
    data() {
        return {
            list: undefined,
            total: 0,
            listLoading: true,
            query: {
                page: 1,
                limit: 30,
                keyword: undefined
            },
            downloadLoading: false
        }
    },
    created() {
        this.getList()
    },
    methods: {
        getList() {
            this.listLoading = true
            get${nameUpper}List(this.query).then(response => {
                this.list = response.data.data.list
                this.total = response.data.data.size
                this.listLoading = false
            }).catch(() => {
                this.list = []
                this.total = 0
                this.listLoading = false
            })
        },
        handleFilter() {
            this.query.page = 1
            this.getList()
        },
        handleDownload() {
            this.downloadLoading = true
            import('@/vendor/Export2Excel').then(excel => {
                const titles = ${titles}
                const fields = ${fields}
                excel.export_json_to_excel2(titles, this.list, fields, '${nameZh}')
            this.downloadLoading = false
            })
        }
    }
}
</script>
