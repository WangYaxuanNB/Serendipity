package com.sum.utils;

import cn.hutool.core.io.FileUtil;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件处理工具类
 */
public class FileUtils {

    public static final String IMAGES_BASE_PATH = "C:\\javafx-sm\\images\\";

    /**
     * 上传图片
     * @param selectedImageFile
     * @return
     */
    public static String uploadImage(File selectedImageFile) {
        String saveFilePath = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String folder = format.format(new Date());
            String fileName = selectedImageFile.getName();
            String saveFileName = buildNewFileName(fileName);
            File baseFolder = new File(IMAGES_BASE_PATH + folder);
            if (!baseFolder.exists()) {
                baseFolder.mkdirs();
            }
            FileUtil.copyFile(selectedImageFile,new File(baseFolder + File.separator + saveFileName));
            saveFilePath = folder + File.separator + saveFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveFilePath;
    }

    private static String buildNewFileName(String fileName) {
        return UUID.randomUUID().toString().replace("-", "").substring(10) + fileName.substring(fileName.lastIndexOf("."));
    }

    public static ImageView createImageView(String imageUrl) {
        ImageView imageView = new ImageView();
        try {
            File imageFile = new File(FileUtils.IMAGES_BASE_PATH + imageUrl);
            imageView.setImage(new Image(imageFile.toURI().toString()));
            if (!imageFile.exists()) {
                imageView.setImage(new Image("images/no_image.png"));
            }
            imageView.getStyleClass().add("note-image");
        } catch (IllegalArgumentException e) {
            imageView.setImage(new Image("images/no_image.png"));
        }
        imageView.setPreserveRatio(false);
        //平滑缩放
        imageView.setSmooth(true);
        imageView.setCursor(Cursor.HAND);
        return imageView;
    }

    public static ImageView createAvatarImageView(String imageUrl) {
        ImageView imageView = new ImageView();
        if(StringUtils.isBlank(imageUrl)){
            imageView.setImage(new Image("images/default_avatar.png"));
            return imageView;
        }
        try {
            File imageFile = new File(FileUtils.IMAGES_BASE_PATH + imageUrl);
            imageView.setImage(new Image(imageFile.toURI().toString()));
            if (!imageFile.exists()) {
                imageView.setImage(new Image("images/default_avatar.png"));
            }
        } catch (IllegalArgumentException e) {
            imageView.setImage(new Image("images/default_avatar.png"));
        }
        return imageView;
    }
}
