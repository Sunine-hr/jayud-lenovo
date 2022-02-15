package com.jayud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.system.entity.User;
import com.jayud.system.mapper.UserMapper;
import com.jayud.system.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author ciro
 * @date 2022/2/15 11:22
 * @description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
