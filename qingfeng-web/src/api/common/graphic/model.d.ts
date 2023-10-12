declare namespace API {

  /** 获取数据返回结果 */
  type GraphicQueryParams = {
    title: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type GraphicListResult = {
    id: string;
    type: number;
    title: string;
    intro: string;
    tpdz: string;
    publish_user: string;
    publish_time: string;
    read_num: number;
    intro: string;
    status: number;
    order_by: number;
    create_time: string;
    remark: string;
  };

  /** 删除数据的参数 */
  type GraphicDelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type GraphicEditParams = {
    id: string;
    type: number;
    title: string;
    intro: string;
    content: string;
    tpdz: string;
    show_tpdz: string;
    fmAddress: string;
    publish_user: string;
    publish_time: string;
    read_num: number;
    order_by: number;
    remark: string;
    status: number;
  };

  /** 更新状态参数 */
  type GraphicEditStatusParams = {
    id: string;
    status: number;
  };

  /** 查询详情 */
  type GraphicInfoResult = {
    id: string;
    type: number;
    title: string;
    intro: string;
    content: string;
    tpdz: string;
    publish_user: string;
    publish_time: string;
    read_num: number;
    intro: string;
    status: number;
    order_by: number;
    create_time: string;
    remark: string;
  }
}