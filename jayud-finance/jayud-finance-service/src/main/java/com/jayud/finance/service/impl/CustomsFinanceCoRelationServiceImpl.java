package com.jayud.finance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.RedisUtils;
import com.jayud.finance.po.CustomsFinanceCoRelation;
import com.jayud.finance.mapper.CustomsFinanceCoRelationMapper;
import com.jayud.finance.service.ICustomsFinanceCoRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 云报关-财务金蝶中的供应商/客户公司名称对应关系表 服务实现类
 * </p>
 *
 * @author william.chen
 * @since 2020-09-24
 */
@Service
public class CustomsFinanceCoRelationServiceImpl extends ServiceImpl<CustomsFinanceCoRelationMapper, CustomsFinanceCoRelation> implements ICustomsFinanceCoRelationService {

    @Autowired
    RedisUtils redisUtils;
    @Value("${relation-setting.redis-key.yunbaoguan.company}")
    String yunbaoguanCompKey;
    @Value("${relation-setting.redis-key.yunbaoguan.fee}")
    String yunbaoguanFeeKey;


    @Override
    public List<CustomsFinanceCoRelation> list(Map<String, Object> para) {
        String yunbaoguanName = MapUtil.getStr(para, "yunbaoguanName");
        String kingdeeName = MapUtil.getStr(para, "kingdeeName");
        String kingdeeCode = MapUtil.getStr(para, "kingdeeCode");
        QueryWrapper<CustomsFinanceCoRelation> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotEmpty(yunbaoguanName)){
            queryWrapper.lambda().like(CustomsFinanceCoRelation::getYunbaoguanName, yunbaoguanName);
        }
        if(StrUtil.isNotEmpty(kingdeeName)){
            queryWrapper.lambda().like(CustomsFinanceCoRelation::getKingdeeCode, kingdeeName);
        }
        if(StrUtil.isNotEmpty(kingdeeCode)){
            queryWrapper.lambda().like(CustomsFinanceCoRelation::getKingdeeCode, kingdeeCode);
        }
        List<CustomsFinanceCoRelation> list = this.list(queryWrapper);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCustomsFinanceCoRelation(CustomsFinanceCoRelation customsFinanceCoRelation) {
        this.saveOrUpdate(customsFinanceCoRelation);
    }

    @Override
    public void clearCompanyRelationMap() {
        //清理redis
        redisUtils.delete(yunbaoguanCompKey);
    }

    @Override
    public Map<String, CustomsFinanceCoRelation> refreshCompanyRelationMap() {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();
        List<CustomsFinanceCoRelation> list = this.list();
        for (CustomsFinanceCoRelation item : list) {
            if (!coRelationMap.containsKey(item.getYunbaoguanName()) && item.getDeprecated() == 0) {
                coRelationMap.put(item.getYunbaoguanName(), item);
            }
        }
        redisUtils.set(yunbaoguanCompKey, JSONUtil.toJsonStr(coRelationMap), RedisUtils.EXPIRE_THIRTY_MIN);
        return coRelationMap;
    }

    @Override
    public Map<String, CustomsFinanceCoRelation> getCompanyRelationMap() {
        Map<String, CustomsFinanceCoRelation> coRelationMap = new HashMap<>();

        String compJson = redisUtils.get(yunbaoguanCompKey);
        if (StringUtils.isNotEmpty(compJson)) {
            Map<String, JSONObject> map = JSONObject.parseObject(compJson, Map.class);
            coRelationMap = map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toJavaObject(CustomsFinanceCoRelation.class)));
        } else {
            coRelationMap = refreshCompanyRelationMap();
        }
        if (CollectionUtil.isEmpty(coRelationMap)) {
            log.error("客户关系表基础数据加载失败！");
        }
        return coRelationMap;
    }

}
