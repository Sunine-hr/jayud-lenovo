package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AuditLineForm;
import com.jayud.oms.model.bo.LineBatchOprForm;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.po.Line;
import com.jayud.oms.model.vo.LineDetailsVO;
import com.jayud.oms.model.vo.LineVO;

import java.util.List;

/**
 * <p>
 * 线路管理 服务类
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
public interface ILineService extends IService<Line> {

    /**
     * 分页查询线路
     * @param form
     * @return
     */
    IPage<LineVO> findLineByPage(QueryLineForm form);

    /**
     * 校验唯一性
     * @param info
     * @return
     */
    boolean checkUniqueByName(Line info);

    /**
     * 校验唯一性
     * @param info
     * @return
     */
    boolean checkUniqueByCode(Line info);

    /**
     * 新增编辑线路
     * @param line
     * @return
     */
    boolean saveOrUpdateLine(Line line);

    /**
     * 批量操作
     * @param form
     */
    void batchOprLine(LineBatchOprForm form);

    /**
     * 删除线路
     * @param id
     */
    void delLine(Long id);

    /**
     * 查看线路详情
     * @param id
     * @return
     */
    LineDetailsVO getLineDetails(Long id);

    /**
     * 线路审核
     * @param form
     */
    void auditLine(AuditLineForm form);

    /**
     * 检查线路是否存在
     * @param id
     * @return
     */
    boolean checkExists(Long id);

    /**
     * 查询启用的线路
     * @param lineName
     * @return
     */
    List<Line> getEnableLine(String lineName);
}
