package com.example.demo.entities;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Role implements Serializable {

    @Id
    private int code;
    private String name;
    private String description;

}
