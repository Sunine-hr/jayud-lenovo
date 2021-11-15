package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.bo.QueryFleetManagementForm;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.FleetManagement;
import com.jayud.oms.model.vo.BankAccountVO;
import com.jayud.oms.model.vo.CustomerAddrVO;
import com.jayud.oms.model.vo.CustomerAddressVO;
import com.jayud.oms.model.vo.FleetManagementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 银行存款
 * </p>
 */
@Mapper
public interface BankAccountMapper extends BaseMapper<BankAccount> {

    IPage<BankAccountVO> findBankAccountByPage(@Param("page") Page<BankAccountVO> page,
                                               @Param("form") BankAccountFrom form);
}
