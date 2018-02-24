package com.makris.site.service.impl;

import com.makris.site.mapper.UserMapper;
import com.makris.site.pojo.User;
import com.makris.site.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper = null;

    private static Logger logger = LogManager.getLogger();

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,  isolation = Isolation.READ_COMMITTED)
    public int insertUser(User user) {
        logger.info("Retrieve user from UserService");
        logger.info(user);
        return userMapper.insertUser(user);
    }
}
