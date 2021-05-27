package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.po.RelocationRecord;
import com.jayud.storage.mapper.RelocationRecordMapper;
import com.jayud.storage.model.vo.RelocationGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.model.vo.StockVO;
import com.jayud.storage.service.IRelocationRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 移库信息表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Service
public class RelocationRecordServiceImpl extends ServiceImpl<RelocationRecordMapper, RelocationRecord> implements IRelocationRecordService {

    @Override
    public List<RelocationGoodsOperationRecordFormVO> getListBySkuAndLocationCode(String sku, String locationCode) {
        return this.baseMapper.getListBySkuAndLocationCode(sku,locationCode);
    }

    @Override
    public IPage<RelocationRecordVO> findByPage(QueryRelocationRecordForm form) {
        Page<StockVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<RelocationRecordVO> pageInfo = this.baseMapper.findByPage(page,form);
        return pageInfo;
    }
}
