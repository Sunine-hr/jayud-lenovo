package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.ClientSecretKey;
import com.jayud.oms.model.vo.ClientSecretKeyVO;

/**
 * 客户对外接口秘钥表
 *
 * @author wh
 * @since 2021-11-10
 */
public interface IClientSecretKeyService extends IService<ClientSecretKey> {

    String bankAccountNum();


    ClientSecretKeyVO findClientSecretKeyOne(String appId);

    Long saveOrUpdateAddr(ClientSecretKey clientSecretKey);

//    IPage<BankAccountVO> findBankAccountByPage(BankAccountFrom form);

    ClientSecretKeyVO findClientSecretOne(String cId);

    ClientSecretKeyVO findClientSecretPublicKeyOne(String publickey);
}
