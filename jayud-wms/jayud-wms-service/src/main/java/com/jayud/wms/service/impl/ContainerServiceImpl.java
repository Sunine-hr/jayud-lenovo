package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.ContainerForm;
import com.jayud.wms.model.po.Container;
import com.jayud.wms.mapper.ContainerMapper;
import com.jayud.wms.service.IContainerService;
import com.jayud.wms.model.vo.ContainerVO;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 容器信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Service
public class ContainerServiceImpl extends ServiceImpl<ContainerMapper, Container> implements IContainerService {


    @Autowired
    private ContainerMapper containerMapper;

    @Override
    public IPage<ContainerVO> selectPage(ContainerForm container,
                                         Integer pageNo,
                                         Integer pageSize,
                                         HttpServletRequest req) {

        Page<ContainerForm> page = new Page<ContainerForm>(pageNo, pageSize);
        IPage<ContainerVO> pageList = containerMapper.pageList(page, container);
        return pageList;
    }

    @Override
    public List<ContainerVO> selectList(ContainerForm container) {
        return containerMapper.list(container);
    }


    @Override
    public boolean saveOrUpdateContainerForm(ContainerForm containerForm) {

        Boolean result = null;
        Container container = ConvertUtil.convert(containerForm, Container.class);
        if (container.getId() != null) {
            container.setUpdateBy(CurrentUserUtil.getUsername());
            container.setUpdateTime(new Date());
            result = this.updateById(container);
        } else {
            container.setCreateBy(CurrentUserUtil.getUsername());
            container.setCreateTime(new Date());
            result = this.saveOrUpdate(container);
        }

        if (result) {
            log.warn("新增或修改库区成功");
            return true;
        }
        return false;
    }

    //校验和 ID查询 单个详情信息
    @Override
    public ContainerForm getContainerOne(String code, Long id, String createBy, Long warehouseId) {

        Container container = new Container();
        container.setCode(code);
        container.setId(id);
        container.setWarehouseId(warehouseId);
        ContainerForm containerForm = containerMapper.getContainerOne(container);
        return containerForm;
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryContainerForExcel(ContainerForm containerForm,
                                                                      Integer pageNo,
                                                                      Integer pageSize,
                                                                      HttpServletRequest req) {
//        Page<ContainerForm> page=new Page<ContainerForm>(pageNo,pageSize);
        List<LinkedHashMap<String, Object>> linkedHashMaps = containerMapper.queryContainerForExcel(containerForm);
        return linkedHashMaps;
    }

    @Override
    public List<Container> getEnableByWarehouseId(Long warehouseId) {
        return this.baseMapper.selectList(new QueryWrapper<>(new Container().setWarehouseId(warehouseId).setStatus(true).setIsDeleted(false)));
    }


}
