package com.sum;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sum.dao.community_interactions.queryAllInteractions;

/**
 * 首页推荐视图，展示笔记瀑布流布局
 */
public class FeedView {
    private GridPane feedGrid;
    private Random random = new Random();
    private double startY; // 用于记录鼠标按下时的Y坐标

    public FeedView() {
        initializeUI();
    }

    /**
     * 初始化UI组件
     */
    private void initializeUI() {
        // 整体容器和滚动区域：整个笔记墙就是放在ScrollPane里
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-border-color: transparent;");

        // 添加鼠标按下事件监听器
        scrollPane.setOnMousePressed(event -> {
            startY = event.getScreenY();
        });

        // 添加鼠标释放事件监听器
        scrollPane.setOnMouseReleased(event -> {
            double deltaY = event.getScreenY() - startY;
            if (deltaY > 50) { // 如果下拉距离超过50像素，触发刷新
                refreshFeed();
            }
        });

        // 瀑布流两列布局（GridPane）
        feedGrid = new GridPane();
        feedGrid.getStyleClass().add("feed-grid");

        // 设置两列瀑布流
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        feedGrid.getColumnConstraints().addAll(column1, column2);
        feedGrid.setVgap(15);
        feedGrid.setHgap(15);
        feedGrid.setPadding(new Insets(12));

        // 添加示例笔记数据
        List<Note> notes = getSampleNotes();
        populateGridWithNotes(notes);

        scrollPane.setContent(feedGrid);
    }

    /**
     * 刷新笔记瀑布流
     */
    private void refreshFeed() {
        // 清空当前的笔记列表
        feedGrid.getChildren().clear();

        // 获取新的笔记数据并重新填充
        List<Note> notes = getSampleNotes();
        populateGridWithNotes(notes);
    }

    /**
     * 将笔记填充到瀑布流网格中
     * @param notes 笔记列表
     */
    private void populateGridWithNotes(List<Note> notes) {
        int row = 0, col = 0;
        int[] columnHeights = {0, 0}; // 跟踪每列的当前高度

        for (Note note : notes) {
            // 根据列高度决定放入哪一列
            col = (columnHeights[0] <= columnHeights[1]) ? 0 : 1;

            // 创建笔记卡片
            VBox noteCard = createNoteCard(note);

            // 添加到瀑布流布局
            feedGrid.add(noteCard, col, row);

            // 更新该列的高度 (根据内容高度和图片高度计算)
            columnHeights[col] += note.getImageHeight() + 100; // 100是内容区域的近似高度

            // 如果需要新行
            if (col == 1) row++;
        }
    }

    /**
     * 创建一个笔记卡片
     * @param note 笔记数据
     * @return 卡片UI组件
     */
    private VBox createNoteCard(Note note) {
        VBox noteCard = new VBox();
        noteCard.getStyleClass().add("note-card");

        // 笔记图片容器
        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("note-image-container");

        // 笔记图片
        String imageUrl = note.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ImageView imageView = new ImageView(new Image(imageUrl));
            imageView.setFitWidth(240);
            imageView.setFitHeight(note.getImageHeight());
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        } else {
            // 如果图片URL为空，使用默认图片
            ImageView defaultImageView = new ImageView(new Image("https://picsum.photos/seed/fashion/400/350"));
            defaultImageView.setFitWidth(240);
            defaultImageView.setFitHeight(note.getImageHeight());
            defaultImageView.setPreserveRatio(true);
            imageContainer.getChildren().add(defaultImageView);
        }

        // 笔记内容区域
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("note-content");

        // 笔记标题
        Label titleLabel = new Label(note.getTitle());
        titleLabel.getStyleClass().add("note-title");

        // 笔记描述
        Label descLabel = new Label(note.getDescription());
        descLabel.getStyleClass().add("note-desc");

        // 底部信息栏 (作者信息和互动按钮)
        HBox footerBox = new HBox();
        footerBox.getStyleClass().add("note-footer");

        // 作者信息部分
        HBox authorBox = new HBox();
        authorBox.getStyleClass().add("author-container");

        // 作者头像
        String authorAvatarUrl = note.getAuthorAvatarUrl();
        if (authorAvatarUrl != null && !authorAvatarUrl.isEmpty()) {
            ImageView avatarView = new ImageView(new Image(authorAvatarUrl));
            avatarView.setFitWidth(24);
            avatarView.setFitHeight(24);

            // 设置圆形头像
            Circle clip = new Circle(12, 12, 12);
            avatarView.setClip(clip);
            avatarView.getStyleClass().add("author-avatar");

            // 作者名称
            Label authorLabel = new Label(note.getAuthor());
            authorLabel.getStyleClass().add("note-author");

            authorBox.getChildren().addAll(avatarView, authorLabel);
        } else {
            // 如果头像URL为空，使用默认头像
            ImageView defaultAvatarView = new ImageView(new Image("https://randomuser.me/api/portraits/women/55.jpg"));
            defaultAvatarView.setFitWidth(24);
            defaultAvatarView.setFitHeight(24);

            // 设置圆形头像
            Circle clip = new Circle(12, 12, 12);
            defaultAvatarView.setClip(clip);
            defaultAvatarView.getStyleClass().add("author-avatar");

            // 作者名称
            Label authorLabel = new Label(note.getAuthor());
            authorLabel.getStyleClass().add("note-author");

            authorBox.getChildren().addAll(defaultAvatarView, authorLabel);
        }

