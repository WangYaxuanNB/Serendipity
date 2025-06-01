package com.serendipity.demo;

//import com.serendipity.demo.HttpClient;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.serendipity.demo.util.JdbcUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Register{
    private Scene scene;
    public Scene getScene() {
        return scene;
    }

    private static final double SCALE_RATIO = 0.6;

    public Register(MainApp app) throws IOException {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1640, 1154);

        Group scaledRoot = new Group(root);
        scaledRoot.getTransforms().add(new Scale(SCALE_RATIO, SCALE_RATIO, 0, 0));

        Scene scene = new Scene(scaledRoot, 1640 * SCALE_RATIO, 1154 * SCALE_RATIO);

        BackgroundFill backgroundFill = new BackgroundFill(
                Color.web("#D9D9D9"), new CornerRadii(26), null
        );
        root.setBackground(new Background(backgroundFill));

        Pane roundedPane = createRightRoundedPanel(app);
        root.getChildren().add(roundedPane);
        roundedPane.setTranslateX(961);
        roundedPane.setTranslateY(22);

        Rectangle clip = new Rectangle(1640, 1154);
        clip.setArcWidth(52);
        clip.setArcHeight(52);
        root.setClip(clip);

        Pane characterPane = Character(scene);
        root.getChildren().add(characterPane);
        this.scene=scene;
    }

    private Pane createRightRoundedPanel(MainApp app) {
        Pane roundedPane = new Pane();
        roundedPane.setPrefSize(644, 1114);
        roundedPane.setStyle("-fx-background-color: white; -fx-background-radius: 26;");

        Image image = new Image(getClass().getResource("/png/Group1.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(41);
        imageView.setFitHeight(41);
        imageView.setX(301);
        imageView.setY(89);
        roundedPane.getChildren().add(imageView);

        Text text1 = new Text("Create Account");
        text1.setFont(Font.font("Inter", FontWeight.BOLD, 48));
        text1.setFill(Color.BLACK);
        double text1Width = text1.getBoundsInLocal().getWidth();
        double pane1Width = roundedPane.getPrefWidth();
        text1.setX((pane1Width - text1Width) / 2);
        text1.setY(271);

        Text text2 = new Text("Please fill in your details");
        text2.setFont(Font.font("Inter", FontWeight.LIGHT, 20));
        text2.setFill(Color.BLACK);
        double text2Width = text2.getBoundsInLocal().getWidth();
        double pane2Width = roundedPane.getPrefWidth();
        text2.setX((pane2Width - text2Width) / 2);
        text2.setY(344);

        // 用户名输入框
        TextField usernameInput = createStyledTextField("Enter your username");
        usernameInput.setTranslateX(55);
        usernameInput.setTranslateY(400);
        Line usernameLine = createUnderline(66, 440, 577);
        usernameLine.setLayoutY(30);

        // 邮箱输入框
        TextField phoneNumberInput = createStyledTextField("Enter your phone number");
        phoneNumberInput.setTranslateX(55);
        phoneNumberInput.setTranslateY(500);
        Line phoneNumberLine = createUnderline(66, 540, 577);
        phoneNumberLine.setLayoutY(30);

        // 密码输入框
        TextField passwordInput = createStyledTextField("Enter your password");
        passwordInput.setTranslateX(55);
        passwordInput.setTranslateY(600);
        Line passwordLine = createUnderline(66, 640, 577);
        passwordLine.setLayoutY(30);

        // 确认密码输入框
        TextField confirmPasswordInput = createStyledTextField("Confirm your password");
        confirmPasswordInput.setTranslateX(55);
        confirmPasswordInput.setTranslateY(700);
        Line confirmPasswordLine = createUnderline(66, 740, 577);
        confirmPasswordLine.setLayoutY(30);

        // 注册按钮
        Button registerButton = new Button("Sign Up");
        registerButton.setStyle("-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;");

        // 按钮悬停效果
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: darkgray; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;"));

        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 26; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: 600; " +
                "-fx-padding: 10 20;"));

        registerButton.setTranslateX(66);
        registerButton.setTranslateY(860);
        registerButton.setPrefHeight(76);
        registerButton.setPrefWidth(511);

        registerButton.setOnMouseClicked(e -> {
            String username = usernameInput.getText().trim();
            String email = phoneNumberInput.getText().trim(); // Assuming phone number field is used for email
            String password = passwordInput.getText().trim();
            String confirmPassword = confirmPasswordInput.getText().trim();

            // 1. 输入验证
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert("注册失败", "请填写所有必填项。");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("注册失败", "两次输入的密码不一致。");
                return;
            }

            // TODO: Add more robust email format validation if needed
            if (!email.contains("@")) {
                 showAlert("注册失败", "邮箱格式不正确。");
                 return;
            }

            // TODO: Add password strength validation (e.g., minimum length, complexity)

            // 2. 数据库交互 - 检查用户名和邮箱唯一性并插入新用户
            String checkExistingSql = "SELECT COUNT(*) FROM user WHERE username = ? OR email = ?";
            String insertUserSql = "INSERT INTO user (username, password, email, registration_time) VALUES (?, ?, ?, ?)";

            try (Connection conn = JdbcUtil.getConnection()) {
                // 检查用户名和邮箱是否存在
                try (PreparedStatement checkStmt = conn.prepareStatement(checkExistingSql)) {
                    JdbcUtil.setParameters(checkStmt, username, email);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            showAlert("注册失败", "用户名或邮箱已存在。");
                            return;
                        }
                    }
                }

                // 插入新用户
                try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {
                    // Note: You should ideally hash passwords before storing them in a real application
                    JdbcUtil.setParameters(insertStmt, username, password, email, Timestamp.valueOf(LocalDateTime.now()));
                    int affectedRows = insertStmt.executeUpdate();

                    if (affectedRows > 0) {
                        showAlert("注册成功", "账号创建成功，请登录。");
                        // 3. 跳转到登录页面
                        try {
                            app.showLoginPage();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        showAlert("注册失败", "创建账号失败。");
                    }
                }

            } catch (SQLException ex) {
                System.err.println("数据库操作失败:");
                ex.printStackTrace();
                showAlert("数据库错误", "注册过程中发生数据库错误：" + ex.getMessage());
            }
        });


        // 添加登录链接
        Text loginText = new Text("Already have an account? ");
        loginText.setFont(Font.font("Inter", 20));
        loginText.setFill(Color.BLACK);

        Text loginLink = new Text("Log In");
        loginLink.setFont(Font.font("Inter", 20));
        loginLink.setFill(Color.BLACK);
        loginLink.setStyle("-fx-cursor: hand;");

        HBox loginContainer = new HBox(5);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.getChildren().addAll(loginText, loginLink);
        loginContainer.setTranslateX(165);
        loginContainer.setTranslateY(960);

        loginLink.setOnMouseEntered(e -> loginLink.setFill(Color.GRAY));
        loginLink.setOnMouseExited(e -> loginLink.setFill(Color.BLACK));
        loginLink.setOnMouseClicked(e -> {
            // TODO: 实现返回登录页面的逻辑
            try {
                app.showLoginPage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        roundedPane.getChildren().addAll(
                text1, text2,
                usernameInput, usernameLine,
                phoneNumberInput, phoneNumberLine,
                passwordInput, passwordLine,
                confirmPasswordInput, confirmPasswordLine,
                registerButton,
                loginContainer
        );

        return roundedPane;
    }

    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setFocusTraversable(false);
        textField.setPrefWidth(511);
        textField.setPrefHeight(61);
        textField.setStyle("-fx-background-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Inter'; " +
                "-fx-font-size: 24px; " +
                "-fx-font-weight: normal;");
        return textField;
    }

    private Line createUnderline(double startX, double startY, double endX) {
        Line line = new Line(startX, startY, endX, startY);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);
        return line;
    }

    private Pane Character(Scene scene) {
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

        root.getChildren().addAll(purpleGroup, blackGroup, yellowGroup, orangeGroup);

        // === 动画 ===
        ParallelTransition all = new ParallelTransition();

        for (Group group : characterGroups) {
            double startY = -400 - rand.nextInt(400);
            int n = 1 + rand.nextInt(3);
            int deg = n * 360;
            int delay = rand.nextInt(300);

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

    // Helper method to show alert dialogs
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
