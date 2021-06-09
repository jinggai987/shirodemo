package com.example.helloshiro.test;

import com.example.helloshiro.HelloshiroApplication;
import com.example.helloshiro.service.IUserDemoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = HelloshiroApplication.class)
//@SuppressWarnings("unused")
@SpringBootTest
public class UserDemoServiceTest {

    @Autowired
    private IUserDemoService iUserDemoService;

    @Test
    public void test(){
        System.out.println(iUserDemoService.getByName("zhangsan"));
    }

}
