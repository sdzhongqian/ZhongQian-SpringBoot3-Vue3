import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据分页列表
export function getListPage(params: API.OrganizeQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.OrganizeListResult>({
    url: '/system/organize/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//保存或更新数据
export function saveOrUpdate(params: API.OrganizeEditParams) {
  let url = '/system/organize';
  let method = 'post';
  if (params.id != '' && params.id != undefined) {
    method = 'put';
  }
  return request({
    url: url,
    method: method,
    data: params
  })
}

//删除数据
export function delData(data: API.OrganizeDelParams) {
  return request({
    url: '/system/organize/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data: API.OrganizeEditStatusParams) {
  return request({
    url: '/system/organize/updateStatus',
    method: 'post',
    data: data
  })
}

//获取数据列表
export function getOrganizeList(params: { parent_id: string | number }) {
  let queryString = querystring.stringify(params);
  return request({
    url: '/system/organize/list?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

//获取角色权限
export function findRoleAuth(params) {
  return request({
    url: '/system/organize/findRoleAuth',
    method: 'post',
    data: params
  })
}

//更新权限
export function updateAuth(params) {
  return request({
    url: '/system/organize/updateAuth',
    method: 'post',
    data: params
  })
}

