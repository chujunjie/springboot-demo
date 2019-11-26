package com.example.springbootdemo.base.mapper;

import com.example.springbootdemo.base.bean.Employee;

public interface EmployeeMapper {


    Employee getEmpById(Integer id);

    int insertEmp(Employee employee);
}
