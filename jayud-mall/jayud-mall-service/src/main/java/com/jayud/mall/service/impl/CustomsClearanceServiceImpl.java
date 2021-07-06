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
import com.jayud.mall.model.po.CustomsClearance;
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
 * 清关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsClearanceServiceImpl extends ServiceImpl<CustomsClearanceMapper, CustomsClearance> implements ICustomsClearanceService {

    //文件服务访问前缀
    @Value("${file.absolute.path:}")
    private String fileAbsolutePath;

    @Autowired
    CustomsClearanceMapper customsClearanceMapper;
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
    public IPage<CustomsClearanceVO> findCustomsClearanceByPage(QueryCustomsClearanceForm form) {
        //定义分页参数
        Page<CustomsClearanceVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<CustomsClearanceVO> pageInfo = customsClearanceMapper.findCustomsClearanceByPage(page, form);
        List<CustomsClearanceVO> records = pageInfo.getRecords();
        records.forEach(customsClearanceVO -> {
            String picUrl = customsClearanceVO.getPicUrl();
            if(StrUtil.isNotEmpty(picUrl)){
                customsClearanceVO.setPicUrl(fileAbsolutePath+picUrl);
            }
        });
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCustomsData(CustomsClearanceForm form) {
        CustomsClearance customsClearance = ConvertUtil.convert(form, CustomsClearance.class);
        Long id = form.getId();
        String idCode = form.getIdCode();
        if(id == null){
            //新增
            QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "idCode,已存在");
            }
            AuthUser user = baseService.getUser();
            customsClearance.setSuttleUnit("KG");
            customsClearance.setGrossUnit("KG");
            customsClearance.setStatus("1");
            customsClearance.setUserId(user.getId().intValue());
            customsClearance.setUserName(user.getName());
            customsClearance.setCreateTime(LocalDateTime.now());
        }else{
            //修改
            QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            queryWrapper.ne("id", id);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "idCode,已存在");
            }
        }

        TemplateUrlVO picUrls = form.getPicUrls();
        if(ObjectUtil.isNotEmpty(picUrls)){
            String relativePath = picUrls.getRelativePath();
            customsClearance.setPicUrl(relativePath);
        }
        //1.保存-清关资料
        this.saveOrUpdate(customsClearance);
        Long customsId = customsClearance.getId();

        //2.保存清关资料的 服务费用
        List<CustomsBaseServiceCostForm> customsBaseServiceCostList = form.getCustomsBaseServiceCostList();
        if(CollUtil.isNotEmpty(customsBaseServiceCostList)){
            List<CustomsBaseServiceCost> customsBaseServiceCosts = ConvertUtil.convertList(customsBaseServiceCostList, CustomsBaseServiceCost.class);
            customsBaseServiceCosts.forEach(customsBaseServiceCost -> {
                customsBaseServiceCost.setType(2);//类型(1报关 2清关)
                customsBaseServiceCost.setCustomsId(customsId);
                Long serviceId = customsBaseServiceCost.getServiceId();
                ServiceGroupVO serviceGroupVO = serviceGroupMapper.findServiceGroupById(serviceId);
                String serviceName = serviceGroupVO.getCodeName();
                customsBaseServiceCost.setServiceName(serviceName);
            });
            QueryWrapper<CustomsBaseServiceCost> qw = new QueryWrapper<>();
            qw.eq("type", 2);//类型(1报关 2清关)
            qw.eq("customs_id", customsId);
            customsBaseServiceCostService.remove(qw);
            customsBaseServiceCostService.saveOrUpdateBatch(customsBaseServiceCosts);
        }

        //3.保存清关资料的 申报价值
        List<CustomsBaseValueForm> customsBaseValueList = form.getCustomsBaseValueList();
        if(CollUtil.isNotEmpty(customsBaseValueList)){
            List<CustomsBaseValue> customsBaseValues = ConvertUtil.convertList(customsBaseValueList, CustomsBaseValue.class);
            customsBaseValues.forEach(customsBaseValue -> {
                customsBaseValue.setType(2);
                customsBaseValue.setCustomsId(customsId);
                String countryCode = customsBaseValue.getCountryCode();
                CountryVO countryVO = countryMapper.findCountryByCode(countryCode);
                String countryName = countryVO.getName();
                customsBaseValue.setCountryName(countryName);
            });
            QueryWrapper<CustomsBaseValue> qw = new QueryWrapper<>();
            qw.eq("type", 2);
            qw.eq("customs_id", customsId);
            customsBaseValueService.remove(qw);
            customsBaseValueService.saveOrUpdateBatch(customsBaseValues);
        }

        //4.保存清关资料的 申报文件
        List<CustomsBaseFileForm> customsBaseFileList = form.getCustomsBaseFileList();
        if(CollUtil.isNotEmpty(customsBaseFileList)){
            customsBaseFileList.forEach(customsBaseFileForm -> {
                customsBaseFileForm.setType(2);
                customsBaseFileForm.setCustomsId(customsId);
                String countryCode = customsBaseFileForm.getCountryCode();
                CountryVO countryVO = countryMapper.findCountryByCode(countryCode);
                String countryName = countryVO.getName();
                customsBaseFileForm.setCountryName(countryName);
                List<TemplateUrlVO> templateUrls = customsBaseFileForm.getTemplateUrls();
                if(CollUtil.isNotEmpty(templateUrls)){
                    String json = JSONUtil.toJsonStr(templateUrls);
                    customsBaseFileForm.setTemplateUrl(json);//模版文件地址
                }else{
                    customsBaseFileForm.setTemplateUrl(null);//模版文件地址
                }
            });
            List<CustomsBaseFile> customsBaseFiles = ConvertUtil.convertList(customsBaseFileList, CustomsBaseFile.class);
            QueryWrapper<CustomsBaseFile> qw = new QueryWrapper<>();
            qw.eq("type", 2);
            qw.eq("customs_id", customsId);
            customsBaseFileService.remove(qw);
            customsBaseFileService.saveOrUpdateBatch(customsBaseFiles);
        }


        return CommonResult.success("保存清关，成功！");
    }

    @Override
    public List<CustomsClearanceVO> findCustomsClearance() {
        QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("","");
        List<CustomsClearance> list = this.list(queryWrapper);
        List<CustomsClearanceVO> customsClearanceVOS = ConvertUtil.convertList(list, CustomsClearanceVO.class);
        return customsClearanceVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditCustomsClearance(AuditCustomsClearanceForm form) {
        AuthUser user = baseService.getUser();
        Long id = form.getId();
        CustomsClearanceVO customsClearanceVO = customsClearanceMapper.findCustomsClearanceById(id);
        if(ObjectUtil.isEmpty(customsClearanceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "清关商品不存在");
        }
        CustomsClearance customsClearance = ConvertUtil.convert(customsClearanceVO, CustomsClearance.class);
        Integer auditStatus = form.getAuditStatus();//审核状态(0待审核 1已审核 2已取消)
        customsClearance.setAuditStatus(auditStatus);
        customsClearance.setAuditUserId(user.getId().intValue());
        customsClearance.setAuditUserName(user.getName());
        this.saveOrUpdate(customsClearance);
    }

    @Override
    public CustomsClearanceVO findCustomsClearanceById(Long id) {
        //1.查询
        CustomsClearanceVO customsClearanceVO = customsClearanceMapper.findCustomsClearanceById(id);
        Long customsId = customsClearanceVO.getId();
        //2.服务费用
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("type", 2);
        paraMap.put("customs_id", customsId);
        List<CustomsBaseServiceCostVO> customsBaseServiceCostList = customsBaseServiceCostMapper.findCustomsBaseServiceCostByParaMap(paraMap);
        customsClearanceVO.setCustomsBaseServiceCostList(customsBaseServiceCostList);
        //3.审报价值
        List<CustomsBaseValueVO> customsBaseValueList = customsBaseValueMapper.findCustomsBaseValueByParaMap(paraMap);
        customsClearanceVO.setCustomsBaseValueList(customsBaseValueList);
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
        customsClearanceVO.setCustomsBaseFileList(customsBaseFileList);
        return customsClearanceVO;
    }
}
