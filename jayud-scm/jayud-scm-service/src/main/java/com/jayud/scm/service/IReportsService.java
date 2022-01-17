package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddReportsForm;
import com.jayud.scm.model.po.Reports;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.ReportsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-17
 */
public interface IReportsService extends IService<Reports> {

    boolean saveOrUpdateReports(AddReportsForm form);

    List<ReportsVO> getReportsByMenuCode(String actionCode);
}
