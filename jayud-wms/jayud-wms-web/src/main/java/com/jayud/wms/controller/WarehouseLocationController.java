package com.jayud.wms.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.bo.*;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import com.jayud.wms.service.IWarehouseAreaService;
import com.jayud.wms.service.IWarehouseLocationService;
import com.jayud.wms.service.IWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;

/**
 * 库位信息 控制类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Api(tags = "库位信息")
@RestController
@RequestMapping("/warehouseLocation")
public class WarehouseLocationController {

    @Autowired
    public IWarehouseLocationService warehouseLocationService;

    @Autowired
    private IWarehouseAreaService warehouseAreaService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private IWarehouseService warehouseService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 分页查询数据
     *
     * @param warehouseLocation 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WarehouseLocationVO>>> selectPage(QueryWarehouseLocationForm warehouseLocation,
                                                                           @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                           HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(warehouseLocationService.selectPage(warehouseLocation, currentPage, pageSize, req)));
    }

    /**
     * 列表查询数据
     *
     * @param warehouseLocation 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WarehouseLocationVO>> selectList(WarehouseLocation warehouseLocation,
                                                        HttpServletRequest req) {
        return BaseResult.ok(warehouseLocationService.selectList(warehouseLocation));
    }

    /**
     * 新增
     *
     * @param warehouseLocation
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WarehouseLocationForm warehouseLocation) {
        WarehouseLocation warehouseLocation1 = this.warehouseLocationService.getWarehouseLocationByCodeAndStatus(warehouseLocation.getCode());
        if (warehouseLocation1 != null) {
            if(null == warehouseLocation.getId()){
                return BaseResult.error("库位编号已存在");
            }
            if (!warehouseLocation1.getId().equals(warehouseLocation.getId())) {
                return BaseResult.error("库位编号已存在");
            }
        }
        return warehouseLocationService.saveOrUpdateWarehouseLocation(warehouseLocation);
    }

    /**
     * 编辑
     *
     * @param warehouseLocation
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WarehouseLocation warehouseLocation) {
        warehouseLocationService.updateById(warehouseLocation);
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
        //todo 库存不为空无法删除
        warehouseLocationService.deleteById(form.getIds());
//        warehouseLocationService.removeById(id);
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
    public BaseResult<WarehouseLocation> queryById(@RequestParam(name = "id", required = true) int id) {
        WarehouseLocation warehouseLocation = warehouseLocationService.getById(id);
        return BaseResult.ok(warehouseLocation);
    }

    /**
     * 根据查询条件导出仓库库位
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出仓库库位")
    @PostMapping(path = "/exportWarehouseLocation")
    public void exportWarehouseLocation(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList("所属仓库", "所属仓库库区", "库位编号", "库位类型", "排数", "列数", "层数", "长宽高", "是否允许混放", "是否入库冻结", "是否出库冻结", "创建人", "创建时间", "备注");
            List<LinkedHashMap<String, Object>> dataList = warehouseLocationService.queryWarehouseLocationForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "仓库库位", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation(value = "上传文件-导入库位信息")
    @RequestMapping(value = "/importExcelByWarehouseLocation", method = RequestMethod.POST)
    public BaseResult importExcelByWarehouseLocation(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return BaseResult.error("文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //配置别名
        Map<String, String> aliasMap = new HashMap<>();
        aliasMap.put("所属仓库", "warehouseName");
        aliasMap.put("所属仓库库区", "warehouseAreaName");
        aliasMap.put("库位编号", "code");
        aliasMap.put("库位类型", "typeDesc");
        aliasMap.put("排数", "row");
        aliasMap.put("列数", "columnNum");
        aliasMap.put("层数", "layers");
        aliasMap.put("长宽高", "lengths");
        aliasMap.put("是否允许混放", "isMixing");
        aliasMap.put("是否入库冻结", "isInFrozen");
        aliasMap.put("是否出库冻结", "是否出库冻结");
        aliasMap.put("创建人", "createBy");
        aliasMap.put("创建时间", "createTime");
        aliasMap.put("备注", "remark");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<WarehouseLocationExcelForm> list = excelReader.read(0, 1, WarehouseLocationExcelForm.class);

        for (int i = 0; i < list.size(); i++) {
            for (int i1 = i + 1; i1 < list.size(); i1++) {
                if (list.get(i).getCode().equals(list.get(i1).getCode())) {
                    return BaseResult.error(list.get(i).getCode() + "：导入数据存在相同的库位编号");
                }
            }
        }

        List<WarehouseLocation> warehouseLocations = new ArrayList<>();
        for (WarehouseLocationExcelForm warehouseLocationExcelForm : list) {

            String s = warehouseLocationExcelForm.checkParam();
            if (!s.equals("成功")) {
                return BaseResult.error(s);
            }
            Warehouse warehouseByName = warehouseService.getWarehouseByName(warehouseLocationExcelForm.getWarehouseName());
            if (warehouseByName == null) {
                return BaseResult.error(warehouseLocationExcelForm.getWarehouseName() + "：所属仓库不存在");
            }
            WarehouseArea warehouseAreaByName = warehouseAreaService.getWarehouseAreaByName(warehouseLocationExcelForm.getWarehouseAreaName(), warehouseByName.getId());
            if (warehouseAreaByName == null) {
                return BaseResult.error(warehouseLocationExcelForm.getWarehouseAreaName() + "：所属库区名称不存在");
            }
            List<SysDictItem> dictList = authClient.selectItemByDictCode("warehouseLocationType").getResult();

            List<LinkedHashMap<String, Object>> warehouseLocationType = new ArrayList<>();
            for (SysDictItem item : dictList){
                if (item.getItemText().equals(warehouseLocationExcelForm.getTypeDesc())){
                    LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
                    maps.put("name",item.getItemText());
                    maps.put("value",item.getItemValue());
                    warehouseLocationType.add(maps);
                }
            }
            if (CollectionUtil.isEmpty(warehouseLocationType)) {
                return BaseResult.error(warehouseLocationExcelForm.getTypeDesc() + "：库位类型不存在");
            }
            WarehouseLocation warehouseLocationByCode = warehouseLocationService.getWarehouseLocationByCode(warehouseLocationExcelForm.getCode(), warehouseByName.getId(), warehouseAreaByName.getId());
            if (warehouseLocationByCode != null) {
                return BaseResult.error(warehouseLocationExcelForm.getCode() + "：该库位编号已存在");
            }
            WarehouseLocation warehouseLocation = new WarehouseLocation();
            warehouseLocation.setWarehouseId(warehouseByName.getId());
            warehouseLocation.setWarehouseAreaId(warehouseAreaByName.getId());
            warehouseLocation.setCode(warehouseLocationExcelForm.getCode());
            warehouseLocation.setType((Long) warehouseLocationType.get(0).get("id"));
            warehouseLocation.setTypeDesc(warehouseLocationExcelForm.getTypeDesc());
            warehouseLocation.setRow(warehouseLocationExcelForm.getRow());
            warehouseLocation.setColumnNum(warehouseLocationExcelForm.getColumnNum());
            warehouseLocation.setLayers(warehouseLocationExcelForm.getLayers());
            if (warehouseLocationExcelForm.getIsInFrozen().equals("是")) {
                warehouseLocation.setIsInFrozen(true);
            } else {
                warehouseLocation.setIsInFrozen(false);
            }
            if (warehouseLocationExcelForm.getIsMixing().equals("是")) {
                warehouseLocation.setIsOutFrozen(true);
            } else {
                warehouseLocation.setIsOutFrozen(true);
            }
            if (warehouseLocationExcelForm.getIsOutFrozen().equals("是")) {
                warehouseLocation.setIsMixing(true);
            } else {
                warehouseLocation.setIsMixing(true);
            }
            String[] split = warehouseLocationExcelForm.getLengths().split("\\*");
            warehouseLocation.setHigh(Double.valueOf(split[2]));
            warehouseLocation.setWide(Double.valueOf(split[1]));
            warehouseLocation.setLength(Double.valueOf(split[0]));
            warehouseLocation.setCreateBy(CurrentUserUtil.getUsername());
            warehouseLocation.setCreateTime(new Date());
        }

        boolean result = this.warehouseLocationService.saveBatch(warehouseLocations);
        return BaseResult.ok();
    }

    /**
     * 入库选择空库位list
     */
    @ApiOperation("入库选择空库位list")
    @PostMapping("/queryWarehouseLocation")
    public BaseResult<List<WarehouseLocationVO>> queryWarehouseLocation(@Valid @RequestBody QueryInventoryForm form) {
        List<WarehouseLocationVO> list = warehouseLocationService.queryWarehouseLocation(form);
        return BaseResult.ok(list);
    }


    /**
     * 获取库位类型
     */
    @ApiOperation("获取库位类型")
    @PostMapping("/getLocationType")
    public BaseResult<List<LinkedHashMap<String, Object>>> getLocationType() {

        List<SysDictItem> dictList = authClient.selectItemByDictCode("warehouseLocationType").getResult();

        List<LinkedHashMap<String, Object>> confs = new ArrayList<>();
        for (SysDictItem item : dictList){
            LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
            maps.put("name",item.getItemText());
            maps.put("value",item.getItemValue());
            confs.add(maps);
        }
        return BaseResult.ok(confs);
    }

    /**
     * 预警消息  todo
     */
    @ApiOperation("预警消息")
    @PostMapping("/warningMessage")
    public void warningMessage() {

    }

}
