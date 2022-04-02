package com.jayud.wms.fegin;



import com.jayud.common.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


/**
 * customs模块消费file模块的接口
 */
@Component
@FeignClient(name = "jayud-crm-web")
public interface CrmClient {


    @PostMapping("/crmCustomer/api/selectListFeign")
    public BaseResult  selectListFeign();
}
