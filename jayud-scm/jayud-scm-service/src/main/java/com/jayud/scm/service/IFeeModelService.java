package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddFeeModelForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.FeeModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.FeeModelVO;

/**
 * <p>
 * 结算方案 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
public interface IFeeModelService extends IService<FeeModel> {

    IPage<FeeModelVO> findByPage(QueryCommonForm form);

    boolean addFeeModel(AddFeeModelForm form);

    boolean modifyDefaultValues(AddFeeModelForm form);

    boolean deleteFeeByIds(DeleteForm form);
}
