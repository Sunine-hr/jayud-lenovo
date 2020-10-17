package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import com.jayud.tms.model.bo.AuditInfoForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.vo.InitComboxVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * tms模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {

    /**
     * 只记录成功操作流程状态
     */
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form);

    /**
     * 记录审核信息
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/saveAuditInfo")
    ApiResult saveAuditInfo(@RequestBody AuditInfoForm form);

    /**
     * 初始化车辆供应商
     * @return
     */
    @RequestMapping(value = "/api/initSupplierInfo")
    ApiResult<List<InitComboxVO>> initSupplierInfo();

    /**
     * 初始化中转仓库
     * @return
     */
    @RequestMapping(value = "/api/initWarehouseInfo")
    ApiResult<List<InitComboxVO>> initWarehouseInfo();


}
