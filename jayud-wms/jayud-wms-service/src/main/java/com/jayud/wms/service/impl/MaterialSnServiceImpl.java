package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.MaterialSn;
import com.jayud.wms.mapper.MaterialSnMapper;
import com.jayud.wms.service.IMaterialSnService;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料sn信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-21
 */
@Service
public class MaterialSnServiceImpl extends ServiceImpl<MaterialSnMapper, MaterialSn> implements IMaterialSnService {


    @Autowired
    private MaterialSnMapper materialSnMapper;

    @Override
    public IPage<MaterialSn> selectPage(MaterialSn materialSn,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<MaterialSn> page=new Page<MaterialSn>(pageNo, pageSize);
        IPage<MaterialSn> pageList= materialSnMapper.pageList(page, materialSn);
        return pageList;
    }

    @Override
    public List<MaterialSn> selectList(MaterialSn materialSn){
        return materialSnMapper.list(materialSn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MaterialSn saveOrUpdateMaterialSn(MaterialSn materialSn) {
        Long id = materialSn.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            materialSn.setCreateBy(CurrentUserUtil.getUsername());
            materialSn.setCreateTime(new Date());

            QueryWrapper<MaterialSn> materialSnQueryWrapper = new QueryWrapper<>();
            materialSnQueryWrapper.lambda().eq(MaterialSn::getIsDeleted, 0);
            List<MaterialSn> list = this.list(materialSnQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        }else{
            //修改 --> update 更新人、更新时间
            materialSn.setUpdateBy(CurrentUserUtil.getUsername());
            materialSn.setUpdateTime(new Date());

            QueryWrapper<MaterialSn> materialSnQueryWrapper = new QueryWrapper<>();
            materialSnQueryWrapper.lambda().ne(MaterialSn::getId, id);
            materialSnQueryWrapper.lambda().eq(MaterialSn::getIsDeleted, 0);
            List<MaterialSn> list = this.list(materialSnQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(materialSn);
        return materialSn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMaterialSn(int id) {
        MaterialSn materialSn = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(materialSn)){
            throw new IllegalArgumentException("物料sn信息不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        materialSn.setUpdateBy(CurrentUserUtil.getUsername());
        materialSn.setUpdateTime(new Date());
        materialSn.setIsDeleted(true);
        this.saveOrUpdate(materialSn);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryMaterialSnForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryMaterialSnForExcel(paramMap);
    }

    @Override
    public void updateByCondition(MaterialSn materialSn, MaterialSn condition) {
        materialSn.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(new Date());
        this.update(materialSn, new QueryWrapper<>(condition));
    }

    @Override
    public List<MaterialSn> getByCondition(MaterialSn condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

}
