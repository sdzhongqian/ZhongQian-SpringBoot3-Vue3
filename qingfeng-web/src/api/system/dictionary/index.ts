import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据分页列表
export function getListPage(params: API.DictionaryQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.DictionaryListResult>({
    url: '/system/dictionary/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//保存或更新数据
export function saveOrUpdate(params: API.DictionaryEditParams) {
  let url = '/system/dictionary';
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
export function delData(data: API.DictionaryDelParams) {
  return request({
    url: '/system/dictionary/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data: API.DictionaryEditStatusParams) {
  return request({
    url: '/system/dictionary/updateStatus',
    method: 'post',
    data: data
  })
}

//查询字典列表
export async function findDictionaryList(params: API.DictionaryQueryParams) {
  let queryString = querystring.stringify(params);
  return await request<API.DictionaryListResult>({
    url: '/system/dictionary/selectList?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export async function findList(params: API.DictionaryQueryParams) {
  let queryString = querystring.stringify(params);
  return await request<API.DictionaryListResult>({
    url: '/system/dictionary/list?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}




