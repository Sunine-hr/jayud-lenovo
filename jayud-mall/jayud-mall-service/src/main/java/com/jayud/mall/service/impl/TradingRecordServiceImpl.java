package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.TradingRecordMapper;
import com.jayud.mall.model.bo.TradingRecordCZForm;
import com.jayud.mall.model.bo.TradingRecordForm;
import com.jayud.mall.model.bo.TradingRecordQueryForm;
import com.jayud.mall.model.po.TradingRecord;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.TradingRecordVO;
import com.jayud.mall.service.ITradingRecordService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
