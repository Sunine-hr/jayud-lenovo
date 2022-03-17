package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.QualityInspectionMaterial;
import com.jayud.wms.service.IQualityInspectionMaterialService;
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
 * 质检物料信息 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "质检物料信息")
@RestController
@RequestMapping("/qualityInspectionMaterial")
public class QualityInspectionMaterialController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IQualityInspectionMaterialService qualityInspectionMaterialService;

    /**
     * 分页查询数据
     *
     * @param qualityInspectionMaterial   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<QualityInspectionMaterial>>> selectPage(QualityInspectionMaterial qualityInspectionMaterial,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(qualityInspectionMaterialService.selectPage(qualityInspectionMaterial,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param qualityInspectionMaterial   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<QualityInspectionMaterial>> selectList(QualityInspectionMaterial qualityInspectionMaterial,
                                                HttpServletRequest req) {
        return BaseResult.ok(qualityInspectionMaterialService.selectList(qualityInspectionMaterial));
    }

    /**
    * 新增
    * @param qualityInspectionMaterial
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody QualityInspectionMaterial qualityInspectionMaterial ){
        qualityInspectionMaterialService.save(qualityInspectionMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param qualityInspectionMaterial
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody QualityInspectionMaterial qualityInspectionMaterial ){
        qualityInspectionMaterialService.updateById(qualityInspectionMaterial);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param qualityInspectionMaterial
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody QualityInspectionMaterial qualityInspectionMaterial ){
        QualityInspectionMaterial qualityInspectionMaterial1 = qualityInspectionMaterialService.saveOrUpdateQualityInspectionMaterial(qualityInspectionMaterial);
        return BaseResult.ok(qualityInspectionMaterial1);
    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        qualityInspectionMaterialService.removeById(id);
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
        qualityInspectionMaterialService.delQualityInspectionMaterial(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<QualityInspectionMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        QualityInspectionMaterial qualityInspectionMaterial = qualityInspectionMaterialService.getById(id);
        return BaseResult.ok(qualityInspectionMaterial);
    }

    /**
     * 根据查询条件导出质检物料信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出质检物料信息")
    @PostMapping(path = "/exportQualityInspectionMaterial")
    public void exportQualityInspectionMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "收货单id",
                "收货单号",
                "质检表id",
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
                "批次号",
                "生产日期",
                "容器号",
                "外部订单号",
                "外部单行号",
                "自定义字段1(是否合格,可能后续上架策略会使用)",
                "自定义字段2",
                "自定义字段3",
                "租户编码",
                "备注信息",
                "是否删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间",
                "检验数量",
                "合格数量",
                "不合格数量",
                "不合格原因（字典值）"
            );
            List<LinkedHashMap<String, Object>> dataList = qualityInspectionMaterialService.queryQualityInspectionMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "质检物料信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
