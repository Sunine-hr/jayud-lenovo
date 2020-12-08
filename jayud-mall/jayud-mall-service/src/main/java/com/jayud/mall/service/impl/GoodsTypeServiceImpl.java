package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.GoodsTypeMapper;
import com.jayud.mall.model.bo.GoodsTypeForm;
import com.jayud.mall.model.po.GoodsType;
import com.jayud.mall.model.vo.GoodsTypeReturnVO;
import com.jayud.mall.model.vo.GoodsTypeVO;
import com.jayud.mall.service.IGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * (报价&货物)类型表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    @Autowired
    GoodsTypeMapper goodsTypeMapper;

    @Override
    public List<GoodsType> findGoodsType(GoodsTypeForm form) {
        QueryWrapper<GoodsType> queryWrapper = new QueryWrapper<>();
        //类型    1报价类型 2货物类型
        String types = form.getTypes();
        if(types != null && types != ""){
            queryWrapper.eq("types", types);
        }
        //启用状态  0-禁用，1-启用
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }else{
            queryWrapper.eq("status", "1");
        }
        //类型名
        String name = form.getName();
        if(name != null && name != ""){
            queryWrapper.eq("name", name);
        }
        //父级id
        Integer fid = form.getFid();
        if(fid != null){
            queryWrapper.eq("fid", fid);
        }
        List<GoodsType> list = goodsTypeMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public GoodsTypeReturnVO findGoodsTypeBy() {
        //1	普货
        QueryWrapper<GoodsType> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("fid", 1);
        List<GoodsType> list1 = goodsTypeMapper.selectList(queryWrapper1);
        List<GoodsTypeVO> goodsTypeVOS1 = ConvertUtil.convertList(list1, GoodsTypeVO.class);
        //2	特货
        QueryWrapper<GoodsType> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("fid", 2);
        List<GoodsType> list2 = goodsTypeMapper.selectList(queryWrapper2);
        List<GoodsTypeVO> goodsTypeVOS2 = ConvertUtil.convertList(list2, GoodsTypeVO.class);

        //返回对象
        GoodsTypeReturnVO goodsTypeReturnVO = new GoodsTypeReturnVO();
        goodsTypeReturnVO.setGeneralCargo(goodsTypeVOS1);
        goodsTypeReturnVO.setSpecialCargo(goodsTypeVOS2);

        return goodsTypeReturnVO;
    }




}