package com.jayud.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.IWmsMaterialBasicInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 物料基本信息 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "物料基本信息")
@RestController
@RequestMapping("/wmsMaterialBasicInfo")
public class WmsMaterialBasicInfoController {

    @Autowired
    public IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;

    @Autowired
    private AuthClient authClient;

    /**
     * 分页查询数据
     *
     * @param wmsMaterialBasicInfoVO   查询条件
     * @return
     */
    @SysDataPermission
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public ListPageRuslt<WmsMaterialBasicInfoVO> selectPage(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                            @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        return PaginationBuilder.buildPageResult(wmsMaterialBasicInfoService.selectPage(wmsMaterialBasicInfoVO,currentPage,pageSize,req));
    }

    /**
     * 列表查询数据
     *
     * @param wmsMaterialBasicInfoVO   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsMaterialBasicInfoVO>> selectList(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsMaterialBasicInfoService.selectList(wmsMaterialBasicInfoVO));
    }

    /**
     * selectWmsMaterialBasicInfoVOList
     *   w  h
     * @param wmsMaterialBasicInfoVO   查询条件
     * @return
     */
    @ApiOperation("列表并且返回包装规格集合查询数据")
    @GetMapping("/selectWmsMaterialBasicInfoVOList")
    public BaseResult<IPage<WmsMaterialBasicInfoVO>> selectWmsMaterialBasicInfoVOList(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                                                 @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                 HttpServletRequest req) {
        return BaseResult.ok(wmsMaterialBasicInfoService.selectWmsMaterialBasicInfoVOListList(wmsMaterialBasicInfoVO,currentPage,pageSize,req));
    }


    /**
     * 根据物料编码 查询物料详情和物料规格
     *   w h
     * @param materialCode
     * @return
     */
    @ApiOperation("根据物料编码 查询物料详情和物料规格")
    @GetMapping(value = "/queryByMaterialCodeOne")
    public BaseResult<WmsMaterialBasicInfoVO> queryByMaterialCodeOne(@RequestParam(name="materialCode",required=true) String materialCode) {

        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(WmsMaterialBasicInfo::getMaterialCode, materialCode);
        WmsMaterialBasicInfo wmsMaterialBasicInfoOne = wmsMaterialBasicInfoService.getOne(condition);

        return BaseResult.ok(wmsMaterialBasicInfoService.selectById(wmsMaterialBasicInfoOne.getId()));
    }



    /**
    * 保存
    * @param wmsMaterialBasicInfoVO
    **/
    @ApiOperation("保存")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO ){
        return wmsMaterialBasicInfoService.add(wmsMaterialBasicInfoVO);
    }

    /**
     * 编辑
     * @param wmsMaterialBasicInfoVO
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO ){
        return wmsMaterialBasicInfoService.add(wmsMaterialBasicInfoVO);
    }

    /**
     * 删除
     * @param wmsMaterialBasicInfoVO
     **/
    @ApiOperation("删除")
    @PostMapping("/del")
    public BaseResult del(@RequestBody WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO){
        for (long id : wmsMaterialBasicInfoVO.getIds()){
            wmsMaterialBasicInfoService.delByMaterialId(id);
        }
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsMaterialBasicInfoVO> queryById(@RequestParam(name="id",required=true) long id) {
        return BaseResult.ok(wmsMaterialBasicInfoService.selectById(id));
    }



    // bases 项目使用
    @ApiOperation("物料类型下拉值整体")
    @GetMapping(value = "/getByMaterialTypeType")
    public BaseResult getByData () {
        Map<String, Object> map = new HashMap<>(16);
        map.put("packingType",getDictMap("packing"));
        map.put("lowUnitType",getDictMap("low_unit"));
        map.put("abc_identifiType",getDictMap("ABC_identifi"));
        map.put("materialGroupType",getDictMap("material_group"));
        map.put("storageConditionsType",getDictMap("storage_conditions"));

        return BaseResult.ok(map);
    }

    private List<LinkedHashMap<String, Object>> getDictMap(String dictCode){
        List<LinkedHashMap<String, Object>> dataList = new ArrayList<>();
        List<SysDictItem> itemList = authClient.selectItemByDictCode(dictCode).getResult();
        for (SysDictItem item : itemList){
            LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
            maps.put("name",item.getItemText());
            maps.put("value",item.getItemValue());
            dataList.add(maps);
        }
        return dataList;
    }






}
