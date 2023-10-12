package com.qingfeng.module.common.utils.sqlutil;


import java.sql.*;

/**
 * @name OracleUtil
 * @description Oracle链接工具类
 * @author anzi
 * @create 2023/9/8
 **/
public class OracleUtil {

    private static String dbDriver = "oracle.jdbc.driver.OracleDriver";
    private String dbConnectionURL = "";
    private String dbUsername = "";//oracle数据库的用户名
    private String dbPassword = "";//oracle数据库的用户密码
    private PreparedStatement sta = null;
    private ResultSet rs = null;
    private Connection conn = null;


    public OracleUtil(String dbConnectionURL, String dbUsername, String dbPassword){
        this.dbConnectionURL = dbConnectionURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    /**
     * @name getConnection
     * @description 连接对象
     * @author anzi
     * @create 2023/9/8 19:05
     **/
    public boolean getConnection() {
        boolean bol = true;
        try {
            //加载驱动程序
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbConnectionURL, dbUsername, dbPassword);
        } catch (Exception e) {
            bol = false;
            e.printStackTrace();
        }
        return bol;
    }

    /**
     * @name update
     * @description sql语句  增加，删除，修改
     * @author anzi
     * @create 2023/9/8 19:05
     **/
    public int update(String sql, Object... obj) {
        int count = 0;
        try {
            sta = conn.prepareStatement(sql);
            if (obj != null) {
                for (int i = 0; i < obj.length; i++) {
                    sta.setObject(i + 1, obj[i]);
                }
            }
            count = sta.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close();
        }
        return count;
    }

    /**
     * @name select
     * @description sql语句
     * @author anzi
     * @create 2023/9/8 19:05
     **/
    public ResultSet select(String sql,Object...obj){
        try {
            sta=conn.prepareStatement(sql);
            if(obj!=null){
                for(int i=0;i<obj.length;i++){
                    sta.setObject(i+1, obj[i]);
                }
            }
            rs=sta.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * @name close
     * @description 关闭资源
     * @author anzi
     * @create 2023/9/8 19:05
     **/
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sta != null) {
                    sta.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
