package com.qingfeng.module.common.utils.sqlutil;

import java.sql.*;
import java.util.List;


/**
 * @name PostgreSQLUtils
 * @description PostgreSQL工具类
 * @author anzi
 * @create 2023/9/8
 **/
public class PostgreSQLUtils {

    private static String CONNECTIONURL = "xsmt"; // 数据库名称
    private static String USER_NAME = "postgres"; // 用户名
    private static String PASS_WORD = "root";     // 密码
    private static Connection c = null;                // 连接对象
    private static Statement stmt = null;                // SQL语句执行
    private ResultSet rs = null;


    public PostgreSQLUtils(String ConnectionURL, String username, String password){
        this.CONNECTIONURL = ConnectionURL;
        this.USER_NAME = username;
        this.PASS_WORD = password;
    }

    /**
     * @name getConnection
     * @description 获取连接
     * @author anzi
     * @create 2023/9/8 19:06
     **/
    public static boolean getConnection() {
        boolean bol = true;
        try {
            Class.forName("org.postgresql.Driver");
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+DATABASE_NAME,USER_NAME,PASS_WORD);
            c = DriverManager
                    .getConnection(CONNECTIONURL, USER_NAME, PASS_WORD);
        } catch (SQLException e) {
            bol = false;
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            bol = false;
            e1.printStackTrace();
        }
        return bol;
    }

    /**
     * @name closeConnection
     * @description 关闭连接
     * @author anzi
     * @create 2023/9/8 19:06
     **/
    public static void closeConnection() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @name select
     * @description 执行查询
     * @author anzi
     * @create 2023/9/8 19:06
     **/
    public ResultSet select(String sql) {
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return rs;
    }

    public int getRecordCount(String sql) {
        int counter = 0;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return counter;
    }



    /**
     * 功能:针对单条记录执行更新操作(新增、修改、删除)
     * @param sql 传入创建表的sql语句
     * @return
     */
    public static boolean executeSql(String sql) {
        boolean flag = true;
        try {
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        }
        closeConnection();
        return flag;
    }

    /**
     * @title executeBatch
     * @description 批量执行SQL
     * @author Administrator
     * @updateTime 2021/4/19 0019 16:41
     */
    public static boolean executeBatchSql(List<String> sqlList) {
        boolean flag = true;
        try {
            stmt = c.createStatement();
            for (String sql : sqlList) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        }
        closeConnection();
        return flag;
    }


// 	<!-- https://mvnrepository.com/artifact/org.lucee/postgresql -->
//	<dependency>
//	    <groupId>org.lucee</groupId>
//	    <artifactId>postgresql</artifactId>
//	    <version>8.3-606.jdbc4</version>
//	</dependency>
}