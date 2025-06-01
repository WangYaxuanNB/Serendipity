package com.serendipity.demo;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import com.serendipity.demo.entity.User;

public class UserEditDialog extends Dialog<User> {

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final TextField emailField = new TextField();

    private User user; // 用于编辑模式下存储当前用户对象

    public UserEditDialog(User userToEdit) {
        this.setTitle(userToEdit == null ? "添加用户" : "编辑用户");

        this.user = userToEdit;

        // 设置对话框按钮
        ButtonType saveButtonType = new ButtonType("保存", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // 创建布局网格
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("用户名:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("密码:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("邮箱:"), 0, 2);
        grid.add(emailField, 1, 2);

        // 如果是编辑模式，填充现有数据
        if (userToEdit != null) {
            usernameField.setText(userToEdit.getUsername());
            passwordField.setText(userToEdit.getPassword()); // 注意：密码通常不应该这样直接显示和编辑明文
            emailField.setText(userToEdit.getEmail());
            // 用户名不可编辑，防止修改已有用户的唯一用户名
            usernameField.setEditable(false);
        }

        getDialogPane().setContent(grid);

        // 设置结果转换器，当点击保存按钮时，从字段获取数据创建 User 对象
        this.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (user == null) { // 添加模式
                    User newUser = new User();
                    // ID 和时间戳在保存到数据库时生成或更新
                    newUser.setUsername(usernameField.getText());
                    newUser.setPassword(passwordField.getText());
                    newUser.setEmail(emailField.getText());
                    return newUser;
                } else { // 编辑模式
                    // 仅更新可编辑的字段
                    user.setPassword(passwordField.getText()); // 密码可以编辑
                    user.setEmail(emailField.getText()); // 邮箱可以编辑
                    return user; // 返回更新后的用户对象
                }
            }
            return null; // 点击取消或关闭对话框时返回 null
        });
    }
} 