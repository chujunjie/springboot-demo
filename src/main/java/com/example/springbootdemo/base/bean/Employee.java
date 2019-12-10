package com.example.springbootdemo.base.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private Integer id;
    private String lastName;
    private Integer gender;
    private String email;
    private Integer dId;
}
