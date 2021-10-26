package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddElectronicFenceForm;
import com.jayud.oms.model.bo.QueryElectronicFenceForm;
import com.jayud.oms.model.po.ElectronicFence;
import com.jayud.oms.mapper.ElectronicFenceMapper;
import com.jayud.oms.model.vo.ElectronicFenceVO;
import com.jayud.oms.service.IElectronicFenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.service.IRegionCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 电子围栏 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-25
 */
@Service
public class ElectronicFenceServiceImpl extends ServiceImpl<ElectronicFenceMapper, ElectronicFence> implements IElectronicFenceService {

    @Autowired
    private IRegionCityService regionCityService;

    @Override
    public void saveOrUpdate(AddElectronicFenceForm form) {
        ElectronicFence convert = ConvertUtil.convert(form, ElectronicFence.class);
        if (convert.getId() == null) {
            //订单编号生成
            StringBuilder orderNo = new StringBuilder();
            orderNo.append("WL").append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd"))
                    .append(StringUtils.zeroComplement(4, this.count() + 1));
            convert.setCreateUser(UserOperator.getToken()).setCreateTime(LocalDateTime.now()).setNumber(orderNo.toString());
        } else {
            convert.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(convert);
    }

    @Override
    public IPage<ElectronicFenceVO> findByPage(QueryElectronicFenceForm form) {
        Page<ElectronicFence> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<ElectronicFenceVO> iPage = this.baseMapper.findByPage(page, form);

        Map<Long, String> regionMap = regionCityService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        iPage.getRecords().forEach(e -> {
            e.setProvinceDesc(regionMap.get(e.getProvince())).setCityDesc(regionMap.get(e.getCity())).setAreaDesc(regionMap.get(e.getArea()))
                    .setAddr(e.getProvinceDesc() + e.getCityDesc() + e.getAreaDesc() + e.getAddr());
        });
        return iPage;
    }
}
