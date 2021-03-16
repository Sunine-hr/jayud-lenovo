package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.AccountBalanceMapper;
import com.jayud.mall.mapper.TradingRecordMapper;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.AccountBalance;
import com.jayud.mall.model.po.TradingRecord;
import com.jayud.mall.model.vo.AccountBalanceVO;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.TradingRecordVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IAccountBalanceService;
import com.jayud.mall.service.ITradingRecordService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 交易记录表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
@Service
public class TradingRecordServiceImpl extends ServiceImpl<TradingRecordMapper, TradingRecord> implements ITradingRecordService {

    @Autowired
    TradingRecordMapper tradingRecordMapper;
    @Autowired
    AccountBalanceMapper accountBalanceMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    IAccountBalanceService accountBalanceService;

    //客户充值
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void customerPay(TradingRecordForm form) {
        Long id = form.getId();
        TradingRecord tradingRecord = ConvertUtil.convert(form, TradingRecord.class);
        if(ObjectUtil.isEmpty(id)){
            //新增
            tradingRecord.setTradingType(1);//交易类型(1充值 2支付)
            String tradingNo = NumberGeneratedUtils.getOrderNoByCode2("TradingNo_CZ");//交易单号(充值)
            tradingRecord.setTradingNo(tradingNo);//交易单号
            tradingRecord.setCreateTime(LocalDateTime.now());//创建时间
            tradingRecord.setStatus("0");//状态(0待审核 1审核通过 2审核不通过)
            tradingRecord.setRemark("充值流水号:"+tradingRecord.getSerialNumber());//交易备注
        }
        List<TemplateUrlVO> voucherUrls = form.getVoucherUrls();
        if(CollUtil.isNotEmpty(voucherUrls)){
            String s = JSONObject.toJSONString(voucherUrls);
            tradingRecord.setVoucherUrl(s);//交易凭证(url)
        }
        this.saveOrUpdate(tradingRecord);
    }

    @Override
    public List<TradingRecordVO> findTradingRecordByCz(TradingRecordCZForm form) {
        List<TradingRecordVO> tradingRecordVOS = tradingRecordMapper.findTradingRecordByCz(form);
        tradingRecordVOS.forEach(tradingRecordVO -> {
            String voucherUrl = tradingRecordVO.getVoucherUrl();
            if(ObjectUtil.isNotEmpty(voucherUrl)){
                String json = voucherUrl;
                try {
                    List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                    });
                    tradingRecordVO.setVoucherUrls(templateUrlVOS);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("json格式错误");
                    tradingRecordVO.setVoucherUrls(new ArrayList<>());
                }
            }else{
                tradingRecordVO.setVoucherUrls(new ArrayList<>());
            }
        });
        return tradingRecordVOS;
    }

    @Override
    public List<TradingRecordVO> findTradingRecord(TradingRecordQueryForm form) {
        List<TradingRecordVO> tradingRecordVOS = tradingRecordMapper.findTradingRecord(form);
        tradingRecordVOS.forEach(tradingRecordVO -> {
            String voucherUrl = tradingRecordVO.getVoucherUrl();
            if(ObjectUtil.isNotEmpty(voucherUrl)){
                String json = voucherUrl;
                try {
                    List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                    });
                    tradingRecordVO.setVoucherUrls(templateUrlVOS);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("json格式错误");
                    tradingRecordVO.setVoucherUrls(new ArrayList<>());
                }
            }else{
                tradingRecordVO.setVoucherUrls(new ArrayList<>());
            }
        });
        return tradingRecordVOS;
    }

    @Override
    public IPage<TradingRecordVO> findTradingRecordCZByPage(QueryTradingRecordCZForm form) {
        //定义分页参数
        Page<TradingRecordVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<TradingRecordVO> pageInfo = tradingRecordMapper.findTradingRecordCZByPage(page, form);
        List<TradingRecordVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(tradingRecordVO -> {
                String voucherUrl = tradingRecordVO.getVoucherUrl();
                if(ObjectUtil.isNotEmpty(voucherUrl)){
                    String json = voucherUrl;
                    try {
                        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                        });
                        tradingRecordVO.setVoucherUrls(templateUrlVOS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("json格式错误");
                        tradingRecordVO.setVoucherUrls(new ArrayList<>());
                    }
                }else{
                    tradingRecordVO.setVoucherUrls(new ArrayList<>());
                }
            });
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<TradingRecordVO> auditTradingRecordCZ(AuditTradingRecordCZForm form) {
        Long id = form.getId();
        TradingRecordVO tradingRecordVO = tradingRecordMapper.findTradingRecordById(id);
        if(ObjectUtil.isEmpty(tradingRecordVO)){
            return CommonResult.error(-1, "充值记录未找到");
        }
        BigDecimal amount = tradingRecordVO.getAmount();
        Long cid = tradingRecordVO.getCid();
        String serialNumber = tradingRecordVO.getSerialNumber();
        String status = tradingRecordVO.getStatus();
        BigDecimal amountFront = form.getAmount();
        Long cidFront = form.getCid();
        String serialNumberFront = form.getSerialNumber();
        String statusFront = form.getStatus();//状态(0待审核 1审核通过 2审核不通过)
        String remarkFront = form.getRemark();
        //选择审核通过时，需要判断交易流水号是否一致
        if(statusFront.equals("1")){
            if(amount.compareTo(amountFront) != 0 || !cid.equals(cidFront) || !serialNumber.equals(serialNumberFront)){
                return CommonResult.error(-1, "充值金额、币种、交易流水号不一致，请核对数据");
            }
        }
        //选择审核不通过时，金额流水号非必填，备注必填
        if(statusFront.equals("2")){
            if(ObjectUtil.isEmpty(remarkFront)){
                return CommonResult.error(-1, "审核不通过时，备注信息必填");
            }
        }
        if(!status.equals("0")){
            return CommonResult.error(-1, "状态错误,只有待审核状态才能进行审核");
        }

        AuthUser user = baseService.getUser();

        //1.修改审核状态
        tradingRecordVO.setStatus(statusFront);//状态
        tradingRecordVO.setRemark(remarkFront);//备注
        tradingRecordVO.setAuditor(user.getId().intValue());//审核人
        tradingRecordVO.setAuditTime(LocalDateTime.now());//审核时间
        TradingRecord tradingRecord = ConvertUtil.convert(tradingRecordVO, TradingRecord.class);
        this.saveOrUpdate(tradingRecord);
        //2.修改账户余额
        if(statusFront.equals("1")){//状态(0待审核 1审核通过 2审核不通过)
            Long customerId = tradingRecordVO.getCustomerId();
            AccountBalanceVO accountBalanceVO = accountBalanceMapper.findAccountBalanceByCustomerIdAndCid(customerId, cid);
            if(ObjectUtil.isNotEmpty(accountBalanceVO)){
                //存在对应货币的余额
                BigDecimal amountBase = accountBalanceVO.getAmount();
                BigDecimal result = amountBase.add(amount);//基础余额+充值金额
                AccountBalance accountBalance = ConvertUtil.convert(accountBalanceVO, AccountBalance.class);
                accountBalance.setAmount(result);
                accountBalanceService.saveOrUpdate(accountBalance);
            }else{
                //不存在对应货币的余额，
                AccountBalance accountBalance = new AccountBalance();
                accountBalance.setCustomerId(customerId);
                accountBalance.setCid(cid);
                accountBalance.setAmount(amount);
                accountBalanceService.saveOrUpdate(accountBalance);
            }
        }
        TradingRecordVO convert = ConvertUtil.convert(tradingRecord, TradingRecordVO.class);
        return CommonResult.success(convert);
    }
}
