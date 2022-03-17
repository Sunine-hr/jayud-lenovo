package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsCustomerDevelopmentSetting;
import com.jayud.wms.service.IWmsCustomerDevelopmentSettingService;
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
 * 客户开发设置表 控制类
 *
 * @author jyd
 * @since 2022-02-14
 */
@Api(tags = "客户开发设置表")
@RestController
@RequestMapping("/wmsCustomerDevelopmentSetting")
public class WmsCustomerDevelopmentSettingController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsCustomerDevelopmentSettingService wmsCustomerDevelopmentSettingService;

    /**
     * 分页查询数据
     *
     * @param wmsCustomerDevelopmentSetting   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsCustomerDevelopmentSetting>>> selectPage(@RequestBody WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting,
                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsCustomerDevelopmentSettingService.selectPage(wmsCustomerDevelopmentSetting,wmsCustomerDevelopmentSetting.getCurrentPage(),wmsCustomerDevelopmentSetting.getPageSize(),req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsCustomerDevelopmentSetting   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsCustomerDevelopmentSetting>> selectList(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsCustomerDevelopmentSettingService.selectList(wmsCustomerDevelopmentSetting));
    }

    /**
    * 新增
    * @param wmsCustomerDevelopmentSetting
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting ){
        wmsCustomerDevelopmentSettingService.save(wmsCustomerDevelopmentSetting);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsCustomerDevelopmentSetting
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting ){
        wmsCustomerDevelopmentSettingService.updateById(wmsCustomerDevelopmentSetting);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
    * 保存(新增+编辑)
    * @param wmsCustomerDevelopmentSetting
    **/
    @ApiOperation("保存(新增+编辑)")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting ){
        WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting1 = wmsCustomerDevelopmentSettingService.saveOrUpdateWmsCustomerDevelopmentSetting(wmsCustomerDevelopmentSetting);
        return BaseResult.ok(wmsCustomerDevelopmentSetting1);
    }

    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/del")
    public BaseResult del(@RequestParam int id){
        wmsCustomerDevelopmentSettingService.removeById(id);
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
        wmsCustomerDevelopmentSettingService.delWmsCustomerDevelopmentSetting(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsCustomerDevelopmentSetting> queryById(@RequestParam(name="id",required=true) int id) {
        WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting = wmsCustomerDevelopmentSettingService.getById(id);
        return BaseResult.ok(wmsCustomerDevelopmentSetting);
    }

    /**
     * 根据查询条件导出客户开发设置表
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出客户开发设置表")
    @PostMapping(path = "/exportWmsCustomerDevelopmentSetting")
    public void exportWmsCustomerDevelopmentSetting(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键ID",
                "应用ID",
                "应用密钥",
                "启用状态：0禁用，1启用",
                "类型：supplierType/commonCarrier/consignee",
                "主体ID(wms_customer_info.id)",
                "供应商服务IP",
                "供应商服务端口",
                "供应商文件服地址(outboundfillepath)",
                "供应商反馈信息地址",
                "外部接口调用时供应商的系统登录用户名",
                "登录密码",
                "供应商时区",
                "开始时间：定时抓取的开始时间，为空则运行时触发",
                "定时抓取时间间隔（为空表示每日只在指定时间刷新，开始和间隔不能同时为空）",
                "定时抓取时间间隔单位",
                "结束时间：定时抓取于每天的何时停止，为空则不停止",
                "请求参数类型：xml，json",
                "请求参数内容",
                "印度值为1，其他为0",
                "",
                "库存地址",
                "rsa加密公钥",
                "rsa加密私钥",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsCustomerDevelopmentSettingService.queryWmsCustomerDevelopmentSettingForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "客户开发设置表", response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.toString());
        }
    }

}
