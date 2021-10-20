package com.jayud.oms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.LineMapper;
import com.jayud.oms.model.bo.AddLineForm;
import com.jayud.oms.model.bo.AuditLineForm;
import com.jayud.oms.model.bo.LineBatchOprForm;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.enums.LineStatusEnum;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.po.Line;
import com.jayud.oms.model.po.RegionCity;
import com.jayud.oms.model.vo.LineDetailsVO;
import com.jayud.oms.model.vo.LineVO;
import com.jayud.oms.service.ILineService;
import com.jayud.oms.service.IRegionCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 线路管理 服务实现类
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
@Service
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements ILineService {

    @Autowired
    private IRegionCityService regionCityService;

    /**
     * 分页查询线路
     * @param form
     * @return
     */
    @Override
    public IPage<LineVO> findLineByPage(QueryLineForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findLineByPage(page, form);
    }

    /**
     * 校验唯一性
     * @param info
     * @return
     */
    @Override
    public boolean checkUniqueByName(Line info) {
        QueryWrapper<Line> condition = new QueryWrapper<>();
        if (info.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(Line::getId, info.getId())
                    .eq(Line::getLineName, info.getLineName()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己名称,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().eq(Line::getLineName, info.getLineName());
        return this.count(condition) > 0;
    }

    /**
     * 校验唯一性
     * @param info
     * @return
     */
    @Override
    public boolean checkUniqueByCode(Line info) {
        QueryWrapper<Line> condition = new QueryWrapper<>();
        if (info.getId() != null) {
            condition.lambda().and(tmp -> tmp.eq(Line::getId, info.getId())
                    .eq(Line::getLineCode, info.getLineCode()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己编码,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().eq(Line::getLineCode, info.getLineCode());
        return this.count(condition) > 0;
    }

    /**
     * 新增编辑线路
     * @param line
     * @return
     */
    @Override
    @Transactional
    public boolean saveOrUpdateLine(Line line) {

        if (Objects.isNull(line.getAuditStatus())) {
            line.setAuditStatus(LineStatusEnum.WAIT_AUDIT.getCode());
        }

        line.setFromAddress(getAddrNames(line.getFromCountry(), line.getFromCity(), line.getFromRegion()));
        line.setToAddress(getAddrNames(line.getToProvince(), line.getToCity(), line.getToRegion()));

        if (Objects.isNull(line.getId())) {
            line.setCreateTime(LocalDateTime.now())
                .setCreatedUser(UserOperator.getToken());
            return this.save(line);
        } else {
            line
                .setUpTime(LocalDateTime.now())
                .setUpUser(UserOperator.getToken());
            return this.updateById(line);
        }

    }

    /**
     * 拼接地址信息
     * @param country
     * @param city
     * @param region
     * @return
     */
    private String getAddrNames(Long country, Long city, Long region) {
        List<RegionCity> addrNames = null;
        if (Objects.isNull(region)) {
            addrNames = regionCityService.getAddrName(country, city);
        } else {
            addrNames = regionCityService.getAddrName(country, city, region);
        }
        List<String> names = addrNames.stream().map(RegionCity::getName).collect(Collectors.toList());
        return CollUtil.join(names, ",");
    }

    /**
     * 批量操作
     * @param form
     */
    @Override
    public void batchOprLine(LineBatchOprForm form) {
        LocalDateTime now = LocalDateTime.now();
        this.update(new UpdateWrapper<Line>().lambda()
                .set(Line::getAuditStatus, form.getAuditStatus())
                .set(Line::getAuditTime, now)
                .set(Line::getUpTime, now)
                .set(Line::getUpUser, UserOperator.getToken())
                .in(Line::getId, form.getList().stream().map(AddLineForm::getId).collect(Collectors.toList())));
    }

    /**
     * 删除线路
     * @param id
     */
    @Override
    public void delLine(Long id) {
        this.remove(new QueryWrapper<Line>().lambda().eq(Line::getId, id));
    }

    /**
     * 查看线路详情
     * @param id
     * @return
     */
    @Override
    public LineDetailsVO getLineDetails(Long id) {
        return this.baseMapper.getLineDetails(id);
    }

    /**
     * 线路审核
     * @param form
     */
    @Override
    public void auditLine(AuditLineForm form) {
        LocalDateTime now = LocalDateTime.now();
        this.update(new UpdateWrapper<Line>().lambda()
                .set(Line::getAuditStatus, form.getAuditStatus())
                .set(Line::getAuditTime, now)
                .set(Line::getUpTime, now)
                .set(Line::getUpUser, UserOperator.getToken())
                .eq(Line::getId, form.getId()));
    }

    /**
     * 检查线路是否存在
     * @param id
     * @return
     */
    @Override
    public boolean checkExists(Long id) {
        return this.count(new QueryWrapper<Line>().lambda().eq(Line::getId, id)) > 0;
    }

    /**
     * 查询启用的线路
     * @param lineName
     * @return
     */
    @Override
    public List<Line> getEnableLine(String lineName) {
        QueryWrapper<Line> condition = new QueryWrapper<>();
        condition.lambda()
                .select(Line::getId, Line::getLineName)
                .eq(Line::getAuditStatus, LineStatusEnum.AUDIT_SUCCESS.getCode());

        if (!StringUtils.isEmpty(lineName)) {
            condition.lambda().like(Line::getLineName, lineName);
        }
        condition.lambda().orderByDesc(Line::getCreateTime);
        return this.baseMapper.selectList(condition);
    }
}
