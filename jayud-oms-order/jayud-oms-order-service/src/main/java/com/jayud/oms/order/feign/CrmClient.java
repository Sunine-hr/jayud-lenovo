package com.jayud.oms.order.feign;


import com.jayud.common.BaseResult;
import com.jayud.oms.order.model.bo.CrmFileForm;
import com.jayud.oms.order.model.bo.QueryCrmFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping(value = "/crmFile/addFile")
    public BaseResult addFile(@RequestBody QueryCrmFile queryCrmFile);

    @ApiOperation("获取附件集合")
    @PostMapping(path = "/crmFile/getFileList")
    public BaseResult<List<CrmFileForm>> getFileList(@RequestParam("business")Long business,@RequestParam("code")String code);

}
