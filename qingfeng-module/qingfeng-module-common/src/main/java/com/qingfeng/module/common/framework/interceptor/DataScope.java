package com.qingfeng.module.common.framework.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @name DataScope
 * @description 数据范围
 * @author anzi
 * @create 2023/9/13
 **/
@Data
@AllArgsConstructor
public class DataScope {
    private String sqlFilter;

}