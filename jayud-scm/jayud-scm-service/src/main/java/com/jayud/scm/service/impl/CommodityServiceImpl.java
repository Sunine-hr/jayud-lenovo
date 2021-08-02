package com.jayud.scm.service.impl;

import cn.hutool.db.ActiveEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.Commodity;
import com.jayud.scm.mapper.CommodityMapper;
import com.jayud.scm.model.po.CommodityEntry;
import com.jayud.scm.model.po.CommodityFollow;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityFollowService commodityFollowService;

    @Autowired
    private IHsCodeService hsCodeService;

    @Autowired
    private ICommodityEntryService commodityEntryService;


    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<Commodity> commodities = new ArrayList<>();
        List<CommodityFollow> commodityFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            Commodity commodity = new Commodity();
            commodity.setId(id.intValue());
            commodity.setVoided(1);
            commodity.setVoidedBy(deleteForm.getId().intValue());
            commodity.setVoidedByDtm(deleteForm.getDeleteTime());
            commodity.setVoidedByName(deleteForm.getName());
            commodities.add(commodity);

            CommodityFollow commodityFollow = new CommodityFollow();
            commodityFollow.setCommodityId(id.intValue());
            commodityFollow.setSType(OperationEnum.DELETE.getCode());
            commodityFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            commodityFollow.setCrtBy(deleteForm.getId().intValue());
            commodityFollow.setCrtByDtm(deleteForm.getDeleteTime());
            commodityFollow.setCrtByName(deleteForm.getName());
            commodityFollows.add(commodityFollow);
        }
        boolean b = this.updateBatchById(commodities);
        if(b){
            log.warn("商品删除成功："+commodities);
            boolean b1 = commodityFollowService.saveBatch(commodityFollows);
            if(!b1){
                log.warn("操作记录表添加失败"+commodityFollows);
            }
        }
        return b;
    }

    @Override
    public IPage<CommodityFormVO> findByPage(QueryCommodityForm commodityForm) {

        Page<CommodityFormVO> page = new Page<>(commodityForm.getPageNum(), commodityForm.getPageSize());
        return this.baseMapper.findByPage(page, commodityForm);
    }

    @Override
    public boolean saveOrUpdateCommodity(AddCommodityForm form) {

        //获取操作人
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Commodity commodity = ConvertUtil.convert(form, Commodity.class);

        CommodityFollow commodityFollow = new CommodityFollow();

        if(form.getId() != null){//id不为空，修改
            commodity.setMdyBy(systemUser.getId().intValue());
            commodity.setMdyByDtm(LocalDateTime.now());
            commodity.setMdyByName(systemUser.getName());

            commodityFollow.setSType(OperationEnum.UPDATE.getCode());
            commodityFollow.setFollowContext(OperationEnum.UPDATE.getDesc());
        }else{//添加
            commodity.setSkuNo(getOrderNo(NoCodeEnum.COMMODITY.getCode(), LocalDateTime.now()));
            commodity.setCrtBy(systemUser.getId().intValue());
            commodity.setCrtByDtm(LocalDateTime.now());
            commodity.setCrtByName(systemUser.getName());
            commodityFollow.setSType(OperationEnum.INSERT.getCode());
            commodityFollow.setFollowContext(OperationEnum.INSERT.getDesc());
        }

        boolean save = this.saveOrUpdate(commodity);

        if(save){
            log.warn("商品添加或修改成功："+commodity);
            commodityFollow.setCrtBy(commodity.getId().intValue());
            commodityFollow.setCrtByDtm(LocalDateTime.now());
            commodityFollow.setCrtByName(systemUser.getName());
            commodityFollow.setFollowContext(commodityFollow.getFollowContext()+commodity.getId());
            boolean save1 = commodityFollowService.save(commodityFollow);
            if(!save1){
                log.warn("操作记录表添加失败"+commodityFollow);
            }
        }
        return save;
    }

    /**
     * 获取商品编号
     * @param code
     * @param date
     * @return
     */
    @Override
    public String getOrderNo(String code,LocalDateTime date) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("i_code",code);
        map.put("i_date",date);
        this.baseMapper.getOrderNo(map);
        return (String)map.get("o_no");
    }

    @Override
    public CommodityVO findCommodityById(Integer id) {
        Commodity commodity = this.getById(id);
        return ConvertUtil.convert(commodity, CommodityVO.class);
    }

    @Override
    public CommodityDetailVO findCommodityDetailById(Integer id) {
        Commodity commodity = this.getById(id);
        CommodityDetailVO commodityDetailVO = ConvertUtil.convert(commodity, CommodityDetailVO.class);
        if(commodityDetailVO.getHsCodeNo() != null){
            HsCodeVO hsCodeVO = hsCodeService.getHsCodeByCodeNo(commodityDetailVO.getHsCodeNo());
            List<CommodityEntryVO> commodityEntryVOS = commodityEntryService.getCommodityEntry(commodityDetailVO.getId());
            for (CommodityEntryVO commodityEntryVO : commodityEntryVOS) {
                commodityEntryVO.setDefaultValue(commodityEntryVO.getElementValue());
                commodityEntryVO.setElementSort(commodityEntryVO.getSortIndex());
                commodityEntryVO.setElementsName(commodityEntryVO.getElementName());
            }
            commodityDetailVO.setHsCodeVO(hsCodeVO);
            commodityDetailVO.setCommodityEntryVOS(commodityEntryVOS);
        }
        return commodityDetailVO;
    }

    @Override
    public boolean taxClassification(TaxCommodityForm form) {
        //获取操作人
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<Commodity> commodities = new ArrayList<>();
        for (Integer integer : form.getId()) {
            Commodity commodity = new Commodity();
            commodity.setId(integer);
            commodity.setMdyBy(systemUser.getId().intValue());
            commodity.setMdyByDtm(LocalDateTime.now());
            commodity.setMdyByName(UserOperator.getToken());
            commodity.setTaxCode(form.getTaxCode());
            commodity.setTaxCodeDate(LocalDateTime.now());
            commodity.setTaxCodeName(form.getTaxClassName());
            commodities.add(commodity);
        }
        boolean b = this.updateBatchById(commodities);
        if(b){
            log.warn("税收归类成功"+commodities);
        }
        return b;
    }

    @Override
    public boolean reviewCommodity(AddReviewCommodityForm form) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //先删除原来的申报要素
        List<CommodityEntryVO> commodityEntry = commodityEntryService.getCommodityEntry(form.getId());
        if(CollectionUtils.isNotEmpty(commodityEntry)){
            boolean result = commodityEntryService.delete(form.getId());
            if(!result){
                log.warn("申报要素删除失败,商品id为"+form.getId());
                return false;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        //审核商品信息
        if(CollectionUtils.isNotEmpty(form.getAddCommodityEntryForms())){
            for (AddCommodityEntryForm addCommodityEntryForm : form.getAddCommodityEntryForms()) {
                addCommodityEntryForm.setElementName(addCommodityEntryForm.getElementsName());
                addCommodityEntryForm.setElementValue(addCommodityEntryForm.getDefaultValue());
                addCommodityEntryForm.setElementSort(addCommodityEntryForm.getSortIndex());

                if(addCommodityEntryForm.getElementName().equals("型号")){
                    stringBuffer.append(addCommodityEntryForm.getElementValue()+"型").append("|");
                }
                stringBuffer.append(addCommodityEntryForm.getElementValue()).append("|");
            }
        }

        Commodity commodity = ConvertUtil.convert(form, Commodity.class);
        commodity.setSkuElements(stringBuffer.length()>0 ? stringBuffer.toString().substring(0,stringBuffer.length()-1):null);
        commodity.setMdyByName(UserOperator.getToken());
        commodity.setMdyBy(systemUser.getId().intValue());
        commodity.setMdyByDtm(LocalDateTime.now());
        boolean update = this.saveOrUpdate(commodity);
        if(!update){
            log.warn("商品归类失败,商品id为"+form.getId());
            return false;
        }

        if(CollectionUtils.isNotEmpty(form.getAddCommodityEntryForms())){
            List<CommodityEntry> commodityEntries = ConvertUtil.convertList(form.getAddCommodityEntryForms(), CommodityEntry.class);
            for (CommodityEntry entry : commodityEntries) {
                entry.setCommodityId(form.getId());
                entry.setCrtBy(systemUser.getId().intValue());
                entry.setCrtByDtm(LocalDateTime.now());
                entry.setCrtByName(UserOperator.getToken());
            }
            boolean b = commodityEntryService.saveBatch(commodityEntries);
            if(!b){
                log.warn("商品申报要素信息添加失败,数据为"+commodityEntries);
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean reviewCommodities(AddReviewCommodityForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        List<CommodityFollow> commodityFollows = new ArrayList<>();


        List<Commodity> commodities = ConvertUtil.convertList(form.getAddCommodityDetailForms(), Commodity.class);
        for (Commodity commodity : commodities) {
            List<CommodityEntryVO> commodityEntry = commodityEntryService.getCommodityEntry(commodity.getId());
            if(CollectionUtils.isNotEmpty(commodityEntry)){
                boolean result = commodityEntryService.delete(form.getId());
                if(!result){
                    log.warn("申报要素删除失败,商品id为"+commodity.getId());
                    return false;
                }
            }

            StringBuffer stringBuffer = new StringBuffer();
            List<AddCommodityEntryForm> addCommodityEntryForms = form.getAddCommodityEntryForms();
            if(CollectionUtils.isNotEmpty(form.getAddCommodityEntryForms())){ //品牌（中文及外文名称）	型号
                for (AddCommodityEntryForm addCommodityEntryForm : addCommodityEntryForms) {

                    addCommodityEntryForm.setElementName(addCommodityEntryForm.getElementsName());
                    addCommodityEntryForm.setElementValue(addCommodityEntryForm.getDefaultValue());
                    addCommodityEntryForm.setElementSort(addCommodityEntryForm.getSortIndex());

                    if(addCommodityEntryForm.getElementName().equals("品牌（中文及外文名称")){
                        addCommodityEntryForm.setElementValue(commodity.getSkuBrand());
                    }
                    if(addCommodityEntryForm.getElementName().equals("型号")){
                        addCommodityEntryForm.setElementValue(commodity.getSkuModel());
                        stringBuffer.append(addCommodityEntryForm.getElementValue()+"型").append("|");
                    }
                    stringBuffer.append(addCommodityEntryForm.getElementValue()).append("|");
                }
            }

            //商品归类保存
            commodity.setSkuElements(stringBuffer.length()>0 ? stringBuffer.toString().substring(0,stringBuffer.length()-1):null);
            commodity.setMdyByName(UserOperator.getToken());
            commodity.setMdyBy(systemUser.getId().intValue());
            commodity.setMdyByDtm(LocalDateTime.now());
            commodity.setHsCodeNo(form.getHsCodeNo());
            commodity.setHkCodeNo(form.getHkCodeNo());
            commodity.setHkCodeName(form.getHkCodeName());
            commodity.setHkControl(form.getHkControl());
            commodity.setStateFlag(form.getStateFlag());
            commodity.setClassBy(systemUser.getId().intValue());
            commodity.setCassByName(UserOperator.getToken());
            commodity.setClassByDtm(LocalDateTime.now());

            boolean update = this.saveOrUpdate(commodity);
            if(!update){
                log.warn("商品归类失败,商品id为"+commodity.getId());
                return false;
            }
            log.warn("商品添加或修改成功："+commodities);

            if(CollectionUtils.isNotEmpty(addCommodityEntryForms)){
                List<CommodityEntry> commodityEntries = ConvertUtil.convertList(addCommodityEntryForms, CommodityEntry.class);
                for (CommodityEntry entry : commodityEntries) {
                    entry.setCommodityId(form.getId());
                    entry.setCrtBy(systemUser.getId().intValue());
                    entry.setCrtByDtm(LocalDateTime.now());
                    entry.setCrtByName(UserOperator.getToken());
                }
                boolean b = commodityEntryService.saveBatch(commodityEntries);
                if(!b){
                    log.warn("商品申报要素信息添加失败,数据为"+commodityEntries);
                    return false;
                }
            }
            CommodityFollow commodityFollow = new CommodityFollow();
            commodityFollow.setCommodityId(commodity.getId());
            commodityFollow.setSType(OperationEnum.UPDATE.getCode());
            commodityFollow.setFollowContext("商品归类"+commodity.getId());
            commodityFollow.setCrtBy(systemUser.getId().intValue());
            commodityFollow.setCrtByDtm(LocalDateTime.now());
            commodityFollow.setCrtByName(systemUser.getName());
            commodityFollows.add(commodityFollow);
        }

        boolean b = commodityFollowService.saveBatch(commodityFollows);
        if(!b){
            log.warn("商品日志操作添加失败："+commodityFollows);
        }

        return true;
    }

    @Override
    public boolean addCommodity(List<AddCommodityModelForm> list) {
        //获取操作人
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<Commodity> commodities = ConvertUtil.convertList(list, Commodity.class);

        List<CommodityFollow> commodityFollows = new ArrayList<>();

        for (Commodity commodity : commodities) {
            CommodityFollow commodityFollow = new CommodityFollow();
            commodity.setSkuNo(getOrderNo(NoCodeEnum.COMMODITY.getCode(),LocalDateTime.now()));
            commodity.setCrtBy(systemUser.getId().intValue());
            commodity.setCrtByDtm(LocalDateTime.now());
            commodity.setCrtByName(systemUser.getName());
            commodityFollow.setSType(OperationEnum.INSERT.getCode());
            commodityFollow.setFollowContext(OperationEnum.INSERT.getDesc()+commodity.getId());
            commodityFollow.setCrtBy(systemUser.getId().intValue());
            commodityFollow.setCrtByDtm(LocalDateTime.now());
            commodityFollow.setCrtByName(systemUser.getName());
            commodityFollows.add(commodityFollow);

        }

        boolean save = this.saveBatch(commodities);

        if(!save){
            log.warn("商品添加或修改失败："+commodities);
            boolean b = commodityFollowService.saveBatch(commodityFollows);
            if(!b){
                log.warn("商品日志操作添加失败："+commodityFollows);
            }
        }
        log.warn("商品添加或修改成功："+commodities);
        log.warn("商品日志操作添加成功："+commodityFollows);
        return true;
    }

    @Override
    public Commodity getCommodityBySkuModelAndSkuBrand(String skuModel, String skuBrand) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Commodity::getSkuModel,skuModel);
        queryWrapper.lambda().eq(Commodity::getSkuBrand,skuBrand);
        return this.getOne(queryWrapper);
    }
}
