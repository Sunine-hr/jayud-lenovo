package com.jayud.wms.controller;


import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.wms.model.po.WmsMaterialToLoactionRelation;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.IWmsMaterialToLoactionRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
/**
 * 物料和库位关系 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "物料和库位关系")
@RestController
@RequestMapping("/wmsMaterialToLoactionRelation")
public class WmsMaterialToLoactionRelationController {

    @Autowired
    public IWmsMaterialToLoactionRelationService wmsMaterialToLoactionRelationService;



    /**
     * 分页查询数据
     *
     * @param wmsMaterialToLoactionRelation   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public ListPageRuslt<WmsMaterialToLoactionRelation> selectPage(WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation,
                                                                   HttpServletRequest req) {
        return PaginationBuilder.buildPageResult(wmsMaterialToLoactionRelationService.selectPage(wmsMaterialToLoactionRelation,req));
    }

    /**
     * 列表查询数据
     *
     * @param wmsMaterialToLoactionRelation   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsMaterialToLoactionRelation>> selectList(WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsMaterialToLoactionRelationService.selectList(wmsMaterialToLoactionRelation));
    }

    /**
    * 新增
    * @param wmsMaterialBasicInfoVO
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO ){
        return BaseResult.ok(SysTips.ADD_SUCCESS);

    }

    /**
     * 编辑
     * @param wmsMaterialBasicInfoVO
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO ){
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


}
