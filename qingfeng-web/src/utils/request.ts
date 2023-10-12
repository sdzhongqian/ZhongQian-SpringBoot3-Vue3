import axios from 'axios';
import { message as $message } from 'ant-design-vue';
import type { AxiosRequestConfig } from 'axios';
import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
import { Storage } from '@/utils/Storage';
import { useUserStore } from '@/store/modules/user';
import { uniqueSlash } from '@/utils/urlUtils';
import { notification } from 'ant-design-vue'

export interface RequestOptions {
  /** 当前接口权限, 不需要鉴权的接口请忽略， 格式：sys:user:add */
  permCode?: string;
  /** 是否直接获取data，而忽略message等 */
  isGetDataDirectly?: boolean;
  /** 请求成功是提示信息 */
  successMsg?: string;
  /** 请求失败是提示信息 */
  errorMsg?: string;
  /** 是否mock数据请求 */
  isMock?: boolean;
}

const UNKNOWN_ERROR = '未知错误，请重试';

/** 真实请求的路径前缀 */
const baseApiUrl = import.meta.env.VITE_BASE_API;
/** mock请求路径前缀 */
const baseMockUrl = import.meta.env.VITE_MOCK_API;

const service = axios.create({
  // baseURL: baseApiUrl,
  timeout: 6000,
});

service.interceptors.request.use(
  (config) => {
    const token = Storage.get(ACCESS_TOKEN_KEY);
    if (token && config.headers) {
      // 请求头token信息，请根据实际情况进行修改
      config.headers['Authorization'] = "Bearer " + token;
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  },
);

service.interceptors.response.use((response) => {
  return response
}, (response) => {
  if (response) {
    console.log('---------------------')
    console.log(response)
    const resp = response.response;
    let errorMessage = resp.data.msg === null ? '系统内部异常，请联系网站管理员' : resp.data.msg
    switch (resp.data.code) {
      case 404:
        errorMessage = resp.data.msg === null ? '很抱歉，资源未找到' : resp.data.msg
        notification.error({
          message: '异常：',
          description: errorMessage
        })
        break
      case 403:
        errorMessage = resp.data.msg === null ? '很抱歉，您暂无该操作权限' : resp.data.msg
        notification.error({
          message: '异常：',
          description: errorMessage
        })
        break
      case 401:
        errorMessage = resp.data.msg === null ? '很抱歉，认证已失效，请重新登录' : resp.data.msg
        notification.error({
          message: '异常：',
          description: errorMessage
        })
        Storage.remove(ACCESS_TOKEN_KEY)
        break
      default:
        if (errorMessage === 'refresh token无效') {
          // Modal.confirm({
          //   title: "提示",
          //   content: "登录已过期，请重新登录', '温馨提示",
          //   onOk: () => {
          //     router.push('/login')
          //   }
          // })
        } else {
          notification.error({
            message: errorMessage,
            description: errorMessage
          })
        }
        return Promise.reject(resp.data);
        break
    }
  }
  return Promise.reject()
})

export type Response<T = any> = {
  code: number;
  message: string;
  data: T;
};

export type BaseResponse<T = any> = Promise<Response<T>>;

/**
 *
 * @param method - request methods
 * @param url - request url
 * @param data - request data or params
 */
export const request = async <T = any>(
  config: AxiosRequestConfig,
  options: RequestOptions = {},
): Promise<T> => {
  try {
    const { successMsg, errorMsg, permCode, isMock, isGetDataDirectly = true } = options;
    // 如果当前是需要鉴权的接口 并且没有权限的话 则终止请求发起
    if (permCode && !useUserStore().perms.includes(permCode)) {
      return $message.error('你没有访问该接口的权限，请联系管理员！');
    }
    const fullUrl = `${(isMock ? baseMockUrl : baseApiUrl) + config.url}`;
    config.url = uniqueSlash(fullUrl);

    const res = await service.request(config);
    successMsg && $message.success(successMsg);
    errorMsg && $message.error(errorMsg);
    return isGetDataDirectly ? res.data : res;
  } catch (error: any) {
    return Promise.reject(error);
  }
};
