package org.project.util;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class RippleAnimation {
    public static void create(double centerX, double centerY, double initialRadius, double finalRadius, Duration totalDuration, Pane animationContainer) {
        //Define the circle/ripple, set line color, fill transparent
        Circle ripple = new Circle(centerX, centerY, initialRadius);
        ripple.setStroke(Color.web("#74bd5b"));
        ripple.setFill(Color.TRANSPARENT);
        ripple.setStrokeWidth(0.9);
        ripple.setOpacity(0);

        //Define the second, smaller ripple
        Circle smallerRipple = new Circle(centerX, centerY, initialRadius * 0.6); // Smaller size
        smallerRipple.setStroke(Color.web("#90c299"));
        smallerRipple.setFill(Color.TRANSPARENT);
        smallerRipple.setStrokeWidth(0.9);
        smallerRipple.setOpacity(0);

        //container will contain two ripples
        animationContainer.getChildren().addAll(ripple, smallerRipple);

        //the visible animation lasts 70% of its timeline, rest duration is the pause before looping
        Duration visualDuration = totalDuration.multiply(0.7);
        Duration restDuration = totalDuration.subtract(visualDuration).add(Duration.seconds(.8));

        //Animation steps: Fade in -> Scale up -> Fade out
        //fade the animation starts at 30%
        FadeTransition fadeIn = new FadeTransition(visualDuration.multiply(0.3), ripple);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(0.5);
        fadeIn.setInterpolator(Interpolator.EASE_IN);

        //scale the animation up
        ScaleTransition scale = new ScaleTransition(visualDuration, ripple);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(finalRadius / initialRadius);
        scale.setToY(finalRadius / initialRadius);
        scale.setInterpolator(Interpolator.EASE_BOTH);

        //finally, fade out animation starts at about 50%, delay a few seconds before looping
        FadeTransition fadeOut = new FadeTransition(visualDuration.multiply(0.3), ripple);
        fadeOut.setFromValue(0.5);
        fadeOut.setToValue(0);
        fadeOut.setDelay(visualDuration.multiply(0.5));
        fadeOut.setInterpolator(Interpolator.EASE_OUT);

        //same steps with the small ripple
        //fade the animation in, about 40% opacity
        FadeTransition fadeInMini = new FadeTransition(visualDuration.multiply(0.2), smallerRipple);
        fadeInMini.setFromValue(0);
        fadeInMini.setToValue(0.4);
        fadeInMini.setInterpolator(Interpolator.EASE_IN);

        //scale the animation up, scale so its final radius matches the first ripple's initial radius
        ScaleTransition scaleUpMiniOne = new ScaleTransition(visualDuration.multiply(0.6), smallerRipple);
        scaleUpMiniOne.setFromX(1);
        scaleUpMiniOne.setFromY(1);
        double targetScale = finalRadius / (initialRadius);
        scaleUpMiniOne.setToX(targetScale);
        scaleUpMiniOne.setToY(targetScale);
        scaleUpMiniOne.setInterpolator(Interpolator.EASE_BOTH);

        //then fade out :0, bout 40% before it disappears, dealy a few seconds before looping
        FadeTransition fadeOutMini = new FadeTransition(visualDuration.multiply(0.3), smallerRipple);
        fadeOutMini.setFromValue(0.4);
        fadeOutMini.setToValue(0);
        fadeOutMini.setDelay(visualDuration.multiply(0.4));
        fadeOutMini.setInterpolator(Interpolator.EASE_OUT);

        //combine all animatoins to a single one
        ParallelTransition animation = new ParallelTransition(scale, fadeIn, fadeOut, scaleUpMiniOne, fadeInMini, fadeOutMini);

        //squentialTransition allows a rest period before looping the animation
        PauseTransition pause = new PauseTransition(restDuration);
        SequentialTransition fullCycle = new SequentialTransition(animation, pause);
        fullCycle.setCycleCount(Animation.INDEFINITE);
        fullCycle.play();
    }

}
