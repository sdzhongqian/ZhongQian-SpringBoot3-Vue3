import { request } from '@/utils/request';
import querystring from 'querystring';

export function upload(formData) {
  return request({
    url: '/upload/uploadFile',
    method: 'post',
    data: formData,
  });
}

//上传本地文件
export function uploadLocalFile(formData) {
  return request({
    url: '/upload/uploadLocalFile',
    method: 'post',
    data: formData,
  });
}

//查询数据列表
export function infoFile(params) {
  let queryString = querystring.stringify(params);
  return request({
    url: '/upload/infoFile?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

//查询附件路径
export function findViewUrl(params) {
  let queryString = querystring.stringify(params);
  return request({
    url: '/upload/viewFile?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}
