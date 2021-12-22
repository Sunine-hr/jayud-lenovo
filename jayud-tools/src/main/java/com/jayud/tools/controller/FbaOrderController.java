package com.jayud.tools.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.tools.model.bo.AddFbaOrderForm;
import com.jayud.tools.model.bo.AddFbaOrderTrackForm;
import com.jayud.tools.model.bo.DeleteForm;
import com.jayud.tools.model.bo.QueryFbaOrderForm;
import com.jayud.tools.model.po.FbaOrder;
import com.jayud.tools.model.vo.FbaOrderVO;
import com.jayud.tools.service.IFbaOrderService;
import com.jayud.tools.service.IFbaOrderTrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * FBA订单 前端控制器
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/fbaOrder")
@Api(tags = "FBA订单")
public class FbaOrderController {

    @Autowired
    private IFbaOrderService fbaOrderService;

    @Autowired
    private IFbaOrderTrackService fbaOrderTrackService;

    /**
     * 分页查询数据
     *
     * @param queryFbaOrderForm   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public CommonResult<CommonPageResult<IPage<FbaOrderVO>>> selectPage(QueryFbaOrderForm queryFbaOrderForm) {
        return CommonResult.success(new CommonPageResult(fbaOrderService.selectPage(queryFbaOrderForm)));
    }

    /**
     * 列表查询数据
     *
     * @param queryFbaOrderForm   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public CommonResult<List<FbaOrderVO>> selectList(QueryFbaOrderForm queryFbaOrderForm) {
        return CommonResult.success(fbaOrderService.selectList(queryFbaOrderForm));
    }

    /**
     * 新增
     * @param addFbaOrderForm
     **/
    @ApiOperation("新增订单")
    @PostMapping("/add")
    public CommonResult add(@Valid @RequestBody AddFbaOrderForm addFbaOrderForm ){

        if(addFbaOrderForm.getId() == null){
            FbaOrder fbaOrderByOrderNo = fbaOrderService.getFbaOrderByOrderNo(addFbaOrderForm.getOrderNo());
            if(fbaOrderByOrderNo != null){
                return CommonResult.error(444,"订单编号已存在");
            }
        }

        fbaOrderService.saveOrUpdateFbaOrder(addFbaOrderForm);
        return CommonResult.success();
    }

    /**
     * 删除
     * @param  form
     **/
    @ApiOperation("删除订单")
    @PostMapping("/del")
    public CommonResult del(@Valid @RequestBody DeleteForm form){
        if(CollectionUtil.isEmpty(form.getIds())){
            return CommonResult.error(444,"id不为空");
        }
        fbaOrderService.deleteById(form.getIds());
        return CommonResult.success();
    }

    /**
     * 根据id查询
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public CommonResult<FbaOrderVO> queryById(@RequestParam(name="id",required=true) int id) {

        return CommonResult.success(fbaOrderService.getFbaOrderById(id));
    }

    /**
     * 新增
     * @param addFbaOrderTrackForm
     **/
    @ApiOperation("新增轨迹")
    @PostMapping("/addFbaOrderTrack")
    public CommonResult addFbaOrderTrack(@Valid @RequestBody AddFbaOrderTrackForm addFbaOrderTrackForm ){

        fbaOrderTrackService.saveOrUpdateFbaOrderTrack(addFbaOrderTrackForm);
        return CommonResult.success();
    }

    /**
     * 删除
     * @param  form
     **/
    @ApiOperation("删除轨迹")
    @PostMapping("/delFbaOrderTrack")
    public CommonResult delFbaOrderTrack(@Valid @RequestBody DeleteForm form){
        if(CollectionUtil.isEmpty(form.getIds())){
            return CommonResult.error(444,"id不为空");
        }
        fbaOrderTrackService.deleteById(form.getIds());
        return CommonResult.success();
    }

    /**
     * 根据订单查询轨迹的信息
     * @param  orderNo
     **/
    @ApiOperation("根据订单查询轨迹的信息")
    @GetMapping("/")
    public CommonResult delFbaOrderTrack(@RequestParam(name="id",required=true) String orderNo){
        FbaOrderVO fbaOrderVO = fbaOrderService.getFbaOrderVOByOrderNo(orderNo);
        return CommonResult.success(fbaOrderVO);
    }

}

