package com.jayud.wms.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.utils.AppUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.WmsCustomerInfoMapper;
import com.jayud.wms.model.bo.WmsCustomerInfoForm;
import com.jayud.wms.model.dto.WmsCustomerInfoDTO;
import com.jayud.wms.model.po.WmsCustomerDevelopmentSetting;
import com.jayud.wms.model.po.WmsCustomerInfo;
import com.jayud.wms.model.vo.WmsCustomerInfoVO;
import com.jayud.wms.service.IWmsCustomerDevelopmentSettingService;
import com.jayud.wms.service.IWmsCustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 租户信息的业务逻辑实现层
 */
@Service
public class WmsCustomerInfoServiceImpl extends ServiceImpl<WmsCustomerInfoMapper, WmsCustomerInfo> implements IWmsCustomerInfoService {


    @Autowired
    private WmsCustomerInfoMapper wmsCustomerInfoMapper;
    @Autowired
    public IWmsCustomerDevelopmentSettingService wmsCustomerDevelopmentSettingService;
    @Autowired
    private AuthClient authClient;

    @Override
    public IPage<WmsCustomerInfoVO> selectPage(WmsCustomerInfo wmsCustomerInfo, Integer pageNo, Integer pageSize, HttpServletRequest req) {


        Page<WmsCustomerInfo> page = new Page<WmsCustomerInfo>(pageNo, pageSize);

        IPage<WmsCustomerInfoVO> pageList = wmsCustomerInfoMapper.pageList(page, wmsCustomerInfo);
        return pageList;
    }

    @Override
    public List<WmsCustomerInfoVO> selectList(WmsCustomerInfo wmsCustomerInfo) {
        List<WmsCustomerInfoVO> wmsCustomerInfos = wmsCustomerInfoMapper.selectList(wmsCustomerInfo);
        return wmsCustomerInfos;
    }

    @Override
    public void saveOrUpdateWmsCustomerInfo(WmsCustomerInfoForm wmsCustomerInfoForm) {

        WmsCustomerInfo wmsCustomerInfoOne = ConvertUtil.convert(wmsCustomerInfoForm, WmsCustomerInfo.class);
        if(wmsCustomerInfoOne.getId() != null){
            wmsCustomerInfoOne.setUpdateBy(CurrentUserUtil.getUsername());
            wmsCustomerInfoOne.setStatus(wmsCustomerInfoForm.getStatus());
            wmsCustomerInfoOne.setUpdateTime(new Date());
            this.wmsCustomerInfoMapper.updateById(wmsCustomerInfoOne);
        }else{
            wmsCustomerInfoOne.setCreateBy(CurrentUserUtil.getUsername());
            wmsCustomerInfoOne.setCreateTime(new Date());
            this.saveOrUpdate(wmsCustomerInfoOne);
        }

        List<SysDictItem> dictList = authClient.selectItemByDictCode("clientType").getResult();
        Map<Long, SysDictItem> clientTypeDict = dictList.stream().collect(Collectors.toMap(x->x.getId(),x->x));
        //给客户配置开发appId和appSecret
        Long customerInfoId = wmsCustomerInfoOne.getId();
        Long customerTypeId = wmsCustomerInfoOne.getCustomerTypeId();
        QueryWrapper<WmsCustomerDevelopmentSetting> wmsCustomerDevelopmentSettingQueryWrapper = new QueryWrapper<>();
        wmsCustomerDevelopmentSettingQueryWrapper.lambda().eq(WmsCustomerDevelopmentSetting::getMainId, customerInfoId);
        wmsCustomerDevelopmentSettingQueryWrapper.lambda().groupBy(WmsCustomerDevelopmentSetting::getMainId);
        WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting = wmsCustomerDevelopmentSettingService.getOne(wmsCustomerDevelopmentSettingQueryWrapper);
        if(ObjectUtil.isEmpty(wmsCustomerDevelopmentSetting)){
            String appId = AppUtils.getAppId();
            String appSecret = AppUtils.getAppSecret(appId);
            wmsCustomerDevelopmentSetting = new WmsCustomerDevelopmentSetting();
            wmsCustomerDevelopmentSetting.setAppId(appId);
            wmsCustomerDevelopmentSetting.setAppSecret(appSecret);
            SysDictItem sysDictItem = clientTypeDict.get(customerTypeId);

            wmsCustomerDevelopmentSetting.setType(sysDictItem.getItemValue());
            wmsCustomerDevelopmentSetting.setMainId(customerInfoId);
            //新增 --> add 创建人、创建时间
            wmsCustomerDevelopmentSetting.setCreateBy(CurrentUserUtil.getUsername());
            wmsCustomerDevelopmentSetting.setCreateTime(new Date());
        }else{
            //修改 --> update 更新人、更新时间
            wmsCustomerDevelopmentSetting.setUpdateBy(CurrentUserUtil.getUsername());
            wmsCustomerDevelopmentSetting.setUpdateTime(new Date());
        }
        wmsCustomerDevelopmentSettingService.saveOrUpdate(wmsCustomerDevelopmentSetting);

        if(true){
            log.warn("新增或修改库区成功");
        }
    }

   //  校验编码和  名称
    @Override
    public WmsCustomerInfo getWmsCustomerInfoByName(String customerCode, String customerName) {
        WmsCustomerInfo wmsCustomerInfo= wmsCustomerInfoMapper.getWmsCustomerInfoByCode(customerCode,customerName);
        return wmsCustomerInfo;
    }
    /**
     * 校验编码
     * @param customerCode
     * @param customerName
     * @return
     */
    @Override
    public WmsCustomerInfo getWmsCustomerInfoByCode(String customerCode, String customerName) {
        WmsCustomerInfo wmsCustomerInfo= wmsCustomerInfoMapper.getWmsCustomerInfoByCode(customerCode,customerName);
        return wmsCustomerInfo;
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWmsCustomerInfoForExcel(WmsCustomerInfo wmsCustomerInfo, HttpServletRequest req) {
        return wmsCustomerInfoMapper.queryWmsCustomerInfoForExcel(wmsCustomerInfo);
    }

    @Override
    public WmsCustomerInfoDTO getCustomerInfoDTOByAppId(String appId) {

        //查询Customer开发设置
        QueryWrapper<WmsCustomerDevelopmentSetting> wmsCustomerDevelopmentSettingQueryWrapper = new QueryWrapper<>();
        wmsCustomerDevelopmentSettingQueryWrapper.lambda().eq(WmsCustomerDevelopmentSetting::getAppId, appId);
        wmsCustomerDevelopmentSettingQueryWrapper.lambda().groupBy(WmsCustomerDevelopmentSetting::getAppId);
        WmsCustomerDevelopmentSetting developmentSetting = wmsCustomerDevelopmentSettingService.getOne(wmsCustomerDevelopmentSettingQueryWrapper);

        if(ObjectUtil.isNotEmpty(developmentSetting)){
            Long mainId = developmentSetting.getMainId();//主体ID(wms_customer_info.id)
            //查询Customer
            WmsCustomerInfo wmsCustomerInfo = wmsCustomerInfoMapper.selectById(mainId);
            if(ObjectUtil.isNotEmpty(wmsCustomerInfo)){
                WmsCustomerInfoDTO dto = ConvertUtil.convert(wmsCustomerInfo, WmsCustomerInfoDTO.class);
                dto.setAppId(developmentSetting.getAppId());
                dto.setAppSecret(developmentSetting.getAppSecret());
                dto.setPublicKey(developmentSetting.getPublicKey());
                dto.setPrivateKey(developmentSetting.getPrivateKey());
                return dto;
            }
        }
        return null;
    }


}