        // 点赞和评论图标
        HBox interactionBox = new HBox();
        interactionBox.getStyleClass().add("interaction-container");

        Label likeLabel = new Label("❤ " + note.getLikes());
        likeLabel.getStyleClass().add("like-icon");

        Label commentLabel = new Label("💬 " + note.getComments());
        commentLabel.getStyleClass().add("comment-icon");

        interactionBox.getChildren().addAll(likeLabel, commentLabel);

        // 设置底部栏的两端对齐
        HBox.setHgrow(authorBox, Priority.ALWAYS);
        footerBox.getChildren().addAll(authorBox, interactionBox);

        // 组合所有元素
        contentBox.getChildren().addAll(titleLabel, descLabel, footerBox);
        noteCard.getChildren().addAll(imageContainer, contentBox);

        return noteCard;
    }

    /**
     * 获取示例笔记数据
     * @return 笔记列表
     */
    private List<Note> getSampleNotes() {
        List<Note> notes = queryAllInteractions();

        // 添加静态示例数据 - 使用更真实的数据
        notes.add(new Note(
            "宁波探店｜隐藏在街角的治愈系咖啡馆", 
            "这家街角的小店真的太温馨了，店主是个超爱笑的姐姐，墙上的复古装饰也很有情调，推荐奶油草莓配方，绝对值得一试～",
            "咖啡爱好者", 
            "https://picsum.photos/seed/coffee/400/500", 
            "https://randomuser.me/api/portraits/women/44.jpg",
            random.nextInt(100) + 50, 
            random.nextInt(20) + 5,
            random.nextInt(100) + 200));

        notes.add(new Note(
            "夏日穿搭|简约清爽风格", 
            "分享一套适合炎炎夏日的清爽穿搭，宽松衬衫搭配高腰牛仔裤，简约却不失时尚感，面料也很透气～",
            "时尚博主Amy", 
            "https://picsum.photos/seed/fashion/400/350", 
            "https://randomuser.me/api/portraits/women/3.jpg",
            random.nextInt(500) + 1000, 
            random.nextInt(50) + 100,
            random.nextInt(100) + 150));

        notes.add(new Note(
            "超治愈的手账排版｜手帐模板分享", 
            "最近迷上了小清新风格的手账排版，分享一下我的排版思路和一些小贴纸素材，希望大家喜欢～",
            "手账达人", 
            "https://picsum.photos/seed/journal/400/450", 
            "https://randomuser.me/api/portraits/women/10.jpg",
            random.nextInt(200) + 300, 
            random.nextInt(30) + 20,
            random.nextInt(100) + 250));

        notes.add(new Note(
            "重庆周末游｜解放碑洪崖洞必吃美食", 
            "周末和闺蜜逛了洪崖洞，人真的超多但风景绝美！推荐几家当地人都爱去的小店，麻辣烫、钵钵鸡都很地道～",
            "吃货小王", 
            "https://picsum.photos/seed/food/400/300", 
            "https://randomuser.me/api/portraits/men/22.jpg",
            random.nextInt(300) + 200, 
            random.nextInt(40) + 15,
            random.nextInt(100) + 180));

        notes.add(new Note(
            "植物种草|超好养的室内绿植推荐", 
            "分享几种新手也能养好的室内植物！龟背竹、绿萝都是气质担当，而且超级好养，放在书桌旁边提神又净化空气～",
            "植物爱好者", 
            "https://picsum.photos/seed/plant/400/380", 
            "https://randomuser.me/api/portraits/women/55.jpg",
            random.nextInt(150) + 100, 
            random.nextInt(25) + 10,
            random.nextInt(100) + 220));

        notes.add(new Note(
            "厨房改造|极简北欧风", 
            "花了一个月时间把10年的老厨房改造成北欧风格，分享一下改造历程和经验，希望能给准备装修的朋友一些参考～",
            "家居达人", 
            "https://picsum.photos/seed/kitchen/400/420", 
            "https://randomuser.me/api/portraits/men/32.jpg",
            random.nextInt(400) + 300, 
            random.nextInt(60) + 30,
            random.nextInt(100) + 200));

        return notes;
    }

    /**
     * 获取瀑布流布局容器
     * @return GridPane布局
     */
    public GridPane getFeedGrid() {
        return feedGrid;
    }
}

