import { request } from '@/utils/request';
import querystring from 'querystring';

//查询数据列表
export function getListPage(params: API.GraphicQueryParams) {
  console.log(params);
  let queryString = querystring.stringify(params);
  return request<API.GraphicListResult>({
    url: '/graphic/findListPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

//保存或更新数据
export function saveOrUpdate(params:API.GraphicEditParams) {
  let url = '/graphic';
  if (params.id != '' && params.id != undefined) {
    return request({
      url: url,
      method: 'put',
      data: params,
    });
  } else {
    return request({
      url: url,
      method: 'post',
      data: params,
    });
  }
}

//删除数据
export function delData(data:API.GraphicDelParams) {
  return request({
    url: '/graphic/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

//更新状态
export function updateStatus(data:API.GraphicEditStatusParams) {
  return request({
    url: '/graphic/updateStatus',
    method: 'post',
    data: data,
  });
}

//更新单状态
export function updateOneStatus(id, status, type) {
  return request({
    url: '/graphic/updateOneStatus',
    method: 'post',
    data: {
      id,
      status,
      type,
    },
  });
}
