package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.HarbourInfoMapper;
import com.jayud.mall.model.bo.HarbourInfoForm;
import com.jayud.mall.model.po.HarbourInfo;
import com.jayud.mall.model.vo.HarbourInfoVO;
import com.jayud.mall.service.IHarbourInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 机场、港口信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class HarbourInfoServiceImpl extends ServiceImpl<HarbourInfoMapper, HarbourInfo> implements IHarbourInfoService {

    @Autowired
    HarbourInfoMapper harbourInfoMapper;


    @Override
    public List<HarbourInfoVO> findHarbourInfo(HarbourInfoForm form) {
        QueryWrapper<HarbourInfo> queryWrapper = new QueryWrapper<>();
        String idCode = form.getIdCode();
        String codeName = form.getCodeName();
        String codeNameEn = form.getCodeNameEn();
        String stateCode = form.getStateCode();
        Integer genre = form.getGenre();
        String status = form.getStatus();
        if(idCode != null && idCode != ""){
            queryWrapper.like("id_code", idCode);
        }
        if(codeName != null && codeName != ""){
            queryWrapper.like("code_name", codeName);
        }
        if(codeNameEn != null && codeNameEn != ""){
            queryWrapper.like("code_name_en", codeNameEn);
        }
        if(stateCode != null && stateCode != ""){
            queryWrapper.like("state_code", stateCode);
        }
        if(genre != null){
            queryWrapper.eq("genre", genre);
        }
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<HarbourInfo> list = harbourInfoMapper.selectList(queryWrapper);
        List<HarbourInfoVO> harbourInfoVOS = ConvertUtil.convertList(list, HarbourInfoVO.class);
        return harbourInfoVOS;
    }
}
