package com.serendipity.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class MainApp extends Application {

    private Stage primaryStage;
    private Long currentUserId; // 新增：存储当前登录用户ID
    private String currentUsername; // 新增：存储当前登录用户名

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("圆角弹窗");
        primaryStage.show();
        primaryStage.setResizable(false);
        this.primaryStage = primaryStage;
        showLoginPage();
    }

    public void showLoginPage() throws IOException {
        login loginPage = new login(this);
        primaryStage.setScene(loginPage.getScene());
        primaryStage.setTitle("登录");
        primaryStage.show();
    }

    public void showRegisterPage() throws IOException {
        Register registerPage = new Register(this);
        primaryStage.setScene(registerPage.getScene());
        primaryStage.setTitle("注册");
        primaryStage.show();
    }

    // 新增方法：显示心情选择页面 (包含8个图标按钮)
    public void showMoodSelectionPage() throws IOException {
        MoodSelectPage moodSelectPage = new MoodSelectPage(this);
        primaryStage.setScene(moodSelectPage.getScene());
        primaryStage.setTitle("选择心情");
        primaryStage.show();
    }

    // 新增方法：显示日历视图页面 (呈现每日心情变化)
    public void showCalendarViewPage() throws IOException {
        CalendarViewPage calendarViewPage = new CalendarViewPage(this);
        primaryStage.setScene(calendarViewPage.getScene());
        primaryStage.setTitle("心情日历");
        primaryStage.show();
    }

    // 新增方法：显示心情趋势图页面 (折线图)
    public void showMoodTrendChartPage(LocalDate date) throws IOException {
        MoodTrendChartPage moodTrendChartPage = new MoodTrendChartPage(this, date);
        primaryStage.setScene(moodTrendChartPage.getScene());
        primaryStage.setTitle("心情趋势");
        primaryStage.show();
    }

    // 新增方法：显示心情趋势图页面 (折线图) - 无参数版本，用于无特定日期跳转
    public void showMoodTrendChartPage() throws IOException {
        // 默认显示最近一段时间的趋势
        showMoodTrendChartPage(LocalDate.now()); // 默认显示今天的趋势
    }

    // 新增方法：显示管理员界面
    public void showAdminPage() throws IOException {
        // TODO: 创建并显示管理员界面
        System.out.println("跳转到管理员界面");
        primaryStage.setTitle("管理员界面");
        
        AdminPage adminPage = new AdminPage(this);
        primaryStage.setScene(adminPage.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // 新增：获取当前登录用户ID
    public Long getCurrentUserId() {
        return currentUserId;
    }

    // 新增：设置当前登录用户ID
    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    // 新增：获取当前登录用户名
    public String getCurrentUsername() {
        return currentUsername;
    }

    // 新增：设置当前登录用户名
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}