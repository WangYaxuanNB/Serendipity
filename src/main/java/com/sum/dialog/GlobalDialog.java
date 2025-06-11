package com.sum.dialog;

import com.sum.utils.ControlUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.kordamp.ikonli.javafx.FontIcon;

@Getter
public class GlobalDialog {

    private Button ok;
    private Button cancel;
    private Stage dialogStage;

    public GlobalDialog(String message, String icon, String iconColor, String title, Boolean showCancel){
        this.dialogStage = new Stage();
        VBox root = new VBox();
        Label messageLabel = new Label(message);
        FontIcon fontIcon = new FontIcon(icon);
        messageLabel.setPrefWidth(300);
        fontIcon.setIconSize(40);
        fontIcon.setIconColor(Paint.valueOf(iconColor));
        messageLabel.setGraphic(fontIcon);
        messageLabel.setStyle("-fx-font-size: 14px;-fx-padding:10px;");
        root.getChildren().add(messageLabel);
        this.ok = ControlUtil.getButton("确定", "#536AF5","#fff", "ci-checkmark-outline");
        HBox hBox = new HBox();
        hBox.getChildren().add(this.ok);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        if(showCancel){
            this.cancel = ControlUtil.getButton("取消", "#bfbfbf", "#fff", "ci-close-outline");
            hBox.setSpacing(10);
            hBox.getChildren().add(this.cancel);
        }
        root.getChildren().add(hBox);
        dialogStage.setTitle(title);
        //设置模式:没关闭当前窗口，不能进行其他成操作
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 400, 110, Color.WHITE);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
        dialogStage.centerOnScreen();
        dialogStage.show();
    }
}
