import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据分页列表
export function getListPage(params: API.MenuQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.MenuListResult>({
    url: '/system/menu/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//保存或更新数据
export function saveOrUpdate(params: API.MenuEditParams) {
  let url = '/system/menu';
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
export function delData(data: API.MenuDelParams) {
  return request({
    url: '/system/menu/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data: API.MenuEditStatusParams) {
  return request({
    url: '/system/menu/updateStatus',
    method: 'post',
    data: data
  })
}

//查询数据列表
export function findMenuList(params: API.MenuQueryParams) {
  return request({
    url: '/system/menu/list',
    method: 'post',
    data: params
  })
}

//查询数据详情
export function findMenuInfo(params: API.MenuQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.MenuInfoResult>({
    url: '/system/menu/info?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}


