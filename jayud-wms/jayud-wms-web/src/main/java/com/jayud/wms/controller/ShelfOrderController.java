package com.jayud.wms.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.QueryShelfOrderForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.ShelfOrder;
import com.jayud.wms.model.vo.ShelfOrderVO;
import com.jayud.wms.service.IShelfOrderService;
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

/**
 * 上架单 控制类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Api(tags = "上架单")
@RestController
@RequestMapping("/shelfOrder")
public class ShelfOrderController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IShelfOrderService shelfOrderService;

    /**
     * 分页查询数据
     *
     * @param queryShelfOrderForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = QueryShelfOrderForm.class)
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<ShelfOrderVO>> selectPage(@RequestBody QueryShelfOrderForm queryShelfOrderForm,
                                                  HttpServletRequest req) {
        IPage<ShelfOrderVO> shelfOrderVOIPage = shelfOrderService.selectPage(queryShelfOrderForm, queryShelfOrderForm.getCurrentPage(), queryShelfOrderForm.getPageSize(), req);
        return BaseResult.ok(shelfOrderVOIPage);
    }


    /**
     * 列表查询数据
     *
     * @param queryShelfOrderForm 查询条件
     * @return
     */
    @SysDataPermission(clazz = QueryShelfOrderForm.class)
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<ShelfOrderVO>> selectList(@RequestBody QueryShelfOrderForm queryShelfOrderForm,
                                               HttpServletRequest req) {
        return BaseResult.ok(shelfOrderService.selectList(queryShelfOrderForm));
    }


    /**
     * 上架操作
     *
     * @param shelfOrder
     **/
    @ApiOperation("上架操作")
    @PostMapping("/shelfOperation")
    public BaseResult shelfOperation(@RequestBody List<ShelfOrder> shelfOrder) {
        if (CollectionUtil.isEmpty(shelfOrder)) {

            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        this.shelfOrderService.shelfOperation(shelfOrder);
        return BaseResult.ok();
    }
//    /**
//     * 新增
//     *
//     * @param shelfOrder
//     **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody ShelfOrder shelfOrder) {
//        shelfOrderService.save(shelfOrder);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }

    /**
     * 编辑
     *
     * @param shelfOrder
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody ShelfOrder shelfOrder) {
        shelfOrderService.updateById(shelfOrder);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 保存(新增+编辑)
     *
     * @param shelfOrder
     **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody ShelfOrder shelfOrder) {
        ShelfOrder shelfOrder1 = shelfOrderService.saveOrUpdateShelfOrder(shelfOrder);
        return BaseResult.ok(shelfOrder1);
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
        shelfOrderService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        shelfOrderService.delShelfOrder(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    @ApiOperation("入库报表")
    @PostMapping(value = "/selectWarehousingReport")
    public BaseResult selectWarehousingReport(@RequestBody QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        return shelfOrderService.selectWarehousingReport(queryShelfOrderTaskForm);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<ShelfOrder> queryById(@RequestParam(name = "id", required = true) int id) {
        ShelfOrder shelfOrder = shelfOrderService.getById(id);
        return BaseResult.ok(shelfOrder);
    }

    /**
     * 根据查询条件导出上架单
     *
     * @param response            响应对象
     * @param queryShelfOrderForm 参数Map
     */
    @SysDataPermission(clazz = QueryShelfOrderForm.class)
    @ApiOperation("根据查询条件导出上架单")
    @PostMapping(path = "/exportShelfOrder")
    public void exportShelfOrder(HttpServletResponse response, @RequestBody QueryShelfOrderForm queryShelfOrderForm,
                                 HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList(

                    "收货单号",
                    "仓库",
                    "货主",
                    "物料编号",
                    "数量",
                    "单位",
                    "容器号",
                    "推荐库位",
                    "生成操作人",
                    "生成时间",
                    "上架任务号",
                    "批次号",
                    "生产日期",
                    "自定义字段1",
                    "自定义字段2",
                    "自定义字段3"
            );
            List<LinkedHashMap<String, Object>> dataList = shelfOrderService.queryShelfOrderForExcel(queryShelfOrderForm, req);
            ExcelUtils.exportExcel(headList, dataList, "上架单", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
