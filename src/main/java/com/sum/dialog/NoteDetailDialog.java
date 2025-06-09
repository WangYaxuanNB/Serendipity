package com.sum.dialog;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sum.domain.entity.Comments;
import com.sum.domain.entity.CommentsInfo;
import com.sum.domain.entity.Notes;
import com.sum.domain.entity.Record;
import com.sum.service.ICommentsService;
import com.sum.service.INotesService;
import com.sum.service.IRecordService;
import com.sum.utils.ContextUtil;
import com.sum.utils.ControlUtil;
import com.sum.utils.FileUtils;
import com.sum.utils.SpringContextUtil;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * 博客详情弹框
 */
@Getter
public class NoteDetailDialog {

    private Stage noteInfoStage;
    private BorderPane root;
    private Notes note;
    private ICommentsService commentsService;
    private INotesService notesService;
    private IRecordService recordService;


    public NoteDetailDialog(Notes note, Button commentsLabel) {
        this.commentsService = SpringContextUtil.getBean(ICommentsService.class);
        this.notesService = SpringContextUtil.getBean(INotesService.class);
        this.recordService = SpringContextUtil.getBean(IRecordService.class);
        this.note = note;
        this.noteInfoStage = new Stage();
        this.root = new BorderPane();
        createTops();
        createCenter();
        createBottom(commentsLabel);
        //设置模式:没关闭当前窗口，不能进行其他成操作
        noteInfoStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 900, 800, Color.WHITE);
        noteInfoStage.setTitle("笔记详情");
        noteInfoStage.setScene(scene);
        noteInfoStage.setResizable(false);
        noteInfoStage.centerOnScreen();
    }

    public NoteDetailDialog(Notes note) {
        this.commentsService = SpringContextUtil.getBean(ICommentsService.class);
        this.notesService = SpringContextUtil.getBean(INotesService.class);
        this.recordService = SpringContextUtil.getBean(IRecordService.class);
        this.note = note;
        this.noteInfoStage = new Stage();
        this.root = new BorderPane();
        createTops();
        createCenter();
        //设置模式:没关闭当前窗口，不能进行其他成操作
        noteInfoStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 900, 800, Color.WHITE);
        noteInfoStage.setTitle("笔记详情");
        noteInfoStage.setScene(scene);
        noteInfoStage.setResizable(false);
        noteInfoStage.centerOnScreen();
    }

    private void createBottom(Button commentsLabel) {
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(10));
        bottom.setAlignment(Pos.CENTER);
        bottom.setPrefWidth(700);
        bottom.setSpacing(20);
        TextField comment = new TextField();
        comment.setPromptText("请输入评论");
        comment.setStyle("-fx-pref-width: 400px;-fx-pref-height: 32px;");
        Button submit = new Button("提交");
        submit.setPrefSize(70, 32);
        submit.setCursor(Cursor.HAND);
        handleSubmit(submit, comment, commentsLabel);
        bottom.getChildren().addAll(comment, submit);
        this.root.setBottom(bottom);
    }

    private void handleSubmit(Button submit, TextField comment, Button commentsLabel) {
        submit.setOnAction(actionEvent -> {
            if (StringUtils.isBlank(comment.getText())) {
                ControlUtil.warning("请输入评论后再提交！");
                return;
            }
            Comments newComment = new Comments();
            newComment.setContent(comment.getText());
            newComment.setAuthor(ContextUtil.getCurrentUser().getUsername());
            newComment.setNoteId(note.getId());
            newComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
            this.getCommentsService().incrementNoteCommentCount(note.getId());
            this.getCommentsService().createComment(newComment);
            createRecords(comment.getText());
            comment.setText("");
            ControlUtil.success("评论成功");
            commentsLabel.setText(String.valueOf(this.getNotesService().getCommentCount(note.getId())));
            this.createCenter();
        });
    }

    public void createRecords(String content){
        Record record = new Record();
        record.setNoteId(this.getNote().getId());
        record.setAuthor(ContextUtil.getCurrentUser().getUsername());
        record.setType(2);
        record.setContent(content);
        record.setCreateTime(new Date());
        this.getRecordService().save(record);
    }

    private void createCenter() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(400);
        VBox content = new VBox();
        content.setPrefWidth(800);
        content.setSpacing(8);
        //查询该笔记的所有评论
        List<CommentsInfo> infoList = this.getCommentsService().queryCommentsInfo(this.getNote().getId());
        for (CommentsInfo commentsInfo : infoList) {
            HBox commentBox = new HBox();
            AvatarStackPane avatarStackPane = new AvatarStackPane(50, commentsInfo.getAvatarUrl(), false);
            commentBox.getChildren().add(avatarStackPane.getMainLayout());
            VBox v = new VBox();
            v.setSpacing(20);
            v.setPrefHeight(40);
            Label author = new Label(commentsInfo.getCommenter());
            FontIcon icon = new FontIcon("ci-user-avatar-filled-alt");
            icon.setIconSize(12);
            author.setGraphic(icon);
            author.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;");
            Text text = new Text("\t" + commentsInfo.getContent());
            text.setLineSpacing(10);
            text.setWrappingWidth(700);
            Label time = new Label("时间：" + DateUtil.format(commentsInfo.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            time.setStyle("-fx-font-size: 10px;-fx-text-fill: #8a8a8a");
            v.getChildren().addAll(author,text,time);
            commentBox.getChildren().add(v);
            content.getChildren().add(commentBox);
        }
        scrollPane.setContent(content);
        this.root.setCenter(scrollPane);
    }

    private void createTops() {
        VBox top = new VBox();
        top.setMaxHeight(400);
        top.setAlignment(Pos.CENTER);
        ImageView imageView = FileUtils.createImageView(note.getImageUrl());
        imageView.setFitWidth(600);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        GridPane gridPane = new GridPane();
        gridPane.setPrefHeight(140);
        gridPane.setPadding(new Insets(16, 10, 10, 10));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(20);

        Label authorLabel = new Label("作者");
        Label authorValue = new Label(this.getNote().getAuthor());

        Label createAtLabel = new Label("创建时间");
        Label createAtValue = new Label("");
        if (ObjectUtil.isNotNull(this.getNote().getCreatedAt())) {
            createAtValue = new Label(DateUtil.format(this.getNote().getCreatedAt(), DatePattern.NORM_DATETIME_PATTERN));
        }
        Label titleLabel = new Label("标题");
        Label titleValue = new Label(this.getNote().getTitle());

        Label contentLabel = new Label("描述");
        Text contentValue = new Text(this.getNote().getDescription());
        contentValue.setWrappingWidth(600);
        contentValue.setLineSpacing(10);
        GridPane.setHalignment(authorLabel, HPos.RIGHT);
        gridPane.add(authorLabel, 0, 0);
        GridPane.setHalignment(createAtLabel, HPos.RIGHT);
        gridPane.add(createAtLabel, 0, 1);
        GridPane.setHalignment(titleLabel, HPos.RIGHT);
        gridPane.add(titleLabel, 0, 2);
        GridPane.setHalignment(contentLabel, HPos.RIGHT);
        gridPane.add(contentLabel, 0, 3);

        GridPane.setHalignment(authorValue, HPos.LEFT);
        gridPane.add(authorValue, 1, 0);
        GridPane.setHalignment(createAtValue, HPos.LEFT);
        gridPane.add(createAtValue, 1, 1);
        GridPane.setHalignment(titleValue, HPos.LEFT);
        gridPane.add(titleValue, 1, 2);
        GridPane.setHalignment(contentValue, HPos.LEFT);
        gridPane.add(contentValue, 1, 3);
        top.getChildren().addAll(imageView, gridPane);
        this.root.setTop(top);
    }
}
