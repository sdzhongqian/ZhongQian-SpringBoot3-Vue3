import { request } from '@/utils/request'
import querystring from 'querystring'
import md5 from 'blueimp-md5'

/**
 * 查询分页列表
 * @param {*} params 
 * @returns 
 */
export function getListPage(params: API.UserQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.UserListResult>({
    url: '/system/user/listPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 保存或更新
 * @param {*} params 
 * @returns 
 */
export function saveOrUpdate(params: API.UserEditParams) {
  let url = '/system/user';
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

/**
 * 删除数据
 * @param {*} ids 
 * @returns 
 */
export function delData(data: API.UserDelParams) {
  return request({
    url: '/system/user/' + data.ids,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 更新状态
 * @param {*} id 
 * @param {*} status 
 * @returns 
 */
export function updateStatus(data: API.UserEditStatusParams) {
  return request({
    url: '/system/user/updateStatus',
    method: 'post',
    data: data
  })
}

/**
 * 获取用户列表
 * @param {*} params 
 * @returns 
 */
export function getList(params: API.UserQueryParams) {
  let queryString = querystring.stringify(params);
  return request<API.UserQueryParams>({
    url: '/system/user/list?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 密码更新
 * @param {*} params 
 * @returns 
 */
export function updatePwd(params: API.UserEditPwdParams) {
  params.login_password = md5(params.login_password)
  params.old_password = md5(params.old_password)
  params.confirm_password = md5(params.confirm_password)
  let queryString = querystring.stringify(params);
  return request({
    url: '/system/user/updatePwd?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 获取用户组织信息分页列表
 * @param {*} params 
 * @returns 
 */
export function getMyOrganizeListPage(params) {
  let queryString = querystring.stringify(params);
  return request({
    url: '/system/user/findUserOrganizeListPage?' + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 保存或更新用户组织信息
 * @param {*} params 
 * @returns 
 */
export function saveOrUpdateUserOrganize(params) {
  return request({
    url: '/system/user/saveOrUpdateUserOrganize',
    method: 'post',
    data: params
  })
}

/**
 * 删除用户组织信息
 * @param {*} id 
 * @returns 
 */
export function delUserOrganize(id) {
  return request({
    url: `/system/user/delUserOrganize/` + id,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 获取用户权限信息
 * @param {*} params 
 * @returns 
 */
export function findRoleAuth(params) {
  return request({
    url: '/system/user/findRoleAuth',
    method: 'post',
    data: params
  })
}

/**
 * 获取组织权限信息
 * @param {*} params 
 * @returns 
 */
export function findOrganizeAuth(params) {
  return request({
    url: '/system/user/findOrganizeAuth',
    method: 'post',
    data: params
  })
}

/**
 * 更新权限信息
 * @param {*} params 
 * @returns 
 */
export function updateAuth(params) {
  return request({
    url: '/system/user/updateAuth',
    method: 'post',
    data: params
  })
}

/**
 * 获取用户详情
 * @param {*} id 
 * @returns 
 */
export function getUserInfo(id) {
  return request({
    url: `/system/user/info?id=${id}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function getSelectData(params) {
  let queryString = querystring.stringify(params);
  return request({
    url: `/system/user/selectData?` + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}


/**
 * updateUser更新用户
 * @param params 
 * @returns 
 */
export function updateMyUser (params) {
  return request({
    url: '/system/user/updateUser',
    method: 'post',
    data: params,
  })
}

/**
 * updatePwd更新密码
 * @param params 
 * @returns 
 */
export function updateMyPwd (params: API.UserEditPwdParams) {
  params.login_password = md5(params.login_password)
  params.old_password = md5(params.old_password)
  params.confirm_password = md5(params.confirm_password)
  return request({
    url: '/system/user/updateMyPwd',
    method: 'post',
    data: params,
  })
}

/**
 * updateSwitchOrganize更新组织切换
 * @param parameter 
 * @returns 
 */
export function updateSwitchOrganize (parameter) {
  return request({
    url: '/system/user/updateSwitchOrganize',
    method: 'post',
    data: parameter,
  })
}


export function findLoginUser(params) {
  let queryString = querystring.stringify(params);
  return request({
    url: `/system/user/findLoginUser?` + queryString,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 导入用户信息
 */
export function saveImportExcel (params) {
  return request({
    url: '/system/user/saveImportExcel',
    method: 'post',
    data: params
  })
}
