package com.jayud.oceanship.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oceanship.bo.AddSeaBillForm;
import com.jayud.oceanship.bo.AddSeaReplenishment;
import com.jayud.oceanship.bo.QueryBulkCargolForm;
import com.jayud.oceanship.bo.QuerySeaBillForm;
import com.jayud.oceanship.po.SeaBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oceanship.vo.SeaBillFormVO;
import com.jayud.oceanship.vo.SeaBillVO;

import java.util.List;

/**
 * <p>
 * 提单信息表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-23
 */
public interface ISeaBillService extends IService<SeaBill> {

    boolean createSeaBill(AddSeaReplenishment addSeaReplenishment);

    IPage<SeaBillFormVO> findBillByPage(QuerySeaBillForm form);

    boolean saveOrUpdateSeaBill(AddSeaBillForm form);

    SeaBillVO getSeaBillById(Long id);

    List<SeaBillVO> getSeaBillByCondition(QueryBulkCargolForm form);

    boolean deleteSeaBill(Long id);
}
