package com.sum;

import org.springframework.context.ApplicationContext;

public class JavaFxSMApplication {
    
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        MainApp.main(args);
    }
    
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
