declare namespace API {

  /** 获取数据返回结果 */
  type UserQueryParams = {
    name: string;
    login_name: string;
    organize_id: string;
    phone: string;
    email: string;
    idcard: string;
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type UserListResult = {
    id: string;
    type: number;
    organize_id: string;
    login_name: string;
    name: string;
    sex: string;
    idcard: string;
    phone: string;
    email: string;
    post_ids: string;
    position: string;
    birth_date: string;
    live_address: string;
    birth_address: string;
    head_address: string;
    motto: string;
    init_password: string;
    pwd_error_num: string;
    pwd_error_time: string;
    last_login_time: string;
    status: number;
    order_by: number;
    create_time: string;
    remark: string;
  };

  /** 删除数据的参数 */
  type UserDelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type UserEditParams = {
    id: string;
    type: number;
    organize_id: string;
    login_name: string;
    name: string;
    sex: number;
    idcard: string;
    phone: string;
    email: string;
    post_ids: Array;
    position: string;
    birth_date: string;
    live_address: string;
    birth_address: string;
    motto: string;
    order_by: number;
    remark: string;
    headAddress: string;
    headImg: string;
    fileIds: string;
    status: number;
    divider: string;
  };

  /** 更新状态参数 */
  type UserEditStatusParams = {
    id: string;
    status: number;
  };

  /** 查询详情 */
  type UserInfoResult = {
    id: string;
    type: number;
    login_name: string;
    name: string;
    sex: string;
    idcard: string;
    phone: string;
    email: string;
    birth_date: string;
    live_address: string;
    birth_address: string;
    head_address: string;
    motto: string;
    init_password: string;
    pwd_error_num: string;
    pwd_error_time: string;
    last_login_time: string;
    status: number;
    order_by: number;
    create_time: string;
    remark: string;
  }


  /** 更新密码 */
  type UserEditPwdParams = {
    ids: string;
    old_password: string;
    login_password: string;
    confirm_password: string;
  };


  /** 用户组织管理 */
  type UserOrganizeParams = {
    id: string;
    type: string;
    organize_id: string;
    organize_name: string;
    position: string;
    order_by: string;
  }; 



  type EducationEditParams = {
    id: string;
    educationList:string
  };

}



