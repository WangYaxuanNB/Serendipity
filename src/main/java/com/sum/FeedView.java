package com.sum;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import com.sum.model.Note;
import com.sum.model.Comment;
import com.sum.service.NoteService;
import com.sum.service.CommentService;
import java.sql.SQLException;
import java.util.List;

public class FeedView {
    private GridPane feedGrid;
    private NoteService noteService;
    private CommentService commentService;

    public FeedView() {
        noteService = new NoteService();
        commentService = new CommentService();
        initializeUI();
        loadNotes();
    }

    private void initializeUI() {
        feedGrid = new GridPane();
        feedGrid.getStyleClass().add("feed-grid");
        feedGrid.setHgap(20);
        feedGrid.setVgap(20);
        feedGrid.setPadding(new Insets(20));
    }

    private void loadNotes() {
        try {
            List<Note> notes = noteService.getAllNotes();
            int column = 0;
            int row = 0;
            
            for (Note note : notes) {
                VBox noteCard = createNoteCard(note);
                feedGrid.add(noteCard, column, row);
                
                column++;
                if (column > 3) { // 每行最多4个卡片
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 显示错误提示
        }
    }

    private VBox createNoteCard(Note note) {
        VBox card = new VBox();
        card.getStyleClass().add("note-card");
        
        // 图片容器
        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("note-image-container");
        
        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("note-image");
        imageView.setFitWidth(280);
        imageView.setPreserveRatio(true);
        
        try {
            Image image = new Image(note.getImageUrl());
            imageView.setImage(image);
        } catch (Exception e) {
            // 如果图片加载失败，显示默认图片
            imageView.setImage(new Image(getClass().getResourceAsStream("/default-note.png")));
        }
        
        imageContainer.getChildren().add(imageView);
        
        // 内容容器
        VBox contentBox = new VBox(8);
        contentBox.getStyleClass().add("note-content");
        contentBox.setPadding(new Insets(12));
        
        // 标题
        Label titleLabel = new Label(note.getTitle());
        titleLabel.getStyleClass().add("note-title");
        titleLabel.setWrapText(true);
        
        // 描述
        Text description = new Text(note.getDescription());
        description.getStyleClass().add("note-description");
        TextFlow descriptionFlow = new TextFlow(description);
        descriptionFlow.setMaxWidth(260);
        
        // 作者信息和互动区域
        HBox interactionBox = new HBox(10);
        interactionBox.getStyleClass().add("interaction-box");
        
        // 作者信息
        HBox authorBox = new HBox(8);
        authorBox.getStyleClass().add("author-box");
        
        ImageView avatarView = new ImageView();
        avatarView.getStyleClass().add("author-avatar");
        avatarView.setFitWidth(24);
        avatarView.setFitHeight(24);
        
        try {
            Image avatar = new Image(note.getAuthorAvatarUrl());
            avatarView.setImage(avatar);
        } catch (Exception e) {
            // 如果头像加载失败，显示默认头像
            avatarView.setImage(new Image(getClass().getResourceAsStream("/default-avatar.png")));
        }
        
        Label authorLabel = new Label(note.getAuthor());
        authorLabel.getStyleClass().add("author-name");
        
        authorBox.getChildren().addAll(avatarView, authorLabel);
        
        // 点赞和评论按钮
        Button likeButton = new Button("❤ " + note.getLikes());
        likeButton.getStyleClass().addAll("icon-button", "like-button");
        
        Button commentButton = new Button("💬 " + note.getCommentCount());
        commentButton.getStyleClass().addAll("icon-button", "comment-button");
        
        // 点击评论按钮显示评论对话框
        commentButton.setOnAction(e -> showComments(note));
        
        interactionBox.getChildren().addAll(authorBox, likeButton, commentButton);
        HBox.setHgrow(authorBox, Priority.ALWAYS);
        
        // 将所有元素添加到卡片中
        contentBox.getChildren().addAll(titleLabel, descriptionFlow, interactionBox);
        card.getChildren().addAll(imageContainer, contentBox);
        
        // 点击卡片显示详情
        card.setOnMouseClicked(e -> showNoteDetail(note));
        
        return card;
    }

    private void showComments(Note note) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("评论");
        dialog.setHeaderText(note.getTitle());
        
        // 创建评论列表
        VBox commentList = new VBox(10);
        commentList.setPadding(new Insets(10));
        
        try {
            List<Comment> comments = commentService.getCommentsByNoteId(note.getId());
            for (Comment comment : comments) {
                commentList.getChildren().add(createCommentItem(comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 创建评论输入区域
        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(10));
        
        TextArea commentInput = new TextArea();
        commentInput.setPromptText("写下你的评论...");
        commentInput.setPrefRowCount(2);
        commentInput.setWrapText(true);
        
        Button submitButton = new Button("发送");
        submitButton.getStyleClass().add("send-comment-button");
        
        submitButton.setOnAction(e -> {
            String content = commentInput.getText().trim();
            if (!content.isEmpty()) {
                try {
                    Comment newComment = new Comment();
                    newComment.setContent(content);
                    newComment.setAuthor("当前用户"); // TODO: 使用实际的用户信息
                    newComment.setNoteId(note.getId());
                    
                    commentService.createComment(newComment);
                    commentService.incrementNoteCommentCount(note.getId());
                    
                    // 刷新评论列表
                    commentList.getChildren().add(createCommentItem(newComment));
                    commentInput.clear();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        inputBox.getChildren().addAll(commentInput, submitButton);
        HBox.setHgrow(commentInput, Priority.ALWAYS);
        
        // 创建滚动面板
        ScrollPane scrollPane = new ScrollPane(commentList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        // 设置对话框内容
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(scrollPane, inputBox);
        
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.show();
    }

    private VBox createCommentItem(Comment comment) {
        VBox commentItem = new VBox(8);
        commentItem.getStyleClass().add("comment-item");
        
        // 评论头部（作者信息）
        HBox header = new HBox(8);
        header.getStyleClass().add("comment-header");
        
        ImageView avatarView = new ImageView();
        avatarView.getStyleClass().add("comment-avatar");
        avatarView.setFitWidth(32);
        avatarView.setFitHeight(32);
        
        try {
            Image avatar = new Image(comment.getAuthorAvatar());
            avatarView.setImage(avatar);
        } catch (Exception e) {
            avatarView.setImage(new Image(getClass().getResourceAsStream("/default-avatar.png")));
        }
        
        VBox authorInfo = new VBox(2);
        authorInfo.getStyleClass().add("author-info");
        
        Label authorLabel = new Label(comment.getAuthor());
        authorLabel.getStyleClass().add("author-name");
        
        Label timeLabel = new Label(comment.getCreateTime().toString());
        timeLabel.getStyleClass().add("comment-time");
        
        authorInfo.getChildren().addAll(authorLabel, timeLabel);
        
        header.getChildren().addAll(avatarView, authorInfo);
        
        // 评论内容
        Text content = new Text(comment.getContent());
        content.getStyleClass().add("comment-content");
        TextFlow contentFlow = new TextFlow(content);
        
        // 评论互动区域
        HBox interaction = new HBox(10);
        interaction.getStyleClass().add("comment-interaction");
        
        Button likeButton = new Button("❤ " + comment.getLikes());
        likeButton.getStyleClass().addAll("icon-button", "like-button");
        
        Button deleteButton = new Button("🗑");
        deleteButton.getStyleClass().addAll("icon-button", "delete-button");
        
        interaction.getChildren().addAll(likeButton, deleteButton);
        
        commentItem.getChildren().addAll(header, contentFlow, interaction);
        
        return commentItem;
    }

    private void showNoteDetail(Note note) {
        // TODO: 实现笔记详情页面
    }

    public VBox getView() {
        return new VBox(feedGrid);
    }
} 