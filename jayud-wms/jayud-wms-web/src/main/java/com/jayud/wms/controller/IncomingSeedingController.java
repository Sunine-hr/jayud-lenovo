package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.IncomingSeedingClientForm;
import com.jayud.common.dto.QueryClientIncomingSeedingForm;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.IncomingSeedingForm;
import com.jayud.wms.model.bo.QueryIncomingSeedingForm;
import com.jayud.wms.model.po.Container;
import com.jayud.wms.model.po.IncomingSeeding;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.vo.IncomingSeedingVO;
import com.jayud.wms.service.IContainerService;
import com.jayud.wms.service.IIncomingSeedingService;
import com.jayud.wms.service.IReceiptService;
import com.jayud.wms.service.IWorkbenchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库播种 控制类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Api(tags = "入库播种")
@RestController
@RequestMapping("/incomingSeeding")
public class IncomingSeedingController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IIncomingSeedingService incomingSeedingService;

    @Autowired
    public IWorkbenchService workbenchService;

    @Autowired
    public IReceiptService receiptService;

    @Autowired
    public IContainerService containerService;

    /**
     * 分页查询数据
     *
     * @param incomingSeeding 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<IncomingSeeding>>> selectPage(IncomingSeeding incomingSeeding,
                                                                       @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                       HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(incomingSeedingService.selectPage(incomingSeeding, currentPage, pageSize, req)));
    }

    /**
     * 列表查询数据
     *
     * @param incomingSeeding 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<IncomingSeeding>> selectList(IncomingSeeding incomingSeeding,
                                                    HttpServletRequest req) {
        return BaseResult.ok(incomingSeedingService.selectList(incomingSeeding));
    }

//    /**
//    * 新增
//    * @param incomingSeeding
//    **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody IncomingSeeding incomingSeeding ){
//        incomingSeedingService.save(incomingSeeding);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }
//
//    /**
//     * 编辑
//     * @param incomingSeeding
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody IncomingSeeding incomingSeeding ){
//        incomingSeedingService.updateById(incomingSeeding);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }
//
//    /**
//    * 保存(新增+编辑)
//    * @param incomingSeeding
//    **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody IncomingSeeding incomingSeeding ){
//        IncomingSeeding incomingSeeding1 = incomingSeedingService.saveOrUpdateIncomingSeeding(incomingSeeding);
//        return BaseResult.ok(incomingSeeding1);
//    }

    @ApiOperation("获取详情信息")
    @PostMapping("/getDetails")
    public BaseResult<IncomingSeedingVO> getDetails(@Valid @RequestBody QueryIncomingSeedingForm form) {
        IncomingSeedingVO tmp = incomingSeedingService.getDetails(form);
        return BaseResult.ok(tmp);
    }


    @ApiOperation("确认更换")
    @PostMapping("/confirmReplacement")
    public BaseResult confirmReplacement(@Valid @RequestBody IncomingSeedingForm form) {
        form.checkParam();
        //TODO 远程调用
        if (!this.workbenchService.exitSeedingPositionNumber(form.getSeedingPositionNum(), 1)) {
            return BaseResult.error("该工作台没有这个播种位");
        }

        incomingSeedingService.confirmReplacement(form);
        return BaseResult.ok();
    }

    @ApiOperation("确认上架")
    @PostMapping("/confirmShelf")
    public BaseResult confirmShelf() {
        incomingSeedingService.confirmShelf();
        return BaseResult.ok();
    }

    /**
     * 物理删除
     *
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id) {
        incomingSeedingService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id) {
        incomingSeedingService.delIncomingSeeding(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<IncomingSeeding> queryById(@RequestParam(name = "id", required = true) int id) {
        IncomingSeeding incomingSeeding = incomingSeedingService.getById(id);
        return BaseResult.ok(incomingSeeding);
    }

    /**
     * 根据查询条件导出入库播种
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出入库播种")
    @PostMapping(path = "/exportIncomingSeeding")
    public void exportIncomingSeeding(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "收货单id",
                    "收货单号",
                    "物料id",
                    "物料编号",
                    "物料名称",
                    "容器号",
                    "数量",
                    "已分配数量",
                    "单位",
                    "租户编码",
                    "备注信息",
                    "是否删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间",
                    "批次号",
                    "生产日期",
                    "自定义字段1(1:合格,2:不合格)(是否合格,可能后续上架策略会使用)",
                    "自定义字段2",
                    "自定义字段3"
            );
            List<LinkedHashMap<String, Object>> dataList = incomingSeedingService.queryIncomingSeedingForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "入库播种", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * app外部调用获取详情信息
     *
     * @param queryClientIncomingSeedingForm
     * @return
     */
    @PostMapping("/client/getDetails")
    public BaseResult<IncomingSeedingVO> getDetailsFeign(@RequestBody QueryClientIncomingSeedingForm queryClientIncomingSeedingForm) {
        QueryIncomingSeedingForm form = ConvertUtil.convert(queryClientIncomingSeedingForm, QueryIncomingSeedingForm.class);
        IncomingSeedingVO tmp = incomingSeedingService.getDetails(form);
        return BaseResult.ok(tmp);
    }

    /**
     * app外部调用修改容器
     *
     * @param incomingSeedingClientForm
     * @return
     */

    @ApiOperation("app外部调用修改容器确认更换")
    @PostMapping("/client/confirmReplacement")
    public BaseResult confirmReplacementFeign(@RequestBody IncomingSeedingClientForm incomingSeedingClientForm) {


        //校验数量
        incomingSeedingClientForm.checkParam();

        Receipt byId = receiptService.getById(incomingSeedingClientForm.getOrderId());

        QueryWrapper<Container> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Container::getCode, incomingSeedingClientForm.getNewContainerNum())// 新容器号编号
                .eq(Container::getWarehouseId, byId.getWarehouseId())//仓库id
                .eq(Container::getStatus, true) // true 启用
                .eq(Container::getIsDeleted, false);  // false 0 否  是否删除
        Container one = containerService.getOne(queryWrapper);
        if (one == null) {
            return BaseResult.error(SysTips.CONTAINER_DOES_NOT_EXIST);
        }
        incomingSeedingClientForm.setNewContainerName(one.getType());//新 容器名称 类型
        incomingSeedingClientForm.setNewContainerNum(one.getCode());//新容器号  容器名称
//        incomingSeedingClientForm.setContainerNum(incomingSeedingClientForm.getContainerNum());//容器号
        IncomingSeedingForm form = ConvertUtil.convert(incomingSeedingClientForm, IncomingSeedingForm.class);
//        form.checkParam();
        //TODO 远程调用
        if (!this.workbenchService.exitSeedingPositionNumber(form.getSeedingPositionNum(), 1)) {
            return BaseResult.error("该工作台没有这个播种位");
        }

        incomingSeedingService.confirmReplacement(form);
        return BaseResult.ok();
    }

}
