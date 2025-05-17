package com.sum;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 笔记详情视图
 * 展示单个笔记的详细内容和评论
 */
public class NoteDetailView {
    private ScrollPane scrollPane;
    private VBox detailContainer;
    private Random random = new Random();
    
    /**
     * 构造函数
     */
    public NoteDetailView() {
        initializeUI();
    }
    
    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        // 创建滚动面板
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: transparent;");
        
        // 创建主容器
        detailContainer = new VBox();
        detailContainer.getStyleClass().add("note-detail-container");
        
        // 创建示例笔记数据
        Note note = new Note(
            "宁波探店｜隐藏在街角的治愈系咖啡馆", 
            "这家街角的小店真的太温馨了，店主是个超爱笑的姐姐，墙上的复古装饰也很有情调，推荐奶油草莓配方，绝对值得一试～\n\n" +
            "🌟推荐指数：⭐⭐⭐⭐⭐\n" +
            "🍰人均消费：58元\n" +
            "📍具体位置：宁波市江北区中山东路258号\n\n" +
            "☕必点单品：\n" +
            "• 奶油草莓拿铁：新鲜草莓搭配自制奶油，甜度适中\n" +
            "• 抹茶舒芙蕾：入口即化，抹茶味浓郁不苦涩\n" +
            "• 肉桂卷：外酥里软，满满的肉桂香\n\n" +
            "🧚‍♀️小贴士：周末人比较多，建议提前预约座位～",
            "咖啡爱好者", 
            "https://picsum.photos/seed/coffee/600/800", 
            "https://randomuser.me/api/portraits/women/44.jpg",
            random.nextInt(100) + 500, 
            random.nextInt(20) + 50,
            800);
            
        // 添加导航栏
        HBox navbar = createNavbar();
        
        // 添加笔记内容区域
        VBox noteContent = createNoteContent(note);
        
        // 添加用户互动区域
        HBox interactionBar = createInteractionBar(note);
        
        // 添加分隔线
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #f0f0f0;");
        
        // 添加推荐笔记区域
        VBox recommendedNotes = createRecommendedNotes();
        
        // 添加评论区域
        VBox commentSection = createCommentSection();
        
        // 添加到主容器
        detailContainer.getChildren().addAll(
            navbar,
            noteContent,
            interactionBar,
            separator,
            recommendedNotes,
            commentSection
        );
        
