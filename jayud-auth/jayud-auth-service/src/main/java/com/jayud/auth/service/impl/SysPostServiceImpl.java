package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysPostForm;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.model.vo.SysPostVO;
import com.jayud.common.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysPost;
import com.jayud.auth.mapper.SysPostMapper;
import com.jayud.auth.service.ISysPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 岗位表 服务实现类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {


    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public IPage<SysPost> selectPage(SysPost sysPost,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysPost> page=new Page<SysPost>(currentPage,pageSize);
        IPage<SysPost> pageList= sysPostMapper.pageList(page, sysPost);


        return pageList;
    }

    @Override
    public List<SysPost> selectList(SysPost sysPost){
        return sysPostMapper.list(sysPost);
    }

    @Override
    public List<SysPostVO> selectSysPostLists(SysPostForm sysPost) {


        List<SysPostVO> sysPostVOS = sysPostMapper.selectSysPostLists(sysPost);

        List<SysPostVO> sysPostVOS1 = buildTree(sysPostVOS, "0");
        return sysPostVOS1;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysPostMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysPostMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public boolean saveOrUpdateSysPost(SysPostForm sysPostForm) {
        Boolean result = null;
        SysPost sysPost = ConvertUtil.convert(sysPostForm, SysPost.class);
        if(sysPost.getId()!=null){ //那就是有id  是个修改




            sysPost.setUpdateBy(CurrentUserUtil.getUsername());
            sysPost.setUpdateTime(new Date());
            result= this.updateById(sysPost);
        }else{
            sysPost.setUpdateBy(CurrentUserUtil.getUsername());
            sysPost.setUpdateTime(new Date());
            result= this.save(sysPost);

        }
        if (result) {
            log.warn("新增或修改库区成功");
            return true;
        }

        return false;
    }


    /**
     * 构建树
     *
     * @param list
     * @param pid
     * @return
     */
    private List<SysPostVO> buildTree(List<SysPostVO> list, String pid) {
        List<SysPostVO> treeList = new ArrayList<>();
        list.forEach(l -> {
            if (StrUtil.equals(pid, l.getParentId().toString())) {
                l.setChildren(buildTree(list, l.getId().toString()));
                treeList.add(l);
            }
        });
        return treeList;
    }
}
