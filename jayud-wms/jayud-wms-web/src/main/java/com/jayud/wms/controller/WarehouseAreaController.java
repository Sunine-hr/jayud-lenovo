package com.jayud.wms.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.QueryWarehouseAreaForm;
import com.jayud.wms.model.bo.WarehouseAreaForm;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.model.vo.WarehouseAreaVO;
import com.jayud.wms.service.IWarehouseAreaService;
import com.jayud.wms.service.IWarehouseLocationService;
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
 * 库区信息 控制类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Api(tags = "库区信息")
@RestController
@RequestMapping("/warehouseArea")
public class WarehouseAreaController {

    @Autowired
    public IWarehouseAreaService warehouseAreaService;

    @Autowired
    private IWarehouseLocationService warehouseLocationService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 分页查询数据
     *
     * @param warehouseArea   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WarehouseAreaVO>>> selectPage(QueryWarehouseAreaForm warehouseArea,
                                                                       @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                       HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(warehouseAreaService.selectPage(warehouseArea,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param warehouseArea   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WarehouseAreaVO>> selectList(WarehouseArea warehouseArea,
                                                    HttpServletRequest req) {
        return BaseResult.ok(warehouseAreaService.selectList(warehouseArea));
    }

    /**
     * 新增
     * @param warehouseArea
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WarehouseAreaForm warehouseArea ){
        WarehouseArea warehouseArea1 = this.warehouseAreaService.getWarehouseAreaByName(warehouseArea.getName(),warehouseArea.getWarehouseId());
        WarehouseArea warehouseArea2 = this.warehouseAreaService.getWarehouseAreaByCode(warehouseArea.getCode(),warehouseArea.getWarehouseId());
        if(warehouseArea.getId() == null || !warehouseArea.getId().equals(warehouseArea1.getId())){
            if(warehouseArea1 != null){
                return BaseResult.error("库区名已存在");
            }
            if(warehouseArea2 != null){
                return BaseResult.error("库区编号已存在");
            }
        }
        if(warehouseArea.getId() != null ){
            if(warehouseArea1 != null && !warehouseArea.getId().equals(warehouseArea1.getId())){
                return BaseResult.error("库区名已存在");
            }
            if(warehouseArea2 != null && !warehouseArea.getId().equals(warehouseArea2.getId())){
                return BaseResult.error("库区编号已存在");
            }
        }
        return warehouseAreaService.saveOrUpdateWarehouseArea(warehouseArea);
    }

    /**
     * 编辑
     * @param warehouseArea
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WarehouseArea warehouseArea ){
        warehouseAreaService.updateById(warehouseArea);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 删除
     * @param  form
     **/
    @ApiOperation("删除")
    @PostMapping("/del")
    public BaseResult del(@Valid @RequestBody DeleteForm form){
        if(CollectionUtil.isEmpty(form.getIds())){
            return BaseResult.error("id不为空");
        }
        for (Long id : form.getIds()) {
            List<WarehouseLocation> list = warehouseLocationService.getWarehouseLocationByWarehouseAreaId(id);
            if(CollectionUtil.isNotEmpty(list)){
                return BaseResult.error("所选库区存在库位，无法删除");
            }
        }
        warehouseAreaService.deleteById(form.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WarehouseAreaVO> queryById(@RequestParam(name="id",required=true) int id) {
        WarehouseArea warehouseArea = warehouseAreaService.getById(id);
        return BaseResult.ok(ConvertUtil.convert(warehouseArea, WarehouseAreaVO.class));
    }

    /**
     * 根据查询条件导出仓库库区
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出仓库库区")
    @PostMapping(path = "/exportWarehouseArea")
    public void exportWarehouseArea(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList("所属仓库", "库区编码", "库区名称", "库区类型", "是否启用","创建人","创建时间");
            List<LinkedHashMap<String, Object>> dataList = warehouseAreaService.queryWarehouseAreaForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "仓库库区", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
