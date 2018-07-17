package com.example.srpingbootjdbc.service;

import com.example.srpingbootjdbc.bean.Employee;
import com.example.srpingbootjdbc.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee getEmpById(Integer id){
        return employeeMapper.getEmpById(id);
    }

    public void insertEmp(Employee employee){
        employeeMapper.insertEmp(employee);
    }
}
