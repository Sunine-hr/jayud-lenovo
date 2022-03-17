package com.jayud.wms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsCustomerDevelopmentSetting;
import com.jayud.wms.mapper.WmsCustomerDevelopmentSettingMapper;
import com.jayud.wms.service.IWmsCustomerDevelopmentSettingService;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户开发设置表 服务实现类
 *
 * @author jyd
 * @since 2022-02-14
 */
@Service
public class WmsCustomerDevelopmentSettingServiceImpl extends ServiceImpl<WmsCustomerDevelopmentSettingMapper, WmsCustomerDevelopmentSetting> implements IWmsCustomerDevelopmentSettingService {


    @Autowired
    private WmsCustomerDevelopmentSettingMapper wmsCustomerDevelopmentSettingMapper;

    @Override
    public IPage<WmsCustomerDevelopmentSetting> selectPage(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsCustomerDevelopmentSetting> page=new Page<WmsCustomerDevelopmentSetting>(pageNo, pageSize);
        IPage<WmsCustomerDevelopmentSetting> pageList= wmsCustomerDevelopmentSettingMapper.pageList(page, wmsCustomerDevelopmentSetting);
        return pageList;
    }

    @Override
    public List<WmsCustomerDevelopmentSetting> selectList(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting){
        return wmsCustomerDevelopmentSettingMapper.list(wmsCustomerDevelopmentSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsCustomerDevelopmentSetting saveOrUpdateWmsCustomerDevelopmentSetting(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting) {
        Long id = wmsCustomerDevelopmentSetting.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            wmsCustomerDevelopmentSetting.setCreateBy(CurrentUserUtil.getUsername());
            wmsCustomerDevelopmentSetting.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<WmsCustomerDevelopmentSetting> wmsCustomerDevelopmentSettingQueryWrapper = new QueryWrapper<>();
            //wmsCustomerDevelopmentSettingQueryWrapper.lambda().eq(WmsCustomerDevelopmentSetting::getCode, wmsCustomerDevelopmentSetting.getCode());
            //wmsCustomerDevelopmentSettingQueryWrapper.lambda().eq(WmsCustomerDevelopmentSetting::getIsDeleted, 0);
            //List<WmsCustomerDevelopmentSetting> list = this.list(wmsCustomerDevelopmentSettingQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        }else{
            //修改 --> update 更新人、更新时间
            wmsCustomerDevelopmentSetting.setUpdateBy(CurrentUserUtil.getUsername());
            wmsCustomerDevelopmentSetting.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<WmsCustomerDevelopmentSetting> wmsCustomerDevelopmentSettingQueryWrapper = new QueryWrapper<>();
            //wmsCustomerDevelopmentSettingQueryWrapper.lambda().ne(WmsCustomerDevelopmentSetting::getId, id);
            //wmsCustomerDevelopmentSettingQueryWrapper.lambda().eq(WmsCustomerDevelopmentSetting::getCode, wmsCustomerDevelopmentSetting.getCode());
            //wmsCustomerDevelopmentSettingQueryWrapper.lambda().eq(WmsCustomerDevelopmentSetting::getIsDeleted, 0);
            //List<WmsCustomerDevelopmentSetting> list = this.list(wmsCustomerDevelopmentSettingQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(wmsCustomerDevelopmentSetting);
        return wmsCustomerDevelopmentSetting;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delWmsCustomerDevelopmentSetting(int id) {
        WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(wmsCustomerDevelopmentSetting)){
            throw new IllegalArgumentException("客户开发设置表不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        wmsCustomerDevelopmentSetting.setUpdateBy(CurrentUserUtil.getUsername());
        wmsCustomerDevelopmentSetting.setUpdateTime(new Date());
        wmsCustomerDevelopmentSetting.setIsDeleted(true);
        this.saveOrUpdate(wmsCustomerDevelopmentSetting);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWmsCustomerDevelopmentSettingForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsCustomerDevelopmentSettingForExcel(paramMap);
    }

}
