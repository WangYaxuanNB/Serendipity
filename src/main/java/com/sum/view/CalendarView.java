package com.sum.view;

import com.sum.MainApp;
import com.sum.domain.entity.Mood;
import com.sum.service.IMoodService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 心情日历视图
 */
@Component
public class CalendarView {
    private MainApp mainApp;
    private Scene scene;
    private VBox root;
    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthYearLabel;
    private static final DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");

    @Autowired
    private IMoodService moodService;

    public CalendarView() {
        // 默认构造函数，由Spring调用
    }

    public void initialize(MainApp mainApp) {
        this.mainApp = mainApp;
        this.currentYearMonth = YearMonth.now();

        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #FDF5F5;");

        // Top area for title and back element
        HBox topArea = new HBox(20);
        topArea.setAlignment(Pos.CENTER_LEFT);

        // 返回元素 (使用 ImageView 和动画或备用文本)
        StackPane backElementPane;
        
        // 直接使用文本作为返回按钮，避免图片加载问题
        Label backLabel = new Label("← 返回");
        backLabel.setFont(Font.font("Arial", 16));
        backLabel.setStyle("-fx-text-fill: #333333;");
        backElementPane = new StackPane(backLabel);
        
        backElementPane.setStyle("-fx-cursor: hand;");

        // Add hover animation to the back element pane
        setupBackButtonHoverAnimation(backElementPane);

        // Add click handler to the back element pane
        setupBackButtonClickHandler(backElementPane);

        // 顶部标题
        Label titleLabel = new Label("心情日历");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setStyle("-fx-text-fill: #333333;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setAlignment(Pos.CENTER);

        topArea.getChildren().addAll(backElementPane, titleLabel);

        // 月份导航
        HBox monthNavigation = createMonthNavigation();

        // 生成报表按钮
        Button generateReportButton = new Button("生成日报表");
        generateReportButton.setStyle("-fx-background-color: #5a5a5a; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 8 15;");
        generateReportButton.setOnAction(e -> generateMoodReport());

        HBox navigationAndReport = new HBox(20);
        navigationAndReport.setAlignment(Pos.CENTER);
        navigationAndReport.getChildren().addAll(monthNavigation, generateReportButton);

        // 日历网格
        calendarGrid = new GridPane();
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setAlignment(Pos.CENTER);
        
        // 创建日历内容
        updateCalendar();

        // 将日历网格放入滚动面板
        ScrollPane scrollPane = new ScrollPane(calendarGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPadding(new Insets(10));

        root.getChildren().addAll(topArea, navigationAndReport, scrollPane);
        
        this.scene = new Scene(root, 800, 600);
    }

    private HBox createMonthNavigation() {
        HBox navigation = new HBox(20);
        navigation.setAlignment(Pos.CENTER);

        Button prevMonth = new Button("←");
        Button nextMonth = new Button("→");
        monthYearLabel = new Label(currentYearMonth.format(monthYearFormatter));
        
        // 设置按钮样式
        String buttonStyle = "-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-background-radius: 20; -fx-min-width: 40; -fx-min-height: 40;";
        prevMonth.setStyle(buttonStyle);
        nextMonth.setStyle(buttonStyle);
        
        monthYearLabel.setFont(Font.font("Arial", 20));
        monthYearLabel.setStyle("-fx-text-fill: #333333;");

        prevMonth.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextMonth.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        navigation.getChildren().addAll(prevMonth, monthYearLabel, nextMonth);
        return navigation;
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.format(monthYearFormatter));

        // 添加星期标题
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(weekDays[i]);
            dayLabel.setFont(Font.font("Arial", 14));
            dayLabel.setStyle("-fx-text-fill: #666666;");
            calendarGrid.add(dayLabel, i, 0);
        }

        // 获取当月第一天是星期几
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;

        // 填充日历格子
        for (int i = 1; i <= currentYearMonth.lengthOfMonth(); i++) {
            LocalDate date = currentYearMonth.atDay(i);
            VBox dayBox = createDayBox(date);
            calendarGrid.add(dayBox, (i + dayOfWeek - 1) % 7, (i + dayOfWeek - 1) / 7 + 1);
        }
    }

