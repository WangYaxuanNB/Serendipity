package com.sum.utils;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StyleUtil {
    public static void setPaneBackground(Pane pane, Color color) {
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public static void setButtonBackground(Button button, Color bg, Color text) {
        button.setBackground(new Background(new BackgroundFill(bg, null, null)));
        button.setTextFill(text);
        button.setCursor(Cursor.HAND);
        BorderStroke borderStroke = new BorderStroke(null, null, Color.WHITE, null, null, null, BorderStrokeStyle.SOLID,
                null, null, null, null);
        button.setBorder(new Border(borderStroke));
        button.setStyle("-fx-background-radius:5");
        button.setPadding(new Insets(10));
    }

    public static void setFont(Labeled node, Color color, double fontSize) {
        node.setTextFill(color);
        node.setFont(Font.font(null, FontWeight.BOLD, fontSize));
    }

    /**
     * 标题字体样式
     * @param title
     * @return
     */
    public static Label genPageTitle(String title) {
        // 页面标题
        Label label = new Label(title);
        StyleUtil.setFont(label, Color.BLACK, 20);
        // 设置上下边距
        label.setPadding(new Insets(10, 0, 8, 0));
        return label;
    }

    public static void setBackgroundImage(Pane pane,String imageUrl) {
        Image headImg = new Image(imageUrl);
        BackgroundImage headBackgroundImage = new BackgroundImage(headImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background headBackground = new Background(headBackgroundImage);
        pane.setBackground(headBackground);
    }
}
