package com.jayud.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysOwerPerssion;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.WmsOwerInfoForm;
import com.jayud.wms.model.po.WmsOwerInfo;
import com.jayud.wms.model.vo.WmsOwerInfoVO;
import com.jayud.wms.service.IWmsOwerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 货主信息 控制类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Api(tags = "货主信息")
@RestController
@RequestMapping("/wmsOwerInfo")
public class WmsOwerInfoController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    public IWmsOwerInfoService wmsOwerInfoService;


    /**
     * 分页查询数据
     *
     * @param wmsOwerInfo 查询条件
     * @return
     */
    @SysOwerPerssion(clazz = WmsOwerInfo.class)
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<WmsOwerInfo>> selectPage(WmsOwerInfo wmsOwerInfo,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {

        return BaseResult.ok(wmsOwerInfoService.selectPage(wmsOwerInfo, pageNo, pageSize, req));
    }

    /**
     * 列表查询数据
     *
     * @param wmsOwerInfo 查询条件
     * @return
     */
    @SysOwerPerssion(clazz = WmsOwerInfo.class)
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOwerInfo>> selectList(WmsOwerInfo wmsOwerInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOwerInfoService.selectList(wmsOwerInfo));
    }

    /**
     * 新增
     *
     * @param wmsOwerInfoForm
     **/
    @ApiOperation("新增")
    @PostMapping("/saveOrUpdate")
    public BaseResult add(@Valid @RequestBody WmsOwerInfoForm wmsOwerInfoForm) {

        if (wmsOwerInfoForm.getId() == null) {
            WmsOwerInfo wmsOwerInfo1 = wmsOwerInfoService.getWmsCustomerInfoCodeName(null, wmsOwerInfoForm.getOwerName());
            if (wmsOwerInfo1 != null) {
                return BaseResult.error("货主名称已存在");
            }
            WmsOwerInfo wmsOwerInfo2 = this.wmsOwerInfoService.getWmsCustomerInfoCodeName(wmsOwerInfoForm.getOwerCode(), null);
            if (wmsOwerInfo2 != null) {
                return BaseResult.error("货主编号已存在");
            }
        }

        boolean wmsOwerInfo = wmsOwerInfoService.saveOrUpdateWmsCustomerInfo(wmsOwerInfoForm);

        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 启用/禁用客户
     *
     * @param
     **/
    @ApiOperation(value = "启用/禁用客户地址,或货主")
    @PostMapping(value = "/enableOrDisable")
    public BaseResult enableOrDisable(@RequestBody WmsOwerInfoForm wmsOwerInfoForm) {
        if (wmsOwerInfoForm.getId() == null) {
            return BaseResult.error("id不为空");
        }
        WmsOwerInfo wmsOwerInfo = new WmsOwerInfo();
        wmsOwerInfo.setId(wmsOwerInfoForm.getId());
        wmsOwerInfo.setIsOn(wmsOwerInfoForm.getIsOn());

        wmsOwerInfoService.updateById(wmsOwerInfo);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }
    /**
     * 删除
     *
     * @param id
     **/
    @ApiOperation("删除")
    @PostMapping("/del")
    public BaseResult del(@RequestBody List<Long> id) {
        if (id.size()== 0) {
            return BaseResult.error("id不为空");
        }
        for (int i = 0; i <id.size() ; i++) {
            WmsOwerInfo wmsOwerInfo = new WmsOwerInfo();
            wmsOwerInfo.setId(id.get(i));
            wmsOwerInfo.setIsDeleted(true);

            wmsOwerInfoService.updateById(wmsOwerInfo);
        }

        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult queryById(@RequestParam(name = "id", required = true) Long id) {
        WmsOwerInfoVO wmsOwerInfoVO = null;
        List<Long> list = new ArrayList<>();
        wmsOwerInfoVO = wmsOwerInfoService.getWmsCustomerInfoVOCodeName(id);
        String s = wmsOwerInfoVO.getWarehouseListString();
        String[] a = s.split(",");
        for (int i = 0; i < a.length; i++) {
            list.add(Long.parseLong(a[i]));
        }

        wmsOwerInfoVO.setWarehouseList(list);


        return BaseResult.ok(wmsOwerInfoVO);
    }

//根据仓库ID查询货主
    @ApiOperation("根据仓库ID查询货主信息下拉")
    @GetMapping(value = "/queryWarehouseById")
    public BaseResult queryWarehouseById(@RequestParam(name = "id", required = true) Long id) {
        WmsOwerInfoForm WmsOwerInfoForm=new WmsOwerInfoForm();
        WmsOwerInfoForm.setWarehouseId(id);
        WmsOwerInfoForm.setIsOn(true);//是否启用   启用 true
        List<WmsOwerInfo> wmsOwerInfos = wmsOwerInfoService.selectWmsOwerInfoWarehouseIdList(WmsOwerInfoForm);

        return BaseResult.ok(wmsOwerInfos);
    }


    /**
     * 根据查询条件导出货主信息
     *
     * @param response 响应对象
     * @param wmsOwerInfo 参数Map
     */
    @SysOwerPerssion(clazz = WmsOwerInfo.class)
    @ApiOperation("根据查询条件导出货主信息")
    @PostMapping(value = "/exportWmsOwerInfoLocation")
    public void exportWmsOwerInfoLocation(HttpServletResponse response,@RequestBody WmsOwerInfo wmsOwerInfo,
                                          HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList("货主编号", "货主名称","联系地址", "联系人", "手机号", "电子邮箱","是否可用","创建人","创建时间");
            List<LinkedHashMap<String, Object>> dataList = wmsOwerInfoService.queryWmsOwerInfoForExcel(wmsOwerInfo, wmsOwerInfo.getCurrentPage(), wmsOwerInfo.getPageSize(), req);
            ExcelUtils.exportExcel(headList, dataList, "货主信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }


}
