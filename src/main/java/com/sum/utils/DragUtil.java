package com.sum.utils;

import javafx.scene.Node;
import javafx.stage.Stage;

public class DragUtil {

    public static void addDragListener(Stage stage, Node root) {
        new DragListener(stage).enableDrag(root);
    }
}
