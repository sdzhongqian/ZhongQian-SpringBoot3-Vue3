declare namespace API {

  /** 获取数据返回结果 */
  type UserReserOrgParams = {
    organize_id: string;
    reason: string;
    message: string;
  };
 
  type PUserEditParams = {
    job_num: string;
    job_date: string;
    job_type: number;
    job_status: number;
    education: string;
    marriage: string;
    nation: string;
    politics_face: string;
    party_time: string;
    basic_salary: string;
    post_salary: string;
    merit_salary: string;
  };
  


}



