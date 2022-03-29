package com.jayud.oms.order.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.BaseResult;
import com.jayud.oms.order.model.bo.CrmFileForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms订单模块消费客户模块的接口
 */
@FeignClient(value = "jayud-crm-web")
public interface CrmClient {

    /**
     * 新增附件
     */
    @RequestMapping(value = "/crmFile/addFile")
    public BaseResult addFile(@RequestParam("crmFiles") List<CrmFileForm> crmFiles, @RequestParam("business")Long business, @RequestParam("code")String code);


}
