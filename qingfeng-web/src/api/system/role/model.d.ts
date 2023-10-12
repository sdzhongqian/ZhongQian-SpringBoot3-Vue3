declare namespace API {

  /** 获取数据返回结果 */
  type RoleQueryParams = {
    name: string;
    short_name: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type RoleListResult = {
    id: string;
    name: string;
    short_name: string;
    status: number;
    order_by: number;
    create_time: string;
    remark: string;
  };

  /** 删除数据的参数 */
  type RoleDelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type RoleEditParams = {
    id: string;
    name: string;
    short_name: string;
    menus:Array;
    order_by: number;
    remark: string;
    status: number;
  };

  /** 更新状态参数 */
  type RoleEditStatusParams = {
    id: string;
    status: number;
  };

  /** 查询详情 */
  type RoleInfoResult = {
    id: string;
    name: string;
    short_name: string;
    status: number;
    menus:Array;
    order_by: number;
    create_time: string;
    remark: string;
  }
}