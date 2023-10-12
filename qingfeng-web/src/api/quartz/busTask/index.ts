import {request} from '@/utils/request'
import querystring from 'querystring'


export function getListPage (params:API.BusTaskQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.BusTaskListResult>({
    url: '/quartz/busTask/listPage?'+queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}
  
export function saveOrUpdate (params:API.BusTaskEditParams) {
  let url = '/quartz/busTask';
  if(params.id!=''&&params.id!=undefined){
    return request({
      url: url,
      method: 'put',
      data: params
    })
  }else{
    return request({
      url: url,
      method: 'post',
      data: params
    })
  }
}

export function del (data:API.BusTaskDelParams) {
  return request({
    url: '/quartz/busTask/'+data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function stopOrRestore(params:API.BusTaskStatusParams) {
  let queryString = querystring.stringify(params);
  return request({
    url: '/quartz/busTask/stopOrRestore?'+queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function execution(jobName,jobGroup) {
  return request({
    url: '/quartz/busTask/execution?jobName='+jobName+'&jobGroup='+jobGroup,
    method: 'get'
  })
}


  