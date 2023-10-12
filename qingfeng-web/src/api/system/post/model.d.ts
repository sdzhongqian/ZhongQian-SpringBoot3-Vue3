declare namespace API {

  /** 获取数据返回结果 */
  type PostQueryParams = {
    name: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type PostListResult = {
    id: string;
    type: number;
    name: string;
    organize_id: string;
    leader_id: string;
    leader_name: string;
    nature: string;
    content: string;
    order_by: string;
    remark: string;
    status: number;
    order_by: number;
    create_time: string;
  };

  /** 删除数据的参数 */
  type PostDelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type PostEditParams = {
    id: string;
    name: string;
    organize_id: string;
    leader_id: string;
    leader_name: string;
    nature: string;
    content: string;
    order_by: string;
    remark: string;
    order_by: number;
    status: number;
  };

  /** 更新状态参数 */
  type PostEditStatusParams = {
    id: string;
    status: number;
  };

  /** 查询详情 */
  type PostInfoResult = {
    id: string;
    id: string;
    type: string;
    name: string;
    organize_id: string;
    leader_id: string;
    leader_name: string;
    nature: string;
    content: string;
    status: string;
    order_by: string;
    remark: string;
    create_user: string;
    create_organize: string;
    create_time: string;
    update_time: string;
    update_user: string;
    status: number;
    order_by: number;
    create_time: string;
  }

}