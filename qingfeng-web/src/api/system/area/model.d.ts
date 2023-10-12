declare namespace API {

  /** 获取数据返回结果 */
  type AreaQueryParams = {
    name: string;
    short_name: string;
    parent_id: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type AreaListResult = {
    id: string;
    parent_name: string;
    name: string;
    short_name: string;
    lng: string;
    lat: string;
    parent_id: string;
    level_num: number;
    status: number;
    order_by: number;
    create_time: string;
    child_num: number;
    remark: string;
  };

  /** 删除数据的参数 */
  type AreaDelParams = {
    ids: string | number;
  };

  /** 更新数据的参数 */
  type AreaEditParams = {
    id: string;
    name: string;
    short_name: string;
    lng: string;
    lat: string;
    parent_id: string;
    order_by: number;
    remark: string;
    status: number;
  };

  /** 更新数据的参数 */
  type AreaEditStatusParams = {
    id: string;
    status: number;
  };

  /** 详情数据 */
  type AreaInfoResult = {
    id: string;
    parent_name: string;
    name: string;
    short_name: string;
    lng: string;
    lat: string;
    parent_id: string;
    level_num: number;
    status: number;
    order_by: number;
    create_time: string;
    child_num: number;
    remark: string;
  }
}