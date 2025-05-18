package com.serendipity.demo.service.impl;

import com.serendipity.demo.repository.HelloRepository;
import com.serendipity.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private HelloRepository helloRepository;

    @Override
    public String sayHello() {
        return helloRepository.getHello();
    }
}
