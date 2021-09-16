package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddAcctPayForm;
import com.jayud.scm.model.bo.AddAcctPayReceiptForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.po.AcctPay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.AcctPayVO;

/**
 * <p>
 * 付款单主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
public interface IAcctPayService extends IService<AcctPay> {

    boolean generatePaymentDocument(AddAcctPayReceiptForm form);

    AcctPayVO getAcctPayById(Integer id);

    boolean saveOrUpdateAcctPay(AddAcctPayForm form);

    boolean delete(DeleteForm deleteForm);

    AcctPay getAcctPayByPayToMeId(Integer id);
}
