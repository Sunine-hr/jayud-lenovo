package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.*;
import com.jayud.oms.mapper.BankAccountMapper;
import com.jayud.oms.mapper.ClientSecretKeyMapper;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.ClientSecretKey;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.BankAccountVO;
import com.jayud.oms.model.vo.ClientSecretKeyVO;
import com.jayud.oms.service.IBankAccountService;
import com.jayud.oms.service.IClientSecretKeyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 客户对外接口秘钥表
 * </p>
 */
@Service
public class ClientSecretKeyServiceImpl extends ServiceImpl<ClientSecretKeyMapper, ClientSecretKey> implements IClientSecretKeyService {


    @Override
    public String bankAccountNum() {
        StringBuilder orderNo = new StringBuilder();
        int count = this.count();
        orderNo.append("BK").append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd"))
                .append(StringUtils.zeroComplement(4, count + 1));
        return orderNo.toString();
    }

    /**
     * //根据客户id查询 客户的秘钥 私钥秘钥解密
     * @param appId
     * @return
     */
    @Override
    public ClientSecretKeyVO findClientSecretKeyOne(String  appId) {
        ClientSecretKey clientSecretKey=null;
        if (Objects.nonNull(appId)) {
            QueryWrapper<ClientSecretKey> condition = new QueryWrapper<>();
            condition.lambda().eq(ClientSecretKey::getAppId, appId);
             clientSecretKey = this.getOne(condition);

        }
        ClientSecretKeyVO convert = ConvertUtil.convert(clientSecretKey, ClientSecretKeyVO.class);
        return convert;
    }

    /**
     * 创建客户秘钥表
     * @param clientSecretKey
     * @return
     */
    @Override
    public Long saveOrUpdateAddr(ClientSecretKey clientSecretKey) {
        if (Objects.isNull(clientSecretKey.getId())) {
            Map<String, String> keys = RSAUtils.createKeys();
            clientSecretKey.setCreateTime(LocalDateTime.now())
                    //公钥
                    .setAppSecret(keys.get("publicKey"))
                    //私钥
                    .setAppPrivateSecret(keys.get("privateKey"))
                    .setAppId(String.valueOf(System.currentTimeMillis()));
            this.save(clientSecretKey);
        }
//        else {
//            this.updateById(clientSecretKey);
//        }
        return clientSecretKey.getId();
    }

    @Override
    public ClientSecretKeyVO findClientSecretOne(String cId) {
        ClientSecretKey clientSecretKey=null;
        if (Objects.nonNull(cId)) {
            QueryWrapper<ClientSecretKey> condition = new QueryWrapper<>();
            condition.lambda().eq(ClientSecretKey::getCustomerInfoId, cId);
            clientSecretKey = this.getOne(condition);

        }
        ClientSecretKeyVO convert = ConvertUtil.convert(clientSecretKey, ClientSecretKeyVO.class);
        return convert;

    }

    @Override
    public ClientSecretKeyVO findClientSecretPublicKeyOne(String publickey) {
        ClientSecretKey clientSecretKey=null;
        if (Objects.nonNull(publickey)) {
            QueryWrapper<ClientSecretKey> condition = new QueryWrapper<>();
            condition.lambda().eq(ClientSecretKey::getAppSecret, publickey);
            clientSecretKey = this.getOne(condition);

        }
        ClientSecretKeyVO convert = ConvertUtil.convert(clientSecretKey, ClientSecretKeyVO.class);
        return convert;
    }

//    @Override
//    public Long saveOrUpdateAddr(BankAccount bankAccount) {
//        if (Objects.isNull(bankAccount.getId())) {
//            bankAccount.setCreateTime(LocalDateTime.now())
//                    .setCreateUser(UserOperator.getToken())
//                    .setStatus(StatusEnum.ENABLE.getCode());
//            this.save(bankAccount);
//        } else {
//            bankAccount.setUpdateTime(LocalDateTime.now())
//                    .setUpdateUser(UserOperator.getToken());
//            this.updateById(bankAccount);
//        }
//        return bankAccount.getId();
//    }

//    @Override
//    public IPage<BankAccountVO> findBankAccountByPage(BankAccountFrom form) {
//        Page<BankAccountVO> page = new Page<>(form.getPageNum(), form.getPageSize());
//        return this.baseMapper.findBankAccountByPage(page, form);
//    }
}
