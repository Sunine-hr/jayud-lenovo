package com.jayud.wms.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.QueryWarehouseForm;
import com.jayud.wms.model.bo.WarehouseForm;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.model.vo.WarehouseVO;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.IWarehouseAreaService;
import com.jayud.wms.service.IWarehouseService;
import com.jayud.wms.service.SysUserOwerPermissionService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓库信息表 控制类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Api(tags = "仓库信息表")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWarehouseService warehouseService;
    @Autowired
    private AuthService authService;

    @Autowired
    private IWarehouseAreaService warehouseAreaService;

    @Autowired
    private SysUserOwerPermissionService sysUserOwerPermissionService;

    @Autowired
    private AuthClient authClient;


    /**
     * 分页查询数据
     *
     * @param queryWarehouseForm 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<Warehouse>>> selectPage(QueryWarehouseForm queryWarehouseForm,
                                                                     @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(warehouseService.selectPage(queryWarehouseForm, currentPage, pageSize, req)));
    }

    /**
     * 列表查询数据
     *
     * @param warehouse 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<Warehouse>> selectList(QueryWarehouseForm warehouse,
                                              HttpServletRequest req) {
        return BaseResult.ok(warehouseService.selectList(warehouse));
    }

    /**
     * 新增
     *
     * @param warehouse
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WarehouseForm warehouse) {
        Warehouse warehouse1 = this.warehouseService.getWarehouseByName(warehouse.getName());
        Warehouse warehouse2 = this.warehouseService.getWarehouseByCode(warehouse.getCode());
        if (warehouse.getId() == null) {

            if (warehouse1 != null) {
                return BaseResult.error("仓库名已存在");
            }
            if (warehouse2 != null) {
                return BaseResult.error("仓库编号已存在");
            }
        }
        if (warehouse.getId() != null) {
            if (warehouse1 != null && !warehouse.getId().equals(warehouse1.getId())) {
                return BaseResult.error("仓库名已存在");
            }
            if (warehouse2 != null && !warehouse.getId().equals(warehouse2.getId())) {
                return BaseResult.error("仓库编号已存在");
            }
        }
        return warehouseService.saveOrUpdateWarehouse(warehouse);
    }

    /**
     * 编辑
     *
     * @param warehouse
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody Warehouse warehouse) {
        warehouseService.updateById(warehouse);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 删除
     *
     * @param form
     **/
    @ApiOperation("删除")
    @PostMapping("/del")
    public BaseResult del(@RequestBody DeleteForm form) {
        if (CollectionUtil.isEmpty(form.getIds())) {
            return BaseResult.error("id不为空");
        }
        for (Long id : form.getIds()) {
            List<WarehouseArea> list = warehouseAreaService.getWarehouseAreaByWarehouseId(id);
            if (CollectionUtil.isNotEmpty(list)) {
                return BaseResult.error("所选仓库存在库区，无法删除");
            }
        }
        warehouseService.deleteById(form.getIds());
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
    public BaseResult<WarehouseVO> queryById(@RequestParam(name = "id", required = true) int id) {
        Warehouse warehouse = warehouseService.getById(id);
        return BaseResult.ok(ConvertUtil.convert(warehouse, WarehouseVO.class));
    }

    @ApiOperation("下拉值")
    @GetMapping(value = "/getByData")
    public BaseResult getByData() {

        //仓库类型
        BaseResult<List<SysDictItem>> wmsWarehouseType = authClient.selectItemByDictCode("wms_warehouse_type");
        //库区类型
        BaseResult<List<SysDictItem>> wmsTheReservoirType = authClient.selectItemByDictCode("wms_the_reservoir_type");
        //库位类型
        BaseResult<List<SysDictItem>> wmsLocationType = authClient.selectItemByDictCode("wms_location_type");

        //todo 策略下拉值
        Map<String, Object> map = new HashMap<>();
        map.put("wmsWarehouseType", wmsWarehouseType);
        map.put("wmsTheReservoirType", wmsTheReservoirType);
        map.put("wmsLocationType", wmsLocationType);
        return BaseResult.ok(map);
    }

    @ApiOperation("库区下拉值")
    @GetMapping(value = "/getByDataByWarehouseId")
    public BaseResult getByDataByWarehouseId(@RequestParam("id") Long id) {
        List<WarehouseArea> warehouseAreas = warehouseAreaService.getWarehouseAreaByWarehouseIdAndStatus(id);
        return BaseResult.ok(warehouseAreas);
    }

    /**
     * 根据查询条件导出仓库
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出仓库")
    @PostMapping(path = "/exportWarehouse")
    public void exportWarehouse(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList("仓库编码", "仓库名称", "仓库类型", "联系人", "手机号", "邮箱", "联系地址", "经度", "纬度", "是否允许混放", "创建人", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = warehouseService.queryWarehouseForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "仓库", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 获取启用仓库
     *
     * @return
     */
    @ApiOperation("获取启用仓库")
    @GetMapping("/selectListByEnable")
    public BaseResult<List<Warehouse>> selectListByEnable(HttpServletRequest req) {
        return BaseResult.ok(warehouseService.getWarehouse());
    }

    @ApiOperation("根据货主id查询仓库信息")
    @GetMapping(value = "/queryWarehouseByOwerId")
    public BaseResult queryWarehouseByOwerId(@RequestParam(name = "owerId", required = true) Long owerId) {
        return warehouseService.queryWarehouseByOwerId(owerId);
    }
}
