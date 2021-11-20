package com.jayud.scm.service;

import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCheckOrderForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CheckOrderVO;

import java.util.List;

/**
 * <p>
 * 提验货主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
public interface ICheckOrderService extends IService<CheckOrder> {

    boolean delete(DeleteForm deleteForm);

    List<CheckOrderVO> getCheckOrderByBookingId(QueryCommonForm form);

    CommonResult automaticGenerationCheckOrder(QueryCommonForm form);

    boolean saveOrUpdateCheckOrder(AddCheckOrderForm form);

    CheckOrderVO getCheckOrderById(Integer id);

    boolean dispatch(List<Integer> checkIds, Integer id, String deliverNo);

    boolean deleteDispatch(List<Integer> checkIds);
}
