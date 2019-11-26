package com.example.springbootdemo.base.controller;

import com.example.springbootdemo.base.bean.Department;
import com.example.springbootdemo.base.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * @Cacheable 的几个属性,支持spel表达式，“#id > 1”
     *         cacheNames:value,指定缓存组件的名字
     *         key:缓存数据使用的key，默认使用方法参数的值
     *         keyGenerator:key的生成器，自定义key生成器的组件id，与key二选一使用
     *         cacheManager:指定缓存管理器；或者cacheResolver指定获取解析器，二选一使用
     *         condition：指定符合条件情况下进行缓存
     *         unless:指定符合条件情况下不进行缓存
     *         sync:是否开启异步模式
     * @CachePut 先调用方法，再更新缓存（用于update方法）
     * @CacheEvict 清除缓存（用于delete方法）
     *         allEntries = true 指定清除这个缓存中的所有数据
     *         beforeInvocation = true 在方法执行之前清除缓存，默认为false，防止数据库删除事务回滚情况
     * @CacheConfig 类名全局配置
     */
    @Cacheable(cacheNames = "emp",condition = "#id > 1")
    @GetMapping("/dept/{id}")
    public Department getDepartment(@PathVariable("id") Integer id){
        System.out.println(id);
        return departmentMapper.getDeptById(id);
    }

    @CacheEvict(allEntries = true,beforeInvocation = true)
    @GetMapping("/dept")
    public Department insertDept(Department department){
        departmentMapper.insertDept(department);
        return department;
    }
}
