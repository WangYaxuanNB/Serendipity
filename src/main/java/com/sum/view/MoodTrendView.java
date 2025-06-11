package com.sum.view;

import com.sum.MainApp;
import com.sum.domain.entity.Mood;
import com.sum.service.IMoodService;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 心情趋势图视图
 */
@Component
public class MoodTrendView {
    private MainApp mainApp;
    private Scene scene;
    private VBox root;
    private LocalDate selectedDate;
    private Map<String, Integer> moodScores;
    
    @Autowired
    private IMoodService moodService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    public MoodTrendView() {
        // 默认构造函数，由Spring调用
        
        // 初始化情绪分数映射
        moodScores = new HashMap<>();
        moodScores.put("开心", 5);
        moodScores.put("兴奋", 5);
        moodScores.put("平静", 4);
        moodScores.put("心动", 4);
        moodScores.put("一般", 3);
        moodScores.put("焦虑", 2);
        moodScores.put("烦躁", 2);
        moodScores.put("伤心", 1);
        moodScores.put("生气", 1);
    }

    public void initialize(MainApp mainApp, LocalDate date) {
        this.mainApp = mainApp;
        this.selectedDate = date;
        
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // 顶部区域 (返回按钮和标题)
        HBox topArea = createTopArea();
        
        // 选定日期的心情信息卡片
        VBox selectedDateCard = createSelectedDateCard();
        
        // 创建本周心情趋势图
        VBox chartContainer = createWeeklyTrendChart();
        
        // 心情洞察区域
        VBox insightsArea = createInsightsArea();
        
        // 将所有组件添加到主布局
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox contentArea = new VBox(20);
        contentArea.getChildren().addAll(selectedDateCard, chartContainer, insightsArea);
        scrollPane.setContent(contentArea);
        
        root.getChildren().addAll(topArea, scrollPane);
        
        this.scene = new Scene(root, 800, 600);
    }
    
