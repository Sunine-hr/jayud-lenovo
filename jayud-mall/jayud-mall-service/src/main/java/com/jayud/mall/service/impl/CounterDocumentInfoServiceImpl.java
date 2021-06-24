package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CounterDocumentInfoMapper;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.CounterDocumentInfoForm;
import com.jayud.mall.model.po.CounterDocumentInfo;
import com.jayud.mall.model.vo.CounterDocumentInfoVO;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICounterDocumentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * (提单)柜子对应-文件信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-24
 */
@Service
public class CounterDocumentInfoServiceImpl extends ServiceImpl<CounterDocumentInfoMapper, CounterDocumentInfo> implements ICounterDocumentInfoService {

    @Autowired
    CounterDocumentInfoMapper counterDocumentInfoMapper;
    @Autowired
    OceanCounterMapper oceanCounterMapper;

    @Autowired
    BaseService baseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCounterDocumentInfo(CounterDocumentInfoForm form) {
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        Long counterId = form.getCounterId();
        OceanCounterVO oceanCounterVO = oceanCounterMapper.findOceanCounterById(counterId);
        if(ObjectUtil.isEmpty(oceanCounterVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子不存在");
        }
        Long counterVOId = oceanCounterVO.getId();
        String cntrNo = oceanCounterVO.getCntrNo();
        List<TemplateUrlVO> templateUrls = form.getTemplateUrls();
        CounterDocumentInfo counterDocumentInfo = ConvertUtil.convert(form, CounterDocumentInfo.class);
        counterDocumentInfo.setCounterId(counterVOId);
        counterDocumentInfo.setCntrNo(cntrNo);
        if(CollUtil.isNotEmpty(templateUrls)){
            String json = JSONUtil.toJsonStr(templateUrls);
            counterDocumentInfo.setTemplateUrl(json);//文件地址
        }
        counterDocumentInfo.setUserId(user.getId().intValue());
        counterDocumentInfo.setUserName(user.getName());
        this.saveOrUpdate(counterDocumentInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCounterDocumentInfo(Long id) {
        //柜子文件id(counter_document_info id)
        CounterDocumentInfoVO counterDocumentInfoVO = counterDocumentInfoMapper.findCounterDocumentInfoById(id);
        if(ObjectUtil.isEmpty(counterDocumentInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "文件不存在");
        }
        //删除文件记录
        this.removeById(id);
    }

    @Override
    public List<CounterDocumentInfoVO> findCounterDocumentInfoByCounterId(Long counterId) {
        List<CounterDocumentInfoVO> counterDocumentInfoList = counterDocumentInfoMapper.findCounterDocumentInfoByCounterId(counterId);
        return counterDocumentInfoList;
    }
}
