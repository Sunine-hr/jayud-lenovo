package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.Material;
import com.jayud.wms.service.IMaterialService;
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
 * 货单物料信息 控制类
 *
 * @author jyd
 * @since 2021-12-21
 */
@Api(tags = "货单物料信息")
@RestController
@RequestMapping("/material")
public class MaterialController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IMaterialService materialService;

    /**
     * 分页查询数据
     *
     * @param material   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<Material>>> selectPage(Material material,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(materialService.selectPage(material,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param material   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<Material>> selectList(Material material,
                                                HttpServletRequest req) {
        return BaseResult.ok(materialService.selectList(material));
    }

    /**
    * 新增
    * @param material
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody Material material ){
        materialService.save(material);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param material
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody Material material ){
        materialService.updateById(material);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param material
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody Material material ){
        Material material1 = materialService.saveOrUpdateMaterial(material);
        return BaseResult.ok(material1);
    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        materialService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id){
        materialService.delMaterial(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<Material> queryById(@RequestParam(name="id",required=true) int id) {
        Material material = materialService.getById(id);
        return BaseResult.ok(material);
    }

    /**
     * 根据查询条件导出货单物料信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出货单物料信息")
    @PostMapping(path = "/exportMaterial")
    public void exportMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "收货单id",
                "收货单号",
                "物料编号",
                "物料名称",
                "物料类型id",
                "物料类型",
                "预期数量",
                "实收数量",
                "单位",
                "重量",
                "体积",
                "物料规格",
                "容器号",
                "收货状态(1:未收,2:收货中,3:已收货,4:撤销)",
                "外部订单号",
                "外部单行号",
                "批次号",
                "生产日期",
                "自定义字段1",
                "自定义字段2",
                "自定义字段3",
                "租户编码",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间",
                "是否直接上架"
            );
            List<LinkedHashMap<String, Object>> dataList = materialService.queryMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "货单物料信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("确认收货--new")
    @PostMapping(path = "/confirmReceipt")
    public BaseResult confirmReceipt(@RequestBody  DeleteForm deleteForm){
        return materialService.confirmReceipt(deleteForm);
    }

    @ApiOperation("撤销收货--new")
    @PostMapping(path = "/cancelConfirmReceipt")
    public BaseResult cancelConfirmReceipt(@RequestBody  DeleteForm deleteForm){
        return materialService.confirmReceipt(deleteForm);
    }

    @ApiOperation("确认上架--new")
    @PostMapping(path = "/confirmPullShelf")
    public BaseResult confirmPullShelf(@RequestBody  DeleteForm deleteForm){
        return materialService.confirmPullShelf(deleteForm);
    }

}
