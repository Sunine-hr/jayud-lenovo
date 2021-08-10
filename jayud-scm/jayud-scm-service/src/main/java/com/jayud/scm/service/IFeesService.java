package com.jayud.scm.service;

import com.jayud.scm.model.po.Fees;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.FeesComboxVO;

import java.util.List;

/**
 * <p>
 * 费用计算公式表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-10
 */
public interface IFeesService extends IService<Fees> {

    List<FeesComboxVO> formulaSettingMaintenance(String modelType);
}
