package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.BNoRule;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 编号规则表 服务类
 *
 * @author jayud
 * @since 2022-03-01
 */
public interface IBNoRuleService extends IService<BNoRule> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: bNoRule
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BNoRule>
     **/
    IPage<BNoRule> selectPage(BNoRule bNoRule,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: bNoRule
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.BNoRule>
     **/
    List<BNoRule> selectList(BNoRule bNoRule);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-01
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);

    /**
     * 根据code获取单据
     * @param sheetCode
     * @return
     */
    BNoRule getNoRulesBySheetCode(String sheetCode);
}
