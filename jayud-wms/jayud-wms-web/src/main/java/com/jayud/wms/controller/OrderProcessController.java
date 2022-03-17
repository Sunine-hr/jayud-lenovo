package com.jayud.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.wms.model.po.OrderProcess;
import com.jayud.wms.service.IOrderProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单流程 控制类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Api(tags = "订单流程")
@RestController
@RequestMapping("/orderProcess")
public class OrderProcessController {

    @Autowired
    public IOrderProcessService orderProcessService;



    /**
     * 分页查询数据
     *
     * @param orderProcess   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<OrderProcess>> selectPage(OrderProcess orderProcess,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(orderProcessService.selectPage(orderProcess,pageNo,pageSize,req));
    }

    /**
     * 列表查询数据
     *
     * @param orderProcess   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<OrderProcess>> selectList(OrderProcess orderProcess,
                                                HttpServletRequest req) {

        return BaseResult.ok(orderProcessService.selectList(orderProcess));
    }

    /**
    * 新增
    * @param orderProcess
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody OrderProcess orderProcess ){
        orderProcessService.save(orderProcess);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param orderProcess
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody OrderProcess orderProcess ){
        orderProcessService.updateById(orderProcess);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 删除
     * @param id
     **/
    @ApiOperation("删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        orderProcessService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<OrderProcess> queryById(@RequestParam(name="id",required=true) int id) {
        OrderProcess orderProcess = orderProcessService.getById(id);
        return BaseResult.ok(orderProcess);
    }

}
