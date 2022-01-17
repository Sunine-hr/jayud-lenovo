package com.jayud.scm.service;

import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.AccountBankBill;
import com.jayud.scm.model.po.AcctReceipt;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.AcctReceiptVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 收款单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
public interface IAcctReceiptService extends IService<AcctReceipt> {

    AcctReceipt getAcctReceiptByJoinBillId(Integer id);

    boolean generateCollectionDoc(AccountBankBill accountBankBill);

    AcctReceiptVO getAcctReceiptById(Integer id);

    boolean saveOrUpdateAcctReceipt(AddAcctReceiptForm form);

    boolean delete(DeleteForm deleteForm);

    boolean lockAmount(QueryCommonForm form);

    boolean modifyExchangeRate(QueryCommonForm form);

    boolean releaseLimit(QueryCommonForm form);

    boolean cancelClaim(QueryCommonForm form);

    void importAcctReceipt(MultipartFile file);

    boolean claim(AddAcctReceiptClaimForm form);

    CommonResult reverseReviewAcctReceipt(PermissionForm form);
}
