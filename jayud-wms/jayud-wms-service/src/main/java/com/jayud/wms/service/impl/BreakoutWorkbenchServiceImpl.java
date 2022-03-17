package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.BreakoutWorkbenchForm;
import com.jayud.wms.model.po.BreakoutWorkbench;
import com.jayud.wms.model.po.SeedingWall;
import com.jayud.wms.model.po.Workbench;
import com.jayud.wms.mapper.BreakoutWorkbenchMapper;
import com.jayud.wms.service.IBreakoutWorkbenchService;
import com.jayud.wms.service.ISeedingWallService;
import com.jayud.wms.service.IWorkbenchService;
import com.jayud.wms.model.vo.WorkbenchVO;
import com.jayud.common.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台类型信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-17
 */
@Service
public class BreakoutWorkbenchServiceImpl extends ServiceImpl<BreakoutWorkbenchMapper, BreakoutWorkbench> implements IBreakoutWorkbenchService {


    @Autowired
    private BreakoutWorkbenchMapper breakoutWorkbenchMapper;
    @Autowired
    private IWorkbenchService workbenchService;
    @Autowired
    private ISeedingWallService seedingWallService;

    @Override
    public IPage<BreakoutWorkbench> selectPage(BreakoutWorkbench breakoutWorkbench,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<BreakoutWorkbench> page=new Page<BreakoutWorkbench>(pageNo, pageSize);
        IPage<BreakoutWorkbench> pageList= breakoutWorkbenchMapper.pageList(page, breakoutWorkbench);
        return pageList;
    }

