package com.jayud.scm.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.AcctReceiptMapper;
import com.jayud.scm.model.vo.AccountBankBillVO;
import com.jayud.scm.model.vo.AcctReceiptVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收款单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Service
public class AcctReceiptServiceImpl extends ServiceImpl<AcctReceiptMapper, AcctReceipt> implements IAcctReceiptService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IAcctReceiptFollowService acctReceiptFollowService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Override
    public AcctReceipt getAcctReceiptByJoinBillId(Integer id) {
        QueryWrapper<AcctReceipt> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AcctReceipt::getJoinBillId,id);
        queryWrapper.lambda().eq(AcctReceipt::getVoided,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean generateCollectionDoc(AccountBankBill accountBankBill) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceipt acctReceipt = ConvertUtil.convert(accountBankBill, AcctReceipt.class);
        acctReceipt.setId(null);
        acctReceipt.setFbillNo(commodityService.getOrderNo(NoCodeEnum.ACCT_RECEIPT.getCode(), LocalDateTime.now()));
        acctReceipt.setCrtBy(systemUser.getId().intValue());
        acctReceipt.setCrtByDtm(LocalDateTime.now());
        acctReceipt.setCrtByName(systemUser.getUserName());
        acctReceipt.setJoinBillId(accountBankBill.getId());
        acctReceipt.setAccRate(new BigDecimal(this.baseMapper.fnGetCurrency(LocalDateTime.now(),ibDataDicEntryService.getTextByDicCodeAndDataValue("1003",acctReceipt.getCurrencyName()),0)));
        boolean save = this.save(acctReceipt);
        log.warn("生成收款单成功");
        return save;
    }

    @Override
    public AcctReceiptVO getAcctReceiptById(Integer id) {
        AcctReceipt acctReceipt = this.getById(id);
        AcctReceiptVO acctReceiptVO = ConvertUtil.convert(acctReceipt, AcctReceiptVO.class);
        return acctReceiptVO;
    }

    @Override
    public boolean saveOrUpdateAcctReceipt(AddAcctReceiptForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
        AcctReceipt acctReceipt = ConvertUtil.convert(form, AcctReceipt.class);
        if(form.getId() != null){
            acctReceipt.setMdyBy(systemUser.getId().intValue());
            acctReceipt.setMdyByDtm(LocalDateTime.now());
            acctReceipt.setMdyByName(systemUser.getUserName());
            acctReceiptFollow.setSType(OperationEnum.UPDATE.getCode());
            acctReceiptFollow.setFollowContext("修改收款单信息，收款单编号为"+acctReceipt.getFbillNo());
        }else{
            acctReceipt.setFbillNo(commodityService.getOrderNo(NoCodeEnum.ACCT_RECEIPT.getCode(),LocalDateTime.now()));
            acctReceipt.setCrtBy(systemUser.getId().intValue());
            acctReceipt.setCrtByDtm(LocalDateTime.now());
            acctReceipt.setCrtByName(systemUser.getUserName());
            acctReceiptFollow.setSType(OperationEnum.INSERT.getCode());
            acctReceiptFollow.setFollowContext("新增收款单信息，收款单编号为"+acctReceipt.getFbillNo());
        }
        boolean result = this.saveOrUpdate(acctReceipt);
        if(result){
            log.warn("新增或修改收款单信息成功");
            acctReceiptFollow.setReceiptId(acctReceipt.getId());
            acctReceiptFollow.setCrtBy(systemUser.getId().intValue());
            acctReceiptFollow.setCrtByDtm(LocalDateTime.now());
            acctReceiptFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.acctReceiptFollowService.save(acctReceiptFollow);
            if(save){
                log.warn("新增或修改，操作记录新增成功");
            }
        }
        return result;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<AcctReceiptFollow> acctReceiptFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
            acctReceiptFollow.setReceiptId(id.intValue());
            acctReceiptFollow.setSType(OperationEnum.DELETE.getCode());
            acctReceiptFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            acctReceiptFollow.setCrtBy(deleteForm.getId().intValue());
            acctReceiptFollow.setCrtByDtm(deleteForm.getDeleteTime());
            acctReceiptFollow.setCrtByName(deleteForm.getName());
            acctReceiptFollows.add(acctReceiptFollow);
        }

        boolean b1 = acctReceiptFollowService.saveBatch(acctReceiptFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+acctReceiptFollows);
        }
        return b1;
    }

    @Override
    public boolean lockAmount(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceipt acctReceipt = new AcctReceipt();
        acctReceipt.setId(form.getId());
        acctReceipt.setLockMoney(form.getLockMoney());
        acctReceipt.setMdyBy(systemUser.getId().intValue());
        acctReceipt.setMdyByDtm(LocalDateTime.now());
        acctReceipt.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctReceipt);
        if(result){
            log.warn("锁定金额成功");
            AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
            acctReceiptFollow.setReceiptId(acctReceipt.getId());
            acctReceiptFollow.setSType(OperationEnum.UPDATE.getCode());
            acctReceiptFollow.setFollowContext(systemUser.getUserName()+"锁定金额"+form.getLockMoney()+";原因为"+(form.getReason() != null?form.getReason():""));
            acctReceiptFollow.setCrtBy(systemUser.getId().intValue());
            acctReceiptFollow.setCrtByDtm(LocalDateTime.now());
            acctReceiptFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.acctReceiptFollowService.save(acctReceiptFollow);
            if(save){
                log.warn("锁定金额，操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean modifyExchangeRate(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceipt acctReceipt = new AcctReceipt();
        acctReceipt.setId(form.getId());
        acctReceipt.setAccRate(form.getAccRate());
        acctReceipt.setMdyBy(systemUser.getId().intValue());
        acctReceipt.setMdyByDtm(LocalDateTime.now());
        acctReceipt.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctReceipt);
        if(result){
            log.warn("修改汇率成功");
            AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
            acctReceiptFollow.setReceiptId(acctReceipt.getId());
            acctReceiptFollow.setSType(OperationEnum.UPDATE.getCode());
            acctReceiptFollow.setFollowContext(systemUser.getUserName()+"修改汇率");
            acctReceiptFollow.setCrtBy(systemUser.getId().intValue());
            acctReceiptFollow.setCrtByDtm(LocalDateTime.now());
            acctReceiptFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.acctReceiptFollowService.save(acctReceiptFollow);
            if(save){
                log.warn("修改汇率，操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean releaseLimit(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceipt acctReceipt = new AcctReceipt();
        acctReceipt.setId(form.getId());
        acctReceipt.setCreditFlag(1);
        acctReceipt.setMdyBy(systemUser.getId().intValue());
        acctReceipt.setMdyByDtm(LocalDateTime.now());
        acctReceipt.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctReceipt);
        if(result){
            log.warn("释放额度成功");
            AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
            acctReceiptFollow.setReceiptId(acctReceipt.getId());
            acctReceiptFollow.setSType(OperationEnum.UPDATE.getCode());
            acctReceiptFollow.setFollowContext(systemUser.getUserName()+"释放额度");
            acctReceiptFollow.setCrtBy(systemUser.getId().intValue());
            acctReceiptFollow.setCrtByDtm(LocalDateTime.now());
            acctReceiptFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.acctReceiptFollowService.save(acctReceiptFollow);
            if(save){
                log.warn("释放额度，操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean cancelClaim(QueryCommonForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceipt acctReceipt = new AcctReceipt();
        acctReceipt.setId(form.getId());
        acctReceipt.setIsClaim(0);
        acctReceipt.setMdyBy(systemUser.getId().intValue());
        acctReceipt.setMdyByDtm(LocalDateTime.now());
        acctReceipt.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctReceipt);
        if(result){
            log.warn("取消认领成功");
            AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
            acctReceiptFollow.setReceiptId(acctReceipt.getId());
            acctReceiptFollow.setSType(OperationEnum.UPDATE.getCode());
            acctReceiptFollow.setFollowContext(systemUser.getUserName()+"取消认领");
            acctReceiptFollow.setCrtBy(systemUser.getId().intValue());
            acctReceiptFollow.setCrtByDtm(LocalDateTime.now());
            acctReceiptFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.acctReceiptFollowService.save(acctReceiptFollow);
            if(save){
                log.warn("取消认领，操作日志添加成功");
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importAcctReceipt(MultipartFile file) {

        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //配置别名
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("付款名称","payCompamyName");
        aliasMap.put("客户名称","customerName");
        aliasMap.put("银行名称","bankName");
        aliasMap.put("银行账号","bankNum");
        aliasMap.put("到账金额","bankMoney");
        aliasMap.put("收款币别","bankCurrency");
        aliasMap.put("手续费","shouFee");
        aliasMap.put("备注","remark");
        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<AddAcctReceiptModelForm> list= excelReader.read(0, 2, AddAcctReceiptModelForm.class);
        log.warn("导入的数据"+list);

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<AcctReceipt> acctReceipts = ConvertUtil.convertList(list, AcctReceipt.class);
        for (AcctReceipt acctReceipt : acctReceipts) {
            acctReceipt.setFbillNo(commodityService.getOrderNo(NoCodeEnum.ACCT_RECEIPT.getCode(), LocalDateTime.now()));
            acctReceipt.setCrtBy(systemUser.getId().intValue());
            acctReceipt.setCrtByDtm(LocalDateTime.now());
            acctReceipt.setCrtByName(systemUser.getUserName());
        }
        boolean save = this.saveBatch(acctReceipts);
        if(save){
            log.warn("导入数据成功:"+acctReceipts);
        }

    }

    @Override
    public boolean claim(AddAcctReceiptClaimForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctReceipt acctReceipt = ConvertUtil.convert(form, AcctReceipt.class);
        acctReceipt.setIsClaim(1);
        acctReceipt.setClaimId(systemUser.getId().intValue());
        acctReceipt.setClaimDate(LocalDateTime.now());
        acctReceipt.setClaimName(systemUser.getUserName());
        acctReceipt.setMdyBy(systemUser.getId().intValue());
        acctReceipt.setMdyByDtm(LocalDateTime.now());
        acctReceipt.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctReceipt);
        if(result){
            log.warn("认领成功");
            AcctReceiptFollow acctReceiptFollow = new AcctReceiptFollow();
            acctReceiptFollow.setReceiptId(acctReceipt.getId());
            acctReceiptFollow.setSType(OperationEnum.UPDATE.getCode());
            acctReceiptFollow.setFollowContext(systemUser.getUserName()+"认领成功");
            acctReceiptFollow.setCrtBy(systemUser.getId().intValue());
            acctReceiptFollow.setCrtByDtm(LocalDateTime.now());
            acctReceiptFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.acctReceiptFollowService.save(acctReceiptFollow);
            if(save){
                log.warn("认领，操作日志添加成功");
            }
        }
        return result;
    }
}
