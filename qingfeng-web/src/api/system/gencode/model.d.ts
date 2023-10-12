declare namespace API {

  /** 获取数据返回结果 */
  type GencodeTableQueryParams = {
    table_name: string;
    table_comment: string;
    start_time: string;
    end_time: string;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type GencodeTableListResult = {
    key: string;
    table_name: string;
    table_comment: string;
    create_time: string;
    update_time: string;
  };

}