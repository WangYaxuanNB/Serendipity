package com.serendipity.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("圆角弹窗");
        primaryStage.show();
        primaryStage.setResizable(false);
        this.primaryStage = primaryStage;
        showLoginPage();
    }

    public void showLoginPage() throws IOException {
        login loginPage = new login(this);
        primaryStage.setScene(loginPage.getScene());
        primaryStage.setTitle("登录");
        primaryStage.show();
    }

    public void showRegisterPage() throws IOException {
        Register registerPage = new Register(this);
        primaryStage.setScene(registerPage.getScene());
        primaryStage.setTitle("注册");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}