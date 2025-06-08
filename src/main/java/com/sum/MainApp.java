package com.sum;

import com.sum.service.IUserService;
import com.sum.view.LoginView;
import com.sum.view.MainFrameView;
import com.sum.view.RegisterView;
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


@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.sum.mapper")
@Getter
@Setter
public class MainApp extends Application {

    private Long currentUserId; // 新增：存储当前登录用户ID
    private String currentUsername; // 新增：存储当前登录用户名
    private ConfigurableApplicationContext context;
    private Stage primaryStage;

    @Resource
    private IUserService userService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        context = SpringApplication.run(MainApp.class);
        context.getAutowireCapableBeanFactory().autowireBean(this);
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
}
