package com.qingfeng.module.common.utils.sqlutil;

import java.sql.*;

/**
 * @name SqlserverUtil
 * @description Sqlserver链接工具类
 * @author anzi
 * @create 2023/9/8
 **/
public class SqlserverUtil {

    private String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// 驱动类类名
    //  jdbc:sqlserver://localhost:1433;DatabaseName=school","",""
    private String dbConnectionURL = "";// 连接URL
    private String dbUsername = "";// 数据库用户名
    private String dbPassword = "";// 数据库密码
    private static Connection conn = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    public SqlserverUtil(String dbConnectionURL, String dbUsername, String dbPassword){
        this.dbConnectionURL = dbConnectionURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    /**
     * @name getConnection
     * @description 获取连接
     * @author anzi
     * @create 2023/9/8 19:06
     **/
    public boolean getConnection() {
        boolean bol = true;
        try {
            Class.forName(dbDriver);// 注册驱动
            conn = DriverManager.getConnection(dbConnectionURL,dbUsername,
                    dbPassword);// 获得连接对象
            System.out.println("成功加载SQL Server驱动程序");
        } catch (ClassNotFoundException e) {// 捕获驱动类无法找到异常
            bol = false;
            System.out.println("找不到SQL Server驱动程序");
            e.printStackTrace();
        } catch (SQLException e) {// 捕获SQL异常
            bol = false;
            e.printStackTrace();
        }
        return bol;
    }


    /**
     * @name select
     * @description 执行sql查询
     * @author anzi
     * @create 2023/9/8 19:07
     **/
    public ResultSet select(String sql) {
        try {
            ps = (PreparedStatement) conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * @name updata
     * @description 增删改均调用这个方法
     * @author anzi
     * @create 2023/9/8 19:07
     **/
    public void updata(String sql) throws Exception {
        try {
            ps = (PreparedStatement) conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new SQLException("insert data Exception: "
                    + sqle.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                throw new Exception("ps close exception: " + e.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                throw new Exception("conn close exception: " + e.getMessage());
            }
        }
    }

    /**
     * @name close
     * @description 关闭连接
     * @author anzi
     * @create 2023/9/8 19:07
     **/
    public void close(){
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
