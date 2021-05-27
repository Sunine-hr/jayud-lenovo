package com.jayud.storage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.storage.model.bo.QueryOrderStorageForm;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.vo.StorageOrderVO;
import com.jayud.storage.service.IStorageOrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
@RestController
@RequestMapping("/storageOrder")
public class StorageOrderController {

    @Autowired
    private IStorageOrderService storageOrderService;

    @ApiOperation(value = "分页查询存仓订单数据")
    @PostMapping("/findByPage")
    public CommonResult<IPage<StorageOrderVO>> findByPage(@RequestBody QueryOrderStorageForm form){

        IPage<StorageOrderVO> page = this.storageOrderService.findByPage(form);
        return CommonResult.success(page);
    }

}

