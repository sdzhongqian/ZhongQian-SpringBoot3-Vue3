package com.qingfeng.module.common.utils.sqlutil;

import com.qingfeng.module.common.utils.Verify;

import java.sql.*;
import java.util.List;

/**
 * @name MysqlUtil
 * @description mysql工具类
 * @author anzi
 * @create 2023/9/8
 **/
public class MysqlUtil {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String dbDriver = "com.mysql.cj.jdbc.Driver";
    private String dbConnectionURL = null;
    private String dbUsername = null;
    private String dbPassword = null;

    public MysqlUtil(String dbConnectionURL, String dbUsername, String dbPassword,String dbDriver){
        this.dbConnectionURL = dbConnectionURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbDriver = dbDriver;
    }

    /**
     * @name getConnection
     * @description 功能：获取数据库连接
     * @author anzi
     * @create 2023/9/8 19:04
     **/
    public boolean getConnection() {
        boolean bol = true;
        try {
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbConnectionURL, dbUsername,
                    dbPassword);
        } catch (Exception e) {
            bol = false;
            e.printStackTrace();
        }
        return bol;
    }

    /**
     * @name select
     * @description 功能：执行查询语句
     * @author anzi
     * @create 2023/9/8 19:04
     **/
    public ResultSet select(String sql) {
        if(Verify.verifyIsNull(conn)){
            getConnection();
        }
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;

    }

    /**
     * @name getRecordCount
     * @description 功能：执行查询语句，获取记录数
     * @author anzi
     * @create 2023/9/8 19:04
     **/
    public int getRecordCount(String sql) {
        int counter = 0;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            while (rs.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close();
        }
        System.out.println("counter总数："+counter);
        return counter;
    }

    /**
     * @name executeupdate
     * @description 功能:针对单条记录执行更新操作(新增、修改、删除)
     * @author anzi
     * @create 2023/9/8 19:03
     **/
    public boolean executeupdate(String sql) throws Exception {
        boolean bol = true;
        try {
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            bol = false;
            sqle.printStackTrace();
        } finally {
            close();
        }
        return bol;
    }

    /**
     * 功能:批量执行SQL(update或delete)
     * @param sqlList
     * ql语句集合
     */
    public boolean executeBatch(List<String> sqlList) {
        boolean bol = true;
        for (String sql : sqlList) {
            try {
                executeupdate(sql);
            } catch (Exception e) {
                bol = false;
                e.printStackTrace();
            }
        }
        return bol;
    }

    /**
     * 功能:关闭数据库的连接
     */
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}