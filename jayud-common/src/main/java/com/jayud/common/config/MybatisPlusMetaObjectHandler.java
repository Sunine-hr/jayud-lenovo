package com.jayud.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.jayud.common.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ciro
 * @date 2021/12/16 11:45
 * @description: mybatisplus自动填充
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        String username = "";
        try {
            username = CurrentUserUtil.getUsername();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (username != "") {
            if(metaObject.hasGetter("tenantCode")){
                this.setFieldValByName("tenantCode", CurrentUserUtil.getUserTenantCode(), metaObject);
            }
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy", username, metaObject);
        }

    }

//    /**
//     * 获取元数据里的动态表名
//     * @param metaObject 元数据对象
//     * @return 表名
//     */
//    private String getDynamicTableName(MetaObject metaObject){
//        Object originalObject = metaObject.getOriginalObject();
//        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(originalObject));
//        JSONObject boundSql = jsonObject.getJSONObject("boundSql");
//        JSONObject parameterObject = boundSql.getJSONObject("parameterObject");
//        return String.valueOf(parameterObject.get(Dynamic_Table_Name));
//    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String username = "";
        try {
            username = CurrentUserUtil.getUsername();
        } catch (Exception e) {

        }
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", username, metaObject);
    }
}
