package com.jayud.wms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.AddStrategyConfForm;
import com.jayud.wms.model.po.StrategyConf;
import com.jayud.wms.model.vo.StrategyConfVO;
import com.jayud.wms.service.AuthService;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略配置 控制类
 *
 * @author jyd
 * @since 2022-01-13
 */
@Api(tags = "策略配置")
@RestController
@RequestMapping("/strategyConf")
public class StrategyConfController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IStrategyConfService strategyConfService;
    @Autowired
    private AuthService authService;


    /**
     * 分页查询数据
     *
     * @param strategyConf 查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<StrategyConf>>> selectPage(StrategyConf strategyConf,
                                                                    @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                    HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(strategyConfService.selectPage(strategyConf, currentPage, pageSize, req)));
    }

    /**
     * 列表查询数据
     *
     * @param strategyConf 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<StrategyConf>> selectList(StrategyConf strategyConf,
                                                 HttpServletRequest req) {
        return BaseResult.ok(strategyConfService.selectList(strategyConf));
    }

    /**
     * 新增
     *
     * @param form
     **/
    @ApiOperation("新增")
    @PostMapping("/addOrUpdate")
    public BaseResult addOrUpdate(@Valid @RequestBody AddStrategyConfForm form) {
        form.checkParam();
        form.assembleData();
        strategyConfService.addOrUpdate(form);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     *
     * @param strategyConf
     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody StrategyConf strategyConf) {
//        strategyConfService.updateById(strategyConf);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

    /**
     * 保存(新增+编辑)
     *
     * @param strategyConf
     **/
//    @ApiOperation("保存(新增+编辑)")
//    @PostMapping("/save")
//    public BaseResult save(@Valid @RequestBody StrategyConf strategyConf) {
//        StrategyConf strategyConf1 = strategyConfService.saveOrUpdateStrategyConf(strategyConf);
//        return BaseResult.ok(strategyConf1);
//    }

    /**
     * 物理删除
     *
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id) {
        strategyConfService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id) {
        strategyConfService.delStrategyConf(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/getDetails")
    public BaseResult<StrategyConfVO> getDetails(@RequestParam(name = "id", required = true) Long id) {
        StrategyConfVO strategyConf = strategyConfService.getDetails(id);
        return BaseResult.ok(strategyConf);
    }


    /**
     * 获取下拉上架策略
     */
    @ApiOperation("获取下拉上架策略")
    @GetMapping(value = "/getDropDownShelfStrategy")
    public BaseResult<List<LinkedHashMap<String, Object>>> getDropDownShelfStrategy() {
        List<LinkedHashMap<String, Object>> confs = authService.queryDictByDictType("shelfStrategy");
        for (LinkedHashMap<String, Object> conf : confs) {
            conf.put("value", MapUtil.getInt(conf, "value"));
        }
        return BaseResult.ok(confs);
    }


    /**
     * 根据查询条件导出策略配置
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出策略配置")
    @PostMapping(path = "/exportStrategyConf")
    public void exportStrategyConf(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "上架策略id",
                    "策略类型值(1:推荐库位,2:推荐库区,3:至库区)",
                    "策略类型描述",
                    "至库区所属仓库id",
                    "至库区所属仓库库区id",
                    "库位冻结状态(0:非冻结,1:冻结)",
                    "库位类型(多个用逗号隔开)",
                    "租户编码",
                    "备注信息",
                    "是否删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间",
                    "排序"
            );
            List<LinkedHashMap<String, Object>> dataList = strategyConfService.queryStrategyConfForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "策略配置", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

}
