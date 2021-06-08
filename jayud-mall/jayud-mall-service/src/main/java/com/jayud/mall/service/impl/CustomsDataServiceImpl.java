package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.CustomsBaseFile;
import com.jayud.mall.model.po.CustomsBaseServiceCost;
import com.jayud.mall.model.po.CustomsBaseValue;
import com.jayud.mall.model.po.CustomsData;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsDataServiceImpl extends ServiceImpl<CustomsDataMapper, CustomsData> implements ICustomsDataService {

    //文件服务访问前缀
    @Value("${file.absolute.path:}")
    private String fileAbsolutePath;

    @Autowired
    CustomsDataMapper customsDataMapper;
    @Autowired
    CustomsBaseServiceCostMapper customsBaseServiceCostMapper;
    @Autowired
    CustomsBaseValueMapper customsBaseValueMapper;
    @Autowired
    CustomsBaseFileMapper customsBaseFileMapper;
    @Autowired
    ServiceGroupMapper serviceGroupMapper;
    @Autowired
    CountryMapper countryMapper;
    @Autowired
    CurrencyInfoMapper currencyInfoMapper;

    @Autowired
    BaseService baseService;
    @Autowired
    ICustomsBaseServiceCostService customsBaseServiceCostService;
    @Autowired
    ICustomsBaseValueService customsBaseValueService;
    @Autowired
    ICustomsBaseFileService customsBaseFileService;


    @Override
    public IPage<CustomsDataVO> findCustomsDataByPage(QueryCustomsDataForm form) {
        //定义分页参数
        Page<CustomsDataVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<CustomsDataVO> pageInfo = customsDataMapper.findCustomsDataByPage(page, form);
        List<CustomsDataVO> records = pageInfo.getRecords();
        records.forEach(customsDataVO -> {
            String picUrl = customsDataVO.getPicUrl();
            if(StrUtil.isNotEmpty(picUrl)){
                customsDataVO.setPicUrl(fileAbsolutePath+picUrl);
            }
        });
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCustomsData(CustomsDataForm form) {
        CustomsData customsData = ConvertUtil.convert(form, CustomsData.class);
        Long id = form.getId();
        if(id == null){
            //新增
            String idCode = form.getIdCode();
            QueryWrapper<CustomsData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error( -1, "idCode,已存在。");
            }
            customsData.setSuttleUnit("KG");
            customsData.setGrossUnit("KG");
            AuthUser user = baseService.getUser();
            if(ObjectUtil.isEmpty(user)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
            }
            customsData.setStatus("1");
            customsData.setUserId(user.getId().intValue());
            customsData.setUserName(user.getName());
            customsData.setCreateTime(LocalDateTime.now());
        }else{
            //修改
            String idCode = form.getIdCode();
            QueryWrapper<CustomsData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            queryWrapper.ne("id", id);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error( -1, "idCode,已存在。");
            }
        }
        TemplateUrlVO picUrls = form.getPicUrls();
        if(ObjectUtil.isNotEmpty(picUrls)){
            String relativePath = picUrls.getRelativePath();//相对路径
            customsData.setPicUrl(relativePath);
        }
        //1.保存-报关资料
        this.saveOrUpdate(customsData);
        Long customsId = customsData.getId();

        //2.保存报关资料的 服务费用
        List<CustomsBaseServiceCostForm> customsBaseServiceCostList = form.getCustomsBaseServiceCostList();
        if(CollUtil.isNotEmpty(customsBaseServiceCostList)){
            List<CustomsBaseServiceCost> customsBaseServiceCosts = ConvertUtil.convertList(customsBaseServiceCostList, CustomsBaseServiceCost.class);
            customsBaseServiceCosts.forEach(customsBaseServiceCost -> {
                customsBaseServiceCost.setType(1);//类型(1报关 2清关)
                customsBaseServiceCost.setCustomsId(customsId);
                Long serviceId = customsBaseServiceCost.getServiceId();
                ServiceGroupVO serviceGroupVO = serviceGroupMapper.findServiceGroupById(serviceId);
                String serviceName = serviceGroupVO.getCodeName();
                customsBaseServiceCost.setServiceName(serviceName);
            });
            QueryWrapper<CustomsBaseServiceCost> qw = new QueryWrapper<>();
            qw.eq("type", 1);//类型(1报关 2清关)
            qw.eq("customs_id", customsId);
            customsBaseServiceCostService.remove(qw);
            customsBaseServiceCostService.saveOrUpdateBatch(customsBaseServiceCosts);
        }

        //3.保存报关资料的 申报价值
        List<CustomsBaseValueForm> customsBaseValueList = form.getCustomsBaseValueList();
        if(CollUtil.isNotEmpty(customsBaseValueList)){
            List<CustomsBaseValue> customsBaseValues = ConvertUtil.convertList(customsBaseValueList, CustomsBaseValue.class);
            customsBaseValues.forEach(customsBaseValue -> {
                customsBaseValue.setType(1);
                customsBaseValue.setCustomsId(customsId);
                String countryCode = customsBaseValue.getCountryCode();
                CountryVO countryVO = countryMapper.findCountryByCode(countryCode);
                String countryName = countryVO.getName();
                customsBaseValue.setCountryName(countryName);
            });
            QueryWrapper<CustomsBaseValue> qw = new QueryWrapper<>();
            qw.eq("type", 1);
            qw.eq("customs_id", customsId);
            customsBaseValueService.remove(qw);
            customsBaseValueService.saveOrUpdateBatch(customsBaseValues);
        }
        //4.保存报关资料的 申报文件
        List<CustomsBaseFileForm> customsBaseFileList = form.getCustomsBaseFileList();
        if(CollUtil.isNotEmpty(customsBaseFileList)){
            customsBaseFileList.forEach(customsBaseFileForm -> {
                customsBaseFileForm.setType(1);
                customsBaseFileForm.setCustomsId(customsId);
                String countryCode = customsBaseFileForm.getCountryCode();
                CountryVO countryVO = countryMapper.findCountryByCode(countryCode);
                String countryName = countryVO.getName();
                customsBaseFileForm.setCountryName(countryName);
                List<TemplateUrlVO> templateUrls = customsBaseFileForm.getTemplateUrls();
                if(CollUtil.isNotEmpty(templateUrls)){
                    String json = JSONUtil.toJsonStr(templateUrls);
                    customsBaseFileForm.setTemplateUrl(json);//模版文件地址
                }
            });
            List<CustomsBaseFile> customsBaseFiles = ConvertUtil.convertList(customsBaseFileList, CustomsBaseFile.class);
            QueryWrapper<CustomsBaseFile> qw = new QueryWrapper<>();
            qw.eq("type", 1);
            qw.eq("customs_id", customsId);
            customsBaseFileService.remove(qw);
            customsBaseFileService.saveOrUpdateBatch(customsBaseFiles);
        }
        return CommonResult.success("保存报关资料，成功!");
    }

    @Override
    public List<CustomsDataVO> findCustomsData() {
        QueryWrapper<CustomsData> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("","");
        List<CustomsData> list = this.list(queryWrapper);
        List<CustomsDataVO> customsDataVOS = ConvertUtil.convertList(list, CustomsDataVO.class);
        return customsDataVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditCustomsData(AuditCustomsDataForm form) {
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "当前登录用户失效，请重新登录");
        }
        Long id = form.getId();
        CustomsDataVO customsDataVO = customsDataMapper.findAuditCustomsDataId(id);
        if(ObjectUtil.isEmpty(customsDataVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报关商品不存在");
        }
        CustomsData customsData = ConvertUtil.convert(customsDataVO, CustomsData.class);
        Integer auditStatus = form.getAuditStatus();
        customsData.setAuditStatus(auditStatus);//审核状态(0待审核 1已审核 2已取消)
        customsData.setAuditUserId(user.getId().intValue());
        customsData.setAuditUserName(user.getName());
        this.saveOrUpdate(customsData);

    }

    @Override
    public CustomsDataVO findCustomsDataById(Long id) {
        //1.查询
        CustomsDataVO customsDataVO = customsDataMapper.findCustomsDataById(id);
        Long customsId = customsDataVO.getId();
        //2.服务费用
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("type", 1);
        paraMap.put("customs_id", customsId);
        List<CustomsBaseServiceCostVO> customsBaseServiceCostList = customsBaseServiceCostMapper.findCustomsBaseServiceCostByParaMap(paraMap);
        customsDataVO.setCustomsBaseServiceCostList(customsBaseServiceCostList);
        //3.审报价值
        List<CustomsBaseValueVO> customsBaseValueList = customsBaseValueMapper.findCustomsBaseValueByParaMap(paraMap);
        customsDataVO.setCustomsBaseValueList(customsBaseValueList);
        //4.申报文件
        List<CustomsBaseFileVO> customsBaseFileList = customsBaseFileMapper.findCustomsBaseFileByParaMap(paraMap);
        if(CollUtil.isNotEmpty(customsBaseFileList)){
            customsBaseFileList.forEach(customsBaseFileVO -> {
                String templateUrl = customsBaseFileVO.getTemplateUrl();
                if(templateUrl != null && templateUrl.length() > 0){
                    String json = templateUrl;
                    try {
                        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                        });
                        customsBaseFileVO.setTemplateUrls(templateUrlVOS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("json格式错误");
                        customsBaseFileVO.setTemplateUrls(new ArrayList<>());
                    }
                }else{
                    customsBaseFileVO.setTemplateUrls(new ArrayList<>());
                }
            });
        }
        customsDataVO.setCustomsBaseFileList(customsBaseFileList);
        return customsDataVO;
    }
}
