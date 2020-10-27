package com.jayud.tms.feign;


import com.jayud.common.ApiResult;
import com.jayud.tms.model.bo.AuditInfoForm;
import com.jayud.tms.model.bo.HandleSubProcessForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.vo.InitComboxVO;
import com.jayud.tms.model.vo.OrderStatusVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 删除前面操作成功的记录
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/api/delOprStatus")
    ApiResult delOprStatus(@RequestParam("orderId") Long orderId);

    /**
     * 子订单流程
     * @return
     */
    @RequestMapping(value = "/api/handleSubProcess")
    ApiResult<List<OrderStatusVO>> handleSubProcess(@RequestBody HandleSubProcessForm form);

}
