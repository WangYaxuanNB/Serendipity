package com.sum;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {
    private VBox loginForm;
    
    public LoginView() {
        initializeUI();
    }
    
    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        // 创建主容器
        loginForm = new VBox(20);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(50));
        loginForm.setStyle("-fx-background-color: white;"); // 添加背景颜色

        // 添加顶部图标
        ImageView logoImageView = new ImageView(new Image("https://placehold.co/64x64"));
        logoImageView.setFitWidth(64);
        logoImageView.setFitHeight(64);
        
        // 欢迎信息
        Label welcomeLabel = new Label("Welcome back!");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
    
        Label subtitleLabel = new Label("Please enter your details");
        subtitleLabel.setFont(Font.font("System", 14));
        
        // 邮箱输入框
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(300);
        
        // 密码输入框
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(300);
        
        // 显示密码按钮
        Button showPasswordButton = new Button("👁️");
        showPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #999;");
        
        HBox passwordContainer = new HBox(10);
        passwordContainer.getChildren().addAll(passwordField, showPasswordButton);
        
        // 记住我选项
        CheckBox rememberMeCheckBox = new CheckBox("Remember for 30 days");
        rememberMeCheckBox.setSelected(true);
        
        // 忘记密码链接
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot password?");
        
        HBox optionsContainer = new HBox(10);
        optionsContainer.getChildren().addAll(rememberMeCheckBox, forgotPasswordLink);
        
        // 登录按钮
        Button loginButton = new Button("Log In");
        loginButton.setStyle("-fx-background-color: #000; -fx-text-fill: white; -fx-padding: 10 20;");
        
        // Google登录按钮
        Button googleLoginButton = new Button("Log in with Google");
        googleLoginButton.setStyle("-fx-background-color: #fff; -fx-border-color: #ddd; -fx-padding: 10 20;");
        
        // 注册链接
        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        
        // 组合所有元素
        loginForm.getChildren().addAll(
            logoImageView,
            welcomeLabel,
            subtitleLabel,
            emailField,
            passwordContainer,
            optionsContainer,
            loginButton,
            googleLoginButton,
            signUpLink
        );
    }
    
    public VBox getLoginForm() {
        return loginForm;
    }
}