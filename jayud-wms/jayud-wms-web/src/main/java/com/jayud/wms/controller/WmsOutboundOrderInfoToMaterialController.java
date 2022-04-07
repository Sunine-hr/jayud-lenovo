package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.ConfirmInformationForm;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;
import com.jayud.wms.service.IWmsOutboundOrderInfoToMaterialService;
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
 * 出库单-物料信息 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "出库单-物料信息")
@RestController
@RequestMapping("/wmsOutboundOrderInfoToMaterial")
public class WmsOutboundOrderInfoToMaterialController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;

    /**
     * 分页查询数据
     *
     * @param wmsOutboundOrderInfoToMaterialVO   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsOutboundOrderInfoToMaterialVO>>> selectPage(WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO,
                                                                                        @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                                        @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                        HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsOutboundOrderInfoToMaterialService.selectPage(wmsOutboundOrderInfoToMaterialVO,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsOutboundOrderInfoToMaterialVO   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundOrderInfoToMaterialVO>> selectList(WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundOrderInfoToMaterialService.selectList(wmsOutboundOrderInfoToMaterialVO));
    }

    /**
    * 新增
    * @param wmsOutboundOrderInfoToMaterial
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundOrderInfoToMaterial wmsOutboundOrderInfoToMaterial ){
        wmsOutboundOrderInfoToMaterialService.save(wmsOutboundOrderInfoToMaterial);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsOutboundOrderInfoToMaterial
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundOrderInfoToMaterial wmsOutboundOrderInfoToMaterial ){
        wmsOutboundOrderInfoToMaterialService.updateById(wmsOutboundOrderInfoToMaterial);
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
        wmsOutboundOrderInfoToMaterialService.phyDelById(id);
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
        wmsOutboundOrderInfoToMaterialService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundOrderInfoToMaterial> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundOrderInfoToMaterial wmsOutboundOrderInfoToMaterial = wmsOutboundOrderInfoToMaterialService.getById(id);
        return BaseResult.ok(wmsOutboundOrderInfoToMaterial);
    }

    /**
     * 根据查询条件导出出库单-物料信息
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出出库单-物料信息")
    @PostMapping(path = "/exportWmsOutboundOrderInfoToMaterial")
    public void exportWmsOutboundOrderInfoToMaterial(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库单号",
                "物料id",
                "物料编码",
                "物料名称",
                "分配量",
                "单位",
                "外部单号",
                "外部单行号",
                "批次号",
                "生产时间",
                "自定义1",
                "自定义2",
                "自定义3",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundOrderInfoToMaterialService.queryWmsOutboundOrderInfoToMaterialForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库单-物料信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("确认出库")
    @PostMapping(path = "/comfirmOutput")
    public BaseResult<List<WmsOutboundOrderInfoToMaterialVO>> comfirmOutput(@RequestBody ConfirmInformationForm confirmInformationForm){
        return wmsOutboundOrderInfoToMaterialService.comfirmOutput(confirmInformationForm);
    }

}
