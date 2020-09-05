package com.jayud.oauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.model.bo.QuerySystemUserForm;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.SystemUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    IPage<SystemUserVO> getPageList(Page page, @Param("form") QuerySystemUserForm form);

}
