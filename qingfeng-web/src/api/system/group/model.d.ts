declare namespace API {

  /** 获取数据返回结果 */
  type GroupQueryParams = {
    name: string;
    short_name: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type GroupListResult = {
    id: string;
    name: string;
    short_name: string;
    status: string;
    order_by: number;
    create_time: string;
    remark: string;
  };

  /** 删除数据的参数 */
  type GroupDelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type GroupEditParams = {
    id: string;
    name: string;
    short_name: string;
    user_names: string;
    user_ids: string;
    order_by: number;
    remark: string;
    status: number;
  };

  /** 更新状态参数 */
  type GroupEditStatusParams = {
    id: string;
    status: number;
  };

  /** 查询详情 */
  type GroupInfoResult = {
    id: string;
    name: string;
    short_name: string;
    status: number;
    order_by: number;
    create_time: string;
    remark: string;
  }



  type GroupUserQueryParams = {
    user_id: string;
    group_id: string;
  }

  type GroupUserResult = {
    id: string;
    user_id: string;
    group_id: string;
    user_name: string;
    create_time: string;
  }

  type GroupUserEditParams = {
    id: string;
    user_id: string;
    group_id: string;
  };

}