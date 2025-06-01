package com.serendipity.demo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.serendipity.demo.util.JdbcUtil;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.sql.Timestamp;

public class CalendarViewPage {
    private MainApp mainApp;
    private Scene scene;
    private VBox root;
    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthYearLabel;
    private static final DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");

    public CalendarViewPage(MainApp mainApp) {
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
        ImageView backArrowIcon = new ImageView();
        try {
            // Use a placeholder image path for the back arrow
            Image backArrowImage = new Image(getClass().getResourceAsStream("/icons/back_arrow.png")); // Replace with actual path if needed
             if (backArrowImage.isError()) {
                 throw new Exception("Image loading failed");
            }
            backArrowIcon.setImage(backArrowImage);
            backArrowIcon.setFitWidth(30); // Adjust size as needed
            backArrowIcon.setFitHeight(30);
            backElementPane = new StackPane(backArrowIcon);

        } catch (Exception e) {
            System.err.println("Failed to load back arrow icon: " + e.getMessage());
            // Fallback: Use a simple text label if image fails
            Label fallbackBackLabel = new Label("← 返回");
            fallbackBackLabel.setFont(Font.font("Arial", 16));
            fallbackBackLabel.setStyle("-fx-text-fill: #333333;");
            backElementPane = new StackPane(fallbackBackLabel);
        }

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
            try {
                mainApp.showMoodTrendChartPage(date); // Pass the selected date
            } catch (java.io.IOException e) {
                System.err.println("Failed to show mood trend chart page: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // 日期标签
        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dateLabel.setFont(Font.font("Arial", 16));
        dateLabel.setStyle("-fx-text-fill: #333333;");

        // 获取当天的情绪记录
        List<MoodRecord> moodRecords = getMoodsForDate(date);
        
        // 显示情绪记录
        if (!moodRecords.isEmpty()) {
            for (MoodRecord record : moodRecords) {
                VBox moodBox = new VBox(5);
                moodBox.setAlignment(Pos.CENTER);
                
                // 心情图标
                ImageView moodIcon = new ImageView();
                try {
                    Image img = new Image(CalendarViewPage.class.getResource("/mood/" + record.getImageName()).toExternalForm());
                    moodIcon.setImage(img);
                    moodIcon.setFitWidth(30);
                    moodIcon.setFitHeight(30);
                } catch (Exception e) {
                    System.out.println("图标加载失败: " + record.getImageName());
                }
                
                // 心情文本
                Label moodLabel = new Label(record.getMoodText());
                moodLabel.setFont(Font.font("Arial", 12));
                moodLabel.setStyle("-fx-text-fill: #666666;");
                
                moodBox.getChildren().addAll(moodIcon, moodLabel);
                dayBox.getChildren().add(moodBox);
            }
        }

        dayBox.getChildren().add(0, dateLabel);
        return dayBox;
    }

    private List<MoodRecord> getMoodsForDate(LocalDate date) {
        List<MoodRecord> moodRecords = new ArrayList<>();
        String sql = "SELECT mood, fh, record_time FROM moods WHERE DATE(record_time) = ? AND user_id = ? ORDER BY record_time DESC LIMIT 1";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setLong(2, mainApp.getCurrentUserId()); // 使用当前登录用户ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    moodRecords.add(new MoodRecord(
                        rs.getString("mood"),
                        rs.getString("fh"),
                        rs.getTimestamp("record_time") // Pass the Timestamp
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("获取心情记录失败:");
            e.printStackTrace();
            // TODO: 显示错误信息给用户
        }

        return moodRecords;
    }

    // Helper method for hover animation (Copied from MoodTrendChartPage)
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

    // Helper method for click handler (Modified to call showMoodSelectionPage)
    private void setupBackButtonClickHandler(StackPane buttonPane) {
        buttonPane.setOnMouseClicked(event -> {
            try {
                mainApp.showMoodSelectionPage(); // Call the method to show mood selection page
            } catch (java.io.IOException ex) {
                System.err.println("Failed to return to mood selection page: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    // 新增：生成心情日报表方法
    private void generateMoodReport() {
        // 1. 获取当前月份心情数据
        List<MoodRecord> monthlyRecords = getMonthlyMoodRecords(currentYearMonth);

        if (monthlyRecords.isEmpty()) {
            System.out.println("当前月份没有心情记录可导出。"); // TODO: Show alert to user
            return;
        }

        // 2. 生成CSV内容 (UTF-8)
        StringBuilder csvContent = new StringBuilder();
        // 修改表头，添加"时间"列
        csvContent.append("日期,时间,心情,图标文件,饮食建议\n"); // CSV Header

        // 创建日期和时间格式化器
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formatter for time

        for (MoodRecord record : monthlyRecords) {
            // 获取并格式化日期和时间
            String dateStr = record.getRecordTimestamp().toLocalDateTime().format(dateFormatter);
            String timeStr = record.getRecordTimestamp().toLocalDateTime().format(timeFormatter); // Format the time
            String suggestion = getDietSuggestion(record.getMoodText());
            // 修改数据行格式，添加时间列
            csvContent.append(String.format("%s,%s,%s,%s,%s\n",
                    escapeCsv(dateStr),
                    escapeCsv(timeStr), // Added time to CSV row
                    escapeCsv(record.getMoodText()),
                    escapeCsv(record.getImageName()),
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
                System.out.println("日报表保存成功: " + file.getAbsolutePath()); // TODO: Show success alert
            } catch (IOException e) {
                System.err.println("保存日报表失败: " + e.getMessage()); // TODO: Show error alert
                e.printStackTrace();
            }
        } else {
             System.out.println("文件保存已取消。"); // TODO: Show cancellation message
        }
    }

    // 新增：从数据库获取当前月份的心情记录
    private List<MoodRecord> getMonthlyMoodRecords(YearMonth yearMonth) {
        List<MoodRecord> monthlyRecords = new ArrayList<>();
        // Adjust the SQL query to select all records for the month
        String sql = "SELECT mood, fh, record_time FROM moods WHERE DATE_FORMAT(record_time, '%Y-%m') = ? AND user_id = ? ORDER BY record_time ASC";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
            stmt.setLong(2, mainApp.getCurrentUserId()); // 使用当前登录用户ID

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Re-using the MoodRecord class, might need to adjust if more data is needed for the report
                    monthlyRecords.add(new MoodRecord(
                        rs.getString("mood"),
                        rs.getString("fh"),
                        rs.getTimestamp("record_time") // Get the full Timestamp
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("获取月度心情记录失败:");
            e.printStackTrace();
            // TODO: 显示错误信息给用户
        }

        return monthlyRecords;
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

    // 新增：根据心情提供饮食建议 (示例方法)
    private String getDietSuggestion(String mood) {
        switch (mood) {
            case "开心":
                return "建议保持均衡饮食，可以适量享受美食。";
            case "平静":
                return "建议多吃富含膳食纤维的食物，如全麦、蔬菜水果。";
            case "难过":
                return "建议多吃富含色氨酸的食物，如牛奶、香蕉、坚果，有助于改善情绪。";
            case "生气":
                return "建议多吃富含B族维生素的食物，如绿叶蔬菜、豆类，有助于缓解压力。";
            case "焦虑":
                return "建议避免过量咖啡因和糖分，多喝水或花草茶。";
            default:
                return "建议保持规律饮食，注意营养均衡。";
        }
    }

    // 用于存储心情记录的内部类 (Updated to include Timestamp)
    private static class MoodRecord {
        private final String moodText;
        private final String imageName;
        private final Timestamp recordTimestamp; // Changed from LocalDate to Timestamp

        // Updated constructor
        public MoodRecord(String moodText, String imageName, Timestamp recordTimestamp) {
            this.moodText = moodText;
            this.imageName = imageName;
            this.recordTimestamp = recordTimestamp;
        }

        public String getMoodText() {
            return moodText;
        }

        public String getImageName() {
            return imageName;
        }

        // Added getter for recordTimestamp
        public Timestamp getRecordTimestamp() {
            return recordTimestamp;
        }
    }

    public Scene getScene() {
        return scene;
    }
} 