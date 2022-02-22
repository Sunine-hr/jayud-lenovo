package com.jayud.common.config;

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

        }
        if (username != "") {
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("createBy", username, metaObject);
        }

    }

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
