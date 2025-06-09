package com.sum.view;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sum.MainApp;
import com.sum.domain.entity.User;
import com.sum.service.IUserService;
import com.sum.utils.ControlUtil;
import com.sum.utils.SpringContextUtil;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import lombok.Getter;

import java.util.*;

@Getter
public class RegisterView {
    private Scene scene;
    private IUserService userService;
    public Scene getScene() {
        return scene;
    }

    private static final double SCALE_RATIO = 0.6;

    public RegisterView(MainApp app) {
        this.userService = SpringContextUtil.getBean(IUserService.class);
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

        Image image = new Image(getClass().getResource("/images/Group1.png").toExternalForm());
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
        TextField phoneNumberInput = createStyledTextField("Enter your email");
        phoneNumberInput.setTranslateX(55);
        phoneNumberInput.setTranslateY(500);
        Line phoneNumberLine = createUnderline(66, 540, 577);
        phoneNumberLine.setLayoutY(30);

        // 密码输入框
        PasswordField passwordInput = createStyledPasswordTextField("Enter your password");
        passwordInput.setTranslateX(55);
        passwordInput.setTranslateY(600);
        Line passwordLine = createUnderline(66, 640, 577);
        passwordLine.setLayoutY(30);

        // 确认密码输入框
        PasswordField confirmPasswordInput = createStyledPasswordTextField("Confirm your password");
        confirmPasswordInput.setTranslateX(55);
        confirmPasswordInput.setTranslateY(700);
        Line confirmPasswordLine = createUnderline(66, 740, 577);
        confirmPasswordLine.setLayoutY(30);

        // 注册按钮
        Button registerButton = new Button("Sign Up");
        registerButton.setCursor(Cursor.HAND);
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
                ControlUtil.warning("请填写所有必填项。");
                return;
            }

            if (!password.equals(confirmPassword)) {
                ControlUtil.warning("两次输入的密码不一致。");
                return;
            }
            if (!email.contains("@")) {
                ControlUtil.warning("邮箱格式不正确。");
                 return;
            }
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(User::getUsername,username).or().like(User::getEmail,email);
            int count = this.getUserService().count(queryWrapper);
            if(count > 0){
                ControlUtil.warning("用户名或邮箱已存在，请重新输入！");
                return;
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setRegistrationTime(new Date());
            this.getUserService().save(user);
            ControlUtil.success("注册成功！");
            usernameInput.setText("");
            phoneNumberInput.setText("");
            passwordInput.setText("");
            confirmPasswordInput.setText("");
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
            app.showLoginPage();
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

    private PasswordField createStyledPasswordTextField(String promptText) {
        PasswordField textField = new PasswordField();
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
        Map<Group, List<EyeView>> eyeMap = new LinkedHashMap<>();
        List<Group> characterGroups = new ArrayList<>();

        Random rand = new Random();

        // === 紫色角色 ===
        Group purpleGroup = new Group();
        double px = 279, pw = 268, ph = 531;
        Rectangle pBody = new Rectangle(px, baselineY - ph, pw, ph);
        pBody.setFill(Color.web("#6729FF"));
        EyeView pEyeView1 = new EyeView(Color.WHITE); pEyeView1.setLayoutX(px + 80); pEyeView1.setLayoutY(baselineY - ph + 100);
        EyeView pEyeView2 = new EyeView(Color.WHITE); pEyeView2.setLayoutX(px + 130); pEyeView2.setLayoutY(baselineY - ph + 100);
        Line pNose = new Line(px + 105, baselineY - ph + 130, px + 105, baselineY - ph + 160);
        pNose.setStrokeWidth(3); pNose.setStroke(Color.BLACK);
        purpleGroup.getChildren().addAll(pBody, pEyeView1, pEyeView2, pNose);
        characterGroups.add(purpleGroup);
        eyeMap.put(purpleGroup, List.of(pEyeView1, pEyeView2));

        // === 黑色角色 ===
        Group blackGroup = new Group();
        double bx = 483, bw = 148, bh = 365;
        Rectangle bBody = new Rectangle(bx, baselineY - bh, bw, bh);
        bBody.setFill(Color.BLACK);
        EyeView bEyeView1 = new EyeView(Color.WHITE); bEyeView1.setLayoutX(bx + 40); bEyeView1.setLayoutY(baselineY - bh + 50);
        EyeView bEyeView2 = new EyeView(Color.WHITE); bEyeView2.setLayoutX(bx + 70); bEyeView2.setLayoutY(baselineY - bh + 50);
        blackGroup.getChildren().addAll(bBody, bEyeView1, bEyeView2);
        characterGroups.add(blackGroup);
        eyeMap.put(blackGroup, List.of(bEyeView1, bEyeView2));

        // === 黄色角色 ===
        Group yellowGroup = new Group();
        double yx = 585, yw = 154, yh = 254, arcH = yw / 2.0;
        Arc yTop = new Arc(yx + yw / 2, baselineY - yh + arcH, yw / 2, arcH, 0, 180);
        yTop.setType(ArcType.ROUND); yTop.setFill(Color.GOLD);
        Rectangle yBottom = new Rectangle(yx, baselineY - yh + arcH, yw, yh - arcH);
        yBottom.setFill(Color.GOLD);
        EyeView yEyeView1 = new EyeView(Color.WHITE); yEyeView1.setLayoutX(yx + 40); yEyeView1.setLayoutY(baselineY - yh + 40);
        EyeView yEyeView2 = new EyeView(Color.WHITE); yEyeView2.setLayoutX(yx + 80); yEyeView2.setLayoutY(baselineY - yh + 40);
        Line yMouth = new Line(yx + 50, baselineY - yh + 80, yx + 90, baselineY - yh + 80);
        yMouth.setStrokeWidth(3); yMouth.setStroke(Color.BLACK);
        yellowGroup.getChildren().addAll(yTop, yBottom, yEyeView1, yEyeView2, yMouth);
        characterGroups.add(yellowGroup);
        eyeMap.put(yellowGroup, List.of(yEyeView1, yEyeView2));

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
                for (EyeView eye : eyes) {
                    double cx = eye.getLayoutX() * SCALE_RATIO;
                    double cy = eye.getLayoutY() * SCALE_RATIO;
                    eye.lookAt(e.getX(), e.getY(), cx, cy);
                }
            });
        });
        return root;
    }

}
