package com.serendipity.demo;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.serendipity.demo.util.JdbcUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SensitiveInfoManagementPanel extends VBox {
    private TableView<SensitiveInfo> tableView;
    private ObservableList<SensitiveInfo> sensitiveInfoList;
    private TextField contentField;
    private ComboBox<String> statusComboBox;

    public SensitiveInfoManagementPanel() {
        setSpacing(20);
        setPadding(new Insets(20));

        // 创建标题
        Label titleLabel = new Label("敏感信息管理");
        titleLabel.setFont(Font.font("Arial", 20));

        // 创建表格
        createTableView();

        // 创建输入区域
        createInputArea();

        // 创建按钮区域
        createButtonArea();

        // 加载数据
        loadSensitiveInfo();

        getChildren().addAll(titleLabel, tableView, createInputArea(), createButtonArea());
    }

    private void createTableView() {
        tableView = new TableView<>();
        sensitiveInfoList = FXCollections.observableArrayList();

        // 创建列
        TableColumn<SensitiveInfo, Integer> idColumn = new TableColumn<>("编号");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<SensitiveInfo, String> contentColumn = new TableColumn<>("内容");
        contentColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());

        TableColumn<SensitiveInfo, String> timeColumn = new TableColumn<>("标记时间");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        TableColumn<SensitiveInfo, String> statusColumn = new TableColumn<>("处理状态");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // 设置列宽
        idColumn.setPrefWidth(80);
        contentColumn.setPrefWidth(300);
        timeColumn.setPrefWidth(150);
        statusColumn.setPrefWidth(100);

        tableView.getColumns().addAll(idColumn, contentColumn, timeColumn, statusColumn);
        tableView.setItems(sensitiveInfoList);
        tableView.setPrefHeight(400);
    }

    private HBox createInputArea() {
        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(10));

        Label contentLabel = new Label("内容：");
        contentField = new TextField();
        contentField.setPrefWidth(300);

        Label statusLabel = new Label("状态：");
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("待处理", "已处理", "已忽略");
        statusComboBox.setValue("待处理");

        inputArea.getChildren().addAll(contentLabel, contentField, statusLabel, statusComboBox);
        return inputArea;
    }

    private HBox createButtonArea() {
        HBox buttonArea = new HBox(10);
        buttonArea.setPadding(new Insets(10));

        Button addButton = new Button("添加");
        Button deleteButton = new Button("删除");
        Button updateButton = new Button("更新状态");

        // 设置按钮样式
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-background-radius: 5; -fx-padding: 8 15;";
        addButton.setStyle(buttonStyle);
        deleteButton.setStyle(buttonStyle.replace("#4CAF50", "#f44336"));
        updateButton.setStyle(buttonStyle.replace("#4CAF50", "#2196F3"));

        // 添加按钮事件
        addButton.setOnAction(e -> addSensitiveInfo());
        deleteButton.setOnAction(e -> deleteSensitiveInfo());
        updateButton.setOnAction(e -> updateSensitiveInfoStatus());

        buttonArea.getChildren().addAll(addButton, deleteButton, updateButton);
        return buttonArea;
    }

    private void loadSensitiveInfo() {
        String sql = "SELECT * FROM minganci ORDER BY id DESC";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            sensitiveInfoList.clear();
            while (rs.next()) {
                SensitiveInfo info = new SensitiveInfo(
                    rs.getInt("id"),
                    rs.getString("contentx"),
                    rs.getTimestamp("bjtime").toLocalDateTime(),
                    rs.getString("status")
                );
                sensitiveInfoList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("错误", "加载敏感信息失败：" + e.getMessage());
        }
    }

    private void addSensitiveInfo() {
        String content = contentField.getText().trim();
        String status = statusComboBox.getValue();

        if (content.isEmpty()) {
            showAlert("警告", "请输入内容！");
            return;
        }

        String sql = "INSERT INTO minganci (contentx, bjtime, status) VALUES (?, ?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, content);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, status);

            stmt.executeUpdate();
            loadSensitiveInfo();
            contentField.clear();
            showAlert("成功", "添加敏感信息成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("错误", "添加敏感信息失败：" + e.getMessage());
        }
    }

    private void deleteSensitiveInfo() {
        SensitiveInfo selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("警告", "请选择要删除的记录！");
            return;
        }

        String sql = "DELETE FROM minganci WHERE id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadSensitiveInfo();
            showAlert("成功", "删除敏感信息成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("错误", "删除敏感信息失败：" + e.getMessage());
        }
    }

    private void updateSensitiveInfoStatus() {
        SensitiveInfo selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("警告", "请选择要更新的记录！");
            return;
        }

        String sql = "UPDATE minganci SET status = ? WHERE id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statusComboBox.getValue());
            stmt.setInt(2, selected.getId());
            stmt.executeUpdate();
            loadSensitiveInfo();
            showAlert("成功", "更新状态成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("错误", "更新状态失败：" + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // 敏感信息数据模型类
    public static class SensitiveInfo {
        private final javafx.beans.property.IntegerProperty id;
        private final javafx.beans.property.StringProperty content;
        private final javafx.beans.property.StringProperty time;
        private final javafx.beans.property.StringProperty status;

        public SensitiveInfo(int id, String content, LocalDateTime time, String status) {
            this.id = new javafx.beans.property.SimpleIntegerProperty(id);
            this.content = new javafx.beans.property.SimpleStringProperty(content);
            this.time = new javafx.beans.property.SimpleStringProperty(
                time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            this.status = new javafx.beans.property.SimpleStringProperty(status);
        }

        public int getId() { return id.get(); }
        public javafx.beans.property.IntegerProperty idProperty() { return id; }

        public String getContent() { return content.get(); }
        public javafx.beans.property.StringProperty contentProperty() { return content; }

        public String getTime() { return time.get(); }
        public javafx.beans.property.StringProperty timeProperty() { return time; }

        public String getStatus() { return status.get(); }
        public javafx.beans.property.StringProperty statusProperty() { return status; }
    }
} 