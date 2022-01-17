package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddFeeLadderForm;
import com.jayud.scm.model.bo.AddFeeLadderSettingForm;
import com.jayud.scm.model.po.FeeLadder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.FeeLadderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 结算条款阶梯价 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
public interface IFeeLadderService extends IService<FeeLadder> {

    List<FeeLadderVO> getFeeLadderByFeeId(Integer id);

    boolean stepPriceSetting(AddFeeLadderSettingForm form);
}