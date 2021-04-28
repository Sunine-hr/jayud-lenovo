package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.model.bo.AreaForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaForm;
import com.jayud.storage.model.bo.WarehouseAreaForm;
import com.jayud.storage.model.po.WarehouseArea;
import com.jayud.storage.mapper.WarehouseAreaMapper;
import com.jayud.storage.model.vo.GoodVO;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import com.jayud.storage.service.IWarehouseAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 仓库区域表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Service
public class WarehouseAreaServiceImpl extends ServiceImpl<WarehouseAreaMapper, WarehouseArea> implements IWarehouseAreaService {

    @Override
    public boolean saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseAreaForm) {
        for (AreaForm areaForm : warehouseAreaForm.getAreaForms()) {
            WarehouseArea warehouseArea = ConvertUtil.convert(areaForm, WarehouseArea.class);
            warehouseArea.setCreateTime(LocalDateTime.now());
            warehouseArea.setCreateUser(UserOperator.getToken());
            warehouseArea.setWarehouseId(warehouseAreaForm.getWarehouseId());
            warehouseArea.setStatus(1);
            warehouseArea.setRemarks(warehouseAreaForm.getRemarks());
            boolean b = this.saveOrUpdate(warehouseArea);
            if(!b){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<WarehouseAreaVO> getList(QueryWarehouseAreaForm form) {
        return this.baseMapper.getList(form);
    }

    @Override
    public IPage<WarehouseAreaVO> findWarehouseAreaByPage(QueryWarehouseAreaForm form) {
        Page<WarehouseAreaVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<WarehouseAreaVO> pageInfo = this.baseMapper.findWarehouseAreaByPage(page,form);
        return pageInfo;
    }
}
