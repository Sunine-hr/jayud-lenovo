package com.jayud.crm.feign;


import com.jayud.auth.model.po.SysDepart;
import com.jayud.common.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * customs模块消费file模块的接口
 */
@Component
//@FeignClient(name = "jayud-auth-web" ,path = "/jayudAuth/sysUser")
@FeignClient(name = "jayud-auth-web")
public interface AuthClient {


    @PostMapping("/sysUser/api/selectList")
    public BaseResult selectListFeign();


    //字段表查询
    @PostMapping(value = "/sysDictItem/api/selectItemByDictCode")
    public BaseResult selectItemByDictCodeFeign(@RequestParam("dictCode") String dictCode);


    //自定义单号
    @PostMapping(value = "/bNoRule/api/getOrderFeign")
    public BaseResult getOrderFeign(@RequestParam(name = "code") String code, @RequestParam(name = "date") Date date);

    /**
     * @description 查询法人主体
     **/
    @ApiOperation("查询法人主体")
    @RequestMapping("/getLegalEntity")
    public BaseResult<List<SysDepart>> getLegalEntity();
}
