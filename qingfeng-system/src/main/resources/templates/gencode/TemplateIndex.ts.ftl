import { request } from '@/utils/request'
import querystring from 'querystring'

//查询数据分页列表
export function getListPage (params:API.${tablePd.bus_name?cap_first}QueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.${tablePd.bus_name?cap_first}ListResult>({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/listPage?'+queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}
 
//保存或更新数据
export function saveOrUpdate (params:API.${tablePd.bus_name?cap_first}EditParams) {
  let url = '/${tablePd.mod_name}/${tablePd.bus_name}';
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
export function delData (data:API.${tablePd.bus_name?cap_first}DelParams) {
  return request({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/'+data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新状态
export function updateStatus(data:API.${tablePd.bus_name?cap_first}EditStatusParams) {
  return request({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/updateStatus',
    method: 'post',
    data: data
  })
}

//查询列表
export async function findList(params: API.${tablePd.bus_name?cap_first}QueryParams) {
  let queryString = querystring.stringify(params);
  return await request<API.${tablePd.bus_name?cap_first}ListResult>({
      url: '/${tablePd.mod_name}/${tablePd.bus_name}/list?' + queryString,
      method: 'get',
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      }
    })
  }
