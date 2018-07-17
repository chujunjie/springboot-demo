package com.example.srpingbootjdbc.mapper;

import com.example.srpingbootjdbc.bean.Employee;

public interface EmployeeMapper {


    Employee getEmpById(Integer id);

    int insertEmp(Employee employee);
}
