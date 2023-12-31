import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据列表
export function getListPage(params: API.RoleQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.RoleListResult>({
    url: '/system/role/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//保存或更新数据
export function saveOrUpdate(params: API.RoleEditParams) {
  let url = '/system/role';
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
export function delData(data: API.RoleDelParams) {
  return request({
    url: '/system/role/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data: API.RoleEditStatusParams) {
  return request({
    url: '/system/role/updateStatus',
    method: 'post',
    data: data
  })
}


//更新权限
export function updateAuth(params) {
  return request({
    url: '/system/role/updateAuth',
    method: 'post',
    data: params
  })
}

//获取角色菜单列表
export function findRoleMenuList(params) {
  return request({
    url: '/system/role/findRoleMenuList',
    method: 'post',
    data: params
  })
}

//查询  
export function getServiceList(parameter) {
  return request({
    url: "",
    method: 'get',
    params: parameter
  })
}