        scrollPane.setContent(detailContainer);
    }
    
    /**
     * 创建导航栏
     * @return 导航栏组件
     */
    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(10, 5, 10, 5));
        navbar.setAlignment(Pos.CENTER_LEFT);
        
        // 返回按钮
        Button backButton = new Button("←");
        backButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #333;"
        );
        
        // 标题
        Label titleLabel = new Label("笔记详情");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        HBox.setMargin(titleLabel, new Insets(0, 0, 0, 10));
        
        // 右侧按钮容器
        HBox rightButtons = new HBox(10);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);
        
        // 分享按钮
        Button shareButton = new Button("分享");
        shareButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #333;"
        );
        
        // 更多按钮
        Button moreButton = new Button("...");
        moreButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #333;" +
            "-fx-font-weight: bold;"
        );
        
        rightButtons.getChildren().addAll(shareButton, moreButton);
        HBox.setHgrow(rightButtons, Priority.ALWAYS);
        
        navbar.getChildren().addAll(backButton, titleLabel, rightButtons);
        return navbar;
    }
    
    /**
     * 创建笔记内容区域
     * @param note 笔记数据
     * @return 笔记内容组件
     */
    private VBox createNoteContent(Note note) {
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(15, 0, 25, 0));
        
        // 作者信息区域
        HBox authorBox = new HBox(12);
        authorBox.setAlignment(Pos.CENTER_LEFT);
        
        // 作者头像
        ImageView avatarView = new ImageView(new Image(note.getAuthorAvatarUrl()));
        avatarView.setFitWidth(44);
        avatarView.setFitHeight(44);
        
        // 设置圆形头像
        Circle clip = new Circle(22, 22, 22);
        avatarView.setClip(clip);
        
        // 作者信息
        VBox authorInfo = new VBox(4);
        Label authorName = new Label(note.getAuthor());
        authorName.setFont(Font.font("System", FontWeight.BOLD, 15));
        
        Label postDate = new Label("3天前");
        postDate.setStyle("-fx-text-fill: #999; -fx-font-size: 13px;");
        
        authorInfo.getChildren().addAll(authorName, postDate);
        
        // 关注按钮
        Button followButton = new Button("+ 关注");
        followButton.getStyleClass().add("follow-button");
        followButton.setStyle("-fx-font-size: 13px; -fx-padding: 6 15;");
        
        HBox.setHgrow(authorInfo, Priority.ALWAYS);
        authorBox.getChildren().addAll(avatarView, authorInfo, followButton);
        
        // 标题
        Label titleLabel = new Label(note.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-line-spacing: 1.3;");
        
        // 图片
        ImageView mainImage = new ImageView(new Image(note.getImageUrl()));
        mainImage.setFitWidth(600);
        mainImage.setPreserveRatio(true);
        mainImage.setStyle("-fx-background-radius: 12px;");
        
        // 内容文本
        Label contentLabel = new Label(note.getDescription());
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 15px; -fx-line-spacing: 1.5; -fx-text-fill: #333;");
        
        // 位置信息
        HBox locationBox = new HBox(6);
        locationBox.setAlignment(Pos.CENTER_LEFT);
        locationBox.setStyle("-fx-background-color: #f7f7f7; -fx-padding: 8 12; -fx-background-radius: 6px;");
        
        Label locationIcon = new Label("📍");
        Label locationText = new Label("宁波市江北区");
        locationText.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
        
        locationBox.getChildren().addAll(locationIcon, locationText);
        
        // 添加到内容区域
        contentBox.getChildren().addAll(
            authorBox,
            titleLabel,
            mainImage,
            contentLabel,
            locationBox
        );
        
        return contentBox;
    }
    
    /**
     * 创建用户互动栏
     * @param note 笔记数据
     * @return 互动栏组件
     */
    private HBox createInteractionBar(Note note) {
        HBox interactionBar = new HBox(30);
        interactionBar.setPadding(new Insets(15, 10, 20, 10));
        interactionBar.setAlignment(Pos.CENTER_LEFT);
        
        // 点赞按钮
        VBox likeBox = new VBox(6);
        likeBox.setAlignment(Pos.CENTER);
        
        Label likeIcon = new Label("♡");
        likeIcon.setStyle("-fx-font-size: 26px; -fx-text-fill: #ff2442;");
        
        Label likeCount = new Label(String.valueOf(note.getLikes()));
        likeCount.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
        
        likeBox.getChildren().addAll(likeIcon, likeCount);
        
        // 收藏按钮
        VBox favoriteBox = new VBox(6);
        favoriteBox.setAlignment(Pos.CENTER);
        
        Label favoriteIcon = new Label("⭐");
        favoriteIcon.setStyle("-fx-font-size: 22px;");
        
        Label favoriteLabel = new Label("收藏");
        favoriteLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
        
        favoriteBox.getChildren().addAll(favoriteIcon, favoriteLabel);
        
        // 评论按钮
        VBox commentBox = new VBox(6);
        commentBox.setAlignment(Pos.CENTER);
        
        Label commentIcon = new Label("💬");
        commentIcon.setStyle("-fx-font-size: 22px;");
        
        Label commentCount = new Label(String.valueOf(note.getComments()));
        commentCount.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
        
        commentBox.getChildren().addAll(commentIcon, commentCount);
        
        // 分享按钮
        VBox shareBox = new VBox(6);
        shareBox.setAlignment(Pos.CENTER);
        
        Label shareIcon = new Label("↗");
        shareIcon.setStyle("-fx-font-size: 22px;");
        
        Label shareLabel = new Label("分享");
        shareLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
        
        shareBox.getChildren().addAll(shareIcon, shareLabel);
        
        interactionBar.getChildren().addAll(likeBox, favoriteBox, commentBox, shareBox);
        
        return interactionBar;
    }
    
    /**
     * 创建推荐笔记区域
     * @return 推荐笔记组件
     */
    private VBox createRecommendedNotes() {
        VBox recommendedBox = new VBox(10);
        recommendedBox.setPadding(new Insets(15, 0, 15, 0));
        
        // 标题
        Label titleLabel = new Label("相关推荐");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        // 推荐笔记滚动区域
        HBox scrollBox = new HBox(10);
        scrollBox.setPadding(new Insets(10, 0, 10, 0));
        
        // 添加3个推荐笔记预览
        for (int i = 1; i <= 3; i++) {
            VBox noteCard = new VBox(5);
            noteCard.setPrefWidth(150);
            
            ImageView imgView = new ImageView(new Image("https://picsum.photos/seed/rec" + i + "/150/150"));
            imgView.setFitWidth(150);
            imgView.setFitHeight(150);
            
            Label noteTitle = new Label("推荐笔记 " + i);
            noteTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            noteTitle.setWrapText(true);
            
            noteCard.getChildren().addAll(imgView, noteTitle);
            scrollBox.getChildren().add(noteCard);
        }
        
        recommendedBox.getChildren().addAll(titleLabel, scrollBox);
        return recommendedBox;
    }
    
    /**
     * 创建评论区域
     * @return 评论区组件
     */
    private VBox createCommentSection() {
        VBox commentBox = new VBox(15);
        commentBox.setPadding(new Insets(15, 0, 30, 0));
        
        // 评论区标题
        HBox headerBox = new HBox();
        Label titleLabel = new Label("评论");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        Label countLabel = new Label("(56)");
        countLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 14px;");
        HBox.setMargin(countLabel, new Insets(0, 0, 0, 5));
        
        headerBox.getChildren().addAll(titleLabel, countLabel);
        
        // 评论输入区域
        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(10, 0, 10, 0));
        inputBox.setAlignment(Pos.CENTER_LEFT);
        
        // 当前用户头像
        ImageView currentAvatar = new ImageView(new Image("https://randomuser.me/api/portraits/women/22.jpg"));
        currentAvatar.setFitWidth(32);
        currentAvatar.setFitHeight(32);
        
        // 设置圆形头像
        Circle clipInput = new Circle(16, 16, 16);
        currentAvatar.setClip(clipInput);
        
        // 评论输入框
        TextField commentInput = new TextField();
        commentInput.setPromptText("说点什么...");
        commentInput.getStyleClass().add("text-field");
        HBox.setHgrow(commentInput, Priority.ALWAYS);
        
        inputBox.getChildren().addAll(currentAvatar, commentInput);
        
        // 评论列表
        VBox commentListBox = new VBox();
        commentListBox.getStyleClass().add("comment-section");
        
        // 添加示例评论
        List<Comment> comments = getSampleComments();
        for (Comment comment : comments) {
            HBox commentItem = createCommentItem(comment);
            commentListBox.getChildren().add(commentItem);
        }
        
        commentBox.getChildren().addAll(headerBox, inputBox, commentListBox);
        return commentBox;
    }
    
    /**
     * 创建单个评论项
     * @param comment 评论数据
     * @return 评论项组件
     */
    private HBox createCommentItem(Comment comment) {
        HBox commentItem = new HBox(10);
        commentItem.getStyleClass().add("comment-item");
        
        // 评论者头像
        ImageView avatarView = new ImageView(new Image(comment.getAvatarUrl()));
        avatarView.setFitWidth(36);
        avatarView.setFitHeight(36);
        
        // 设置圆形头像
        Circle clip = new Circle(18, 18, 18);
        avatarView.setClip(clip);
        
        // 评论内容区域
        VBox contentBox = new VBox(5);
        HBox.setHgrow(contentBox, Priority.ALWAYS);
        
        // 评论者名称
        Label nameLabel = new Label(comment.getUsername());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        
        // 评论内容
        Label textLabel = new Label(comment.getText());
        textLabel.setWrapText(true);
        textLabel.setStyle("-fx-font-size: 13px;");
        
        // 评论底部信息
        HBox footerBox = new HBox(15);
        footerBox.setAlignment(Pos.CENTER_LEFT);
        
        // 发布时间
        Label timeLabel = new Label(comment.getTime());
        timeLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 12px;");
        
        // 点赞
        HBox likeBox = new HBox(5);
        likeBox.setAlignment(Pos.CENTER_LEFT);
        
        Label likeIcon = new Label("♡");
        likeIcon.setStyle("-fx-text-fill: #999; -fx-font-size: 12px;");
        
        Label likeCount = new Label(String.valueOf(comment.getLikes()));
        likeCount.setStyle("-fx-text-fill: #999; -fx-font-size: 12px;");
        
        likeBox.getChildren().addAll(likeIcon, likeCount);
        
        // 回复按钮
        Label replyBtn = new Label("回复");
        replyBtn.setStyle("-fx-text-fill: #999; -fx-font-size: 12px;");
        
        footerBox.getChildren().addAll(timeLabel, likeBox, replyBtn);
        
        contentBox.getChildren().addAll(nameLabel, textLabel, footerBox);
        
        commentItem.getChildren().addAll(avatarView, contentBox);
        
        return commentItem;
    }
    
    /**
     * 获取示例评论数据
     * @return 评论列表
     */
    private List<Comment> getSampleComments() {
        List<Comment> comments = new ArrayList<>();
        
        comments.add(new Comment(
            "小甜甜",
            "https://randomuser.me/api/portraits/women/33.jpg",
            "哇这家店我也去过！奶油草莓拿铁真的太好喝了，而且店主人特别好，给我们额外赠送了小饼干～",
            "2天前",
            23
        ));
        
        comments.add(new Comment(
            "咖啡控",
            "https://randomuser.me/api/portraits/men/42.jpg",
            "看起来真不错！周末去打卡，顺便问下博主，这家店需要提前预约吗？",
            "1天前",
            8
        ));
        
        comments.add(new Comment(
            "宁波吃货",
            "https://randomuser.me/api/portraits/women/68.jpg",
            "已经收藏了，感谢博主分享～想问一下肉桂卷会很甜吗？",
            "12小时前",
            5
        ));
        
        return comments;
    }
    
    /**
     * 获取笔记详情视图
     * @return 详情视图
     */
    public ScrollPane getNoteDetailView() {
        return scrollPane;
    }
}

/**
 * 评论数据模型类
 */
class Comment {
    private String username;  // 用户名
    private String avatarUrl; // 头像URL
    private String text;      // 评论内容
    private String time;      // 发布时间
    private int likes;        // 点赞数
    
    public Comment(String username, String avatarUrl, String text, String time, int likes) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.text = text;
        this.time = time;
        this.likes = likes;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getText() { return text; }
    public String getTime() { return time; }
    public int getLikes() { return likes; }
} 