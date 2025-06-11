package com.sum.view;

import com.sum.domain.entity.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Date;
import java.util.Optional;

/**
 * 用户编辑对话框
 */
public class UserEditDialog extends Dialog<User> {

    private TextField usernameField;
    private PasswordField passwordField;
    private TextField emailField;
    private final User user;

    /**
     * 创建用户编辑对话框
     * @param user 要编辑的用户，如果为null则为添加新用户
     */
    public UserEditDialog(User user) {
        this.user = user;
        
        setTitle(user == null ? "添加用户" : "编辑用户");
        setHeaderText(null);
        setResizable(false);
        
        // 创建表单
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 20));
        
        usernameField = new TextField();
        usernameField.setPromptText("用户名");
        usernameField.setPrefWidth(250);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("密码");
        
        emailField = new TextField();
        emailField.setPromptText("电子邮箱");
        
        // 如果是编辑模式，填充现有数据
        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            emailField.setText(user.getEmail());
        }
        
        // 创建标签
        Label usernameLabel = new Label("用户名:");
        usernameLabel.setTextFill(Color.BLACK);
        
        Label passwordLabel = new Label("密码:");
        passwordLabel.setTextFill(Color.BLACK);
        
        Label emailLabel = new Label("电子邮箱:");
        emailLabel.setTextFill(Color.BLACK);
        
        // 添加到表单
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        
        // 设置列宽自动调整
        GridPane.setHgrow(usernameField, Priority.ALWAYS);
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setHgrow(emailField, Priority.ALWAYS);
        
        getDialogPane().setContent(grid);
        
        // 添加按钮
        ButtonType saveButtonType = new ButtonType("保存", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        // 设置按钮样式
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        // 设置结果转换器
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                User resultUser = user == null ? new User() : user;
                resultUser.setUsername(usernameField.getText());
                resultUser.setPassword(passwordField.getText());
                resultUser.setEmail(emailField.getText());
                
                // 设置创建时间和最后登录时间
                if (user == null) {
                    resultUser.setRegistrationTime(new Date());
                    resultUser.setLastLoginTime(new Date());
                }
                
                return resultUser;
            }
            return null;
        });
        
        // 聚焦第一个字段
        if (user == null) {
            Platform.runLater(() -> usernameField.requestFocus());
        } else {
            Platform.runLater(() -> passwordField.requestFocus());
        }
    }
} 