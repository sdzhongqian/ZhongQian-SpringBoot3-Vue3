declare namespace API {

  /** 获取数据返回结果 */
  type OrganizeQueryParams = {
    name: string;
    short_name: string;
    parent_id: string;
    code: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type OrganizeListResult = {
    id: string;
    parent_name: string;
    name: string;
    short_name: string;
    parent_id: string;
    code: string;
    level_num: number;
    status: number;
    order_by: number;
    create_time: string;
    child_num: number;
    remark: string;
  };

  /** 删除部门的参数 */
  type OrganizeDelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type OrganizeEditParams = {
    id: string;
    name: string;
    short_name: string;
    code: string;
    parent_id: string;
    order_by: number;
    remark: string;
    status: number;
  };

  /** 更新某个部门需要传的参数 */
  type OrganizeEditStatusParams = {
    id: string;
    status: number;
  };

  /** 部门详情 */
  type OrganizeInfoResult = {
    id: string;
    parent_name: string;
    name: string;
    short_name: string;
    parent_id: string;
    code: string;
    level_num: number;
    status: number;
    order_by: number;
    create_time: string;
    child_num: number;
    remark: string;
  }
}