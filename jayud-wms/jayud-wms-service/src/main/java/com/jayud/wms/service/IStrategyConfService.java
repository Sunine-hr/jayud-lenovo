package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.AddStrategyConfForm;
import com.jayud.wms.model.po.StrategyConf;
import com.jayud.wms.model.vo.StrategyConfVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略配置 服务类
 *
 * @author jyd
 * @since 2022-01-13
 */
public interface IStrategyConfService extends IService<StrategyConf> {

    /**
    *  分页查询
    * @param strategyConf
    * @param req
    * @return
    */
    IPage<StrategyConf> selectPage(StrategyConf strategyConf,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param strategyConf
    * @return
    */
    List<StrategyConf> selectList(StrategyConf strategyConf);

    /**
     * 保存(新增+编辑)
     * @param strategyConf
     */
    StrategyConf saveOrUpdateStrategyConf(StrategyConf strategyConf);

    /**
     * 逻辑删除
     * @param id
     */
    void delStrategyConf(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryStrategyConfForExcel(Map<String, Object> paramMap);


    void addOrUpdate(AddStrategyConfForm form);

    StrategyConfVO getDetails(Long id);
}
