package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.InventoryCheckDetailPostForm;
import com.jayud.wms.model.po.InventoryCheckDetail;
import com.jayud.wms.service.IInventoryCheckDetailService;
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
 * 库存盘点明细表 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "库存盘点明细")
@RestController
@RequestMapping("/inventoryCheckDetail")
public class InventoryCheckDetailController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IInventoryCheckDetailService inventoryCheckDetailService;

    /**
     * 分页查询数据
     *
     * @param inventoryCheckDetail 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<InventoryCheckDetail>>> selectPage(InventoryCheckDetail inventoryCheckDetail,
                                                                                @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(inventoryCheckDetailService.selectPage(inventoryCheckDetail, currentPage, pageSize, req)));
    }

    /**
     * 列表查询数据
     *
     * @param inventoryCheckDetail 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<InventoryCheckDetail>> selectList(InventoryCheckDetail inventoryCheckDetail,
                                                             HttpServletRequest req) {
        return BaseResult.ok(inventoryCheckDetailService.selectList(inventoryCheckDetail));
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<InventoryCheckDetail> queryById(@RequestParam(name = "id", required = true) int id) {
        InventoryCheckDetail inventoryCheckDetail = inventoryCheckDetailService.getById(id);
        return BaseResult.ok(inventoryCheckDetail);
    }

    /**
     * 根据查询条件导出库存盘点明细表
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出库存盘点明细表")
    @PostMapping(path = "/exportInventoryCheckDetail")
    public void exportInventoryCheckDetail(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键ID",
                    "库位编号",
                    "盘点状态",
                    "库区",
                    "物料编号",
                    "物料名称",
                    "物料类型",
                    "容器号",
                    "库存数量",
                    "盘点数量",
                    "盘盈数量",
                    "盘亏数量",
                    "物料规格",
                    "生产日期",
                    "批次号",
                    "备注1",
                    "备注2",
                    "备注3"
            );
            List<LinkedHashMap<String, Object>> dataList = inventoryCheckDetailService.queryInventoryCheckDetailForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "库存盘点明细表", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 盘点明细新增
     **/
    @ApiOperation("盘点明细新增")
    @PostMapping("/add")
    public BaseResult<InventoryCheckDetail> add(@Valid @RequestBody InventoryCheckDetail inventoryCheckDetail) {
        InventoryCheckDetail detail = inventoryCheckDetailService.add(inventoryCheckDetail);
        return BaseResult.ok(detail);
    }

    /**
     * 盘点明细确认过账(勾选n个)
     */
    @ApiOperation("盘点明细确认过账(勾选n个)")
    @PostMapping("/confirmPost")
    public BaseResult confirmPost(@Valid @RequestBody InventoryCheckDetailPostForm form) {
        boolean b = inventoryCheckDetailService.confirmPost(form);
        return BaseResult.ok();
    }

    @ApiOperation("确认盘点")
    @PostMapping("/ConfirmTheInventoryPost")
    public BaseResult confirmTheInventoryPost(@RequestBody InventoryCheckDetailPostForm form) {
        boolean b = inventoryCheckDetailService.affirmPost(form);
        return BaseResult.ok();
    }

    @ApiOperation("撤销盘点")
    @PostMapping("/reverseConfirmationCountPost")
    public BaseResult reverseConfirmationCountPost(@RequestBody InventoryCheckDetailPostForm form) {

        boolean b = inventoryCheckDetailService.withdrawPost(form);
        return BaseResult.ok();
    }


}
