package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.ClientSecretKey;
import com.jayud.oms.model.vo.BankAccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户对外接口秘钥表
 * </p>
 */
@Mapper
public interface ClientSecretKeyMapper extends BaseMapper<ClientSecretKey> {

//    IPage<BankAccountVO> findBankAccountByPage(@Param("page") Page<BankAccountVO> page,
//                                               @Param("form") BankAccountFrom form);
}
