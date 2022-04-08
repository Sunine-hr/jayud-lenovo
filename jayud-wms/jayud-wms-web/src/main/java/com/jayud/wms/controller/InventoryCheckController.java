package com.jayud.wms.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.InventoryCheckForm;
import com.jayud.wms.model.bo.WorkbenchCheckAffirmForm;
import com.jayud.wms.model.po.InventoryCheck;
import com.jayud.wms.model.po.InventoryCheckDetail;
import com.jayud.wms.model.vo.InventoryCheckAppVO;
import com.jayud.wms.model.vo.InventoryCheckVO;
import com.jayud.wms.model.vo.WorkbenchCheckVO;
import com.jayud.wms.service.IInventoryCheckService;
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
 * 库存盘点表 控制类
 *
 * @author jyd
 * @since 2021-12-27
 */
@Api(tags = "库存盘点表")
@RestController
@RequestMapping("/inventoryCheck")
public class InventoryCheckController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IInventoryCheckService inventoryCheckService;

    /**
     * 分页查询数据
     *
     * @param inventoryCheck   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<InventoryCheck>>> selectPage(@RequestBody InventoryCheck inventoryCheck,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(inventoryCheckService.selectPage(inventoryCheck,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param inventoryCheck   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<InventoryCheck>> selectList(InventoryCheck inventoryCheck,
                                                HttpServletRequest req) {
        return BaseResult.ok(inventoryCheckService.selectList(inventoryCheck));
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<InventoryCheck> queryById(@RequestParam(name="id",required=true) int id) {
        InventoryCheck inventoryCheck = inventoryCheckService.getById(id);
        return BaseResult.ok(inventoryCheck);
    }

    /**
     * 根据查询条件导出库存盘点表
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出库存盘点表")
    @PostMapping(path = "/exportInventoryCheck")
    public void exportInventoryCheck(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键ID",
                "盘点任务号",
                "仓库名称",
                "盘点状态",
                "盘点类型",
                "库存数量",
                "盘点数量",
                "盘点人",
                "盘点开始时间",
                "完成时间",
                "创建人",
                "创建时间"
            );
            List<LinkedHashMap<String, Object>> dataList = inventoryCheckService.queryInventoryCheckForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "库存盘点表", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

    /**
     * 生成盘点任务
     *     惠州道科，添加 货架id，工作台id
     */
    @ApiOperation("生成盘点任务")
    @PostMapping("/generateInventoryCheck")
    public BaseResult<InventoryCheckVO> generateInventoryCheck(@Valid @RequestBody InventoryCheckForm bo){
        InventoryCheckVO vo = inventoryCheckService.generateInventoryCheck(bo);
        return BaseResult.ok(vo);
    }

    /**
     * 查看盘点报告
     */
    @ApiOperation("查看盘点报告")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/lookInventoryCheck")
    public BaseResult<InventoryCheckVO> lookInventoryCheck(@RequestParam(name="id",required=true) int id) {
        InventoryCheckVO vo = inventoryCheckService.lookInventoryCheck(id);
        return BaseResult.ok(vo);
    }


    /**
     * 确认完成盘点
     */
    @ApiOperation("确认盘点过账")
    @PostMapping(value = "/confirmCompleteInventoryCheck")
    public BaseResult confirmCompleteInventoryCheck(@RequestParam(name="id",required=true) int id){
        boolean b = inventoryCheckService.confirmCompleteInventoryCheck(id);
        return BaseResult.ok(b);
    }


    /**
     * 盘点作业任务(汇总)查询Feign
     *
     * @param paramMap   查询条件
     * @return
     */
    @ApiOperation("盘点作业任务(汇总)查询Feign")
    @PostMapping("/selectPageByFeign")
    public BaseResult<CommonPageResult<IPage<InventoryCheckAppVO>>> selectPageByFeign(@RequestBody Map<String, Object> paramMap,
                                                                                  HttpServletRequest req) {
        InventoryCheck inventoryCheck = BeanUtil.fillBeanWithMap(paramMap, new InventoryCheck(), false);
        return BaseResult.ok(new CommonPageResult(inventoryCheckService.selectPageByFeign(inventoryCheck,inventoryCheck.getCurrentPage(), inventoryCheck.getPageSize(),req)));
    }

    /**
     * 去盘点(盘点详情)
     */
    @ApiOperation("去盘点(盘点详情)")
    @PostMapping("/queryInventoryCheckByApp")
    public BaseResult<InventoryCheckAppVO> queryInventoryCheckByApp(@RequestBody Map<String, Object> paramMap){
        InventoryCheckAppVO inventoryCheckAppVO = BeanUtil.fillBeanWithMap(paramMap, new InventoryCheckAppVO(), false);
        String checkCode = inventoryCheckAppVO.getCheckCode();
        InventoryCheckAppVO vo = inventoryCheckService.queryInventoryCheckByCheckCode(checkCode);
        return BaseResult.ok(vo);
    }

    /**
     * PDA App 盘点确认
     */
    @ApiOperation("盘点确认")
    @PostMapping("/inventoryCheckCompletedByApp")
    public BaseResult<InventoryCheckAppVO> inventoryCheckCompletedByApp(@RequestBody Map<String, Object> paramMap){
        InventoryCheckAppVO inventoryCheckAppVO = BeanUtil.fillBeanWithMap(paramMap, new InventoryCheckAppVO(), false);
        //如果有多条 确认当前任务后，返回下一条的任务，没有下一条任务，返回空
        InventoryCheckAppVO vo = inventoryCheckService.inventoryCheckCompletedByApp(inventoryCheckAppVO);
        return BaseResult.ok(vo);
    }

    /**
     * 惠州道科
     * 工作台盘点，确认，货架+物料+数量(盘点数量)+库位编号
     */
    @ApiOperation("工作台盘点，确认")
    @PostMapping(value = "/workbenchCheckAffirm")
    public BaseResult workbenchCheckAffirm(@RequestBody WorkbenchCheckAffirmForm form){
        inventoryCheckService.workbenchCheckAffirm(form);
        return BaseResult.ok();
    }

    /**
     * 惠州道科
     * 工作台盘点，查询货架号对应的示意图和盘点任务
     */
    @ApiOperation("工作台盘点查询货架号对应的示意图和盘点任务")
    @PostMapping(value = "workbenchCheckQueryByShelfCode")
    public BaseResult<WorkbenchCheckVO> workbenchCheckQueryByShelfCode(@RequestBody WorkbenchCheckAffirmForm form){
        WorkbenchCheckVO vo = inventoryCheckService.workbenchCheckQueryByShelfCode(form);
        return BaseResult.ok(vo);

    }

    /**
     * 惠州道科
     * 工作台盘点，撤销盘点
     */
    @ApiOperation("工作台盘点，撤销盘点")
    @PostMapping(value = "/workbenchCheckRevocation")
    public BaseResult workbenchCheckRevocation(@RequestBody List<InventoryCheckDetail> inventoryCheckDetails){
        inventoryCheckService.workbenchCheckRevocation(inventoryCheckDetails);
        return BaseResult.ok();
    }

    /**
     * 惠州道科
     * 工作台盘点，货架回库
     */
    @ApiOperation("工作台盘点，货架回库")
    @PostMapping(value = "/workbenchCheckBackLibrary")
    public BaseResult workbenchCheckBackLibrary(@RequestBody WorkbenchCheckAffirmForm form){
        inventoryCheckService.workbenchCheckBackLibrary(form);
        return BaseResult.ok();
    }

}
