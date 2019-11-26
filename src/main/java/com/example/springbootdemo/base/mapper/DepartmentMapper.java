package com.example.springbootdemo.base.mapper;

import com.example.springbootdemo.base.bean.Department;
import org.apache.ibatis.annotations.*;


public interface DepartmentMapper {

    @Select("select * from department where id = #{id}")
    Department getDeptById(Integer id);

    @Delete("delete * from department where id = #{id}")
    int delDeptById(Integer id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into department(name) values (#{departmentName})")
    int insertDept(Department department);

    @Update("update department set name = #{departmentName} where id = #{id}")
    int updateDept(Department department);
}
