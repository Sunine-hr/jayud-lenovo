package com.jayud.auth.feign;

import com.jayud.common.BaseResult;
import com.jayud.wms.model.po.Warehouse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ciro
 * @date 2022/4/9 13:59
 * @description: wms模块
 */
@Component
@FeignClient(name = "jayud-wms-web")
public interface WmsClient {

    @PostMapping("/warehouse/selectList")
    public BaseResult<List<Warehouse>> selectWarehouseList(@RequestParam("tenantCode") String tenantCode, @RequestParam("status") Integer status);

}
