package com.sum;

import com.sum.service.IUserService;
import com.sum.view.*;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;
import java.time.LocalDate;


@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.sum.mapper")
@Getter
@Setter
public class MainApp extends Application {

    private Long currentUserId; // 存储当前登录用户ID
    private String currentUsername; // 存储当前登录用户名
    private ConfigurableApplicationContext context;
    private Stage primaryStage;

    @Resource
    private IUserService userService;
    
    @Resource
    private MoodSelectView moodSelectView;
    
    @Resource
    private CalendarView calendarView;
    
    @Resource
    private MoodTrendView moodTrendView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        context = SpringApplication.run(MainApp.class);
        context.getAutowireCapableBeanFactory().autowireBean(this);
        JavaFxSMApplication.setApplicationContext(context);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("圆角弹窗");
        primaryStage.show();
        primaryStage.setResizable(false);
        this.primaryStage = primaryStage;
        showLoginPage();
    }

    public void showLoginPage() {
        LoginView loginPage = new LoginView(this);
        primaryStage.setScene(loginPage.getScene());
        primaryStage.setTitle("登录");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showRegisterPage() {
        RegisterView registerView = new RegisterView(this);
        primaryStage.setScene(registerView.getScene());
        primaryStage.setTitle("注册");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * 显示主界面
     */
    public void showMainFrame() {
        this.getPrimaryStage().close();
        MainFrameView mainFrameView = new MainFrameView();
        mainFrameView.getMainFrameStage().show();
    }
    
    /**
     * 显示心情选择页面
     */
    public void showMoodSelectView() {
        moodSelectView.initialize(this);
        primaryStage.setScene(moodSelectView.getScene());
        primaryStage.setTitle("选择心情");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    /**
     * 显示心情日历页面
     */
    public void showCalendarView() {
        calendarView.initialize(this);
        primaryStage.setScene(calendarView.getScene());
        primaryStage.setTitle("心情日历");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    /**
     * 显示心情趋势图页面（带有特定日期）
     */
    public void showMoodTrendView(LocalDate date) {
        moodTrendView.initialize(this, date);
        primaryStage.setScene(moodTrendView.getScene());
        primaryStage.setTitle("心情趋势");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
    /**
     * 显示心情趋势图页面（使用当前日期）
     */
    public void showMoodTrendView() {
        showMoodTrendView(LocalDate.now());
    }
    
    /**
     * 显示管理员界面
     */
    public void showAdminPage() {
        this.getPrimaryStage().close();
        AdminView adminView = context.getBean(AdminView.class);
        adminView.initialize(this);
        adminView.getAdminStage().show();
    }
}
