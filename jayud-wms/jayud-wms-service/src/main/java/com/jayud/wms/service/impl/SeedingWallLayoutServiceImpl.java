package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.wms.mapper.SeedingWallLayoutMapper;
import com.jayud.wms.mapper.SeedingWallMapper;
import com.jayud.wms.model.po.BreakoutWorkbench;
import com.jayud.wms.model.po.SeedingWall;
import com.jayud.wms.model.po.SeedingWallLayout;
import com.jayud.wms.model.po.Workbench;
import com.jayud.wms.model.vo.SeedingWallLayoutColumnVo;
import com.jayud.wms.model.vo.SeedingWallLayoutTwoVo;
import com.jayud.wms.model.vo.SeedingWallLayoutVo;
import com.jayud.wms.service.IBreakoutWorkbenchService;
import com.jayud.wms.service.ISeedingWallLayoutService;
import com.jayud.wms.service.ISeedingWallService;
import com.jayud.wms.service.IWorkbenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 播种位布局 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class SeedingWallLayoutServiceImpl extends ServiceImpl<SeedingWallLayoutMapper, SeedingWallLayout> implements ISeedingWallLayoutService {


    @Autowired
    private SeedingWallLayoutMapper seedingWallLayoutMapper;
    @Autowired
    private SeedingWallMapper seedingWallMapper;
    @Autowired
    private ISeedingWallService seedingWallService;
    @Autowired
    private IWorkbenchService workbenchService;
    @Autowired
    private IBreakoutWorkbenchService breakoutWorkbenchService;

    @Override
    public IPage<SeedingWallLayout> selectPage(SeedingWallLayout seedingWallLayout,
                                               Integer pageNo,
                                               Integer pageSize,
                                               HttpServletRequest req) {

        Page<SeedingWallLayout> page = new Page<SeedingWallLayout>(pageNo, pageSize);
        IPage<SeedingWallLayout> pageList = seedingWallLayoutMapper.pageList(page, seedingWallLayout);
        return pageList;
    }

    @Override
    public List<SeedingWallLayout> selectList(SeedingWallLayout seedingWallLayout) {
        return seedingWallLayoutMapper.list(seedingWallLayout);
    }

    @Override
    public SeedingWallLayoutVo queryBySeedingWallId(Integer seedingWallId) {

        SeedingWall seedingWall = seedingWallMapper.selectById(seedingWallId);
        if (ObjectUtil.isEmpty(seedingWall)) {
            throw new IllegalArgumentException("播种墙不存在，操作失败");
        }

        //示例：3 * 4  即 3行4列
        Integer row = seedingWall.getRow();//行
        Integer column = seedingWall.getColumn();//列
        if(ObjectUtil.isEmpty(row) || ObjectUtil.isEmpty(column)){
            throw new IllegalArgumentException("播种墙行列数不能为空，操作失败");
        }

        //查询 播种位布局 是否存在
        QueryWrapper<SeedingWallLayout> seedingWallLayoutQueryWrapper = new QueryWrapper();
        seedingWallLayoutQueryWrapper.lambda().eq(SeedingWallLayout::getSeedingWallId, seedingWallId);
        List<SeedingWallLayout> layoutList = this.seedingWallLayoutMapper.selectList(seedingWallLayoutQueryWrapper);


        SeedingWallLayoutVo seedingWallLayoutVo = new SeedingWallLayoutVo();
        seedingWallLayoutVo.setSeedingWallId(Long.valueOf(seedingWallId));

        if (CollUtil.isEmpty(layoutList)) {
            // 播种位布局 为空，构造播种位
            SeedingWallLayout[][] layout = new SeedingWallLayout[row][column];
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < column; c++) {
                    SeedingWallLayoutColumnVo columnVo = new SeedingWallLayoutColumnVo();
                    columnVo.setSeedingWallId(Long.valueOf(seedingWallId));
                    columnVo.setCode("");
                    columnVo.setTagNum("");
                    columnVo.setPriority(1);
                    columnVo.setStatus(true);
                    SeedingWallLayout seedingWallLayout = ConvertUtil.convert(columnVo, SeedingWallLayout.class);
                    layout[r][c] = seedingWallLayout;
                }
            }
            seedingWallLayoutVo.setLayout(layout);

        } else {
            // 播种位布局 不为空，构造播种位 需要id

            Long size = Long.valueOf(row) * Long.valueOf(column);
            Long size1 = Long.valueOf(layoutList.size());

            SeedingWallLayout[][] layout = new SeedingWallLayout[row][column];
            if (size1.compareTo(size) != 0) {
                //数据库的布局，和播种墙的不同，重新构造
                for (int r = 0; r < row; r++) {
                    for (int c = 0; c < column; c++) {
                        SeedingWallLayoutColumnVo columnVo = new SeedingWallLayoutColumnVo();
                        columnVo.setSeedingWallId(Long.valueOf(seedingWallId));
                        columnVo.setCode("");
                        columnVo.setTagNum("");
                        columnVo.setPriority(1);
                        columnVo.setStatus(true);
                        SeedingWallLayout seedingWallLayout = ConvertUtil.convert(columnVo, SeedingWallLayout.class);
                        layout[r][c] = seedingWallLayout;
                    }
                }
            } else {
                //数据库的布局，和播种墙的相同同
                int rc = 0;
                for (int r = 0; r < row; r++) {
                    for (int c = 0; c < column; c++) {
                        SeedingWallLayout seedingWallLayout = layoutList.get(rc);
                        layout[r][c] = seedingWallLayout;
                        rc++;
                    }
                }
            }
            seedingWallLayoutVo.setLayout(layout);
        }

        return seedingWallLayoutVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSeedingWallLayout(SeedingWallLayoutVo bo) {
        Long seedingWallId = bo.getSeedingWallId();
        SeedingWall seedingWall = seedingWallMapper.selectById(seedingWallId);
        if (ObjectUtil.isEmpty(seedingWall)) {
            throw new IllegalArgumentException("播种墙不存在，操作失败");
        }
        SeedingWallLayout[][] layout = bo.getLayout();
        List<SeedingWallLayout> seedingWallLayoutList = new ArrayList<>();
        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[r].length; c++) {
                seedingWallLayoutList.add(layout[r][c]);
            }
        }

        //(播种墙id+播种位编号),唯一组合值不能重复
        List<String> collect = seedingWallLayoutList.stream()
                .map(k -> (k.getSeedingWallId() + "_" + k.getCode()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(播种墙id+播种位编号),唯一组合值不能重复");
        }

        QueryWrapper<SeedingWallLayout> seedingWallLayoutQueryWrapper = new QueryWrapper<>();
        seedingWallLayoutQueryWrapper.lambda().eq(SeedingWallLayout::getSeedingWallId, seedingWallId);
        this.remove(seedingWallLayoutQueryWrapper);

        this.saveOrUpdateBatch(seedingWallLayoutList);
    }

    @Override
    public List<SeedingWallLayoutTwoVo> sketchMap(String workbenchCode, Long type) {
        //查询工作台信息
        Workbench workbenche = this.workbenchService.getOne(new QueryWrapper<>(new Workbench().setCode(workbenchCode).setStatus(1).setIsDeleted(false)));
        if (workbenche.getId() == null) {
            throw new ServiceException("暂时没有可用的工作台");
        }
        //然后查询工作台分播墙列表
        if (workbenche.getType() != 2) {
            throw new ServiceException("该工作台类型不是分播类型");
        }
        List<BreakoutWorkbench> breakoutWorkbenchs = this.breakoutWorkbenchService.getBaseMapper().selectList(new QueryWrapper<>(new BreakoutWorkbench().setWorkbenchId(workbenche.getId())));
        if (CollectionUtil.isEmpty(breakoutWorkbenchs)) {
            throw new ServiceException("该工作台暂无绑定分播墙");
        }
        List<String> seedingWallCodes = breakoutWorkbenchs.stream().map(BreakoutWorkbench::getCode).collect(Collectors.toList());
        //过滤类型,没有设置,说明返回所有类型
        QueryWrapper<SeedingWall> condition = new QueryWrapper<>();
        condition.lambda().eq(SeedingWall::getType, type).eq(SeedingWall::getIsDeleted, false).eq(SeedingWall::getStatus, 1).in(SeedingWall::getCode, seedingWallCodes);
        List<SeedingWall> seedingWalls = this.seedingWallService.list(condition);
        if (CollectionUtil.isEmpty(seedingWalls)) {
            throw new ServiceException("该工作台暂无可用分播墙");
        }

        //根据播种id,组合成播种墙布局图
        List<SeedingWallLayoutTwoVo> list = new ArrayList<>();
        seedingWalls.forEach(e -> {
            SeedingWallLayoutTwoVo seedingWallLayoutVo = this.queryBySeedingWallId(e.getId().intValue(), 1);
            list.add(seedingWallLayoutVo);
        });

        return list;
    }

    @Override
    public SeedingWallLayoutTwoVo queryBySeedingWallId(int seedingWallId, int status) {
        SeedingWall seedingWall = seedingWallMapper.selectById(seedingWallId);
        if (ObjectUtil.isEmpty(seedingWall)) {
            throw new IllegalArgumentException("播种墙不存在，操作失败");
        }

        //示例：3 * 4  即 3行4列
        Integer row = seedingWall.getRow();//行
        Integer column = seedingWall.getColumn();//列

        //查询 播种位布局 是否存在
        QueryWrapper<SeedingWallLayout> seedingWallLayoutQueryWrapper = new QueryWrapper();
        seedingWallLayoutQueryWrapper.lambda().eq(SeedingWallLayout::getSeedingWallId, seedingWallId).eq(SeedingWallLayout::getStatus, status);
        List<SeedingWallLayout> layoutList = this.seedingWallLayoutMapper.selectList(seedingWallLayoutQueryWrapper);


        SeedingWallLayoutTwoVo seedingWallLayoutVo = new SeedingWallLayoutTwoVo();
        seedingWallLayoutVo.setSeedingWallId(Long.valueOf(seedingWallId));

        if (CollUtil.isEmpty(layoutList)) {
            throw new ServiceException("暂无可用的播种墙");
        } else {
            // 播种位布局 不为空，构造播种位 需要id

//            Long size = Long.valueOf(row) * Long.valueOf(column);
//            Long size1 = Long.valueOf(layoutList.size());
//            SeedingWallLayout[][] layout;
//            if (column < layoutList.size()) {
//                layout = new SeedingWallLayout[0][layoutList.size()];
//            }
            List<List<SeedingWallLayout>> list = new ArrayList<>();
            SeedingWallLayout[][] layout = new SeedingWallLayout[][]{};

            //数据库的布局，和播种墙的相同同
            int rc = 0;
            for (int r = 0; r < row; r++) {
                List<SeedingWallLayout> tmps = new ArrayList<>();
                for (int c = 0; c < column; c++) {
                    try {
                        SeedingWallLayout seedingWallLayout = layoutList.get(rc);
//                        layout[r][c] = seedingWallLayout;
                        tmps.add(seedingWallLayout);
                        rc++;
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    }
                }
                if (!CollectionUtil.isEmpty(tmps)) {
                    list.add(tmps);
                }
            }
            seedingWallLayoutVo.setLayout(list);
        }

        return seedingWallLayoutVo;
    }

}
