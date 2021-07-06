package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryBusinessLogForm;
import com.jayud.mall.model.po.BusinessLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.BusinessLogVO;

/**
 * <p>
 * 业务日志表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-24
 */
public interface IBusinessLogService extends IService<BusinessLog> {

    /**
     * 分页查询业务操作日志
     * @param form
     * @return
     */
    IPage<BusinessLogVO> findBusinessLogByPage(QueryBusinessLogForm form);
}
