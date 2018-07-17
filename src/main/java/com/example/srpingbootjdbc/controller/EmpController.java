package com.example.srpingbootjdbc.controller;

import com.example.srpingbootjdbc.bean.Employee;
import com.example.srpingbootjdbc.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping("/emp/{id}")
    @Cacheable(cacheNames = "emp",key = "'emp_'+#id")
    public Employee getEmployee(@PathVariable Integer id){
        System.out.println(id);
        return empService.getEmpById(id);
    }

    @GetMapping("/emp")
    public Employee insertEmployee(Employee employee){
        empService.insertEmp(employee);
        return employee;
    }
}
