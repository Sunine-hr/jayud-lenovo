package com.jayud.common.fegin;


import com.jayud.common.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * customs模块消费file模块的接口
 */
@Component
@FeignClient(name = "jayud-auth-web")
public interface AuthClient {





    /**
     * @description 根据id查询子节点
     * @author  ciro
     * @date   2021/12/9 10:50
     * @param: id
     * @return: java.util.List<java.util.Map>
     **/
    @PostMapping(value = "/sysDepart/selectChilderListById")
    List<Map> selectChilderListById(@RequestParam(name = "id", required = true) String id);

    /**
     * @description 根据用户查询仓库
     * @author  ciro
     * @date   2022/4/11 9:26
     * @param: userId
     * @return: com.jayud.common.BaseResult<java.util.List<java.util.Map>>
     **/
    @GetMapping(value = "/sysUserToWarehouse/selectList")
    public BaseResult<List<Map>> getWarehouseByUserId(@RequestParam("userId") Long userId);




}
