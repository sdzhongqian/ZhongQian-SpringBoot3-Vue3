import { request } from '@/utils/request';
import querystring from 'querystring';

/**
 * 查询教育经历列表
 * @param {*} params
 * @returns
 */
export function findEducationList(params) {
  let queryString = querystring.stringify(params);
  return request<API.UserListResult>({
    url: '/personnel/puser/findEducationList?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

/**
 * 查询证件照信息列表
 * @param {*} params
 * @returns
 */
export function findPhotoList(params) {
  let queryString = querystring.stringify(params);
  return request<API.UserListResult>({
    url: '/personnel/puser/findPhotoList?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

/**
 * 查询工作经历列表
 * @param {*} params
 * @returns
 */
export function findWorkexperList(params) {
  let queryString = querystring.stringify(params);
  return request<API.UserListResult>({
    url: '/personnel/puser/findWorkexperList?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

/**
 * 查询员工拓展信息
 * @param params
 * @returns
 */
export function findPuserInfo(params) {
  let queryString = querystring.stringify(params);
  return request<API.UserListResult>({
    url: '/personnel/puser/findPuserInfo?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  });
}

/**
 * 保存教育经历
 * @param {*} params
 * @returns
 */
export function saveEducation(params) {
  return request({
    url: '/personnel/puser/saveEducation',
    method: 'post',
    data: params,
  });
}

/**
 * 保存证件照信息
 * @param {*} params
 * @returns
 */
export function savePhoto(params) {
  return request({
    url: '/personnel/puser/savePhoto',
    method: 'post',
    data: params,
  });
}

/**
 * 保存工作经历
 * @param {*} params
 * @returns
 */
export function saveWorkexper(params) {
  return request({
    url: '/personnel/puser/saveWorkexper',
    method: 'post',
    data: params,
  });
}

/**
 * 保存员工完善信息
 * @param params
 * @returns
 */
export function savePuser(params) {
  return request({
    url: '/personnel/puser/savePuser',
    method: 'post',
    data: params,
  });
}

/**
 * 组织切换
 * @param params
 * @returns
 */
export function updateOrganize(params) {
  return request({
    url: '/personnel/puser/updateOrganize',
    method: 'post',
    data: params,
  });
}

/**
 * 状态修改
 * @param params
 * @returns
 */
export function updatePStatus(params) {
  return request({
    url: '/personnel/puser/updateStatus',
    method: 'post',
    data: params,
  });
}
