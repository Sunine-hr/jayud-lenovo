package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgBillFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HgBillFollowVO;
import com.jayud.scm.model.vo.HgBillVO;

/**
 * <p>
 * 入库单跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
public interface IHgBillFollowService extends IService<HgBillFollow> {

    IPage<HgBillFollowVO> findListByHgBillId(QueryCommonForm form);
}
