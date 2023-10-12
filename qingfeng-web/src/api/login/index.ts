import type { BaseResponse } from '@/utils/request';
import { request } from '@/utils/request';

/**
 * @description 登录
 * @param {LoginParams} data
 * @returns
 */
export function login(data: API.LoginParams) {
  return request<BaseResponse<API.LoginResult>>(
    {
      url: '/auth/login',
      method: 'post',
      data,
      headers: {
        'Content-Type': "application/x-www-form-urlencoded"
      },
    },
    {
      isGetDataDirectly: false,
    },
  );
}
/**
 * @description 获取验证码
 */
export function getImageCaptcha(params?: API.CaptchaParams) {
  return request<API.CaptchaResult>({
    url: '/auth/captcha',
    method: 'get',
    params,
  });
}

export function getInfo () {
  return request({
    url: '/system/user/findUserInfo',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}


export function logout () {
  return request({
    url: '/auth/logout',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}


export function verifyLogin(data: any) {
  return request(
    {
      url: '/system/user/verifyLogin',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
      },
    }
  );
}