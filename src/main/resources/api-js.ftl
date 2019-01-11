import request from '@/utils/request'

// 创建${nameZh}
export function add${nameUpper}(data) {
  return request({
    url: '/api/admin/${nameLower}/add',
    method: 'post',
    data
  })
}

// 删除${nameZh}
export function delete${nameUpper}(data) {
  return request({
    url: '/api/admin/${nameLower}/delete',
    method: 'post',
    data
  })
}

// 批量删除${nameZh}
export function batchDelete${nameUpper}(data) {
  return request({
    url: '/api/admin/${nameLower}/delete/batch',
    method: 'post',
    data
  })
}

// 更新${nameZh}
export function update${nameUpper}(data) {
  return request({
    url: '/api/admin/${nameLower}/update',
    method: 'post',
    data
  })
}

// 获取${nameZh}单条记录详情
export function find${nameUpper}ById(data) {
  return request({
    url: '/api/admin/${nameLower}/findById',
    method: 'post',
    data
  })
}

// 根据id批量查找${nameZh}
export function find${nameUpper}ByIds(data) {
  return request({
    url: '/api/admin/${nameLower}/findByIds',
    method: 'post',
    data
  })
}

// 获取${nameZh}列表
export function get${nameUpper}List(query) {
  return request({
    url: '/api/admin/${nameLower}/list',
    method: 'get',
    params: query
  })
}
