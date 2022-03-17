package com.jayud.wms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.wms.mapper.StrategyConfMapper;
import com.jayud.wms.model.bo.AddStrategyConfForm;
import com.jayud.wms.model.po.StrategyConf;
import com.jayud.wms.model.vo.StrategyConfVO;
import com.jayud.wms.service.IStrategyConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略配置 服务实现类
 *
 * @author jyd
 * @since 2022-01-13
 */
@Service
public class StrategyConfServiceImpl extends ServiceImpl<StrategyConfMapper, StrategyConf> implements IStrategyConfService {


    @Autowired
    private StrategyConfMapper strategyConfMapper;

    @Override
    public IPage<StrategyConf> selectPage(StrategyConf strategyConf,
                                          Integer pageNo,
                                          Integer pageSize,
                                          HttpServletRequest req) {

        Page<StrategyConf> page = new Page<StrategyConf>(pageNo, pageSize);
        IPage<StrategyConf> pageList = strategyConfMapper.pageList(page, strategyConf);
        return pageList;
    }

    @Override
    public List<StrategyConf> selectList(StrategyConf strategyConf) {
        return strategyConfMapper.list(strategyConf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StrategyConf saveOrUpdateStrategyConf(StrategyConf strategyConf) {
        Long id = strategyConf.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            strategyConf.setCreateBy(CurrentUserUtil.getUsername());
            strategyConf.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<StrategyConf> strategyConfQueryWrapper = new QueryWrapper<>();
            //strategyConfQueryWrapper.lambda().eq(StrategyConf::getCode, strategyConf.getCode());
            //strategyConfQueryWrapper.lambda().eq(StrategyConf::getIsDeleted, 0);
            //List<StrategyConf> list = this.list(strategyConfQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            strategyConf.setUpdateBy(CurrentUserUtil.getUsername());
            strategyConf.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<StrategyConf> strategyConfQueryWrapper = new QueryWrapper<>();
            //strategyConfQueryWrapper.lambda().ne(StrategyConf::getId, id);
            //strategyConfQueryWrapper.lambda().eq(StrategyConf::getCode, strategyConf.getCode());
            //strategyConfQueryWrapper.lambda().eq(StrategyConf::getIsDeleted, 0);
            //List<StrategyConf> list = this.list(strategyConfQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(strategyConf);
        return strategyConf;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delStrategyConf(int id) {
        StrategyConf strategyConf = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(strategyConf)) {
            throw new IllegalArgumentException("策略配置不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        strategyConf.setUpdateBy(CurrentUserUtil.getUsername());
        strategyConf.setUpdateTime(new Date());
        strategyConf.setIsDeleted(true);
        this.saveOrUpdate(strategyConf);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryStrategyConfForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryStrategyConfForExcel(paramMap);
    }

    @Override
    public void addOrUpdate(AddStrategyConfForm form) {
        StrategyConf strategyConf = ConvertUtil.convert(form, StrategyConf.class);
        if (form.getId() == null) {
            this.save(strategyConf);
        } else {
            this.update(null, Wrappers.<StrategyConf>lambdaUpdate()
                    .set(StrategyConf::getType, strategyConf.getType())
                    .set(StrategyConf::getTypeDesc, strategyConf.getTypeDesc())
                    .set(StrategyConf::getWarehouseId, strategyConf.getWarehouseId())
                    .set(StrategyConf::getWarehouseAreaId, strategyConf.getWarehouseAreaId())
                    .set(StrategyConf::getFrozen, strategyConf.getFrozen())
                    .set(StrategyConf::getLocationType, strategyConf.getLocationType())
                    .eq(StrategyConf::getId, strategyConf.getId()));
        }
    }

    @Override
    public StrategyConfVO getDetails(Long id) {
        StrategyConf strategyConf = this.getById(id);
        StrategyConfVO strategyConfVO = ConvertUtil.convert(strategyConf, StrategyConfVO.class);

        String locationType = strategyConfVO.getLocationType();
        if (!StringUtils.isEmpty(locationType)) {
            String[] locationTypes = locationType.split(",");
            for (int i = 0; i < locationTypes.length; i++) {
                String type = locationTypes[i];
                if (!StringUtils.isEmpty(type)) {
                    switch (i) {
                        case 0:
                            strategyConfVO.setLocationTypeOne(Long.valueOf(type));
                            break;
                        case 1:
                            strategyConfVO.setLocationTypeTwo(Long.valueOf(type));
                            break;
                        case 2:
                            strategyConfVO.setLocationTypeThree(Long.valueOf(type));
                            break;
                    }
                }
            }
        }
        return strategyConfVO;
    }

}
