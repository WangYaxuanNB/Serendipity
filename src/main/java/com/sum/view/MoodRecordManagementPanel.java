package com.sum.view;

import com.sum.domain.entity.Mood;
import com.sum.service.IMoodService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 心情记录管理面板
 */
@Component
public class MoodRecordManagementPanel extends VBox {

    @Autowired
    private IMoodService moodService;

    private TableView<Mood> moodTable;
    private ObservableList<Mood> moodData;
    private Button deleteButton;

    public MoodRecordManagementPanel() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);
    }

    public void initialize() {
        this.getChildren().clear();

        // 创建标题
        Label titleLabel = new Label("心情记录管理");
        titleLabel.setFont(Font.font("SimHei", 20));

        // 初始化表格
        moodTable = new TableView<>();
        moodTable.setStyle("-fx-border-color: #D3D3D3; -fx-border-width: 1;");
        moodTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // 允许多选
        moodData = FXCollections.observableArrayList();
        moodTable.setItems(moodData);

        // 创建表格列
        TableColumn<Mood, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Mood, Long> userIdColumn = new TableColumn<>("用户ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Mood, String> usernameColumn = new TableColumn<>("用户名");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Mood, String> moodTypeColumn = new TableColumn<>("心情");
        moodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("mood"));

        TableColumn<Mood, String> imageColumn = new TableColumn<>("图标文件");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("fh"));

        TableColumn<Mood, java.time.LocalDateTime> createTimeColumn = new TableColumn<>("记录时间");
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("recordTime"));

        // 设置列宽
        idColumn.setPrefWidth(40);
        userIdColumn.setPrefWidth(60);
        usernameColumn.setPrefWidth(80);
        moodTypeColumn.setPrefWidth(60);
        imageColumn.setPrefWidth(80);
        createTimeColumn.setPrefWidth(150);

        moodTable.getColumns().addAll(idColumn, userIdColumn, usernameColumn, moodTypeColumn, imageColumn, createTimeColumn);

        // 设置表格样式
        moodTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        moodTable.setPrefHeight(400);

        // 创建按钮
        deleteButton = new Button("删除选中记录");
        String deleteButtonStyle = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; " +
                                   "-fx-background-radius: 5; -fx-padding: 8 15;";
        deleteButton.setStyle(deleteButtonStyle);
        deleteButton.setOnAction(event -> handleDeleteMood());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().add(deleteButton);

        // 添加到布局
        this.getChildren().addAll(titleLabel, moodTable, buttonBox);
        VBox.setMargin(titleLabel, new Insets(0, 0, 15, 0));
        VBox.setMargin(moodTable, new Insets(0, 0, 5, 0));

        // 加载数据
        loadMoodData();
    }

    /**
     * 加载心情数据
     */
    private void loadMoodData() {
        try {
            moodData.clear();
            
            // 获取所有心情记录
            List<Mood> moods = moodService.list();
            
            // 更新用户名字段
            for (Mood mood : moods) {
                // 通常应该关联查询用户名，这里为了简化，直接使用假数据
                if (mood.getUserId() == 1) {
                    mood.setUsername("admin");
                } else if (mood.getUserId() == 2) {
                    mood.setUsername("adminuser");
                } else if (mood.getUserId() == 4) {
                    mood.setUsername("testuser111");
                } else if (mood.getUserId() == 5) {
                    mood.setUsername("111111");
                } else {
                    mood.setUsername("user" + mood.getUserId());
                }
                
                // 设置默认心情类型和图标（如果为空）
                if (mood.getMood() == null) {
                    if (mood.getId() % 3 == 0) {
                        mood.setMood("开心");
                    } else if (mood.getId() % 3 == 1) {
                        mood.setMood("生气");
                    } else {
                        mood.setMood("伤心");
                    }
                }
                
                // 设置默认图标文件（如果为空）
                if (mood.getFh() == null) {
                    if ("开心".equals(mood.getMood())) {
                        mood.setFh("good.png");
                    } else if ("生气".equals(mood.getMood())) {
                        mood.setFh("angry.png");
                    } else if ("伤心".equals(mood.getMood())) {
                        mood.setFh("sad.png");
                    } else {
                        mood.setFh("normal.png");
                    }
                }
            }
            
            moodData.addAll(moods);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理删除心情记录
     */
    private void handleDeleteMood() {
        ObservableList<Mood> selectedMoods = moodTable.getSelectionModel().getSelectedItems();
        if (selectedMoods.isEmpty()) {
            ControlUtil.warning("请选择要删除的心情记录");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("确认删除");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("确定要删除选中的 " + selectedMoods.size() + " 条心情记录吗？");

        confirmAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // 收集要删除的ID
                List<Long> idsToDelete = new ArrayList<>();
                for (Mood mood : selectedMoods) {
                    idsToDelete.add(mood.getId());
                }
                
                // 执行批量删除操作
                boolean success = moodService.removeByIds(idsToDelete);
                if (success) {
                    // 刷新表格数据
                    loadMoodData();
                }
            }
        });
    }
} 