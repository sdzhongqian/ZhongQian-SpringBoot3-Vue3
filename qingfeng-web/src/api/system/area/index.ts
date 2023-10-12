import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据分页列表
export function getListPage(params: API.AreaQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.AreaListResult>({
    url: '/system/area/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//保存或更新数据
export function saveOrUpdate(params: API.AreaEditParams) {
  let url = '/system/area';
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
export function delData(data: API.AreaDelParams) {
  return request({
    url: '/system/area/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data: API.AreaEditStatusParams) {
  return request({
    url: '/system/area/updateStatus',
    method: 'post',
    data: data
  })
}

//查询字典列表
export async function findAreaList(params: API.AreaQueryParams) {
  let queryString = querystring.stringify(params);
  return await request<API.AreaListResult>({
    url: '/system/area/list?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}




