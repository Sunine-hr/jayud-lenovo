package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddCustomerLineForm;
import com.jayud.oms.model.bo.QueryCustomerLineForm;
import com.jayud.oms.model.po.CustomerLine;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.CustomerLineDetailsVO;
import com.jayud.oms.model.vo.CustomerLineVO;

/**
 * <p>
 * 客户线路管理 服务类
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
public interface ICustomerLineService extends IService<CustomerLine> {

    /**
     * 分页查询客户线路
     * @param form
     * @return
     */
    IPage<CustomerLineVO> findCustomerLineByPage(QueryCustomerLineForm form);

    /**
     * 检查是否已存在
     * @param id
     * @return
     */
    boolean checkExists(Long id);

    /**
     * 删除客户线路
     * @param id
     */
    void delLine(Long id);

    /**
     * 查看客户线路详情
     * @param id
     * @return
     */
    CustomerLineDetailsVO getCustomerLineDetails(Long id);

    /**
     * 新增编辑客户线路
     * @param customerLine
     * @return
     */
    boolean saveOrUpdateCustomerLine(AddCustomerLineForm customerLine);

    /**
     * 客户线路唯一性检查
     * @param customerLineTemp
     */
    void checkUnique(CustomerLine customerLineTemp);

    /**
     * 检查客户线路名称是否存储
     * @param id
     * @param customerLineName
     * @return
     */
    boolean exitName(Long id, String customerLineName);

    /**
     * 检查客户线路编号是否存储
     * @param id
     * @param customerLineCode
     * @return
     */
    boolean exitCode(Long id, String customerLineCode);
}
