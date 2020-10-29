package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.CostInfoMapper;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.service.ICostInfoService;
import com.jayud.oms.service.ICostTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private RedisUtils redisUtils;
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

            List<CostTypeVO> costTypes = this.costTypeService.findCostTypeByIds(list);
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
        String loginUser = redisUtils.get("loginUser", 100);
        CostInfo costInfo = ConvertUtil.convert(form, CostInfo.class);
        if (Objects.isNull(costInfo.getId())) {
            costInfo.setCreateTime(LocalDateTime.now());
            costInfo.setCreateUser(loginUser);
            return this.save(costInfo);
        } else {
            costInfo.setUpTime(LocalDateTime.now());
            costInfo.setUpUser(loginUser);
            return this.updateById(costInfo);
        }
    }

    /**
     * 根据id查询费用名称
     */
    @Override
    public CostInfoVO getById(Long id) {
        CostInfo costInfo = this.baseMapper.selectById(id);
        return ConvertUtil.convert(costInfo, CostInfoVO.class);
    }

    /**
     * 更新为无效状态
     *
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByIds(List<Long> ids) {
        String loginUser = redisUtils.get("loginUser", 100);
        List<CostInfo> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        ids.stream().forEach(id -> {
            list.add(new CostInfo().setId(id).setStatus(StatusEnum.INVALID.getCode())
                    .setUpTime(now).setUpUser(loginUser));
        });

        return this.updateBatchById(list);
    }

    @Override
    public List<CostInfo> findCostInfo() {
        return baseMapper.selectList(null);
    }
}
