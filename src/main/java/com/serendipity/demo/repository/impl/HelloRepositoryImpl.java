package com.serendipity.demo.repository.impl;

import com.serendipity.demo.repository.HelloRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HelloRepositoryImpl implements HelloRepository {

    @Override
    public String getHello() {
        return "Hello from Repository!";
    }
}
