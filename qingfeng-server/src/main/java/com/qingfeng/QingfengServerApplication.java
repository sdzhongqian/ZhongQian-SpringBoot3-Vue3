package com.qingfeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan({"com.qingfeng.*.mapper","com.qingfeng.*.*.mapper"})
@SpringBootApplication
@ServletComponentScan
public class QingfengServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingfengServerApplication.class, args);
    }

}
