package com.sum.ui.comment;

import com.sum.model.Comment;
import com.sum.service.CommentService;
import com.sum.service.NoteService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Pos;
import java.sql.SQLException;

public class CommentView extends VBox {
    private final int noteId;
    private final CommentService commentService;
    private final NoteService noteService;
    private final VBox commentsContainer;
    private final TextArea commentInput;
    private final Button submitButton;
    private final Label commentCountLabel;

    public CommentView(int noteId) {
        this.noteId = noteId;
        this.commentService = new CommentService();
        this.noteService = new NoteService();
        
        setSpacing(15);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");
        
        // 评论数量显示
        int commentCount = 0;
        try {
            commentCount = noteService.getCommentCount(noteId);
        } catch (SQLException e) {
            showError("加载评论数量失败", e.getMessage());
        }
        commentCountLabel = new Label("评论 (" + commentCount + ")");
        commentCountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        getChildren().add(commentCountLabel);
        
        // 评论输入区域
        VBox inputBox = new VBox(10);
        inputBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-border-radius: 8;");
        
        commentInput = new TextArea();
        commentInput.setPromptText("写下你的评论...");
        commentInput.setPrefRowCount(3);
        commentInput.setWrapText(true);
        commentInput.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 4;");
        
        submitButton = new Button("发送评论");
        submitButton.setStyle("-fx-background-color: #1a73e8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 4;");
        submitButton.setOnAction(e -> handleSubmitComment());
        
        inputBox.getChildren().addAll(commentInput, submitButton);
        getChildren().add(inputBox);
        
        // 评论列表容器
        commentsContainer = new VBox(15);
        getChildren().add(commentsContainer);
        
        // 加载评论
        loadComments();
    }
    
    private void loadComments() {
        commentsContainer.getChildren().clear();
        try {
            List<Comment> comments = commentService.getCommentsByNoteId(noteId);
            for (Comment comment : comments) {
                commentsContainer.getChildren().add(createCommentCard(comment));
            }
        } catch (SQLException e) {
            showError("加载评论失败", e.getMessage());
        }
    }
    
    private VBox createCommentCard(Comment comment) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10, 0, 10, 0));
        card.getStyleClass().add("comment-item");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getStyleClass().add("comment-header");
        HBox.setHgrow(headerBox, Priority.ALWAYS);

        ImageView avatarView;
        String authorAvatarUrl = comment.getAuthorAvatar();
        if (authorAvatarUrl != null && !authorAvatarUrl.trim().isEmpty()) {
            try {
                avatarView = new ImageView(new Image(authorAvatarUrl));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid comment author avatar URL: " + authorAvatarUrl);
                avatarView = new ImageView(new Image("https://via.placeholder.com/32x32?text=Avatar"));
            }
        } else {
            avatarView = new ImageView(new Image("https://via.placeholder.com/32x32?text=Avatar"));
        }
        avatarView.setFitWidth(32);
        avatarView.setFitHeight(32);
        avatarView.getStyleClass().add("comment-avatar");

        VBox authorInfo = new VBox(2);
        authorInfo.getStyleClass().add("author-info");

        Label authorLabel = new Label(comment.getAuthor());
        authorLabel.getStyleClass().add("author-name");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Label timeLabel = new Label(comment.getCreateTime().toLocalDateTime().format(formatter));
        timeLabel.getStyleClass().add("comment-time");

        authorInfo.getChildren().addAll(authorLabel, timeLabel);

        Label contentLabel = new Label(comment.getContent());
        contentLabel.setWrapText(true);
        contentLabel.getStyleClass().add("comment-content");

        HBox interactionBox = new HBox(10);
        interactionBox.setAlignment(Pos.CENTER_RIGHT);
        interactionBox.getStyleClass().add("comment-interaction");
        HBox.setHgrow(interactionBox, Priority.ALWAYS);

        Button likeButton = new Button("❤ " + comment.getLikes());
        likeButton.getStyleClass().addAll("icon-button", "like-button");
        likeButton.setOnAction(e -> {
            try {
                commentService.incrementLikes(comment.getId());
                loadComments();
            } catch (SQLException ex) {
                showError("点赞失败", ex.getMessage());
            }
        });

        Button deleteButton = new Button("删除");
        deleteButton.getStyleClass().addAll("icon-button", "delete-button");
        deleteButton.setOnAction(e -> handleDeleteComment(comment));

        headerBox.getChildren().addAll(avatarView, authorInfo);
        interactionBox.getChildren().addAll(likeButton, deleteButton);

        HBox contentAndInteraction = new HBox(10);
        HBox.setHgrow(contentLabel, Priority.ALWAYS);
        contentAndInteraction.getChildren().addAll(contentLabel, interactionBox);

        card.getChildren().addAll(headerBox, contentAndInteraction);
        return card;
    }
    
    private void handleDeleteComment(Comment comment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("删除评论");
        alert.setHeaderText(null);
        alert.setContentText("确定要删除这条评论吗？");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    commentService.deleteComment(comment.getId(), noteId);
                    loadComments();
                    commentCountLabel.setText("评论 (" + noteService.getCommentCount(noteId) + ")");
                } catch (SQLException e) {
                    showError("删除评论失败", e.getMessage());
                }
            }
        });
    }
    
    private void handleSubmitComment() {
        String content = commentInput.getText().trim();
        if (content.isEmpty()) {
            showAlert("提示", "请输入评论内容");
            return;
        }
        
        try {
            // 1. 获取当前用户信息 (TODO: 替换为实际的用户获取逻辑)
            String currentAuthor = "当前用户"; 
            String currentAuthorAvatar = "https://via.placeholder.com/32x32?text=User";

            // 2. 创建Comment对象并设置属性
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setAuthor(currentAuthor);
            comment.setAuthorAvatar(currentAuthorAvatar);
            comment.setNoteId(noteId);
            
            // 3. 调用CommentService保存评论
            commentService.createComment(comment);

            // 4. 更新评论计数
            noteService.incrementCommentCount(noteId);
            commentCountLabel.setText("评论 (" + noteService.getCommentCount(noteId) + ")");
            
            // 5. 清空输入框
            commentInput.clear();
            
            // 6. 刷新评论列表
            loadComments();

        } catch (SQLException e) {
            showError("发布评论失败", e.getMessage());
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 