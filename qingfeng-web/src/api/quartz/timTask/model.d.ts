declare namespace API {

  /** 获取数据返回结果 */
  type TimTaskQueryParams = {
    job_name: string;
    job_group: string;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type TimTaskListResult = {
    job_name: string;
    job_group: string;
    description: string;
    job_class_name: string;
    cron_expression: string;
    trigger_name: string;
    trigger_state: string;
    old_job_name: string;
    old_job_group: string;
    start_time:string;
  };

  /** 更新某个部门需要传的参数 */
  type TimTaskEditParams = {
    job_name: string;
    job_group: string;
    description: string;
    job_class_name: string;
    cron_expression: string;
    trigger_name: string;
    trigger_state: string;
    old_job_name: string;
    old_job_group: string;
    start_time:string;
  };


}