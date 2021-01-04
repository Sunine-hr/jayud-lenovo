package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.SupplierInfoMapper;
import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.bo.QueryAuditSupplierInfoForm;
import com.jayud.oms.model.bo.QuerySupplierInfoForm;
import com.jayud.oms.model.enums.AuditStatusEnum;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.enums.SettlementTypeEnum;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.ProductBiz;
import com.jayud.oms.model.po.ProductClassify;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.SupplierInfoVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.IProductClassifyService;
import com.jayud.oms.service.ISupplierInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 供应商信息 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements ISupplierInfoService {
    //服务类型
    @Autowired
    private IProductClassifyService productClassifyService;
    @Autowired
    private IAuditInfoService auditInfoService;

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    @Override
    public IPage<SupplierInfoVO> findSupplierInfoByPage(QuerySupplierInfoForm form) {
        Page<SupplierInfoVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<SupplierInfoVO> iPage = this.baseMapper.findSupplierInfoByPage(page, form);
        for (SupplierInfoVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getProductClassify())) {
                continue;
            }
            String[] tmps = record.getProductClassify().split(",");
            List<Long> ids = new ArrayList<>();
            for (String tmp : tmps) {
                ids.add(Long.parseLong(tmp));
            }
            List<ProductClassify> productClassifies = productClassifyService.getByIds(ids);
            StringBuilder sb = new StringBuilder();
            for (ProductClassify productClassify : productClassifies) {
                sb.append(productClassify.getName()).append(",");
            }
            record.setProductClassify(sb.substring(0, sb.length() - 1));
            record.setSettlementType(SettlementTypeEnum.getDesc(record.getSettlementType()));
            //查询审核状态
            AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(record.getId(), AuditTypeDescEnum.ONE.getTable());
            record.setAuditStatus(AuditStatusEnum.getDesc(auditInfo.getAuditStatus()));
        }

        return iPage;
    }

    /**
     * 新增编辑供应商
     *
     * @param form
     * @return
     */
    @Override
    @Transactional
    public boolean saveOrUpdateSupplierInfo(AddSupplierInfoForm form) {
        StringBuilder sb = new StringBuilder();
        for (Long id : form.getProductClassifyIds()) {
            sb.append(id).append(",");
        }

        SupplierInfo supplierInfo = ConvertUtil.convert(form, SupplierInfo.class);
        supplierInfo.setProductClassifyIds(sb.substring(0, sb.length() - 1));

        boolean isTrue;
        if (Objects.isNull(supplierInfo.getId())) {
            supplierInfo.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());

            isTrue = this.save(supplierInfo);
        } else {
            supplierInfo.setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken())
                    .setSupplierCode(null);
            isTrue = this.updateById(supplierInfo);
        }
        //创建审核表
        auditInfoService.saveOrUpdateAuditInfo(new AuditInfo()
                .setExtId(supplierInfo.getId())
                .setExtDesc(AuditTypeDescEnum.ONE.getTable())
                .setAuditTypeDesc(AuditTypeDescEnum.ONE.getDesc())
                .setAuditStatus(AuditStatusEnum.CW_WAIT.getCode())
        );
        return isTrue;
    }

    /**
     * 分页查询供应商审核信息
     */
    @Override
    public IPage<SupplierInfoVO> findAuditSupplierInfoByPage(QueryAuditSupplierInfoForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());

        form.setAuditTableDesc(AuditTypeDescEnum.ONE.getTable());
        IPage<SupplierInfoVO> iPage = this.baseMapper.findAuditSupplierInfoByPage(page, form);
        for (SupplierInfoVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getProductClassify())) {
                continue;
            }
            String[] tmps = record.getProductClassify().split(",");
            List<Long> ids = new ArrayList<>();
            for (String tmp : tmps) {
                ids.add(Long.parseLong(tmp));
            }
            List<ProductClassify> productClassifies = productClassifyService.getByIds(ids);
            StringBuilder sb = new StringBuilder();
            for (ProductClassify productClassify : productClassifies) {
                sb.append(productClassify.getName()).append(",");
            }
            record.setProductClassify(sb.substring(0, sb.length() - 1));
            record.setSettlementType(SettlementTypeEnum.getDesc(record.getSettlementType()));
            record.setAuditStatus(AuditStatusEnum.getDesc(record.getAuditStatus()));
        }
        return iPage;
    }


    /**
     * 获取启用审核通过供应商
     */
    @Override
    public List<SupplierInfo> getApprovedSupplier(String... fields) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        if (fields != null) {
            condition.select(fields);
        }
        condition.lambda().eq(SupplierInfo::getStatus, StatusEnum.ENABLE.getCode());
        List<SupplierInfo> supplierInfos = this.baseMapper.selectList(condition);
        //查询所有审核通过的供应商
        List<SupplierInfo> tmp = new ArrayList<>();
        for (SupplierInfo supplierInfo : supplierInfos) {
            AuditInfo info = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfo.getId()
                    , AuditTypeDescEnum.ONE.getTable());
            if (info == null) {
                continue;
            }
            if (AuditStatusEnum.SUCCESS.getCode().equals(info.getAuditStatus())) {
                tmp.add(supplierInfo);
            }
        }
        return tmp;
    }

    /**
     * 校验唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(SupplierInfo supplierInfo) {
        QueryWrapper<SupplierInfo> condition = new QueryWrapper<>();
        if (supplierInfo.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(SupplierInfo::getId, supplierInfo.getId())
                    .eq(SupplierInfo::getSupplierChName, supplierInfo.getSupplierChName()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己名称,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().and(tmp -> tmp.eq(SupplierInfo::getSupplierCode, supplierInfo.getSupplierCode())
                .or().eq(SupplierInfo::getSupplierChName, supplierInfo.getSupplierChName()));
        return this.count(condition) > 0;
    }


}
