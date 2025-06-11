package com.sum.view;

import com.sum.domain.entity.SensitiveInfo;
import com.sum.service.ISensitiveInfoService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 敏感信息管理面板
 */
@Component
public class SensitiveInfoManagementPanel extends VBox {

    private TableView<SensitiveInfoUI> tableView;
    private ObservableList<SensitiveInfoUI> sensitiveInfoList;
    private TextField contentField;
    private ComboBox<String> statusComboBox;
    
    @Autowired
    private ISensitiveInfoService sensitiveInfoService;

    public SensitiveInfoManagementPanel() {
        setSpacing(20);
        setPadding(new Insets(20));
    }

    public void initialize() {
        this.getChildren().clear();

        // 创建标题
        Label titleLabel = new Label("敏感信息管理");
        titleLabel.setFont(Font.font("SimHei", 20));

        // 创建表格
        createTableView();

        // 加载数据
        loadSensitiveInfo();

        getChildren().addAll(titleLabel, tableView, createInputArea(), createButtonArea());
    }
    
    /**
     * 创建表格视图
     */
    private void createTableView() {
        tableView = new TableView<>();
        sensitiveInfoList = FXCollections.observableArrayList();

        // 创建列
        TableColumn<SensitiveInfoUI, Integer> idColumn = new TableColumn<>("编号");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<SensitiveInfoUI, String> contentColumn = new TableColumn<>("内容");
        contentColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
        
        TableColumn<SensitiveInfoUI, String> timeColumn = new TableColumn<>("标记时间");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        
        TableColumn<SensitiveInfoUI, String> statusColumn = new TableColumn<>("处理状态");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        
        // 设置列宽
        idColumn.setPrefWidth(80);
        contentColumn.setPrefWidth(300);
        timeColumn.setPrefWidth(150);
        statusColumn.setPrefWidth(100);

        tableView.getColumns().addAll(idColumn, contentColumn, timeColumn, statusColumn);
        tableView.setItems(sensitiveInfoList);
        tableView.setPrefHeight(400);
        
        // 设置表格样式
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    /**
     * 创建输入区域
     */
    private HBox createInputArea() {
        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(10));
        inputArea.setAlignment(Pos.CENTER_LEFT);

        Label contentLabel = new Label("内容：");
        contentField = new TextField();
        contentField.setPrefWidth(300);

        Label statusLabel = new Label("状态：");
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("待处理", "已处理", "已忽略");
        statusComboBox.setValue("待处理");
        statusComboBox.setPrefWidth(100);

        inputArea.getChildren().addAll(contentLabel, contentField, statusLabel, statusComboBox);
        return inputArea;
    }
    
    /**
     * 创建按钮区域
     */
    private HBox createButtonArea() {
        HBox buttonArea = new HBox(10);
        buttonArea.setPadding(new Insets(10));
        buttonArea.setAlignment(Pos.CENTER);

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

    /**
     * 从数据库加载敏感信息列表
     */
    private void loadSensitiveInfo() {
        sensitiveInfoList.clear();
        
        // 从数据库获取敏感信息列表
        List<SensitiveInfo> infoList = sensitiveInfoService.list();
        
        // 转换为UI显示对象
        for (SensitiveInfo info : infoList) {
            sensitiveInfoList.add(new SensitiveInfoUI(
                info.getId(),
                info.getContentx(),
                info.getBjtime(),
                info.getStatus()
            ));
        }
    }

    /**
     * 添加敏感信息到数据库
     */
    private void addSensitiveInfo() {
        String content = contentField.getText().trim();
        String status = statusComboBox.getValue();

        if (content.isEmpty()) {
            showAlert("警告", "请输入内容！");
            return;
        }

        // 创建敏感信息对象
        SensitiveInfo newInfo = new SensitiveInfo();
        newInfo.setContentx(content);
        newInfo.setBjtime(LocalDateTime.now());
        newInfo.setStatus(status);
        
        try {
            // 保存到数据库
            boolean success = sensitiveInfoService.save(newInfo);
            if (success) {
                // 刷新数据
                loadSensitiveInfo();
                // 清空输入框
                contentField.clear();
                showAlert("成功", "添加敏感信息成功！");
            } else {
                showAlert("错误", "添加敏感信息失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("错误", "添加敏感信息失败：" + e.getMessage());
        }
    }

    /**
     * 从数据库删除敏感信息
     */
    private void deleteSensitiveInfo() {
        SensitiveInfoUI selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("警告", "请选择要删除的记录！");
            return;
        }

        try {
            // 从数据库删除
            boolean success = sensitiveInfoService.removeById(selected.getId());
            if (success) {
                // 刷新数据
                loadSensitiveInfo();
                showAlert("成功", "删除敏感信息成功！");
            } else {
                showAlert("错误", "删除敏感信息失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("错误", "删除敏感信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新敏感信息状态
     */
    private void updateSensitiveInfoStatus() {
        SensitiveInfoUI selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("警告", "请选择要更新的记录！");
            return;
        }

        try {
            // 获取要更新的记录
            SensitiveInfo info = sensitiveInfoService.getById(selected.getId());
            if (info != null) {
                // 更新状态
                info.setStatus(statusComboBox.getValue());
                
                // 保存到数据库
                boolean success = sensitiveInfoService.updateById(info);
                if (success) {
                    // 刷新数据
                    loadSensitiveInfo();
                    showAlert("成功", "更新状态成功！");
                } else {
                    showAlert("错误", "更新状态失败！");
                }
            } else {
                showAlert("错误", "未找到要更新的记录！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("错误", "更新状态失败：" + e.getMessage());
        }
    }

    /**
     * 显示提示框
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * 敏感信息UI类，用于界面显示
     */
    public static class SensitiveInfoUI {
        private final IntegerProperty id;
        private final StringProperty content;
        private final StringProperty time;
        private final StringProperty status;

        public SensitiveInfoUI(Integer id, String content, LocalDateTime time, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.content = new SimpleStringProperty(content);
            this.time = new SimpleStringProperty(
                time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            this.status = new SimpleStringProperty(status);
        }

        public int getId() { return id.get(); }
        public IntegerProperty idProperty() { return id; }

        public String getContent() { return content.get(); }
        public StringProperty contentProperty() { return content; }

        public String getTime() { return time.get(); }
        public StringProperty timeProperty() { return time; }

        public String getStatus() { return status.get(); }
        public StringProperty statusProperty() { return status; }
    }
} 