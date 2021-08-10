package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddFeeForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.Fee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.FeeVO;

import java.util.List;

/**
 * <p>
 * 结算方案条款 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
public interface IFeeService extends IService<Fee> {

    IPage<FeeVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateFee(AddFeeForm form);

    FeeVO getFeeById(Integer id);

    boolean deleteFeeByIds(DeleteForm form);

    List<Long> getFeeByFeeModelId(Integer id);

    boolean copyFee(Integer id);
}
