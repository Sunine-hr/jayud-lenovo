package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsMaterialToAttribute;
import com.jayud.wms.service.IWmsMaterialToAttributeService;
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
 * 物料管理-批属性配置 控制类
 *
 * @author jyd
 * @since 2022-01-12
 */
@Api(tags = "物料管理-批属性配置")
@RestController
@RequestMapping("/wmsMaterialToAttribute")
public class WmsMaterialToAttributeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsMaterialToAttributeService wmsMaterialToAttributeService;

    /**
     * 分页查询数据
     *
     * @param wmsMaterialToAttribute   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsMaterialToAttribute>>> selectPage(WmsMaterialToAttribute wmsMaterialToAttribute,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsMaterialToAttributeService.selectPage(wmsMaterialToAttribute,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsMaterialToAttribute   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsMaterialToAttribute>> selectList(WmsMaterialToAttribute wmsMaterialToAttribute,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsMaterialToAttributeService.selectList(wmsMaterialToAttribute));
    }

    /**
    * 新增
    * @param wmsMaterialToAttribute
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsMaterialToAttribute wmsMaterialToAttribute ){
        wmsMaterialToAttributeService.save(wmsMaterialToAttribute);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsMaterialToAttribute
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsMaterialToAttribute wmsMaterialToAttribute ){
        wmsMaterialToAttributeService.updateById(wmsMaterialToAttribute);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam int id){
        wmsMaterialToAttributeService.phyDelById(id);
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
        wmsMaterialToAttributeService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsMaterialToAttribute> queryById(@RequestParam(name="id",required=true) int id) {
        WmsMaterialToAttribute wmsMaterialToAttribute = wmsMaterialToAttributeService.getById(id);
        return BaseResult.ok(wmsMaterialToAttribute);
    }

    /**
     * 根据查询条件导出物料管理-批属性配置
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出物料管理-批属性配置")
    @PostMapping(path = "/exportWmsMaterialToAttribute")
    public void exportWmsMaterialToAttribute(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "物料基本信息id",
                "物料基本信息编码",
                "批属性类型(1-批属性1,2-批属性2,3-批属性3,4-批属性4,5-批属性5)",
                "是否可见",
                "是否必填",
                "属性名称",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsMaterialToAttributeService.queryWmsMaterialToAttributeForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "物料管理-批属性配置", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("获取初始化批属性配置")
    @GetMapping(path = "/getInitAttribute")
    public BaseResult<List<WmsMaterialToAttribute>> getInitAttribute(){
        return BaseResult.ok(wmsMaterialToAttributeService.initAttribute());
    }

}
