package com.serendipity.demo;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Parent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jboss.jandex.Main;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.MediaType;
//import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.serendipity.demo.util.JdbcUtil;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class login {
    private Scene scene;
    private static final double SCALE_RATIO = 0.6;
    private static final double EYE_RADIUS = 10;
    private static final double PUPIL_RADIUS = 4;

    public Scene getScene() {
        return scene;
    }

    public login(MainApp app) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
        //绝对定位
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1640, 1154);
        // 原样添加 UI 元素到 root...

        Group scaledRoot = new Group(root); // 新建包裹 root 的容器
        scaledRoot.getTransforms().add(new Scale(SCALE_RATIO, SCALE_RATIO, 0, 0)); // 整体缩放

        Scene scene = new Scene(scaledRoot, 1640 * SCALE_RATIO, 1154 * SCALE_RATIO);


        // 设置背景颜色和圆角
        BackgroundFill backgroundFill = new BackgroundFill(
                Color.web("#D9D9D9"), new CornerRadii(26), null
        );
        root.setBackground(new Background(backgroundFill));

        Pane roundedPane = createRightRoundedPanel(app);
        root.getChildren().add(roundedPane);
        roundedPane.setTranslateX(961); // 设置 X 轴相对偏移
        roundedPane.setTranslateY(22); // 设置 Y 轴相对偏移






        // 可选：裁剪内容以实现真正的圆角遮罩效果
        Rectangle clip = new Rectangle(1640, 1154);
        clip.setArcWidth(52); // 圆角宽度（26*2）
        clip.setArcHeight(52);
        root.setClip(clip);



        // 设置舞台（弹窗）样式
