package com.jayud.scm.service;

import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddAccountBankBillForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AccountBankBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.AccountBankBillVO;

/**
 * <p>
 * 水单主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
public interface IAccountBankBillService extends IService<AccountBankBill> {

    AccountBankBillVO getAccountBankBillById(Integer id);

    boolean saveOrUpdateAccountBankBill(AddAccountBankBillForm form);

    boolean delete(DeleteForm deleteForm);

    CommonResult reviewWaterBill(PermissionForm form);

    CommonResult reverseReviewWaterBill(PermissionForm form);

    boolean arrival(QueryCommonForm form);
}
