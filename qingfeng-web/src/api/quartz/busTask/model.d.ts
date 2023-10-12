declare namespace API {

  /** 获取数据返回结果 */
  type BusTaskQueryParams = {
    job_name: string;
    job_group: string;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type BusTaskListResult = {
    id: string;
    type: number;
    job_name: string;
    job_group: string;
    notice_user: string;
    job_class_name: string;
    cron_expression: string;
    trigger_time: string;
    trigger_state: string;
    order_by: number;
    create_time: string;
    remark: string;
  };

  /** 删除数据的参数 */
  type BusTaskDelParams = {
    ids: string | number;
  };

  /** 更新数据的参数 */
  type BusTaskEditParams = {
    id: string;
    type: number;
    job_name: string;
    job_group: string;
    notice_user: string;
    job_class_name: string;
    cron_expression: string;
    trigger_time: string;
    trigger_state: string;
    description: string;
    order_by: number;
    create_time: string;
    remark: string;
  };

  type BusTaskStatusParams = {
    id: string;
    job_name: string;
    job_group: string;
    trigger_time: string;
  };

  /** 详情数据 */
  type BusTaskInfoResult = {
    id: string;
    type: number;
    job_name: string;
    job_group: string;
    notice_user: string;
    job_class_name: string;
    cron_expression: string;
    trigger_time: string;
    trigger_state: string;
    description: string;
    order_by: number;
    create_time: string;
    remark: string;
  }
}