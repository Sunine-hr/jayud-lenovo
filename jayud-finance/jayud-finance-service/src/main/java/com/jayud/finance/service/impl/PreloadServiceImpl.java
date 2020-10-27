package com.jayud.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.jayud.finance.po.CustomsFinanceFeeRelation;
import com.jayud.finance.service.ICustomsFinanceCoRelationService;
import com.jayud.finance.service.ICustomsFinanceFeeRelationService;
import com.jayud.finance.service.PreloadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PreloadServiceImpl implements PreloadService {
    @Autowired
    RedisUtils redisUtils;
    @Value("${relation-setting.redis-key.yunbaoguan.company}")
    String yunbaoguanCompKey;
    @Value("${relation-setting.redis-key.yunbaoguan.fee}")
    String yunbaoguanFeeKey;

    @Autowired
    ICustomsFinanceCoRelationService coRelationService;

    @Autowired
    ICustomsFinanceFeeRelationService feeRelationService;

    @Override
    public Map<String, CustomsFinanceCoRelation> getCompanyRelationMap() {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();

        String compJson = redisUtils.get(yunbaoguanCompKey);
        if (StringUtils.isNotEmpty(compJson)) {
            coRelationMap = (Map<String, CustomsFinanceCoRelation>) JSONObject.parseObject(compJson, Map.class);
        } else {
            coRelationMap = refreshCompanyRelationMap();
        }
        if (CollectionUtil.isEmpty(coRelationMap)) {
            log.error("客户关系表基础数据加载失败！");
        }
        return coRelationMap;
    }

    @Override
    public Map<String, CustomsFinanceFeeRelation> getFeeRelationMap() {
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = new HashMap<>();

        String feeJson = redisUtils.get(yunbaoguanFeeKey);
        if (StringUtils.isNotEmpty(feeJson)) {
            feeRelationMap = (Map<String, CustomsFinanceFeeRelation>) JSONObject.parseObject(feeJson, Map.class);
        } else {
            feeRelationMap = refreshFeeRelationMap();
        }
        if (CollectionUtil.isEmpty(feeRelationMap)) {
            log.error("费用关系表基础数据加载失败！");
        }
        return feeRelationMap;
    }

    @Override
    public Map<String, CustomsFinanceCoRelation> refreshCompanyRelationMap() {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        List<CustomsFinanceCoRelation> list = coRelationService.list();
        for (CustomsFinanceCoRelation item : list) {
            if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                coRelationMap.put(item.getYunbaoguanName(), item);
            }
        }
        redisUtils.set(yunbaoguanCompKey, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        return coRelationMap;
    }

    @Override
    public Map<String, CustomsFinanceFeeRelation> refreshFeeRelationMap() {
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = new HashMap<>();
        List<CustomsFinanceFeeRelation> list = feeRelationService.list();
        for (CustomsFinanceFeeRelation item : list) {
            if (!feeRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                feeRelationMap.put(item.getYunbaoguanName(), item);
            }
        }
        redisUtils.set(yunbaoguanFeeKey, JSONUtil.toJsonStr(feeRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        return feeRelationMap;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, CustomsFinanceCoRelation> addCompanyRelation(CustomsFinanceCoRelation coRelation) {
        coRelationService.save(coRelation);
        return refreshCompanyRelationMap();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, CustomsFinanceFeeRelation> addFeeRelation(CustomsFinanceFeeRelation feeRelation) {
        feeRelationService.save(feeRelation);
        return refreshFeeRelationMap();
    }

}
