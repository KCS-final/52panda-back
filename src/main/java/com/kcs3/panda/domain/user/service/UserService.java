package com.kcs3.panda.domain.user.service;

import com.kcs3.panda.domain.user.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {

    List<User> getUsers() throws ExecutionException, InterruptedException;

}
