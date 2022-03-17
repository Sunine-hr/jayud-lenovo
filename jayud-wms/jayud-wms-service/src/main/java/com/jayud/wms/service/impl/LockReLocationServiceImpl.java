package com.jayud.wms.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.LockReLocation;
import com.jayud.wms.mapper.LockReLocationMapper;
import com.jayud.wms.service.ILockReLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐库位锁定 服务实现类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Service
public class LockReLocationServiceImpl extends ServiceImpl<LockReLocationMapper, LockReLocation> implements ILockReLocationService {


    @Autowired
    private LockReLocationMapper lockReLocationMapper;

    @Override
    public IPage<LockReLocation> selectPage(LockReLocation lockReLocation,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<LockReLocation> page=new Page<LockReLocation>(pageNo, pageSize);
        IPage<LockReLocation> pageList= lockReLocationMapper.pageList(page, lockReLocation);
        return pageList;
    }

    @Override
    public List<LockReLocation> selectList(LockReLocation lockReLocation){
        return lockReLocationMapper.list(lockReLocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LockReLocation saveOrUpdateLockReLocation(LockReLocation lockReLocation) {
        Long id = lockReLocation.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
//            lockReLocation.setCreateBy(CurrentUserUtil.getUsername());
//            lockReLocation.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<LockReLocation> lockReLocationQueryWrapper = new QueryWrapper<>();
            //lockReLocationQueryWrapper.lambda().eq(LockReLocation::getCode, lockReLocation.getCode());
            //lockReLocationQueryWrapper.lambda().eq(LockReLocation::getIsDeleted, 0);
            //List<LockReLocation> list = this.list(lockReLocationQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        }else{
            //修改 --> update 更新人、更新时间
//            lockReLocation.setUpdateBy(CurrentUserUtil.getUsername());
//            lockReLocation.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<LockReLocation> lockReLocationQueryWrapper = new QueryWrapper<>();
            //lockReLocationQueryWrapper.lambda().ne(LockReLocation::getId, id);
            //lockReLocationQueryWrapper.lambda().eq(LockReLocation::getCode, lockReLocation.getCode());
            //lockReLocationQueryWrapper.lambda().eq(LockReLocation::getIsDeleted, 0);
            //List<LockReLocation> list = this.list(lockReLocationQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(lockReLocation);
        return lockReLocation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delLockReLocation(int id) {
        LockReLocation lockReLocation = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(lockReLocation)){
            throw new IllegalArgumentException("推荐库位锁定不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
//        lockReLocation.setUpdateBy(CurrentUserUtil.getUsername());
//        lockReLocation.setUpdateTime(new Date());
        lockReLocation.setIsDeleted(true);
        this.saveOrUpdate(lockReLocation);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryLockReLocationForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryLockReLocationForExcel(paramMap);
    }

}
