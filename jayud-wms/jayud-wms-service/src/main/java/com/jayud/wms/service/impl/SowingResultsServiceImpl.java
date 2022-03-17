package com.jayud.wms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.QuerySowingResultsForm;
import com.jayud.wms.model.po.IncomingSeeding;
import com.jayud.wms.model.po.SeedingWallLayout;
import com.jayud.wms.model.po.SowingResults;
import com.jayud.wms.model.enums.SowingResultsStatusEnum;
import com.jayud.wms.mapper.SowingResultsMapper;
import com.jayud.wms.service.IIncomingSeedingService;
import com.jayud.wms.service.ISeedingWallLayoutService;
import com.jayud.wms.service.ISowingResultsService;
import com.jayud.wms.model.vo.SeedingWallLayoutTwoVo;
import com.jayud.wms.model.vo.SowingResultsVO;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 播种结果 服务实现类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Service
public class SowingResultsServiceImpl extends ServiceImpl<SowingResultsMapper, SowingResults> implements ISowingResultsService {


    @Autowired
    private SowingResultsMapper sowingResultsMapper;
    @Autowired
    private ISeedingWallLayoutService seedingWallLayoutService;
    @Autowired
    private IIncomingSeedingService incomingSeedingService;

    @Override
    public IPage<SowingResultsVO> selectPage(QuerySowingResultsForm querySowingResultsForm,
                                             Integer pageNo,
                                             Integer pageSize,
                                             HttpServletRequest req) {

        Page<QuerySowingResultsForm> page = new Page<QuerySowingResultsForm>(pageNo, pageSize);
        IPage<SowingResultsVO> pageList = sowingResultsMapper.pageList(page, querySowingResultsForm);
        return pageList;
    }

    @Override
    public List<SowingResultsVO> selectList(QuerySowingResultsForm querySowingResultsForm) {
        return sowingResultsMapper.list(querySowingResultsForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SowingResults saveOrUpdateSowingResults(SowingResults sowingResults) {
        Long id = sowingResults.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            sowingResults.setCreateBy(CurrentUserUtil.getUsername());
            sowingResults.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<SowingResults> sowingResultsQueryWrapper = new QueryWrapper<>();
            //sowingResultsQueryWrapper.lambda().eq(SowingResults::getCode, sowingResults.getCode());
            //sowingResultsQueryWrapper.lambda().eq(SowingResults::getIsDeleted, 0);
            //List<SowingResults> list = this.list(sowingResultsQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            sowingResults.setUpdateBy(CurrentUserUtil.getUsername());
            sowingResults.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<SowingResults> sowingResultsQueryWrapper = new QueryWrapper<>();
            //sowingResultsQueryWrapper.lambda().ne(SowingResults::getId, id);
            //sowingResultsQueryWrapper.lambda().eq(SowingResults::getCode, sowingResults.getCode());
            //sowingResultsQueryWrapper.lambda().eq(SowingResults::getIsDeleted, 0);
            //List<SowingResults> list = this.list(sowingResultsQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(sowingResults);
        return sowingResults;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSowingResults(int id) {
        SowingResults sowingResults = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(sowingResults)) {
            throw new IllegalArgumentException("播种结果不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        sowingResults.setUpdateBy(CurrentUserUtil.getUsername());
        sowingResults.setUpdateTime(new Date());
        sowingResults.setIsDeleted(true);
        this.saveOrUpdate(sowingResults);
    }

    @Override
    public List<LinkedHashMap<String, Object>> querySowingResultsForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.querySowingResultsForExcel(paramMap);
    }

    @Override
    public List<SowingResults> getByCondition(SowingResults condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

    @Override
    public List<SeedingWallLayoutTwoVo> sketchMap() {
        //TODO 远程调用
        List<SeedingWallLayoutTwoVo> seedingWallLayoutVos = this.seedingWallLayoutService.sketchMap(CurrentUserUtil.getCurrrentUserWorkbenchCode(), 1L);

        //查询扫描结果
        QueryWrapper<SowingResults> condition = new QueryWrapper<>();
        condition.lambda().in(SowingResults::getStatus, Arrays.asList(SowingResultsStatusEnum.TWO.getCode(), SowingResultsStatusEnum.THREE.getCode()))
                .eq(SowingResults::getIsDeleted, false);
        Map<String, SowingResults> map = this.baseMapper.selectList(condition).stream().collect(Collectors.toMap(e -> e.getSeedingPositionNum(), e -> e));

        //查询播种墙位置
        for (SeedingWallLayoutTwoVo seedingWallLayoutVo : seedingWallLayoutVos) {
            List<List<SeedingWallLayout>> layout = seedingWallLayoutVo.getLayout();
            for (int x = 0; x < layout.size(); x++) {
                List<SeedingWallLayout> seedingWallLayouts = layout.get(x);
                for (int y = 0; y < seedingWallLayouts.size(); y++) {
//                    SeedingWallLayout seedingWallLayout = layout[x][y];
                    SeedingWallLayout seedingWallLayout = seedingWallLayouts.get(y);
                    SowingResults sowingResults = map.get(seedingWallLayout.getCode());
                    if (sowingResults != null) {
                        seedingWallLayout.setColumnOne(true);
                    } else {
                        seedingWallLayout.setColumnOne(false);
                    }
                }
            }
        }

        return seedingWallLayoutVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        SowingResults sowingResults = this.getById(id);
        SowingResults tmp = new SowingResults();
        tmp.setId(id);
        this.updateById(tmp.setIsDeleted(true));
        //返回数量
        IncomingSeeding incomingSeeding = this.incomingSeedingService.getById(sowingResults.getIncomingSeedingId());
        Double num = incomingSeeding.getAllocatedQuantity() - sowingResults.getSowingQuantity();
        IncomingSeeding update = new IncomingSeeding();
        update.setId(incomingSeeding.getId()).setUpdateTime(new Date()).setUpdateBy(CurrentUserUtil.getUsername());
        this.incomingSeedingService.updateById(update.setAllocatedQuantity(num));
    }

}
