package com.serendipity.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MoodInputPage extends Application {

    private TextField nameField;
    private TextField moodField;

    @Override
    public void start(Stage primaryStage) {
        nameField = new TextField();
        nameField.setPromptText("请输入姓名");

        moodField = new TextField();
        moodField.setPromptText("请输入心情");

        Button submitButton = new Button("提交");
        submitButton.setOnAction(e -> insertData());

        VBox vbox = new VBox(10,
                new Label("姓名："), nameField,
                new Label("心情："), moodField,
                submitButton
        );
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("心情记录输入页");
        primaryStage.show();
    }

    private void insertData() {
        String name = nameField.getText().trim();
        String mood = moodField.getText().trim();

        if (name.isEmpty() || mood.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "姓名和心情不能为空！");
            return;
        }

        // ✅ 替换为你的数据库信息
        String url = "jdbc:mysql://localhost:3306/register_demo?useSSL=false&serverTimezon e=UTC";
        String user = "root";
        String password = "保密    ";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO mood (name, mood) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, mood);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "保存成功！");
            nameField.clear();
            moodField.clear();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "数据库连接失败！");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
