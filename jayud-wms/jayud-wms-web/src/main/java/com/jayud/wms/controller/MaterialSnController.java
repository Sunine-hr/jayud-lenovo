package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.MaterialSn;
import com.jayud.wms.service.IMaterialSnService;
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
 * 物料sn信息 控制类
 *
 * @author jyd
 * @since 2021-12-21
 */
@Api(tags = "物料sn信息")
@RestController
@RequestMapping("/materialSn")
public class MaterialSnController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IMaterialSnService materialSnService;

    /**
     * 分页查询数据
     *
     * @param materialSn   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<MaterialSn>>> selectPage(MaterialSn materialSn,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(materialSnService.selectPage(materialSn,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param materialSn   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<MaterialSn>> selectList(MaterialSn materialSn,
                                                HttpServletRequest req) {
        return BaseResult.ok(materialSnService.selectList(materialSn));
    }

    /**
    * 新增
    * @param materialSn
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody MaterialSn materialSn ){
        materialSnService.save(materialSn);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param materialSn
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody MaterialSn materialSn ){
        materialSnService.updateById(materialSn);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param materialSn
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody MaterialSn materialSn ){
        MaterialSn materialSn1 = materialSnService.saveOrUpdateMaterialSn(materialSn);
        return BaseResult.ok(materialSn1);
    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        materialSnService.removeById(id);
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
        materialSnService.delMaterialSn(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<MaterialSn> queryById(@RequestParam(name="id",required=true) int id) {
        MaterialSn materialSn = materialSnService.getById(id);
        return BaseResult.ok(materialSn);
    }

    /**
     * 根据查询条件导出物料sn信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出物料sn信息")
    @PostMapping(path = "/exportMaterialSn")
    public void exportMaterialSn(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "收货单id",
                "收货单号",
                "物料序列号(SN)",
                "物料编号",
                "物料名称",
                "物料类型id",
                "物料类型",
                "重量",
                "体积",
                "物料规格",
                "预期数量",
                "单位",
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
            List<LinkedHashMap<String, Object>> dataList = materialSnService.queryMaterialSnForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "物料sn信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
