import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/${entityName?uncap_first}/list',
    method: 'post',
    params
  })
}

export function update${entityName}(params) {
  return request({
    url: '/${entityName?uncap_first}/update',
    method: 'post',
    data: params
  })
}

export function add${entityName}(params) {
  return request({
    url: '/${entityName?uncap_first}/add',
    method: 'post',
    data: params
  })
}

export function delete${entityName}(params) {
  return request({
    url: '/${entityName?uncap_first}/delete',
    method: 'get',
    params: {
      id: params
    }
  })
}