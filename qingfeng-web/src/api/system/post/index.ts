import { request } from '@/utils/request';
import querystring from 'querystring';

//查询数据分页列表
export function getListPage(params: API.PostQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.PostListResult>({
    url: '/system/post/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

//保存或更新数据
export function saveOrUpdate(params: API.PostEditParams) {
  let url = '/system/post';
  let method = 'post';
  if (params.id != '' && params.id != undefined) {
    method = 'put';
  }
  return request({
    url: url,
    method: method,
    data: params,
  });
}

//删除数据
export function delData(data: API.PostDelParams) {
  return request({
    url: '/system/post/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

//更新状态
export function updateStatus(data: API.PostEditStatusParams) {
  return request({
    url: '/system/post/updateStatus',
    method: 'post',
    data: data,
  });
}

//查询列表
export async function findList(params: API.PostQueryParams) {
  let queryString = querystring.stringify(params);
  return await request<API.PostListResult>({
    url: '/system/post/list?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

//获取角色权限
export function findRoleAuth(params) {
  return request({
    url: '/system/post/findRoleAuth',
    method: 'post',
    data: params,
  });
}

//更新权限
export function updateAuth(params) {
  return request({
    url: '/system/post/updateAuth',
    method: 'post',
    data: params,
  });
}