    @Override
    public List<BreakoutWorkbench> selectList(BreakoutWorkbench breakoutWorkbench){
        return breakoutWorkbenchMapper.list(breakoutWorkbench);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BreakoutWorkbench saveOrUpdateBreakoutWorkbench(BreakoutWorkbench breakoutWorkbench) {
        Long id = breakoutWorkbench.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
//            breakoutWorkbench.setCreateBy(CurrentUserUtil.getUsername());
//            breakoutWorkbench.setCreateTime(new Date());

            QueryWrapper<BreakoutWorkbench> breakoutWorkbenchQueryWrapper = new QueryWrapper<>();
            breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getCode, breakoutWorkbench.getCode());
            breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getIsDeleted, 0);
            List<BreakoutWorkbench> list = this.list(breakoutWorkbenchQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        }else{
            //修改 --> update 更新人、更新时间
//            breakoutWorkbench.setUpdateBy(CurrentUserUtil.getUsername());
//            breakoutWorkbench.setUpdateTime(new Date());

            QueryWrapper<BreakoutWorkbench> breakoutWorkbenchQueryWrapper = new QueryWrapper<>();
            breakoutWorkbenchQueryWrapper.lambda().ne(BreakoutWorkbench::getId, id);
            breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getCode, breakoutWorkbench.getCode());
            breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getIsDeleted, 0);
            List<BreakoutWorkbench> list = this.list(breakoutWorkbenchQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(breakoutWorkbench);
        return breakoutWorkbench;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBreakoutWorkbench(int id) {
        BreakoutWorkbench breakoutWorkbench = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(breakoutWorkbench)){
            throw new IllegalArgumentException("工作台类型信息不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
//        breakoutWorkbench.setUpdateBy(CurrentUserUtil.getUsername());
//        breakoutWorkbench.setUpdateTime(new Date());
        breakoutWorkbench.setIsDeleted(true);
        this.saveOrUpdate(breakoutWorkbench);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryBreakoutWorkbenchForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryBreakoutWorkbenchForExcel(paramMap);
    }

    @Override
    public WorkbenchVO queryByWorkbenchId(Integer workbenchId) {
        WorkbenchVO vo = new WorkbenchVO();
        Workbench workbench = workbenchService.getById(workbenchId);
        vo = ConvertUtil.convert(workbench, WorkbenchVO.class);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台不存在，请检查数据");
        }
        Integer type = workbench.getType();//工作台类型(1:普通,2:分播,3:交接)
        if(type == 2){
            //分播工作台，需要查询 管理的播种墙，其他类型不需要
            QueryWrapper<BreakoutWorkbench> breakoutWorkbenchQueryWrapper = new QueryWrapper<>();
            breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getWorkbenchId, workbenchId);
            List<BreakoutWorkbench> list = this.baseMapper.selectList(breakoutWorkbenchQueryWrapper);
            vo.setBreakoutWorkbenches(list);
        }else{
            vo.setBreakoutWorkbenches(new ArrayList<>());
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBreakoutWorkbench(WorkbenchVO bo) {
        Long workbenchId = bo.getId();
        Workbench workbench = workbenchService.getById(workbenchId);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台不存在，请检查数据");
        }
        workbench = ConvertUtil.convert(bo, Workbench.class);
        workbenchService.saveOrUpdate(workbench);

        //1先删除
        QueryWrapper<BreakoutWorkbench> breakoutWorkbenchQueryWrapper = new QueryWrapper<>();
        breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getWorkbenchId, workbenchId);
        this.baseMapper.delete(breakoutWorkbenchQueryWrapper);

        Integer type = workbench.getType();//工作台类型(1:普通,2:分播,3:交接)
        if(type == 2){
            //分播工作台，需要管理的播种墙，其他类型不需要
            //2再保存
            List<BreakoutWorkbench> breakoutWorkbenches = bo.getBreakoutWorkbenches();
            if(CollUtil.isNotEmpty(breakoutWorkbenches)){
                int size = breakoutWorkbenches.size();
                if(size != 1){
                    throw new IllegalArgumentException("一个分播工作台只能关联一个播种墙，请检查数据");
                }
                this.saveOrUpdateBatch(breakoutWorkbenches);
            }
        }
    }

    @Override
    public BreakoutWorkbench addBreakoutWorkbench(BreakoutWorkbenchForm bo) {
        Long workbenchId = bo.getWorkbenchId();
        Workbench workbench = workbenchService.getById(workbenchId);
        if(ObjectUtil.isEmpty(workbench)){
            throw new IllegalArgumentException("工作台不存在，请检查数据");
        }
        Long warehouseIdByWorkbench = workbench.getWarehouseId();
        String code = bo.getSeedingWallCode();//编号,播种墙编号
        QueryWrapper<SeedingWall> seedingWallQueryWrapper = new QueryWrapper<>();
        seedingWallQueryWrapper.lambda().eq(SeedingWall::getCode, code);
        seedingWallQueryWrapper.lambda().eq(SeedingWall::getIsDeleted, 0);
        List<SeedingWall> seedingWalls = seedingWallService.list(seedingWallQueryWrapper);
        if(CollUtil.isEmpty(seedingWalls)){
            throw new IllegalArgumentException("播种墙编号不存在，请检查数据");
        }
        SeedingWall seedingWall = seedingWalls.get(0);
        Long warehouseIdByseedingWall = seedingWall.getWarehouseId();
        if(!warehouseIdByWorkbench.equals(warehouseIdByseedingWall)){
            throw new IllegalArgumentException("播种墙编号 与 工作台，所属仓库不一致");
        }
        QueryWrapper<BreakoutWorkbench> breakoutWorkbenchQueryWrapper = new QueryWrapper<>();
        breakoutWorkbenchQueryWrapper.lambda().eq(BreakoutWorkbench::getCode, code);
        List<BreakoutWorkbench> breakoutWorkbenches = this.baseMapper.selectList(breakoutWorkbenchQueryWrapper);
        if(CollUtil.isNotEmpty(breakoutWorkbenches)){
            throw new IllegalArgumentException("播种墙编号已被其他工作台使用，请检查数据");
        }

        BreakoutWorkbench breakoutWorkbench = new BreakoutWorkbench();
        breakoutWorkbench.setCode(seedingWall.getCode());
        breakoutWorkbench.setRemark(seedingWall.getRemark());
        breakoutWorkbench.setType(2);//工作台类型(1:普通,2:分播,3:交接)  只有分播工作台，才能加播种墙，这里固定值为2
        breakoutWorkbench.setWorkbenchId(workbenchId);

        return breakoutWorkbench;
    }

}
