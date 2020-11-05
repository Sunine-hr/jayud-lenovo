package com.jayud.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.bo.SensitiveCommodityForm;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.model.vo.SensitiveCommodityVO;

import java.util.List;

/**
 * <p>
 * 敏感品名表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
public interface ISensitiveCommodityService extends IService<SensitiveCommodity> {

    /**
     * 查询敏感品名list
     * @return
     */
    List<SensitiveCommodity> getSensitiveCommodityList(QuerySensitiveCommodityForm form);

    /**
     * 保存敏感品名
     * @param sensitiveCommodityForm
     */
    CommonResult saveSensitiveCommodity(SensitiveCommodityForm sensitiveCommodityForm);

    /**
     * 删除敏感品名
     * @param id
     */
    void deleteSensitiveCommodityById(Long id);

    /**
     * 分页查询敏感品名
     * @param form
     * @return
     */
    IPage<SensitiveCommodityVO> findSensitiveCommodityByPage(QuerySensitiveCommodityForm form);

    /**
     * <p>导入品名</p>
     * @param list
     */
    void importExcelV2(List<SensitiveCommodity> list);
}
