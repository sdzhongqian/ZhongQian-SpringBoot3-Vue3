declare namespace API {

  /** 获取数据返回结果 */
  type MenuQueryParams = {
    name: string;
    short_name: string;
    parent_id: string;
    id: string;
    status: number;
    page?: number;
    limit?: number;
  };


  /** 获取数据返回结果 */
  type MenuListResult = {
    id: string;
    type: number;
    parent_name: string;
    name: string;
    path: string;
    redirect: string;
    component: string;
    title: string;
    keepAlive: string | boolean;
    icon: string;
    permission: string;
    parent_id: string;
    level_num: number;
    status: number;
    order_by: number;
    create_time: string;
    child_num: number;
    remark: string;
  };

  /** 删除数据的参数 */
  type MenuDelParams = {
    ids: string | number;
  };

  /** 更新数据的参数 */
  type MenuEditParams = {
    id: string;
    type: number;
    name: string;
    path: string;
    redirect: string;
    component: string;
    title: string;
    keepAlive: string | boolean;
    icon: string;
    permission: string;
    parent_id: string;
    order_by: number;
    remark: string;
    status: number;
  };

  /** 更新数据的参数 */
  type MenuEditStatusParams = {
    id: string;
    status: number;
  };

  /** 详情数据 */
  type MenuInfoResult = {
    id: string;
    type: number;
    parent_name: string;
    name: string;
    path: string;
    redirect: string;
    component: string;
    title: string;
    keepAlive: string | boolean;
    icon: string;
    permission: string;
    parent_id: string;
    level_num: number;
    status: number;
    order_by: number;
    create_time: string;
    child_num: number;
    remark: string;
  }
}