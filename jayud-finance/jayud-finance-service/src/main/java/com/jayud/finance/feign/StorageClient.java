package com.jayud.finance.feign;

import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.SubOrderCloseOpt;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费storage模块的接口
 */
@FeignClient(value = "jayud-storage-web")
public interface StorageClient {

    @ApiOperation(value = "根据id查询仓库名称")
    @RequestMapping("/api/storage/getFastOrderByMainOrder")
    public CommonResult getFastOrderByMainOrder(@RequestParam("mainOrderNos") List<String> mainOrderNos);
}
