package com.sum.utils;

import cn.hutool.core.util.ObjectUtil;
import com.sum.base.Business;
import com.sum.dialog.GlobalDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;


public class ControlUtil {

    /**
     * 成功
     */
    public static void defaultSuccess() {
        GlobalDialog globalDialog = new GlobalDialog("操作成功！", "ci-checkmark-outline", "#536AF5", "提示信息", false);
        globalDialog.getOk().setOnAction(actionEvent -> globalDialog.getDialogStage().close());
    }

    public static void success(String msg) {
        GlobalDialog globalDialog = new GlobalDialog(msg, "ci-checkmark-outline", "#536AF5", "提示信息", false);
        globalDialog.getOk().setOnAction(actionEvent -> globalDialog.getDialogStage().close());
    }

    public static void success(Business business) {
        GlobalDialog globalDialog = new GlobalDialog("操作成功！", "ci-checkmark-outline", "#536AF5", "提示信息", false);
        globalDialog.getOk().setOnAction(actionEvent -> {
            business.doIt();
            globalDialog.getDialogStage().close();
        });
    }

    public static void success(String message, Business business) {
        GlobalDialog globalDialog = new GlobalDialog(message, "ci-checkmark-outline", "#536AF5", "提示信息", false);
        globalDialog.getOk().setOnAction(actionEvent -> {
            business.doIt();
            globalDialog.getDialogStage().close();
        });
    }

    /**
     * 告警
     *
     * @param message
     */
    public static void warning(String message) {
        GlobalDialog globalDialog = new GlobalDialog(message, "ci-warning-alt-filled", "#E6A23C", "告警信息", false);
        globalDialog.getOk().setOnAction(actionEvent -> globalDialog.getDialogStage().close());
    }


    public static void confirm(String message, Business ok) {
        GlobalDialog globalDialog = new GlobalDialog(message, "ci-warning-filled", "#F56C6C", "风险信息", true);
        globalDialog.getOk().setOnAction(eOk -> {
            ok.doIt();
            globalDialog.getDialogStage().close();
        });
        globalDialog.getCancel().setOnAction(cancelOk -> {
            globalDialog.getDialogStage().close();
        });
    }

    public static Button getButton(String text, String color, String icon) {
        Button button = new Button(text);
        button.setPrefSize(70, 32);
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(14);
        fontIcon.setIconColor(Paint.valueOf("#fff"));
        button.setGraphic(fontIcon);
        button.setStyle("-fx-background-color: " + color + ";-fx-text-fill: #fff;");
        button.setCursor(Cursor.HAND);
        return button;
    }

    public static Button getSmallButton(String text, String color, String icon) {
        Button button = new Button();
        button.setTooltip(new Tooltip(text));
        button.setPrefSize(22, 22);
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(16);
        fontIcon.setIconColor(Paint.valueOf("#fff"));
        button.setGraphic(fontIcon);
        button.setStyle("-fx-background-color: " + color + ";-fx-text-fill: #fff;");
        button.setCursor(Cursor.HAND);
        return button;
    }

    public static Button getButton(String text, String bgColor, String fontColor, String icon) {
        Button button = new Button(text);
        button.setPrefSize(70, 32);
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(14);
        button.setGraphic(fontIcon);
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: #E7E7E7;-fx-border-color: #B5B5B5;-fx-border-radius: 3px;");
        return button;
    }

    public static Button getButton(Double width, Double height, String text, String bgColor, String fontColor, String icon) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(14);
        fontIcon.setIconColor(Paint.valueOf(fontColor));
        button.setGraphic(fontIcon);
        button.setStyle("-fx-background-color: " + bgColor + ";-fx-text-fill: " + fontColor + ";");
        button.setCursor(Cursor.HAND);
        return button;
    }

    public static Button getGlobalSearchButton() {
        Button button = new Button();
        button.setPrefSize(32, 32);
        FontIcon fontIcon = new FontIcon("ci-search");
        fontIcon.setIconColor(Paint.valueOf("#000"));
        button.setGraphic(fontIcon);
        button.setStyle("-fx-background-color: #fff;-fx-text-fill: #000;");
        button.setCursor(Cursor.HAND);
        return button;
    }

    public static Button getSearchButton() {
        Button button = new Button();
        FontIcon fontIcon = new FontIcon("ci-search");
        fontIcon.setIconColor(Paint.valueOf("#000"));
        button.setGraphic(fontIcon);
        button.setStyle("-fx-background-color: #bfbfbf;-fx-text-fill: #000;");
        button.setCursor(Cursor.HAND);
        return button;
    }

}
