package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddSeaPortForm;
import com.jayud.oms.model.bo.QuerySeaPortForm;
import com.jayud.oms.model.po.SeaPort;
import com.jayud.oms.mapper.SeaPortMapper;
import com.jayud.oms.model.vo.SeaPortVO;
import com.jayud.oms.service.ISeaPortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 船港口地址表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-30
 */
@Service
public class SeaPortServiceImpl extends ServiceImpl<SeaPortMapper, SeaPort> implements ISeaPortService {

    @Override
    public IPage<SeaPortVO> findByPage(QuerySeaPortForm form) {
        Page<SeaPortVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public SeaPort isCodeExistence(String code) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",code);
        return this.getOne(queryWrapper);
    }

    @Override
    public SeaPort isNameExistence(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",name);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateSeaPort(AddSeaPortForm form) {
        SeaPort convert = ConvertUtil.convert(form, SeaPort.class);
        convert.setCreateTime(LocalDateTime.now());
        convert.setCreateUser(UserOperator.getToken());
        convert.setStatus(1);
        return this.saveOrUpdate(convert);
    }

    @Override
    public boolean deleteSeaPort(Long id) {
        SeaPort seaPort = this.getById(id);
        seaPort.setStatus(0);
        return this.saveOrUpdate(seaPort);
    }

    @Override
    public boolean saveOrUpdateBatchSeaPort(List<AddSeaPortForm> list) {

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getCode() == null || list.get(i).getCode() ==""){
                list.remove(list.get(i));
            }
            if(this.isCodeExistence(list.get(i).getCode()) != null){
                list.remove(list.get(i));
            }
            if(this.isNameExistence(list.get(i).getName()) != null){
                list.remove(list.get(i));
            }
            for (int i1 = i+1; i1 < list.size(); i1++) {
                if(list.get(i).getCode().equals(list.get(i1).getCode())){
                    list.remove(list.get(i1));
                }
                if(list.get(i).getName().equals(list.get(i1).getName())){
                    list.remove(list.get(i1));
                }
            }
        }
        List<SeaPort> seaPorts = ConvertUtil.convertList(list, SeaPort.class);
        for (SeaPort seaPort : seaPorts) {
            seaPort.setStatus(1);
            seaPort.setCreateUser("admin");
            seaPort.setCreateTime(LocalDateTime.now());
        }
        return this.saveOrUpdateBatch(seaPorts);
    }

}
