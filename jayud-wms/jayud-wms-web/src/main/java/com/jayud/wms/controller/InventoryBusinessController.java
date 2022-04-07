package com.jayud.wms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.po.InventoryBusiness;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.IInventoryBusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 库存事务表 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "库存事务")
@RestController
@RequestMapping("/inventoryBusiness")
public class InventoryBusinessController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IInventoryBusinessService inventoryBusinessService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthClient authClient;

    /**
     * 分页查询数据
     *
     * @param inventoryBusiness 查询条件
     * @return
     */

    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<InventoryBusiness>>> selectPage(@RequestBody InventoryBusiness inventoryBusiness,
                                                                             HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(inventoryBusinessService.selectPage(inventoryBusiness, inventoryBusiness.getCurrentPage(), inventoryBusiness.getPageSize(), req)));
    }

    /**
     * 列表查询数据
     *
     * @param inventoryBusiness 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<InventoryBusiness>> selectList(InventoryBusiness inventoryBusiness,
                                                          HttpServletRequest req) {
        return BaseResult.ok(inventoryBusinessService.selectList(inventoryBusiness));
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<InventoryBusiness> queryById(@RequestParam(name = "id", required = true) int id) {
        InventoryBusiness inventoryBusiness = inventoryBusinessService.getById(id);
        return BaseResult.ok(inventoryBusiness);
    }

    /**
     * 根据查询条件导出库存事务表
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出库存事务表")
    @PostMapping(path = "/exportInventoryBusiness")
    public void exportInventoryBusiness(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "序号",
                    "事务编号",
                    "类型",
                    "仓库",
                    "货主",
                    "原库位编码",
                    "原容器号",
                    "原数量",
                    "新库位编码",
                    "新容器号",
                    "数量变化",
                    "物料编号",
                    "物料规格",
                    "批次号",
                    "生产日期",
                    "自定义1",
                    "自定义2",
                    "自定义3",
                    "操作人",
                    "操作时间"
            );
            List<LinkedHashMap<String, Object>> dataList = inventoryBusinessService.queryInventoryBusinessForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "库存事务表", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }


    /**
     * 库存事务 的下拉框数据
     *
     * @return
     */
    @ApiOperation("库存事务 的下拉框数据")
    @GetMapping(path = "/queryInventoryBusiness")
    public BaseResult queryInventoryBusiness() {
        //库存事务 的下拉框数据
        BaseResult<List<SysDictItem>> wmsStorageTransactionType = authClient.selectItemByDictCode("wms_storage_transaction_type");
        Map<String, Object> map = new HashMap<>();
        map.put("wmsStorageTransactionType", wmsStorageTransactionType);
        return BaseResult.ok(map);
    }


}
