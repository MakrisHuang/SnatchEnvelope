package com.makris.site.service;

import com.makris.site.pojo.User;

public interface UserService {
    User findUserByName(String username);
    int insertUser(User user);
    int updateUser(User user);
    int deleteUser(long id);
}
