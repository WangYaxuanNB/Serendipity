package com.sum.view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.sum.dao.impl.NoteDaoImpl;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import com.sum.model.Comment;
import com.sum.model.Note;
import com.sum.service.CommentService;
import com.sum.service.NoteService;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

public class FeedView extends ScrollPane {

    private CommentService commentService = new CommentService(); // Initialize service
    private NoteService noteService = new NoteService(); // Initialize service
    private NoteDaoImpl noteDao = new NoteDaoImpl();
    private FlowPane feedGrid;

    public FeedView() {
        // Setup ScrollPane
        setFitToWidth(true);
        setFitToHeight(true); // Adjust as needed
        setStyle("-fx-background-color: #f5f5f5;");

        // Setup FlowPane for notes grid
        feedGrid = new FlowPane();
        feedGrid.setPadding(new Insets(12));
        feedGrid.setHgap(15);
        feedGrid.setVgap(15);
        feedGrid.setStyle("-fx-background-color: #f5f5f5;");

        setContent(feedGrid);

        // Initial load of notes
        refreshFeed();
    }

    public void refreshFeed() {
        feedGrid.getChildren().clear(); // Clear existing notes
        List<Note> notes =noteDao.getAll();
        for (Note note : notes) {
            VBox noteCard = createNoteCard(note);
            feedGrid.getChildren().add(noteCard);
        }
    }

    private VBox createNoteCard(Note note) {
        VBox card = new VBox();
        card.getStyleClass().add("note-card");

        // Image
        ImageView imageView = new ImageView();
        String imageUrl = note.getImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
             // Use a placeholder or handle error if image loading fails
            try {
                Image image = new Image(imageUrl, true);
                image.errorProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        System.err.println("Failed to load image: " + imageUrl);
                         imageView.setImage(new Image("https://via.placeholder.com/220x150?text=Load+Error"));
                         imageView.setFitWidth(220);
                         imageView.setPreserveRatio(true);
                    }
                });
                imageView.setImage(image);
                imageView.setFitWidth(220); // Adjust based on card width
                imageView.setPreserveRatio(true);
                 imageView.getStyleClass().add("note-image");
            } catch (IllegalArgumentException e) {
                 // Use a placeholder image
                 imageView.setImage(new Image("https://via.placeholder.com/220x150?text=Invalid+URL"));
                 imageView.setFitWidth(220);
                 imageView.setPreserveRatio(true);
            }
        } else {
             // Use a placeholder image if no URL
             imageView.setImage(new Image("https://via.placeholder.com/220x150?text=No+Image"));
             imageView.setFitWidth(220);
             imageView.setPreserveRatio(true);
        }

        // Content VBox
        VBox contentBox = new VBox(5);
        contentBox.setPadding(new Insets(8));
        contentBox.getStyleClass().add("note-content");

        // Title
        Label titleLabel = new Label(note.getTitle());
        titleLabel.getStyleClass().add("note-title");
        titleLabel.setWrapText(true);

        // Author and Interaction
        VBox authorBox = new VBox(2);
        authorBox.setPadding(new Insets(4, 0, 0, 0));
        authorBox.getStyleClass().add("author-box");

        Label authorLabel = new Label(note.getAuthor());
        authorLabel.getStyleClass().add("author-name");

        // Likes (Make it clickable)
        Label likesLabel = new Label("❤ " + note.getLikes());
        likesLabel.getStyleClass().add("icon-button");
        likesLabel.setCursor(Cursor.HAND);

        likesLabel.setOnMouseClicked(event -> {
            try {
                noteService.incrementLikes(note.getId());
                int currentLikes = note.getLikes();
                note.setLikes(currentLikes + 1);
                likesLabel.setText("❤ " + note.getLikes());

            } catch (SQLException e) {
                e.printStackTrace();
                showError("点赞失败", "无法更新点赞数：" + e.getMessage());
            }
        });

        // Comments (Placeholder for now, will be integrated with CommentView)
        Label commentsLabel = new Label("💬 " + note.getCommentCount());
        commentsLabel.getStyleClass().add("icon-button");
        commentsLabel.setCursor(Cursor.HAND);

        // Interaction Box (aligned to the right)
        HBox interactionBox = new HBox(10);
        interactionBox.setAlignment(Pos.CENTER_RIGHT);
        interactionBox.getChildren().addAll(likesLabel, commentsLabel);

        authorBox.getChildren().addAll(authorLabel);

        // Combine author info and interaction buttons in a single HBox for horizontal alignment
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.getChildren().addAll(authorBox, interactionBox);
        HBox.setHgrow(authorBox, Priority.ALWAYS);

        contentBox.getChildren().addAll(titleLabel, bottomBox);

        card.getChildren().addAll(imageView, contentBox);

        card.setOnMouseClicked(event -> openNoteDetail(note));

        return card;
    }

    private void loadComments(Note note, VBox commentsContainer) {
        commentsContainer.getChildren().clear();
        try {
            List<Comment> comments = commentService.getCommentsByNoteId(note.getId());
            for (Comment comment : comments) {
                Label commentLabel = new Label(comment.getAuthor() + ": " + comment.getContent());
                commentLabel.setPadding(new Insets(5));
                commentsContainer.getChildren().add(commentLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("加载评论失败", "无法从数据库获取评论：" + e.getMessage());
        }
    }

    private void openNoteDetail(Note note) {
        System.out.println("Opening detail for note: " + note.getTitle());
    }

    private void handleSubmitComment(Note note, TextField commentInput, VBox commentsContainer, Label commentCountLabel) {
        String commentText = commentInput.getText().trim();
        if (commentText.isEmpty()) {
            return;
        }

        String authorName = "示例用户";

        Comment newComment = new Comment();
        newComment.setNoteId(note.getId());
        newComment.setAuthor(authorName);
        newComment.setContent(commentText);
        newComment.setCreateTime(new Timestamp(System.currentTimeMillis()));

        try {
            commentService.createComment(newComment);
            noteService.incrementCommentCount(note.getId());
            note.setCommentCount(note.getCommentCount() + 1);
            commentCountLabel.setText(String.valueOf(note.getCommentCount()));

            loadComments(note, commentsContainer);

            commentInput.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            showError("发布评论失败", "无法保存评论：" + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public FlowPane getFeedGrid() {
        return feedGrid;
    }

    public ScrollPane getView() {
        return this;
    }
}
