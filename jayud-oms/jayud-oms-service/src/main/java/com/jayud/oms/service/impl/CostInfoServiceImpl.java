package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.CostInfoMapper;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.ICostInfoService;
import com.jayud.oms.service.ICostTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 费用名描述 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Service
public class CostInfoServiceImpl extends ServiceImpl<CostInfoMapper, CostInfo> implements ICostInfoService {

    @Autowired
    private ICostTypeService costTypeService;

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    @Override
    public IPage<CostInfoVO> findCostInfoByPage(QueryCostInfoForm form) {

        //模糊查询费用类别
        List<String> ids = null;
        if (StringUtils.isNotEmpty(form.getCostType())) {
            QueryWrapper<CostType> condition = new QueryWrapper<>();
            condition.lambda().select(CostType::getId).like(CostType::getCodeName, form.getCostType());
            List<CostType> tmps = this.costTypeService.getBaseMapper().selectList(condition);
            ids = tmps.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(ids)) {
                ids = new ArrayList<>();
                ids.add("-1");
            }
        }


        //定义分页参数
        Page<CostInfoVO> page = new Page(form.getPageNum(), form.getPageSize());
        IPage<CostInfoVO> iPage = this.baseMapper.findCostInfoByPage(page, form, ids);
        for (CostInfoVO record : iPage.getRecords()) {
            String[] cids = record.getCodeName().split(",");
            List<Long> list = new ArrayList<>(cids.length);
            for (String cid : cids) {
                list.add(Long.parseLong(cid));
            }

            List<CostTypeVO> costTypes = this.costTypeService.getCostTypeByIds(list);
            //拼接费用类型
            StringBuilder sb = new StringBuilder();
            for (CostTypeVO costType : costTypes) {
                sb.append(costType.getCodeName()).append(",");
            }
            record.setCodeName(sb.substring(0, sb.length() - 1));
        }
        return iPage;
    }

    /**
     * 新增编辑费用名称
     *
     * @param form
     * @return
     */
    @Override
    public boolean saveOrUpdateCostInfo(AddCostInfoForm form) {
        StringBuilder sb = new StringBuilder();
        for (Long cid : form.getCids()) {
            sb.append(cid).append(",");
        }
        CostInfo costInfo = ConvertUtil.convert(form, CostInfo.class);
        costInfo.setCids(sb.substring(0, sb.length() - 1));

        if (Objects.isNull(costInfo.getId())) {
            costInfo.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            return this.save(costInfo);
        } else {
            costInfo.setIdCode(null)
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(costInfo);
        }
    }

//    /**
//     * 根据id查询费用名称
//     */
//    @Override
//    public CostInfoVO getById(Long id) {
//        CostInfo costInfo = this.baseMapper.selectById(id);
//        return ConvertUtil.convert(costInfo, CostInfoVO.class);
//    }

    /**
     * 更改启用/禁用状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisableCostInfo(Long id) {
        //查询当前状态
        QueryWrapper<CostInfo> condition = new QueryWrapper<>();
        condition.lambda().select(CostInfo::getStatus).eq(CostInfo::getId, id);
        CostInfo tmp = this.baseMapper.selectOne(condition);

        String status = "1".equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        CostInfo costInfo = new CostInfo().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(costInfo);
    }

    @Override
    public List<CostInfo> findCostInfo() {
        return baseMapper.selectList(null);
    }

    /**
     * 校验唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(CostInfo costInfo) {
        QueryWrapper<CostInfo> condition = new QueryWrapper<>();
        if (costInfo.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(CostInfo::getId, costInfo.getId())
                    .eq(CostInfo::getName, costInfo.getName()));
            int count = this.count(condition);
            //匹配到自己名称,不进行唯一校验
            if (count == 0) {
                condition = new QueryWrapper<>();
                condition.lambda().eq(CostInfo::getName, costInfo.getName());
                return this.count(condition) > 0;
            } else {
                return false;
            }
        } else {
            condition.lambda().eq(CostInfo::getIdCode, costInfo.getIdCode())
                    .or().eq(CostInfo::getName, costInfo.getName());
            return this.count(condition) > 0;
        }
    }

    @Override
    public List<CostInfo> getCostInfoByStatus(String status) {
        QueryWrapper<CostInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(CostInfo::getStatus, status);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 下拉根据费用类别查询费用名称
     *
     * @return
     */
    @Override
    public List<InitComboxStrVO> getCostInfoByCostTypeName(String costTypeName) {
        List<CostType> costTypes = this.costTypeService.getByCondition(new CostType().setCodeName(costTypeName));
        return this.getCostInfoByCostType(costTypes);
    }

    /**
     * 下拉根据费用类别code查询费用名称
     *
     * @return
     */
    @Override
    public List<InitComboxStrVO> getCostInfoByCostTypeCode(String costTypeCde) {
        List<CostType> costTypes = this.costTypeService.getByCondition(new CostType().setCode(costTypeCde));
        return this.getCostInfoByCostType(costTypes);
    }

    /**
     * 下拉根据费用类别查询费用名称
     *
     * @return
     */
    @Override
    public List<InitComboxStrVO> getCostInfoByCostType(List<CostType> costTypes) {
        if (CollectionUtils.isEmpty(costTypes)) {
            return null;
        }
        CostType costType = costTypes.get(0);
        QueryWrapper<CostInfo> condition = new QueryWrapper<>();
        condition.lambda().like(CostInfo::getCids, costType.getId());

        List<InitComboxStrVO> list = new ArrayList<>();
        for (CostInfo costInfo : this.baseMapper.selectList(condition)) {
            String[] split = costInfo.getCids().split(",");
            for (String cid : split) {
                if (costType.getId().equals(Long.valueOf(cid))) {
                    InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
                    initComboxStrVO.setId(costInfo.getId());
                    initComboxStrVO.setName(costInfo.getName());
                    initComboxStrVO.setCode(costInfo.getIdCode());
                    list.add(initComboxStrVO);
                }
            }
        }
        return list;
    }

    @Override
    public String getCostNameByCostCode(String costCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id_code", costCode);
        return this.getOne(queryWrapper).getName();
    }

    @Override
    public Map<String, List<InitComboxVO>> initCostTypeByCostInfoCode() {
        List<CostInfo> costInfos = this.getCostInfoByStatus(StatusEnum.ENABLE.getCode());
        Map<Long, CostType> costTypeMap = this.costTypeService.getEnableCostType().stream().collect(Collectors.toMap(CostType::getId, e -> e));
        Map<String, List<InitComboxVO>> map = new HashMap<>();
        for (CostInfo costInfo : costInfos) {
            String[] split = costInfo.getCids().split(",");
            List<InitComboxVO> costTypeComboxs = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isEmpty(s)) continue;
                CostType costType = costTypeMap.get(Long.valueOf(s));
                if (costType == null) continue;
                InitComboxVO initComboxVO = new InitComboxVO();
                initComboxVO.setName(costType.getCodeName());
                initComboxVO.setId(costType.getId());
                costTypeComboxs.add(initComboxVO);
            }
            map.put(costInfo.getIdCode(), costTypeComboxs);
        }
        return map;
    }
}
