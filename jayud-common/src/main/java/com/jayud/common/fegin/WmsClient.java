package com.jayud.common.fegin;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * customs模块消费file模块的接口
 */
@Component
@FeignClient(name = "jayud-wms-web")
public interface WmsClient {


    /**
     * @description 根据用户id查询货主id
     * @author  ciro
     * @date   2021/12/8 17:15
     * @param: userId
     * @return: java.util.List<java.lang.String>
     **/
    @GetMapping(value = "/sysuserOwerpermission/getOwerIdByUserId")
    List<String> getOwerIdByUserId(@RequestParam(name = "userId", required = false) String userId);

    /**
    * @description 根据用户id查询仓库
    * @author  ciro
    * @date   2022/3/16 17:39:07
    * @param userId
    * @return: java.util.List<java.lang.String>
    **/
    @GetMapping(value = "/sysuserOwerpermission/getWarehouseIdByUserId")
    List<String> getWarehouseIdByUserId(@RequestParam(name = "userId", required = false) String userId);


}
