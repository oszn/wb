package com.example.demo.MybaitsInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select ("SELECT * from WeiboInformation where id=#{id}")
    List<comment> SelectComment(@Param ("id")String id);

}
