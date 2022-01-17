package com.jayud.scm.service;

import com.jayud.scm.model.po.FeeList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.FeeListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 结算条款费用明细 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
public interface IFeeListService extends IService<FeeList> {

    List<FeeListVO> getFeeListByFeeId(Integer id);

    void delete(Integer id);
}
