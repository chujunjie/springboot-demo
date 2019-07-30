package com.example.springbootdemo.mapper;

import com.example.springbootdemo.bean.Employee;

public interface EmployeeMapper {


    Employee getEmpById(Integer id);

    int insertEmp(Employee employee);
}
