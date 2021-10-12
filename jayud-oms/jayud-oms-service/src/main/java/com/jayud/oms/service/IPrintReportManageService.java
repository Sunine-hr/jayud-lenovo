package com.jayud.oms.service;

import com.jayud.oms.model.po.PrintReportManage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 打印报表管理 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-08
 */
public interface IPrintReportManageService extends IService<PrintReportManage> {

    List<PrintReportManage> getByCondition(PrintReportManage printReportManage);
}
