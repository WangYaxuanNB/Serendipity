package com.serendipity.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import com.serendipity.demo.util.JdbcUtil;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.animation.FadeTransition;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.animation.ScaleTransition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.List;

public class MoodTrendChartPage {

    private MainApp mainApp;
    private Scene scene;
    private VBox root;
    private LineChart<String, Number> moodChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private LocalDate selectedDate;

    // TODO: 定义心情文本到数值的映射
    private static final Map<String, Integer> MOOD_VALUE_MAP = new HashMap<>();
    static {
        MOOD_VALUE_MAP.put("生气", 1);
        MOOD_VALUE_MAP.put("烦躁", 2);
        MOOD_VALUE_MAP.put("伤心", 3);
        MOOD_VALUE_MAP.put("开心", 4);
        MOOD_VALUE_MAP.put("兴奋", 5);
        MOOD_VALUE_MAP.put("心动", 6);
    }

    // Constructor with selected date
    public MoodTrendChartPage(MainApp mainApp, LocalDate selectedDate) {
        this.mainApp = mainApp;
        this.selectedDate = selectedDate; // Store the selected date

        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #FDF5F5;"); // 背景色示例

        // Top area for title and back button
        HBox topArea = new HBox(20); // Spacing between title and button
        topArea.setAlignment(Pos.CENTER_LEFT); // Align items to the left

        // 顶部标题
        Label titleLabel = new Label("心情趋势图");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setStyle("-fx-text-fill: #333333;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS); // Allow title to take available space
        titleLabel.setAlignment(Pos.CENTER); // Center the title in the remaining space

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

        backElementPane.setStyle("-fx-cursor: hand;"); // Change cursor to hand on hover

        // Add hover animation to the back element pane
        setupBackButtonHoverAnimation(backElementPane);

        // Add click handler to the back element pane
        setupBackButtonClickHandler(backElementPane);

        topArea.getChildren().addAll(backElementPane, titleLabel);

        // 创建图表轴
        xAxis = new CategoryAxis();
        xAxis.setLabel("日期");
        yAxis = new NumberAxis();
        yAxis.setLabel("心情值");
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(7); // 根据心情数量调整上限
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        yAxis.setForceZeroInRange(false); // 允许Y轴不从0开始

        // 创建折线图
        moodChart = new LineChart<>(xAxis, yAxis);
        moodChart.setTitle("心情趋势 (" + selectedDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")) + " 前后)"); // Update title based on selected date
        moodChart.setLegendVisible(false); // 隐藏图例
        moodChart.setPrefSize(700, 400); // 设置图表首选大小
        moodChart.setStyle("-fx-background-color: white; -fx-background-radius: 10;"); // 背景色和圆角，参考 MoodSelectPage 卡片样式

        // TODO: 从数据库加载数据并添加到图表
        loadMoodData();

        // 创建心情值映射提示区域
        VBox moodMapArea = new VBox(5); // Spacing between mood items
        moodMapArea.setAlignment(Pos.CENTER);

        Label mapTitle = new Label("心情值映射:");
        mapTitle.setFont(Font.font("Arial", 16));
        mapTitle.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
        moodMapArea.getChildren().add(mapTitle);

        HBox moodItemsContainer = new HBox(15); // Spacing between mood items
        moodItemsContainer.setAlignment(Pos.CENTER);

        // Create labels for each mood-value pair with potential animation
        for (Map.Entry<String, Integer> entry : MOOD_VALUE_MAP.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList())) { // Sort by value for consistent display
            Label moodItemLabel = new Label(entry.getKey() + "(" + entry.getValue() + ")");
            moodItemLabel.setFont(Font.font("Arial", 14));
            moodItemLabel.setStyle("-fx-text-fill: #666666;");

            // Add subtle fade-in animation to moodItemLabel
            moodItemLabel.setOpacity(0); // Start with invisible
            FadeTransition ft = new FadeTransition(Duration.millis(500), moodItemLabel); // 500ms duration
            ft.setFromValue(0.0); // Start from fully transparent
            ft.setToValue(1.0);   // End at fully opaque
            // Optional: Add a delay for staggered animation
            // ft.setDelay(Duration.millis(entry.getValue() * 100)); // Stagger based on mood value
            ft.play();

            moodItemsContainer.getChildren().add(moodItemLabel);
        }

        moodMapArea.getChildren().add(moodItemsContainer);

        // 将图表和提示区域添加到布局
        VBox chartAndMapArea = new VBox(20); // Spacing between chart and map area
        chartAndMapArea.setAlignment(Pos.CENTER);
        chartAndMapArea.getChildren().addAll(moodChart, moodMapArea); // Add moodMapArea here

        root.getChildren().addAll(topArea, chartAndMapArea);

        this.scene = new Scene(root, 800, 700); // Adjust scene size again
    }

    private void loadMoodData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("心情值"); // 系列名称

        // TODO: 实现从数据库获取最近一段时间的心情记录
        // 获取最近30天的日期
        LocalDate endDate = selectedDate; // Use the selected date as the end date
        LocalDate startDate = endDate.minusDays(30);

        String sql = "SELECT DATE(record_time) as record_date, mood FROM moods " +
                     "WHERE DATE(record_time) BETWEEN ? AND ? AND user_id = ? " +
                     "ORDER BY record_time ASC"; // 按时间升序排列

        // 使用 TreeMap 存储数据，保证日期有序且每天只取最新一条记录
        Map<LocalDate, String> dailyLatestMood = new TreeMap<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            stmt.setLong(3, mainApp.getCurrentUserId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate recordDate = rs.getDate("record_date").toLocalDate();
                    String moodText = rs.getString("mood");
                    // 对于同一天，后面的记录会覆盖前面的，从而只保留最新的记录
                    dailyLatestMood.put(recordDate, moodText);
                }
            }
        } catch (SQLException e) {
            System.err.println("加载心情数据失败:");
            e.printStackTrace();
        }

        // 将获取的数据添加到图表系列并添加 tooltip
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd");
        for (Map.Entry<LocalDate, String> entry : dailyLatestMood.entrySet()) {
            LocalDate date = entry.getKey();
            String moodText = entry.getValue();
            Integer moodValue = MOOD_VALUE_MAP.get(moodText);

            if (moodValue != null) {
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(date.format(dateFormatter), moodValue);
                series.getData().add(dataPoint);
            } else {
                System.out.println("未知心情类型: " + moodText + "，无法添加到图表");
            }
        }

        moodChart.getData().add(series);

        // Tooltip requires nodes to be created after adding data to series
        // Iterate through data points and apply tooltips
        series.getData().forEach(data -> {
            if (data.getNode() != null) {
                // Find the mood text from the value using the map
                String moodText = MOOD_VALUE_MAP.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(data.getYValue()))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse("未知心情");

                Tooltip tooltip = new Tooltip(data.getXValue() + ": " + moodText);
                tooltip.setShowDelay(Duration.millis(100));
                Tooltip.install(data.getNode(), tooltip);

                // Add subtle animation to the data point node
                // Example: Scale animation on hover
                data.getNode().setOnMouseEntered(event -> data.getNode().setScaleY(1.2));
                data.getNode().setOnMouseExited(event -> data.getNode().setScaleY(1.0));
            }
        });
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
            try {
                mainApp.showCalendarViewPage(); // Call the method to show calendar page
            } catch (java.io.IOException ex) {
                System.err.println("Failed to return to calendar view: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    public Scene getScene() {
        return scene;
    }
} 