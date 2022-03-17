package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.ShelfStrategy;
import com.jayud.wms.model.po.StrategyConf;
import com.jayud.wms.service.IShelfStrategyService;
import com.jayud.wms.service.IStrategyConfService;
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
import java.util.*;

/**
 * 上架策略 控制类
 *
 * @author jyd
 * @since 2022-01-13
 */
@Api(tags = "上架策略")
@RestController
@RequestMapping("/shelfStrategy")
public class ShelfStrategyController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IShelfStrategyService shelfStrategyService;
    @Autowired
    public IStrategyConfService strategyConfService;

    /**
     * 分页查询数据
     *
     * @param shelfStrategy 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<ShelfStrategy>>> selectPage(ShelfStrategy shelfStrategy,
                                                                     @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(shelfStrategyService.selectPage(shelfStrategy, currentPage, pageSize, req)));
    }

    /**
     * 列表查询数据
     *
     * @param shelfStrategy 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<ShelfStrategy>> selectList(ShelfStrategy shelfStrategy,
                                                  HttpServletRequest req) {
        return BaseResult.ok(shelfStrategyService.selectList(shelfStrategy));
    }

    /**
     * 新增
     *
     * @param shelfStrategy
     **/
//    @ApiOperation("新增")
//    @PostMapping("/add")
//    public BaseResult add(@Valid @RequestBody ShelfStrategy shelfStrategy) {
//        shelfStrategyService.save(shelfStrategy);
//        return BaseResult.ok(SysTips.ADD_SUCCESS);
//    }

    /**
     * 编辑
     *
     * @param shelfStrategy
     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody ShelfStrategy shelfStrategy) {
//        shelfStrategyService.updateById(shelfStrategy);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

    /**
     * 保存(新增+编辑)
     *
     * @param shelfStrategy
     **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody ShelfStrategy shelfStrategy) {
        ShelfStrategy shelfStrategy1 = shelfStrategyService.saveOrUpdateShelfStrategy(shelfStrategy);
        return BaseResult.ok(shelfStrategy1);
    }

    /**
     * 获取启用策略
     *
     * @return
     */
    @ApiOperation("获取启用策略")
    @GetMapping("/getByEnabled")
    public BaseResult<List<ShelfStrategy>> getByEnabled() {
        ShelfStrategy shelfStrategy = new ShelfStrategy();
        shelfStrategy.setIsDeleted(false);
        return BaseResult.ok(shelfStrategyService.selectList(shelfStrategy));
    }

    /**
     * 物理删除
     *
     * @param id
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
//    @GetMapping("/del")
//    public BaseResult del(@RequestParam int id) {
//        shelfStrategyService.removeById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id) {
        List<StrategyConf> strategyConfs = strategyConfService.selectList(new StrategyConf().setShelfStrategyId(Long.valueOf(id)).setIsDeleted(false));
        if (strategyConfs.size() > 0) {
            return BaseResult.error("请先删除策略顺序的数据");
        }
        shelfStrategyService.delShelfStrategy(id);
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
    public BaseResult<ShelfStrategy> queryById(@RequestParam(name = "id", required = true) int id) {
        ShelfStrategy shelfStrategy = shelfStrategyService.getById(id);
        return BaseResult.ok(shelfStrategy);
    }

    /**
     * 根据查询条件导出上架策略
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出上架策略")
    @PostMapping(path = "/exportShelfStrategy")
    public void exportShelfStrategy(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "上架策略名称",
                    "租户编码",
                    "备注信息",
                    "是否删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = shelfStrategyService.queryShelfStrategyForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "上架策略", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }


    /**
     * 获取策略优先级
     *
     * @param materialCodes
     */
    @ApiOperation("获取策略优先级")
//    @GetMapping(value = "/getPolicyPrioritys")
    public BaseResult<Map<String, String>> getPolicyPrioritys(@RequestBody Set<String> materialCodes) {
        Map<String, String> map = shelfStrategyService.getPolicyPrioritys(materialCodes);
        return BaseResult.ok(map);
    }

    /**
     * 获取策略优先级
     *
     * @param materialCodes
     */
    @ApiOperation("获取策略优先级")
    @GetMapping(value = "/getPolicyPriority")
    public BaseResult<Map<String, String>> getPolicyPriority(@RequestParam(value = "materialCode") String materialCode,
                                                         @RequestParam(value = "warehouseId") Long warehouseId) {
        Map<String, String> map = shelfStrategyService.getPolicyPriority(materialCode, warehouseId);
        return BaseResult.ok(map);
    }

}
