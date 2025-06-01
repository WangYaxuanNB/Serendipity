package com.serendipity.demo;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.serendipity.demo.entity.User; // 导入 User 实体类
import com.serendipity.demo.util.JdbcUtil; // 导入 JdbcUtil
import javafx.scene.control.Alert; // 导入 Alert
import javafx.scene.control.Alert.AlertType; // 导入 AlertType
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory; // 导入 PropertyValueFactory
import javafx.scene.control.Label; // 导入 Label
import javafx.scene.text.Font; // 导入 Font

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // 导入 Timestamp
import java.time.LocalDateTime; // 导入 LocalDateTime
import java.util.Optional;

public class UserManagementPanel extends VBox {

    private TableView<User> userTable;
    private ObservableList<User> userData;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    public UserManagementPanel() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        // 创建标题
        Label titleLabel = new Label("用户管理");
        titleLabel.setFont(Font.font("Arial", 20));

        // 初始化表格
        userTable = new TableView<>();
        userData = FXCollections.observableArrayList();
        userTable.setItems(userData);

        // 创建表格列
        TableColumn<User, Long> idColumn = new TableColumn<>("编号");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("用户名");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("密码");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> emailColumn = new TableColumn<>("邮箱");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, LocalDateTime> registrationTimeColumn = new TableColumn<>("注册时间");
        registrationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("registrationTime"));

        TableColumn<User, LocalDateTime> lastLoginTimeColumn = new TableColumn<>("最后登录时间");
        lastLoginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastLoginTime"));

        userTable.getColumns().addAll(idColumn, usernameColumn, passwordColumn, emailColumn, registrationTimeColumn, lastLoginTimeColumn);

        // 初始化按钮
        addButton = new Button("添加");
        editButton = new Button("编辑");
        deleteButton = new Button("删除");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);

        this.getChildren().addAll(titleLabel, userTable, buttonBox);

        // 添加按钮事件处理
        addButton.setOnAction(event -> handleAddUser());
        editButton.setOnAction(event -> handleEditUser());
        deleteButton.setOnAction(event -> handleDeleteUser());

        // 加载用户数据
        loadUserData();

        // 设置按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-background-radius: 5; -fx-padding: 8 15;";
        addButton.setStyle(buttonStyle);
        editButton.setStyle(buttonStyle.replace("#4CAF50", "#2196F3")); // 蓝色用于编辑
        deleteButton.setStyle(buttonStyle.replace("#4CAF50", "#f44336")); // 红色用于删除
    }

    // 从数据库加载用户数据
    private void loadUserData() {
        userData.clear();
        String sql = "SELECT id, username, password, email, registration_time, last_login_time FROM user";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRegistrationTime(rs.getTimestamp("registration_time").toLocalDateTime());
                if (rs.getTimestamp("last_login_time") != null) {
                     user.setLastLoginTime(rs.getTimestamp("last_login_time").toLocalDateTime());
                }
                userData.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error loading user data from database:"); // 输出错误信息到控制台
            e.printStackTrace();
            showAlert(AlertType.ERROR, "加载用户数据失败", "从数据库加载用户数据时发生错误。");
        }
    }

    // 处理添加用户操作
    private void handleAddUser() {
        UserEditDialog dialog = new UserEditDialog(null); // 添加模式，不传入用户对象
        Optional<User> result = dialog.showAndWait();

        result.ifPresent(newUser -> {
            if (validateUser(newUser, true)) { // 进行验证，true 表示添加模式
                saveUser(newUser); // 保存新用户到数据库
            }
        });
    }

    // 处理编辑用户操作
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UserEditDialog dialog = new UserEditDialog(selectedUser); // 编辑模式，传入选中的用户对象
            Optional<User> result = dialog.showAndWait();

            result.ifPresent(updatedUser -> {
                if (validateUser(updatedUser, false)) { // 进行验证，false 表示编辑模式
                    saveUser(updatedUser); // 更新数据库中的用户数据
                }
            });
        } else {
            showAlert(AlertType.WARNING, "未选择用户", "请从表格中选择一个要编辑的用户。");
        }
    }

    // 处理删除用户操作
    private void handleDeleteUser() {
         User selectedUser = userTable.getSelectionModel().getSelectedItem();
         if (selectedUser != null) {
             Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
             confirmAlert.setTitle("确认删除");
             confirmAlert.setHeaderText(null);
             confirmAlert.setContentText("确定要删除用户 " + selectedUser.getUsername() + " 吗？");

             Optional<ButtonType> result = confirmAlert.showAndWait();
             if (result.isPresent() && result.get() == ButtonType.OK) {
                 deleteUser(selectedUser); // 执行删除操作
             }
         } else {
             showAlert(AlertType.WARNING, "未选择用户", "请从表格中选择一个要删除的用户。");
         }
    }

    // 执行删除用户操作
    private void deleteUser(User user) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            JdbcUtil.setParameters(stmt, user.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(AlertType.INFORMATION, "删除成功", "用户 " + user.getUsername() + " 已成功删除。");
                loadUserData(); // 刷新表格数据
            } else {
                showAlert(AlertType.WARNING, "删除失败", "未找到要删除的用户或删除失败。");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user from database:"); // 输出错误信息到控制台
            e.printStackTrace();
            showAlert(AlertType.ERROR, "数据库操作失败", "删除用户时发生数据库错误。");
        }
    }

    // 验证用户数据
    private boolean validateUser(User user, boolean isAddMode) {
        if (user.getUsername().trim().isEmpty() || user.getPassword().trim().isEmpty() || user.getEmail().trim().isEmpty()) {
            showAlert(AlertType.WARNING, "验证失败", "用户名、密码和邮箱不能为空。");
            return false;
        }

        // 用户名和邮箱格式验证 (简化，可根据需要完善)
        if (!user.getEmail().contains("@")) {
             showAlert(AlertType.WARNING, "验证失败", "邮箱格式不正确。");
             return false;
        }

        // 密码强度验证 (与 UserServiceImple 中的规则一致)
        String password = user.getPassword();
        if (password.length() < 6) {
            showAlert(AlertType.WARNING, "验证失败", "密码长度必须不少于6位。");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showAlert(AlertType.WARNING, "验证失败", "密码必须包含至少一个大写字母。");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            showAlert(AlertType.WARNING, "验证失败", "密码必须包含至少一个小写字母。");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showAlert(AlertType.WARNING, "验证失败", "密码必须包含至少一个数字。");
            return false;
        }

        // 唯一性检查 (仅在添加模式下检查用户名，添加和编辑模式下检查邮箱)
        try (Connection conn = JdbcUtil.getConnection()) {
            if (isAddMode) {
                String checkUsernameSql = "SELECT COUNT(*) FROM user WHERE username = ?";
                try (PreparedStatement stmt = conn.prepareStatement(checkUsernameSql)) {
                    JdbcUtil.setParameters(stmt, user.getUsername());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            showAlert(AlertType.WARNING, "验证失败", "用户名已存在。");
                            return false;
                        }
                    }
                }
            }

            // 检查邮箱唯一性 (排除当前用户在编辑模式下)
            String checkEmailSql = isAddMode ? "SELECT COUNT(*) FROM user WHERE email = ?" : "SELECT COUNT(*) FROM user WHERE email = ? AND id != ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkEmailSql)) {
                if (isAddMode) {
                    JdbcUtil.setParameters(stmt, user.getEmail());
                } else {
                    JdbcUtil.setParameters(stmt, user.getEmail(), user.getId());
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        showAlert(AlertType.WARNING, "验证失败", "邮箱已注册。");
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during user validation database check:"); // 输出错误信息到控制台
            e.printStackTrace();
            showAlert(AlertType.ERROR, "数据库验证失败", "检查用户名或邮箱唯一性时发生数据库错误。");
            return false;
        }

        return true; // 所有验证通过
    }

    // 将用户保存到数据库 (用于添加和编辑)
    private void saveUser(User user) {
        String sql;
        // 检查用户ID，判断是插入还是更新
        if (user.getId() == null) { // 添加操作
            sql = "INSERT INTO user (username, password, email, registration_time, last_login_time) VALUES (?, ?, ?, ?, ?)";
        } else { // 编辑操作 (仅更新密码和邮箱)
            sql = "UPDATE user SET password = ?, email = ? WHERE id = ?";
        }

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (user.getId() == null) {
                 JdbcUtil.setParameters(stmt, user.getUsername(), user.getPassword(), user.getEmail(), Timestamp.valueOf(LocalDateTime.now()), null);
            } else {
                 JdbcUtil.setParameters(stmt, user.getPassword(), user.getEmail(), user.getId());
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(AlertType.INFORMATION, "保存成功", "用户数据已成功保存。");
                loadUserData(); // 刷新表格数据
            } else {
                showAlert(AlertType.ERROR, "保存失败", "保存用户数据时发生错误。");
            }
        } catch (SQLException e) {
             System.err.println("Error saving user data to database:"); // 输出错误信息到控制台
            e.printStackTrace();
            // 这里的唯一性检查已经在 validateUser 方法中处理，所以这里的 SQLException 更多是其他数据库错误
            showAlert(AlertType.ERROR, "数据库操作失败", "保存用户数据时发生数据库错误。");
        }
    }

    // 显示提示框
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 