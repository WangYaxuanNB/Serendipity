package com.sum.dialog;

import com.sum.annotations.ViewHeader;
import com.sum.domain.entity.Comments;
import com.sum.domain.entity.Notes;
import com.sum.service.ICommentsService;
import com.sum.service.INotesService;
import com.sum.utils.ContextUtil;
import com.sum.utils.ControlUtil;
import com.sum.utils.SpringContextUtil;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 笔记详情弹框
 */
@Getter
public class CommentsDialog {

    private Stage stage;
    private BorderPane root;
    private Notes note;
    private ICommentsService commentsService;
    private INotesService notesService;


    public CommentsDialog(Notes note, Button commentsLabel) {
        this.commentsService = SpringContextUtil.getBean(ICommentsService.class);
        this.notesService = SpringContextUtil.getBean(INotesService.class);
        this.note = note;
        this.stage = new Stage();
        this.root = new BorderPane();
        createCenter();
        createBottom(commentsLabel);
        this.root.getStylesheets().add("css/main.css");
        //设置模式:没关闭当前窗口，不能进行其他成操作
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 600, 500, Color.WHITE);
        stage.setTitle("评论列表");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private void createCenter() {
        List<Comments> commentsList = new ArrayList<>();
        try {
            commentsList = this.getCommentsService().getCommentsByNoteId(this.getNote().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TableView<Comments> tableView = createTableView(new Comments(), commentsList);
        Label placeholder = new Label("当前暂无评论...");
        tableView.setPlaceholder(placeholder);
        this.getRoot().setCenter(tableView);
    }

    private void createBottom(Button commentsLabel) {
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(10));
        bottom.setAlignment(Pos.CENTER);
        bottom.setPrefWidth(700);
        bottom.setSpacing(20);
        TextField comment = new TextField();
        comment.setPromptText("请输入评论");
        comment.getStyleClass().add("comment-text-field");
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
            comment.setText("");
            ControlUtil.success("评论成功");
            commentsLabel.setText(String.valueOf(this.getNotesService().getCommentCount(note.getId())));
            this.createCenter();
        });
    }

    public TableView<Comments> createTableView(Comments comment, List<Comments> dataList) {
        TableView<Comments> tableView = new TableView<>();
        tableView.setItems(FXCollections.observableArrayList(dataList));
        Field[] fields = comment.getClass().getDeclaredFields();
        for (Field field : fields) {
            ViewHeader viewHeader = field.getAnnotation(ViewHeader.class);
            if (null != viewHeader) {
                TableColumn<Comments, String> column = new TableColumn<>(viewHeader.value());
                tableView.setRowFactory(tv -> {
                    TableRow<Comments> row = new TableRow<>();
                    row.setPrefHeight(80);
                    return row;
                });
                if (StringUtils.equals(field.getName(), "content")) {
                    column.setPrefWidth(300);
                } else {
                    column.setPrefWidth(140);
                }
                column.setStyle("-fx-wrap-text:true;cursor:hand;");
                column.setSortable(false);
                column.setResizable(false);
                column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
                tableView.getColumns().add(column);
            }
        }
        return tableView;
    }
}
