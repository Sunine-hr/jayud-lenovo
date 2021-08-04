package com.jayud.oauth.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.config.FeignRequestInterceptor;
import com.jayud.oauth.model.po.SystemMenu;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * tms模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web", configuration = FeignRequestInterceptor.class)
public interface OmsClient {

    @ApiModelProperty(value = "获取菜单待处理数")
    @RequestMapping(value = "/api/getMenuPendingNum")
    public ApiResult getMenuPendingNum(@RequestBody List<SystemMenu> menusList);


    /**
     * 客户管理菜单待处理数量
     *
     * @param menusList
     * @return
     */
    @RequestMapping(value = "/api/getCustomerMenuPendingNum")
    public ApiResult getCustomerMenuPendingNum(@RequestBody List<SystemMenu> menusList);

    /**
     * 供应商管理菜单待处理数量
     *
     * @param menusList
     * @return
     */
    @RequestMapping(value = "/api/getSupplierMenuPendingNum")
    public ApiResult getSupplierMenuPendingNum(@RequestBody List<SystemMenu> menusList);

    @ApiOperation(value = "查询字典")
    @PostMapping(value = "/api/findDict")
    public ApiResult<List<Object>> findDictType(@RequestParam("dictTypeCode") String dictTypeCode);

}
