package com.makris.site.service.impl;

import com.makris.site.mapper.UserMapper;
import com.makris.site.pojo.User;
import com.makris.site.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper = null;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,  isolation = Isolation.READ_COMMITTED)
    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,  isolation = Isolation.READ_COMMITTED)
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,  isolation = Isolation.READ_COMMITTED)
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,  isolation = Isolation.READ_COMMITTED)
    public int deleteUser(long id) {
        return userMapper.deleteUser(id);
    }
}
