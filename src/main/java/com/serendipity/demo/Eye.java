package com.serendipity.demo;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Eye extends StackPane {
    private static final double EYE_RADIUS = 10;
    private static final double PUPIL_RADIUS = 4;
    private final Circle eyeball;
    private final Circle pupil;

    public Eye(Color eyeballColor) {
        eyeball = new Circle(EYE_RADIUS, eyeballColor);
        eyeball.setStroke(Color.BLACK);
        pupil = new Circle(PUPIL_RADIUS, Color.BLACK);
        getChildren().addAll(eyeball, pupil);
    }

    public void lookAt(double mouseX, double mouseY, double centerX, double centerY) {
        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double max = EYE_RADIUS - PUPIL_RADIUS - 2;
        if (dist > max) {
            dx = dx / dist * max;
            dy = dy / dist * max;
        }
        pupil.setTranslateX(dx);
        pupil.setTranslateY(dy);
    }
}
