package com.jayud.wms.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.model.po.WarehouseShelf;
import com.jayud.wms.service.IWarehouseLocationService;
import com.jayud.wms.service.IWarehouseService;
import com.jayud.wms.service.IWarehouseShelfService;
import com.jayud.wms.service.SysUserOwerPermissionService;
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
import java.util.stream.Collectors;

/**
 * 货架信息 控制类
 *
 * @author jyd
 * @since 2022-03-05
 */
@Api(tags = "货架信息")
@RestController
@RequestMapping("/warehouseShelf")
public class WarehouseShelfController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWarehouseShelfService warehouseShelfService;

    @Autowired
    private SysUserOwerPermissionService sysUserOwerPermissionService;

    @Autowired
    public IWarehouseService warehouseService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private IWarehouseLocationService warehouseLocationService;


    /**
     * 分页查询数据
     *
     * @param warehouseShelf   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WarehouseShelf>>> selectPage(@RequestBody WarehouseShelf warehouseShelf,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(warehouseShelfService.selectPage(warehouseShelf,warehouseShelf.getCurrentPage(),warehouseShelf.getPageSize(),req)));
    }

    /**
     * 列表查询数据
     *
     * @param warehouseShelf   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WarehouseShelf>> selectList(WarehouseShelf warehouseShelf,
                                                HttpServletRequest req) {
        return BaseResult.ok(warehouseShelfService.selectList(warehouseShelf));
    }

    /**
    * 新增
    * @param warehouseShelf
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WarehouseShelf warehouseShelf ){
        warehouseShelfService.save(warehouseShelf);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param warehouseShelf
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WarehouseShelf warehouseShelf ){
        warehouseShelfService.updateById(warehouseShelf);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param warehouseShelf
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody WarehouseShelf warehouseShelf ){
        WarehouseShelf warehouseShelfByCode = warehouseShelfService.getWarehouseShelfBycode(warehouseShelf.getCode());
        if(null != warehouseShelf.getId()){
            //判断编码是否已存在

            if(warehouseShelf.getId().equals(warehouseShelfByCode.getId())){
                return BaseResult.error(444,"该货架编码已存在");
            }
        }else {
            if(null != warehouseShelfByCode){
                return BaseResult.error(444,"该货架编码已存在");
            }
        }

        WarehouseShelf warehouseShelf1 = warehouseShelfService.saveOrUpdateWarehouseShelf(warehouseShelf);
        return BaseResult.ok(warehouseShelf1);
    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        warehouseShelfService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        //判断如果下面有库位则不能删除
        List<WarehouseLocation> warehouseLocationByWarehouseAreaId = warehouseLocationService.getWarehouseLocationByWarehouseShelfId(id);
        if(CollectionUtil.isNotEmpty(warehouseLocationByWarehouseAreaId)){
            return BaseResult.error(444,"货架下存在库位，无法删除");
        }
        warehouseShelfService.delWarehouseShelf(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WarehouseShelf> queryById(@RequestParam(name="id",required=true) int id) {
        WarehouseShelf warehouseShelf = warehouseShelfService.getById(id);
        return BaseResult.ok(warehouseShelf);
    }

    /**
     * 根据查询条件导出货架信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出货架信息")
    @PostMapping(path = "/exportWarehouseShelf")
    public void exportWarehouseShelf(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "所属仓库id",
                "所属仓库库区id",
                "货架编号",
                "货架类型code(TC01立体货架,TC02平铺货架)",
                "货架类型desc",
                "状态（0 禁用 1启用）",
                "租户编码",
                "备注信息",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间",
                "货架位置状态(1在固定位 2在移动中 3在工作台)"
            );
            List<LinkedHashMap<String, Object>> dataList = warehouseShelfService.queryWarehouseShelfForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "货架信息", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

    @ApiOperation("下拉值")
    @GetMapping(value = "/getByData")
    public BaseResult getByData() {
        //货架类型
        List<SysDictItem> sysDictItemList = authClient.selectItemByDictCode("shelf_type_code").getResult();
        List<LinkedHashMap<String, Object>> warehouseShelfType = new ArrayList<>();
        sysDictItemList.forEach(sysDictItem -> {
            LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
            maps.put("name",sysDictItem.getItemText());
            maps.put("value",sysDictItem.getItemValue());
            warehouseShelfType.add(maps);
        });


        List<Warehouse> warehouses = warehouseService.getWarehouse();
        Map<String, String> warehouseIdMap = sysUserOwerPermissionService.getOwerIdByUserId(CurrentUserUtil.getUserId().toString()).stream().collect(Collectors.toMap(e -> e, e -> e));

        warehouses = warehouses.stream().filter(e -> warehouseIdMap.get(e.getId()) != null).collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("warehouseShelfType", warehouseShelfType);
        map.put("warehouses", warehouses);
        return BaseResult.ok(map);
    }

    @ApiOperation("货架下拉值")
    @GetMapping(value = "/getDataByWarehouseAreaId")
    public BaseResult getDataByWarehouseAreaId(@RequestParam("id") Long id) {
        List<WarehouseShelf> warehouseShelves = warehouseShelfService.getDataByWarehouseAreaId(id);
        return BaseResult.ok(warehouseShelves);
    }


    @ApiOperation(value = "上传文件-导入商品信息")
    @RequestMapping(value = "/importExcelByCustomerGoods", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult importExcelByCustomerGoods(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return BaseResult.error(-1, "文件为空！");
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
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("*货架编号","code");
        aliasMap.put("*货架类型","typeDesc");
        aliasMap.put("*所属仓库代码","warehouseCode");
        aliasMap.put("*所属库区","warehouseAreaCode");
//        aliasMap.put("品牌","skuBrand");
//        aliasMap.put("产地","skuOrigin");
//        aliasMap.put("商品描述","skuNotes");
//        aliasMap.put("配件","accessories");
//        aliasMap.put("单位净重","unitNw");
//        aliasMap.put("参考价","referencePrice");
//        aliasMap.put("料号","itemNo");
//        aliasMap.put("备注","remark");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<WarehouseShelf> list = excelReader.read(0, 1, WarehouseShelf.class);


        return BaseResult.ok(list);
    }

}
