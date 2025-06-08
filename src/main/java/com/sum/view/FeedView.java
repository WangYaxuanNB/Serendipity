package com.sum.view;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sum.dialog.CommentsDialog;
import com.sum.dialog.NoteDetailDialog;
import com.sum.domain.entity.Comments;
import com.sum.domain.entity.Notes;
import com.sum.domain.entity.Record;
import com.sum.service.ICommentsService;
import com.sum.service.INotesService;
import com.sum.service.IRecordService;
import com.sum.utils.ContextUtil;
import com.sum.utils.FileUtils;
import com.sum.utils.SpringContextUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
public class FeedView extends ScrollPane {

    private ICommentsService commentsService;
    private INotesService notesService;
    private FlowPane feedGrid;
    private String keyWords;
    private IRecordService recordService;

    public FeedView(String keyWords) {
        this.keyWords = keyWords;
        this.commentsService = SpringContextUtil.getBean(ICommentsService.class);
        this.notesService = SpringContextUtil.getBean(INotesService.class);
        this.recordService = SpringContextUtil.getBean(IRecordService.class);
        setFitToWidth(true);
        setFitToHeight(true);
        setStyle("-fx-background-color: #f5f5f5;");
        feedGrid = new FlowPane();
        feedGrid.setPadding(new Insets(12));
        feedGrid.setHgap(15);
        feedGrid.setVgap(15);
        feedGrid.setStyle("-fx-background-color: #f5f5f5;");
        setContent(feedGrid);
        refreshFeed();
    }

    public void refreshFeed() {
        feedGrid.getChildren().clear();
        INotesService notesService = SpringContextUtil.getBean(INotesService.class);
        LambdaQueryWrapper<Notes> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(this.getKeyWords())){
            String kw = this.getKeyWords();
            queryWrapper.like(Notes::getTitle,kw)
                    .or().like(Notes::getDescription,kw)
                    .or().like(Notes::getAuthor,kw);
        }
        queryWrapper.orderByDesc(Notes::getCreatedAt);
        List<Notes> notes = notesService.list(queryWrapper);
        for (Notes note : notes) {
            VBox noteCard = createNoteCard(note);
            feedGrid.getChildren().add(noteCard);
        }
    }

    private VBox createNoteCard(Notes note) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("note-card");
        ImageView imageView = FileUtils.createImageView(note.getImageUrl());
        imageView.setFitWidth(280);
        imageView.setFitHeight(200);
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefHeight(200);
        imageBox.setPrefWidth(300);
        imageBox.getChildren().add(imageView);
        VBox contentBox = new VBox(5);
        contentBox.setPadding(new Insets(8));
        contentBox.getStyleClass().add("note-content");
        Label titleLabel = new Label(note.getTitle());
        titleLabel.getStyleClass().add("note-title");
        titleLabel.setWrapText(true);
        VBox authorBox = new VBox(2);
        authorBox.setPadding(new Insets(4, 0, 0, 0));
        authorBox.getStyleClass().add("author-box");
        Label authorLabel = new Label(note.getAuthor());
        authorLabel.getStyleClass().add("author-name");

        //点赞
        Button likes = new Button(String.valueOf(note.getLikes()));
        likes.getStyleClass().add("icon-button");
        FontIcon likesIcon = new FontIcon("ci-favorite-filled");
        likesIcon.setIconColor(Paint.valueOf("#ff2442"));
        likes.setGraphic(likesIcon);
        likesIcon.setIconSize(16);
        likes.setCursor(Cursor.HAND);
        likes.setOnMouseClicked(event -> {
            this.getNotesService().incrementLikes(note.getId());
            long currentLikes = note.getLikes();
            note.setLikes(currentLikes + 1);
            likes.setText(String.valueOf(note.getLikes()));
            createRecords(note.getId());
        });

        //评论
        Button comments = new Button(String.valueOf(note.getCommentCount()));
        comments.getStyleClass().add("icon-button");
        FontIcon commentsIcon = new FontIcon("ci-chat-bot");
        comments.setGraphic(commentsIcon);
        commentsIcon.setIconSize(16);
        comments.setCursor(Cursor.HAND);
        HBox interactionBox = new HBox(10);
        interactionBox.setAlignment(Pos.CENTER_RIGHT);
        interactionBox.getChildren().addAll(likes, comments);
        authorBox.getChildren().addAll(authorLabel);
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.getChildren().addAll(authorBox, interactionBox);
        HBox.setHgrow(authorBox, Priority.ALWAYS);
        contentBox.getChildren().addAll(titleLabel, bottomBox);
        card.getChildren().addAll(imageBox, contentBox);
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                openNoteDetail(note,comments);
            }
        });
        return card;
    }

    public void createRecords(Long noteId){
        Record record = new Record();
        record.setNoteId(noteId);
        record.setAuthor(ContextUtil.getCurrentUser().getUsername());
        record.setType(1);
        record.setCreateTime(new Date());
        this.getRecordService().save(record);
    }

    private void openComments(Notes note, Button commentsLabel) {
        CommentsDialog commentsDialog = new CommentsDialog(note, commentsLabel);
        commentsDialog.getStage().show();
    }

    private void loadComments(Notes note, VBox commentsContainer) {
        commentsContainer.getChildren().clear();
        List<Comments> comments = this.getCommentsService().getCommentsByNoteId(note.getId());
        for (Comments comment : comments) {
            Label commentLabel = new Label(comment.getAuthor() + ": " + comment.getContent());
            commentLabel.setPadding(new Insets(5));
            commentsContainer.getChildren().add(commentLabel);
        }
    }

    private void openNoteDetail(Notes note,Button comments) {
        NoteDetailDialog noteDetailDialog = new NoteDetailDialog(note,comments);
        noteDetailDialog.getNoteInfoStage().show();
    }

    private void handleSubmitComment(Notes note, TextField commentInput, VBox commentsContainer, Label commentCountLabel) {
        String commentText = commentInput.getText().trim();
        if (commentText.isEmpty()) {
            return;
        }
        String authorName = "示例用户";
        Comments newComment = new Comments();
        newComment.setNoteId(note.getId());
        newComment.setAuthor(authorName);
        newComment.setContent(commentText);
        newComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        this.getCommentsService().createComment(newComment);
        this.getNotesService().incrementCommentCount(note.getId());
        note.setCommentCount(note.getCommentCount() + 1);
        commentCountLabel.setText(String.valueOf(note.getCommentCount()));
        loadComments(note, commentsContainer);
        commentInput.clear();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ScrollPane getView() {
        return this;
    }
}
