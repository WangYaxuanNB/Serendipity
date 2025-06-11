package com.sum.view;

import com.sum.domain.entity.User;
import com.sum.service.IUserService;
import com.sum.utils.ControlUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 用户管理面板
 */
public class UserManagementPanel extends VBox {
    
    private final IUserService userService;
    private TableView<User> userTable;
    private ObservableList<User> userData;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    
    public UserManagementPanel(IUserService userService) {
        this.userService = userService;
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);
        
        initialize();
    }
    
    private void initialize() {
        // 创建标题
        Label titleLabel = new Label("用户管理");
        titleLabel.setFont(Font.font("SimHei", 20));
        
        // 创建用户表格
        userTable = new TableView<>();
        userTable.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1;");
        userData = FXCollections.observableArrayList();
        
        // 创建表格列
        TableColumn<User, Long> idColumn = new TableColumn<>("编号");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<User, String> usernameColumn = new TableColumn<>("用户名");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<User, String> passwordColumn = new TableColumn<>("密码");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        
        TableColumn<User, String> emailColumn = new TableColumn<>("邮箱");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<User, java.util.Date> createTimeColumn = new TableColumn<>("注册时间");
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("registrationTime"));
        createTimeColumn.setCellFactory(column -> new TableCell<User, Date>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormat.format(item));
                }
            }
        });
        
        TableColumn<User, java.util.Date> lastLoginTimeColumn = new TableColumn<>("最后登录时间");
        lastLoginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("lastLoginTime"));
        lastLoginTimeColumn.setCellFactory(column -> new TableCell<User, Date>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormat.format(item));
                }
            }
        });
        
        userTable.getColumns().addAll(
                idColumn, 
                usernameColumn, 
                passwordColumn, 
                emailColumn, 
                createTimeColumn, 
                lastLoginTimeColumn
        );
        
        // 设置列宽
        idColumn.setPrefWidth(50);
        usernameColumn.setPrefWidth(100);
        passwordColumn.setPrefWidth(100);
        emailColumn.setPrefWidth(150);
        createTimeColumn.setPrefWidth(150);
        lastLoginTimeColumn.setPrefWidth(150);
        
        // 设置表格样式
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setPrefHeight(400);
        userTable.setItems(userData);
        
        // 加载用户数据
        loadUserData();
        
        // 创建按钮
        addButton = new Button("添加");
        editButton = new Button("编辑");
        deleteButton = new Button("删除");
        
        // 设置按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-background-radius: 5; -fx-padding: 8 15;";
        addButton.setStyle(buttonStyle);
        editButton.setStyle(buttonStyle.replace("#4CAF50", "#2196F3")); // 蓝色用于编辑
        deleteButton.setStyle(buttonStyle.replace("#4CAF50", "#f44336")); // 红色用于删除
        
        // 添加按钮事件
        addButton.setOnAction(event -> handleAddUser());
        editButton.setOnAction(event -> handleEditUser());
        deleteButton.setOnAction(event -> handleDeleteUser());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        
        this.getChildren().addAll(titleLabel, userTable, buttonBox);
        VBox.setMargin(titleLabel, new Insets(0, 0, 15, 0));
        VBox.setMargin(userTable, new Insets(0, 0, 5, 0));
    }
    
    /**
     * 加载用户数据
     */
    private void loadUserData() {
        userData.clear();
        List<User> users = userService.list();
        userData.addAll(users);
    }
    
    /**
     * 处理添加用户操作
     */
    private void handleAddUser() {
        UserEditDialog dialog = new UserEditDialog(null);
        Optional<User> result = dialog.showAndWait();
        
        result.ifPresent(newUser -> {
            // 验证用户数据
            if (validateUser(newUser, true)) {
                // 保存用户
                boolean success = userService.save(newUser);
                if (success) {
                    // 刷新表格数据
                    loadUserData();
                }
            }
        });
    }
    
    /**
     * 处理编辑用户操作
     */
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UserEditDialog dialog = new UserEditDialog(selectedUser);
            Optional<User> result = dialog.showAndWait();
            
            result.ifPresent(updatedUser -> {
                // 验证用户数据
                if (validateUser(updatedUser, false)) {
                    // 更新用户
                    boolean success = userService.updateById(updatedUser);
                    if (success) {
                        // 刷新表格数据
                        loadUserData();
                    }
                }
            });
        } else {
            ControlUtil.warning("请选择要编辑的用户");
        }
    }
    
    /**
     * 处理删除用户操作
     */
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("确认删除");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("确定要删除用户 " + selectedUser.getUsername() + " 吗？");

            confirmAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    // 执行删除操作
                    boolean success = userService.removeById(selectedUser.getId());
                    if (success) {
                        // 刷新表格数据
                        loadUserData();
                    }
                }
            });
        } else {
            ControlUtil.warning("请选择要删除的用户");
        }
    }
    
    /**
     * 验证用户数据
     * @param user 用户对象
     * @param isAddMode 是否为添加模式
     * @return 验证结果
     */
    private boolean validateUser(User user, boolean isAddMode) {
        // 基本验证
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            ControlUtil.warning("用户名不能为空");
            return false;
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            ControlUtil.warning("密码不能为空");
            return false;
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !user.getEmail().contains("@")) {
            ControlUtil.warning("邮箱格式不正确");
            return false;
        }
        
        // 密码强度验证
        String password = user.getPassword();
        if (password.length() < 6) {
            ControlUtil.warning("密码长度必须不少于6位");
            return false;
        }
        
        // 用户名唯一性验证（仅添加模式需要）
        if (isAddMode) {
            User existingUser = userService.lambdaQuery()
                    .eq(User::getUsername, user.getUsername())
                    .one();
            if (existingUser != null) {
                ControlUtil.warning("用户名已存在");
                return false;
            }
        }
        
        // 邮箱唯一性验证
        User existingUser = userService.lambdaQuery()
                .eq(User::getEmail, user.getEmail())
                .ne(isAddMode ? null : User::getId, isAddMode ? null : user.getId())
                .one();
        if (existingUser != null) {
            ControlUtil.warning("邮箱已被使用");
            return false;
        }
        
        return true;
    }
} 