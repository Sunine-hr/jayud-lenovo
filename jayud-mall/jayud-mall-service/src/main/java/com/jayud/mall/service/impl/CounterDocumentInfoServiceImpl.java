package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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

import java.util.ArrayList;
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
        if(CollUtil.isNotEmpty(counterDocumentInfoList)){
            counterDocumentInfoList.forEach(counterDocumentInfoVO -> {
                //文件展示
                String templateUrl = counterDocumentInfoVO.getTemplateUrl();
                if(StrUtil.isNotEmpty(templateUrl)){
                    try {
                        List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                        counterDocumentInfoVO.setTemplateUrls(templateUrls);
                        String showTemplateUrl = "";
                        for (int i=0; i<templateUrls.size(); i++){
                            TemplateUrlVO templateUrlVO = templateUrls.get(i);
                            String fileName = templateUrlVO.getFileName();
                            if(i==0){
                                showTemplateUrl = fileName;
                            }else{
                                showTemplateUrl += "," + fileName;
                            }
                        }
                        counterDocumentInfoVO.setTemplateUrl(showTemplateUrl);

                    } catch (Exception e) {
                        counterDocumentInfoVO.setTemplateUrls(new ArrayList<>());
                    }
                }else{
                    counterDocumentInfoVO.setTemplateUrls(new ArrayList<>());
                }
            });
        }
        return counterDocumentInfoList;
    }

    @Override
    public CounterDocumentInfoVO findCounterDocumentInfoById(Long id) {

        CounterDocumentInfoVO counterDocumentInfoVO = counterDocumentInfoMapper.findCounterDocumentInfoById(id);
        //文件展示
        String templateUrl = counterDocumentInfoVO.getTemplateUrl();
        if(StrUtil.isNotEmpty(templateUrl)){
            try {
                List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                counterDocumentInfoVO.setTemplateUrls(templateUrls);
                String showTemplateUrl = "";
                for (int i=0; i<templateUrls.size(); i++){
                    TemplateUrlVO templateUrlVO = templateUrls.get(i);
                    String fileName = templateUrlVO.getFileName();
                    if(i==0){
                        showTemplateUrl = fileName;
                    }else{
                        showTemplateUrl += "," + fileName;
                    }
                }
                counterDocumentInfoVO.setTemplateUrl(showTemplateUrl);

            } catch (Exception e) {
                counterDocumentInfoVO.setTemplateUrls(new ArrayList<>());
            }
        }else{
            counterDocumentInfoVO.setTemplateUrls(new ArrayList<>());
        }
        return counterDocumentInfoVO;
    }
}