    private HBox createTopArea() {
        HBox topArea = new HBox(20);
        topArea.setAlignment(Pos.CENTER_LEFT);
        
        // 返回按钮
        StackPane backButtonPane;
        
        // 直接使用文本作为返回按钮，避免图片加载问题
        Label backLabel = new Label("← 返回");
        backLabel.setFont(Font.font("Arial", 16));
        backLabel.setStyle("-fx-text-fill: #333333;");
        backButtonPane = new StackPane(backLabel);
        
        backButtonPane.setStyle("-fx-cursor: hand;");
        
        // 添加悬停动画
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), backButtonPane);
        scaleIn.setToX(1.2);
        scaleIn.setToY(1.2);
        
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), backButtonPane);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        
        backButtonPane.setOnMouseEntered(e -> scaleIn.playFromStart());
        backButtonPane.setOnMouseExited(e -> scaleOut.playFromStart());
        
        // 点击事件 - 返回日历视图
        backButtonPane.setOnMouseClicked(e -> mainApp.showCalendarView());
        
        // 标题
        Label titleLabel = new Label("心情趋势");
        titleLabel.setFont(Font.font("Arial", 24));
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setAlignment(Pos.CENTER);
        
        topArea.getChildren().addAll(backButtonPane, titleLabel);
        return topArea;
    }
    
    private VBox createSelectedDateCard() {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        
        // 日期标题
        Label dateLabel = new Label(selectedDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        dateLabel.setFont(Font.font("Arial", 18));
        
        // 获取选定日期的心情记录
        List<Mood> moods = moodService.getMoodsForDate(mainApp.getCurrentUserId(), selectedDate);
        
        if (moods.isEmpty()) {
            Label noDataLabel = new Label("这一天没有记录心情哦~");
            noDataLabel.setStyle("-fx-text-fill: #888;");
            card.getChildren().addAll(dateLabel, noDataLabel);
        } else {
            // 展示当天的所有心情记录
            HBox moodsContainer = new HBox(10);
            moodsContainer.setAlignment(Pos.CENTER_LEFT);
            
            for (Mood mood : moods) {
                VBox moodItem = new VBox(5);
                moodItem.setAlignment(Pos.CENTER);
                
                // 心情图标
                try {
                    ImageView moodIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/mood/" + mood.getFh())));
                    moodIcon.setFitWidth(40);
                    moodIcon.setFitHeight(40);
                    
                    // 心情文本
                    Label moodLabel = new Label(mood.getMood());
                    moodLabel.setStyle("-fx-font-size: 14px;");
                    
                    // 记录时间
                    Label timeLabel = new Label(mood.getRecordTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");
                    
                    moodItem.getChildren().addAll(moodIcon, moodLabel, timeLabel);
                    moodsContainer.getChildren().add(moodItem);
                } catch (Exception e) {
                    System.err.println("无法加载心情图标: " + mood.getFh());
                }
            }
            
            card.getChildren().addAll(dateLabel, moodsContainer);
        }
        
        return card;
    }
    
    private VBox createWeeklyTrendChart() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        
        Label chartTitle = new Label("本周心情趋势");
        chartTitle.setFont(Font.font("Arial", 18));
        
        // 创建X轴和Y轴
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 5, 1);
        xAxis.setLabel("日期");
        yAxis.setLabel("心情指数");
        
        // 创建折线图
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("本周心情变化");
        lineChart.setLegendVisible(false);
        
        // 获取本周数据并添加到图表
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // 计算本周开始日期(周日)和结束日期(周六)
        LocalDate startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        // 填充本周每天的数据
        List<String> weekDays = new ArrayList<>();
        Map<String, Double> dailyScores = new HashMap<>();
        
        for (int i = 0; i <= 6; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            String dateStr = currentDate.format(DATE_FORMATTER);
            weekDays.add(dateStr);
            
            // 获取当天所有心情记录
            List<Mood> dayMoods = moodService.getMoodsForDate(mainApp.getCurrentUserId(), currentDate);
            
            // 计算当天平均心情分数
            if (!dayMoods.isEmpty()) {
                double totalScore = 0;
                for (Mood mood : dayMoods) {
                    // 根据心情文本获取分数，如果找不到则默认为3分
                    Integer score = moodScores.getOrDefault(mood.getMood(), 3);
                    totalScore += score;
                }
                double avgScore = totalScore / dayMoods.size();
                dailyScores.put(dateStr, avgScore);
                series.getData().add(new XYChart.Data<>(dateStr, avgScore));
            } else {
                // 如果当天没有记录，用null表示
                series.getData().add(new XYChart.Data<>(dateStr, null));
            }
        }
        
        lineChart.getData().add(series);
        
        container.getChildren().addAll(chartTitle, lineChart);
        return container;
    }
    
    private VBox createInsightsArea() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        
        Label insightTitle = new Label("心情洞察");
        insightTitle.setFont(Font.font("Arial", 18));
        
        // 生成洞察文本
        TextFlow insightFlow = new TextFlow();
        
        // 获取本周所有心情记录
        LocalDate startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        
        List<Mood> weekMoods = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            weekMoods.addAll(moodService.getMoodsForDate(mainApp.getCurrentUserId(), currentDate));
        }
        
        // 根据记录数量生成不同的洞察内容
        if (weekMoods.isEmpty()) {
            Text noDataText = new Text("本周还没有记录心情哦！坚持每天记录，可以更好地了解自己的情绪变化~");
            noDataText.setStyle("-fx-fill: #666;");
            insightFlow.getChildren().add(noDataText);
        } else {
            // 计算主要情绪
            Map<String, Integer> moodCounts = new HashMap<>();
            for (Mood mood : weekMoods) {
                moodCounts.put(mood.getMood(), moodCounts.getOrDefault(mood.getMood(), 0) + 1);
            }
            
            // 找出出现最多的情绪
            String dominantMood = Collections.max(moodCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
            
            // 生成洞察文本
            Text insightText = new Text(String.format(
                    "本周共记录了 %d 条心情，其中「%s」是您最常见的情绪。",
                    weekMoods.size(), dominantMood
            ));
            insightText.setStyle("-fx-fill: #333;");
            
            Text tipsText = new Text("\n\n");
            
            // 根据主要情绪提供建议
            if (moodScores.getOrDefault(dominantMood, 3) >= 4) {
                tipsText.setText(tipsText.getText() + "您本周心情不错！建议保持健康的生活方式和积极的社交活动，继续维持良好的情绪状态。");
            } else if (moodScores.getOrDefault(dominantMood, 3) <= 2) {
                tipsText.setText(tipsText.getText() + "您本周心情似乎有些低落。尝试进行一些让您感到愉快的活动，例如运动、听音乐或与朋友交流。如果情绪持续低落，建议寻求专业帮助。");
            } else {
                tipsText.setText(tipsText.getText() + "您本周心情平稳。保持规律的作息和健康的饮食习惯，可以帮助您维持稳定的情绪状态。");
            }
            tipsText.setStyle("-fx-fill: #666;");
            
            insightFlow.getChildren().addAll(insightText, tipsText);
        }
        
        container.getChildren().addAll(insightTitle, insightFlow);
        return container;
    }

    public Scene getScene() {
        return scene;
    }
} 