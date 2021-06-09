package com.example.helloshiro.service;

import com.example.helloshiro.mapper.UserDemoMapper;
import com.example.helloshiro.model.UserDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserDemoService")
public class UserDemoServiceImpl implements IUserDemoService{
    @Autowired
    private UserDemoMapper iUserDemoMapper;

    @Override
    public UserDemo getByName(String name) {
        return iUserDemoMapper.getByName(name);
    }
}
