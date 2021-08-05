package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.SystemConfTypeEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.SystemConf;
import com.jayud.oms.service.ISystemConfService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息系统配置 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/systemConf")
public class SystemConfController {

    @Autowired
    private ISystemConfService systemConfService;

    @ApiOperation("添加配置")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody Map<String, Object> map) {
        if (map == null) {
            return CommonResult.error(400, "请配置属性");
        }
        for (Object value : map.values()) {
            if (StringUtils.isEmpty(value.toString())) {
                return CommonResult.error(400, "请配置属性");
            }
        }
        Integer type = MapUtil.getInt(map, "type");
        Integer id = MapUtil.getInt(map, "id");
        SystemConf systemConf = new SystemConf();
        systemConf.setId(id);
        if (systemConf.getId() == null) {
            systemConf.setCreateTime(LocalDateTime.now());
            systemConf.setCreateUser(UserOperator.getToken());
        } else {
            systemConf.setUpdateTime(LocalDateTime.now());
            systemConf.setUpdateUser(UserOperator.getToken());
        }
        systemConf.setConfData(new JSONObject(map).toString());
        systemConf.setType(type);
        this.systemConfService.saveOrUpdate(systemConf);
        return CommonResult.success();
    }


    @ApiOperation("所有配置")
    @PostMapping("/list")
    public CommonResult list(@RequestBody Map<String, Object> map) {
        List<SystemConf> list = this.systemConfService.list();
        JSONObject tmp = new JSONObject();
        list.forEach(e -> {
            JSONObject jsonObject = new JSONObject(e);
            JSONObject confData = jsonObject.getJSONObject("confData");
            confData.put("id", e.getId());
            tmp.put(SystemConfTypeEnum.getEnum(e.getType()).getKey(), confData);
        });
        return CommonResult.success(tmp);
    }
}


