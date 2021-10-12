package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.PrintReportManage;
import com.jayud.oms.service.IPrintReportManageService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 打印报表管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-08
 */
@RestController
@RequestMapping("/printReportManage")
public class PrintReportManageController {

    @Autowired
    private IPrintReportManageService printReportManageService;

    /**
     * 根据唯一标识返回模板链接
     */
    @ApiOperation(value = "根据唯一标识返回模板链接")
    @PostMapping(value = "/getUrlByiUniqueIden")
    public CommonResult<String> getUrlByiUniqueIden(@RequestBody Map<String, Object> map) {
        String code = MapUtil.getStr(map, "code");
        if (StringUtils.isEmpty(code)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<PrintReportManage> list = this.printReportManageService.getByCondition(new PrintReportManage().setUniqueIden(code));

        if (CollectionUtils.isEmpty(list)) {
            return CommonResult.error(400, "没有绑定模板地址");
        }
        PrintReportManage printReportManage = list.get(0);
        String[] paramKeys = printReportManage.getParams().split(",");
        if (paramKeys.length > 0) {
            StringBuilder sb = new StringBuilder();
            if (printReportManage.getTemplateUrl().contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }

            for (String paramKey : paramKeys) {
                Object value = map.get(paramKey);
                sb.append(paramKey).append("=").append(value).append("&");
            }
            String str = sb.toString();
            if (sb.toString().endsWith("&")) {
                str = sb.substring(0, sb.length() - 1);
            }
            printReportManage.setTemplateUrl(printReportManage.getTemplateUrl() + str);
        }
        return CommonResult.success(printReportManage.getTemplateUrl());
    }


}

