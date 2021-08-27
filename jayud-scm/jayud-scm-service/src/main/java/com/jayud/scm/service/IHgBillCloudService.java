package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgBillCloud;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HgBillCloudVO;

/**
 * <p>
 * 报关单一对接跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-26
 */
public interface IHgBillCloudService extends IService<HgBillCloud> {

    IPage<HgBillCloudVO> findByPage(QueryCommonForm form);
}
