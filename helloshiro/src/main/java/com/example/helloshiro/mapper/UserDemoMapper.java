package com.example.helloshiro.mapper;

import com.example.helloshiro.model.UserDemo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDemoMapper {

    UserDemo getByName(@Param("name") String name);

}
