package com.serendipity.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.cell.PropertyValueFactory;
import com.serendipity.demo.util.JdbcUtil;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MoodRecordManagementPanel extends VBox {

    private TableView<MoodRecord> moodRecordTable; // TableView to display records
    private ObservableList<MoodRecord> moodRecordsData; // Data for the table
    private Button deleteButton; // Delete button

    public MoodRecordManagementPanel() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        // Remove internal title label
        // Label titleLabel = new Label("心情记录管理");
        // titleLabel.setFont(Font.font("Arial", 18));

        // Create TableView
        moodRecordTable = new TableView<>();
        moodRecordTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multiple selection

        // Create columns
        TableColumn<MoodRecord, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<MoodRecord, Long> userIdColumn = new TableColumn<>("用户ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        // New Username Column
        TableColumn<MoodRecord, String> usernameColumn = new TableColumn<>("用户名");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<MoodRecord, String> moodColumn = new TableColumn<>("心情");
        moodColumn.setCellValueFactory(new PropertyValueFactory<>("mood"));

        TableColumn<MoodRecord, String> fhColumn = new TableColumn<>("图标文件");
        fhColumn.setCellValueFactory(new PropertyValueFactory<>("fh"));

        TableColumn<MoodRecord, Timestamp> recordTimeColumn = new TableColumn<>("记录时间");
        recordTimeColumn.setCellValueFactory(new PropertyValueFactory<>("recordTime"));

        // Add columns to TableView (insert username column after userId)
        moodRecordTable.getColumns().addAll(idColumn, userIdColumn, usernameColumn, moodColumn, fhColumn, recordTimeColumn);

        // Create Delete Button
        deleteButton = new Button("删除选中记录");
        deleteButton.setOnAction(event -> deleteSelectedRecords());

        // Load data
        moodRecordsData = FXCollections.observableArrayList();
        loadMoodRecords();
        moodRecordTable.setItems(moodRecordsData);

        // Add table and delete button to the VBox
        this.getChildren().addAll(moodRecordTable, deleteButton); // Removed titleLabel

        // 设置删除按钮样式
        String deleteButtonStyle = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; " +
                                 "-fx-background-radius: 5; -fx-padding: 8 15;";
        deleteButton.setStyle(deleteButtonStyle);
    }

    private void loadMoodRecords() {
        moodRecordsData.clear(); // Clear existing data
        // Modified SQL to join with user table and select username
        String sql = "SELECT m.id, m.user_id, u.username, m.mood, m.fh, m.record_time FROM moods m JOIN user u ON m.user_id = u.id ORDER BY m.record_time DESC";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                long userId = rs.getLong("user_id");
                String username = rs.getString("username"); // Get username
                String mood = rs.getString("mood");
                String fh = rs.getString("fh");
                Timestamp recordTime = rs.getTimestamp("record_time");

                // Pass username to MoodRecord constructor
                moodRecordsData.add(new MoodRecord(id, userId, username, mood, fh, recordTime));
            }

        } catch (SQLException e) {
            System.err.println("加载心情记录失败:");
            e.printStackTrace();
            // TODO: 显示错误信息给用户
        }
    }

    private void deleteSelectedRecords() {
        ObservableList<MoodRecord> selectedRecords = moodRecordTable.getSelectionModel().getSelectedItems();

        if (selectedRecords.isEmpty()) {
            System.out.println("没有选中任何记录。"); // TODO: Show alert to user
            return;
        }

        // Collect IDs of selected records
        List<Long> recordIdsToDelete = new ArrayList<>();
        for (MoodRecord record : selectedRecords) {
            recordIdsToDelete.add(record.getId());
        }

        // Build the SQL delete statement
        String sql = "DELETE FROM moods WHERE id IN ("
                   + recordIdsToDelete.stream()
                                      .map(Object::toString)
                                      .collect(Collectors.joining(", "))
                   + ")";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("成功删除 " + affectedRows + " 条记录。"); // TODO: Show success message
                loadMoodRecords(); // Refresh the table data
            } else {
                System.out.println("删除失败或没有匹配的记录。"); // TODO: Show failure message
            }

        } catch (SQLException e) {
            System.err.println("删除心情记录失败:");
            e.printStackTrace();
            // TODO: 显示错误信息给用户
        }
    }

    // Inner class to represent a Mood Record for TableView
    public static class MoodRecord {
        private final long id;
        private final long userId;
        private final String username; // New field for username
        private final String mood;
        private final String fh;
        private final Timestamp recordTime;

        // Updated constructor to include username
        public MoodRecord(long id, long userId, String username, String mood, String fh, Timestamp recordTime) {
            this.id = id;
            this.userId = userId;
            this.username = username; // Assign username
            this.mood = mood;
            this.fh = fh;
            this.recordTime = recordTime;
        }

        // Getters (required for PropertyValueFactory)
        public long getId() {
            return id;
        }

        public long getUserId() {
            return userId;
        }

        // New getter for username
        public String getUsername() {
            return username;
        }

        public String getMood() {
            return mood;
        }

        public String getFh() {
            return fh;
        }

        public Timestamp getRecordTime() {
            return recordTime;
        }
    }
} 