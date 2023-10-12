import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据分页列表
export function getListPage (params:API.GroupQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.GroupListResult>({
    url: '/system/group/listPage?'+queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}
 
//保存或更新数据
export function saveOrUpdate (params:API.GroupEditParams) {
  let url = '/system/group';
  let method = 'post';
  if(params.id!=''&&params.id!=undefined){
    method = 'put';
  }
  return request({
    url: url,
    method: method,
    data: params
  })
}

//删除数据
export function delData (data:API.GroupDelParams) {
  return request({
    url: '/system/group/'+data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data:API.GroupEditStatusParams) {
  return request({
    url: '/system/group/updateStatus',
    method: 'post',
    data: data
  })
}

//查询组用户信息
export function findGroupUser (params:API.GroupUserQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.GroupUserResult>({
    url: '/system/group/findGroupUser?'+queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