    private VBox createDayBox(LocalDate date) {
        VBox dayBox = new VBox(5);
        dayBox.setAlignment(Pos.TOP_CENTER);
        dayBox.setPadding(new Insets(10));
        dayBox.setPrefSize(100, 100);
        dayBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-cursor: hand;");

        // Add click event handler
        dayBox.setOnMouseClicked(event -> {
            System.out.println("Clicked on date: " + date);
            mainApp.showMoodTrendView(date); // 跳转到趋势图页面
        });

        // 日期标签
        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dateLabel.setFont(Font.font("Arial", 16));
        dateLabel.setStyle("-fx-text-fill: #333333;");

        // 获取当天的情绪记录
        List<Mood> moodRecords = getMoodsForDate(date);
        
        // 显示情绪记录
        if (!moodRecords.isEmpty()) {
            for (Mood record : moodRecords) {
                VBox moodBox = new VBox(5);
                moodBox.setAlignment(Pos.CENTER);
                
                // 心情图标
                ImageView moodIcon = new ImageView();
                try {
                    Image img = new Image(getClass().getResourceAsStream("/images/mood/" + record.getFh()));
                    moodIcon.setImage(img);
                    moodIcon.setFitWidth(30);
                    moodIcon.setFitHeight(30);
                } catch (Exception e) {
                    System.out.println("图标加载失败: " + record.getFh());
                }
                
                // 心情文本
                Label moodLabel = new Label(record.getMood());
                moodLabel.setFont(Font.font("Arial", 12));
                moodLabel.setStyle("-fx-text-fill: #666666;");
                
                moodBox.getChildren().addAll(moodIcon, moodLabel);
                dayBox.getChildren().add(moodBox);
            }
        }

        dayBox.getChildren().add(0, dateLabel);
        return dayBox;
    }

    private List<Mood> getMoodsForDate(LocalDate date) {
        // 使用服务获取当天心情记录
        return moodService.getMoodsForDate(mainApp.getCurrentUserId(), date);
    }

    // Helper method for hover animation
    private void setupBackButtonHoverAnimation(StackPane buttonPane) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), buttonPane);
        scaleIn.setToX(1.2); // Scale up to 120%
        scaleIn.setToY(1.2);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), buttonPane);
        scaleOut.setToX(1.0); // Scale back to 100%
        scaleOut.setToY(1.0);

        buttonPane.setOnMouseEntered(event -> scaleIn.playFromStart());
        buttonPane.setOnMouseExited(event -> scaleOut.playFromStart());
    }

    // Helper method for click handler
    private void setupBackButtonClickHandler(StackPane buttonPane) {
        buttonPane.setOnMouseClicked(event -> {
            mainApp.showMoodSelectView(); // 返回心情选择页面
        });
    }

    // 生成心情日报表方法
    private void generateMoodReport() {
        // 1. 获取当前月份心情数据
        List<Mood> monthlyRecords = moodService.getMonthlyMoodRecords(mainApp.getCurrentUserId(), currentYearMonth);

        if (monthlyRecords.isEmpty()) {
            System.out.println("当前月份没有心情记录可导出。");
            return;
        }

        // 2. 生成CSV内容 (UTF-8)
        StringBuilder csvContent = new StringBuilder();
        // 修改表头，添加"时间"列
        csvContent.append("日期,时间,心情,图标文件,饮食建议\n"); // CSV Header

        // 创建日期和时间格式化器
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formatter for time

        for (Mood record : monthlyRecords) {
            // 获取并格式化日期和时间
            String dateStr = record.getRecordTime().format(dateFormatter);
            String timeStr = record.getRecordTime().format(timeFormatter); // Format the time
            String suggestion = getDietSuggestion(record.getMood());
            // 修改数据行格式，添加时间列
            csvContent.append(String.format("%s,%s,%s,%s,%s\n",
                    escapeCsv(dateStr),
                    escapeCsv(timeStr), // Added time to CSV row
                    escapeCsv(record.getMood()),
                    escapeCsv(record.getFh()),
                    escapeCsv(suggestion)));
        }

        // 3. 弹出文件保存对话框并保存文件
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存心情日报表");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV 文件 (*.csv)", "*.csv"));
        fileChooser.setInitialFileName(currentYearMonth.format(monthYearFormatter) + "心情日报表.csv");

        // Get the stage from the current scene or main application
        Stage stage = (Stage) root.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {

                // 添加 UTF-8 BOM 以提高兼容性，尤其是在 Windows Excel 中
                fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

                writer.write(csvContent.toString());
                System.out.println("日报表保存成功: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("保存日报表失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
             System.out.println("文件保存已取消。");
        }
    }

    // Helper method to escape special characters in CSV fields
    private String escapeCsv(String field) {
        if (field == null) {
            return "";
        }
        // Escape double quotes by doubling them and enclose the field in double quotes if it contains
        // double quotes, commas, or newlines.
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        } else {
            return field;
        }
    }

    // 根据心情提供饮食建议 (示例方法)
    private String getDietSuggestion(String mood) {
        switch (mood) {
            case "开心":
                return "建议保持均衡饮食，可以适量享受美食。";
            case "平静":
                return "建议多吃富含膳食纤维的食物，如全麦、蔬菜水果。";
            case "难过":
            case "伤心":
                return "建议多吃富含色氨酸的食物，如牛奶、香蕉、坚果，有助于改善情绪。";
            case "生气":
                return "建议多吃富含B族维生素的食物，如绿叶蔬菜、豆类，有助于缓解压力。";
            case "焦虑":
            case "烦躁":
                return "建议避免过量咖啡因和糖分，多喝水或花草茶。";
            case "兴奋":
            case "心动":
                return "建议多喝水，注意避免过度刺激的食物。";
            default:
                return "建议保持规律饮食，注意营养均衡。";
        }
    }

    public Scene getScene() {
        return scene;
    }
} 