//        dialog.initStyle(StageStyle.UNDECORATED); // 无边框弹窗
        Pane characterPane = Character(scene);
        root.getChildren().add(characterPane);
        this.scene=scene;
    }

    private Pane createRightRoundedPanel(MainApp app) {
        Pane  roundedPane = new Pane (); // 设置矩形的宽度和高度
        roundedPane.setPrefSize(644, 1114);
        roundedPane.setStyle("-fx-background-color: white; -fx-background-radius: 26;");


        Image image = new Image(getClass().getResource("/png/Group1.png").toExternalForm()); // 可替换为本地图片路径
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(41);
        imageView.setFitHeight(41);
        imageView.setX(301); // 设置图片位置
        imageView.setY(89);
        roundedPane.getChildren().add(imageView);


        Text text1 = new Text("Welcome back!"); // 设置显示的文本内容
        text1.setFont(Font.font("Inter", FontWeight.BOLD ,48)); // 设置字体和大小
        text1.setFill(Color.BLACK); // 设置字体颜色
        double text1Width = text1.getBoundsInLocal().getWidth(); // 获取文本的宽度
        double pane1Width = roundedPane.getPrefWidth(); // 获取 Pane 的宽度
        text1.setX((pane1Width - text1Width) / 2); // 水平居中
        text1.setY(271);//230+31

        Text text2 = new Text("please enter your details"); // 设置显示的文本内容
        text2.setFont(Font.font("Inter", FontWeight.LIGHT ,20)); // 设置字体和大小
        text2.setFill(Color.BLACK); // 设置字体颜色
        double text2Width = text2.getBoundsInLocal().getWidth(); // 获取文本的宽度
        double pane2Width = roundedPane.getPrefWidth(); // 获取 Pane 的宽度
        text2.setX((pane2Width - text2Width) / 2); // 水平居中
        text2.setY(344);

        TextField emailInput1 = new TextField();
        emailInput1.setPromptText("Enter your username");

        emailInput1.setFocusTraversable(false); // set focus traversable false.
        emailInput1.setPrefWidth(511);   // 设置首选宽度
        emailInput1.setPrefHeight(61);   // 设置首选高度
        emailInput1.setTranslateX(55);
        emailInput1.setTranslateY(400);

        // 设置输入框样式
        emailInput1.setStyle("-fx-background-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Inter'; " +   // 设置字体为 Inter
                "-fx-font-size: 24px; " +        // 设置字体大小为 24px
                "-fx-font-weight: normal;");     // 设置字体为常规（Regular）样式

        Line line1 = new Line(66, 437, 577, 437);
        line1.setStroke(Color.BLACK); // 设置颜色为黑色
        line1.setStrokeWidth(2); // 设置线条宽度
        line1.setLayoutY(20);


        //2合1演示
        TextField emailInput2 = new TextField();
        emailInput2.setPromptText("Enter your Password");

        emailInput2.setFocusTraversable(false); // set focus traversable false.
        emailInput2.setPrefWidth(511);   // 设置首选宽度
        emailInput2.setPrefHeight(61);   // 设置首选高度
        emailInput2.setTranslateX(55);
        emailInput2.setTranslateY(515);

        // 设置输入框样式
        emailInput2.setStyle("-fx-background-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Inter'; " +   // 设置字体为 Inter
                "-fx-font-size: 24px; " +        // 设置字体大小为 24px
                "-fx-font-weight: normal;");     // 设置字体为常规（Regular）样式

        Line line2 = new Line(66, 552, 577, 552);
        line2.setStroke(Color.BLACK); // 设置颜色为黑色
        line2.setStrokeWidth(2); // 设置线条宽度
        line2.setLayoutY(20);


        StackPane stackPane = new StackPane();
        StackPane.setAlignment(line2, Pos.BOTTOM_CENTER);
        stackPane.getChildren().addAll(emailInput2, line2);
        stackPane.setTranslateX(55);
        stackPane.setTranslateY(515);

        roundedPane.getChildren().add(text1);
        roundedPane.getChildren().add(text2);
        roundedPane.getChildren().add(emailInput1);
        roundedPane.getChildren().add(line1);
        roundedPane.getChildren().add(emailInput2);
        roundedPane.getChildren().add(line2);

        Button loginButton = new Button("log in");
        loginButton.setStyle("-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;");
        // 悬停时改变按钮的背景颜色
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: darkgray; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;"));

        // 悬停结束时恢复原背景颜色
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;"));

        // 点击时改变按钮背景，提供反馈
        loginButton.setOnMousePressed(e -> loginButton.setStyle("-fx-background-color: gray; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;"));

        // 释放点击时恢复背景颜色
        loginButton.setOnMouseReleased(e -> loginButton.setStyle("-fx-background-color: darkgray; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;"));
        loginButton.setOnAction(e -> {
            String username = emailInput1.getText().trim();
            String password = emailInput2.getText().trim();
            // String role = userRadio.isSelected() ? "USER" : "ADMIN"; // 不再通过单选按钮确定角色

            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("请填写用户名和密码");
                return;
            }

            // 使用 JDBC 进行数据库验证
            String sql = "SELECT id, username, password, email FROM user WHERE username = ? AND password = ?";
            try (Connection conn = JdbcUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                JdbcUtil.setParameters(stmt, username, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // 登录成功
                        long userId = rs.getLong("id"); // 获取用户ID
                        String fetchedUsername = rs.getString("username"); // 获取用户名

                        // 在 MainApp 中存储当前登录用户信息
                        app.setCurrentUserId(userId);
                        app.setCurrentUsername(fetchedUsername);

                        // 更新用户的 last_login_time 字段
                        updateLastLoginTime(userId);

                        // String role = determineUserRole(fetchedUsername); // 根据用户名确定角色
                        String role = "USER"; // 默认设置为普通用户

                        // 硬编码管理员账号验证 (示例)
                        if ("admin".equals(username) && "admin".equals(password)) {
                             role = "ADMIN";
                        }

                        System.out.println("登录成功，用户: " + fetchedUsername + ", 角色: " + role);

                        // 根据角色进行跳转
                        try {
                            if ("ADMIN".equals(role)) {
                                app.showAdminPage(); // 跳转到管理员界面
                            } else {
                                app.showMoodSelectionPage(); // 跳转到普通用户界面
                            }
                        } catch (IOException ex) {
                            System.out.println("跳转失败：" + ex.getMessage());
                        }

                    } else {
                        // 登录失败
                        System.out.println("登录失败：用户名或密码错误");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("数据库连接或查询失败：" + ex.getMessage());
                ex.printStackTrace();
            }
        });


        loginButton.setTranslateX(66);
        loginButton.setTranslateY(591);
        loginButton.setPrefHeight(76);
        loginButton.setPrefWidth(511);

        // 添加注册链接
        Text signUpText = new Text("Don't have an account? ");
        signUpText.setFont(Font.font("Inter", 20));
        signUpText.setFill(Color.BLACK);

        Text signUpLink = new Text("Sign Up");
        signUpLink.setFont(Font.font("Inter", 20));
        signUpLink.setFill(Color.BLACK);
        signUpLink.setStyle("-fx-cursor: hand;");

        // 创建水平布局容器
        HBox signUpContainer = new HBox(5); // 5是元素之间的间距
        signUpContainer.setAlignment(Pos.CENTER);
        signUpContainer.getChildren().addAll(signUpText, signUpLink);
        signUpContainer.setTranslateX(165);
        signUpContainer.setTranslateY(691);

        // 添加鼠标悬停效果
        signUpLink.setOnMouseEntered(e -> signUpLink.setFill(Color.GRAY));
        signUpLink.setOnMouseExited(e -> signUpLink.setFill(Color.BLACK));

        // 添加点击事件
        signUpLink.setOnMouseClicked(e -> {
            // TODO: 在这里添加跳转到注册页面的逻辑
            try {
                app.showRegisterPage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        roundedPane.getChildren().add(loginButton);
        roundedPane.getChildren().add(signUpContainer);



        return roundedPane;


    }


    private Pane Character(Scene scene){
        Pane root = new Pane();

        // 放到一个 Group 里，方便整体缩放和偏移
        Group container = new Group(root);

        // 设置整体缩放（比如放大到 1.2 倍）
        double scaleFactor = 1.2;
        container.getTransforms().add(new Scale(scaleFactor, scaleFactor, 0, 0));

        // 设置 X 轴偏移（比如右移 100 像素）
        container.setTranslateX(100);

        double baselineY = 900;
        Map<Group, List<Eye>> eyeMap = new LinkedHashMap<>();
        List<Group> characterGroups = new ArrayList<>();

        Random rand = new Random();

        // === 紫色角色 ===
        Group purpleGroup = new Group();
        double px = 279, pw = 268, ph = 531;
        Rectangle pBody = new Rectangle(px, baselineY - ph, pw, ph);
        pBody.setFill(Color.web("#6729FF"));
        Eye pEye1 = new Eye(Color.WHITE); pEye1.setLayoutX(px + 80); pEye1.setLayoutY(baselineY - ph + 100);
        Eye pEye2 = new Eye(Color.WHITE); pEye2.setLayoutX(px + 130); pEye2.setLayoutY(baselineY - ph + 100);
        Line pNose = new Line(px + 105, baselineY - ph + 130, px + 105, baselineY - ph + 160);
        pNose.setStrokeWidth(3); pNose.setStroke(Color.BLACK);
        purpleGroup.getChildren().addAll(pBody, pEye1, pEye2, pNose);
        characterGroups.add(purpleGroup);
        eyeMap.put(purpleGroup, List.of(pEye1, pEye2));

        // === 黑色角色 ===
        Group blackGroup = new Group();
        double bx = 483, bw = 148, bh = 365;
        Rectangle bBody = new Rectangle(bx, baselineY - bh, bw, bh);
        bBody.setFill(Color.BLACK);
        Eye bEye1 = new Eye(Color.WHITE); bEye1.setLayoutX(bx + 40); bEye1.setLayoutY(baselineY - bh + 50);
        Eye bEye2 = new Eye(Color.WHITE); bEye2.setLayoutX(bx + 70); bEye2.setLayoutY(baselineY - bh + 50);
        blackGroup.getChildren().addAll(bBody, bEye1, bEye2);
        characterGroups.add(blackGroup);
        eyeMap.put(blackGroup, List.of(bEye1, bEye2));

        // === 黄色角色 ===
        Group yellowGroup = new Group();
        double yx = 585, yw = 154, yh = 254, arcH = yw / 2.0;
        Arc yTop = new Arc(yx + yw / 2, baselineY - yh + arcH, yw / 2, arcH, 0, 180);
        yTop.setType(ArcType.ROUND); yTop.setFill(Color.GOLD);
        Rectangle yBottom = new Rectangle(yx, baselineY - yh + arcH, yw, yh - arcH);
        yBottom.setFill(Color.GOLD);
        Eye yEye1 = new Eye(Color.WHITE); yEye1.setLayoutX(yx + 40); yEye1.setLayoutY(baselineY - yh + 40);
        Eye yEye2 = new Eye(Color.WHITE); yEye2.setLayoutX(yx + 80); yEye2.setLayoutY(baselineY - yh + 40);
        Line yMouth = new Line(yx + 50, baselineY - yh + 80, yx + 90, baselineY - yh + 80);
        yMouth.setStrokeWidth(3); yMouth.setStroke(Color.BLACK);
        yellowGroup.getChildren().addAll(yTop, yBottom, yEye1, yEye2, yMouth);
        characterGroups.add(yellowGroup);
        eyeMap.put(yellowGroup, List.of(yEye1, yEye2));

        // === 橙色角色（最顶层） ===
        Group orangeGroup = new Group();
        double ox = 128, ow = 405, oh = 200;
        Arc oBody = new Arc(ox + ow / 2, baselineY, ow / 2, oh, 0, 180);
        oBody.setType(ArcType.ROUND); oBody.setFill(Color.ORANGE);
        Circle oE1 = new Circle(ox + 130, baselineY - 80, 4, Color.BLACK);
        Circle oE2 = new Circle(ox + 160, baselineY - 80, 4, Color.BLACK);
        Circle oE3 = new Circle(ox + 190, baselineY - 80, 4, Color.BLACK);
        Arc oMouth = new Arc(ox + 160, baselineY - 30, 40, 20, 0, 180);
        oMouth.setType(ArcType.ROUND); oMouth.setFill(Color.BLACK);
        orangeGroup.getChildren().addAll(oBody, oE1, oE2, oE3, oMouth);
        characterGroups.add(orangeGroup);

        root.getChildren().addAll(purpleGroup, blackGroup, yellowGroup, orangeGroup); // 橙色最后添加在最顶层

        // === 动画 ===
        ParallelTransition all = new ParallelTransition();

        for (Group group : characterGroups) {
            double startY = -400 - rand.nextInt(400); // -400 ~ -800
            int n = 1 + rand.nextInt(3);              // 1 ~ 3 圈
            int deg = n * 360;
            int delay = rand.nextInt(300);            // 可选动画错位

            group.setTranslateY(startY);
            group.setScaleY(0.3);
            group.setScaleX(0.8);
            group.setRotate(0);

            Duration duration = Duration.seconds(1.8);

            TranslateTransition drop = new TranslateTransition(duration, group);
            drop.setToY(0);
            drop.setInterpolator(Interpolator.EASE_BOTH);
            drop.setDelay(Duration.millis(delay));

            RotateTransition rotate = new RotateTransition(duration, group);
            rotate.setByAngle(deg);
            rotate.setDelay(Duration.millis(delay));

            ScaleTransition scaleY = new ScaleTransition(duration, group);
            scaleY.setFromY(0.3);
            scaleY.setToY(1);
            scaleY.setDelay(Duration.millis(delay));

            ScaleTransition scaleX = new ScaleTransition(duration, group);
            scaleX.setFromX(0.8);
            scaleX.setToX(1);
            scaleX.setDelay(Duration.millis(delay));

            ParallelTransition p = new ParallelTransition(drop, rotate, scaleX, scaleY);
            p.setOnFinished(e -> {
                group.setRotate(0);
                group.setScaleX(1);
                group.setScaleY(1);
            });

            all.getChildren().add(p);
        }

        all.play();
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            eyeMap.forEach((group, eyes) -> {
                for (Eye eye : eyes) {
                    double cx = eye.getLayoutX() * SCALE_RATIO;
                    double cy = eye.getLayoutY() * SCALE_RATIO;
                    eye.lookAt(e.getX(), e.getY(), cx, cy);
                }
            });
        });
        return root;
    }

    // 新增方法：更新用户的最后登录时间
    private void updateLastLoginTime(long userId) {
        String sql = "UPDATE user SET last_login_time = ? WHERE id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, userId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("用户 " + userId + " 的最后登录时间已更新。");
            } else {
                System.out.println("更新用户 " + userId + " 的最后登录时间失败。");
            }
        } catch (SQLException e) {
            System.err.println("更新最后登录时间数据库操作失败:");
            e.printStackTrace();
            // TODO: 显示错误信息给用户或记录到日志
        }
    }

}