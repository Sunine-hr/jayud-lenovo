package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.BillClearanceInfoForm;
import com.jayud.mall.model.bo.ClearanceInfoCaseForm;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.jayud.mall.mapper.ClearanceInfoCaseMapper;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import com.jayud.mall.service.IBillClearanceInfoService;
import com.jayud.mall.service.IClearanceInfoCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 清关文件箱号 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Service
public class ClearanceInfoCaseServiceImpl extends ServiceImpl<ClearanceInfoCaseMapper, ClearanceInfoCase> implements IClearanceInfoCaseService {

    @Autowired
    IClearanceInfoCaseService clearanceInfoCaseService;
    /**
     * 集合数据
     * @param
     * @return
     */
    public IPage<ClearanceInfoCaseVO> findClearanceInfoCaseByPage(ClearanceInfoCaseForm form){
        //定义分页参数
        Page<ClearanceInfoCaseVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<ClearanceInfoCaseVO> pageInfo = clearanceInfoCaseService.findClearanceInfoCaseByPage(form);
        return  pageInfo;
    }


    /**
     * 增加
     * @param clearanceInfoCase
     */
    public void insertClearanceInfoCase(ClearanceInfoCaseForm clearanceInfoCase){
        clearanceInfoCaseService.insertClearanceInfoCase(clearanceInfoCase);
    }

    /**
     * 修改
     * @param clearanceInfoCase
     */
    public void updateClearanceInfoCase(ClearanceInfoCaseForm clearanceInfoCase){
        clearanceInfoCaseService.updateClearanceInfoCase(clearanceInfoCase);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteClearanceInfoCase(Long id){
        clearanceInfoCaseService.deleteClearanceInfoCase(id);
    }

}
