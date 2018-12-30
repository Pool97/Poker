package client.ui.effects;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class PulseAnimator {
    private JComponent target;
    private Animator animator;
    private BufferedImage glow;
    private boolean animationEnabled;

    public PulseAnimator(JComponent target){
        this.target = target;
    }

    public void createPulseEffect(){
        glow = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        glow = GraphicsUtilities.createCompatibleImage(target.getWidth(), target.getHeight());

        BufferedImageOp filter = new ColorTintFilter(Color.LIGHT_GRAY, 0.8f);
        glow = filter.filter(glow, null);
    }

    public void startPulseEffect() {
        PropertySetter setter = new PropertySetter(target, "alpha", 0.0f, 1.0f);
        animator = new Animator(600, Animator.INFINITE, Animator.RepeatBehavior.REVERSE, setter);
        animator.start();
    }

    public void drawPulseEffect(Graphics g, Shape clipShape, float alpha){
        Graphics2D g2 = (Graphics2D) g;
        g2.setClip(clipShape);
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
        g2.drawImage(glow, 0, 0, target.getWidth(), target.getHeight(), null);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.dispose();
    }

    public boolean isRunning(){
        if(animator != null)
            return animator.isRunning();
        return false;
    }

    public void stop(){
        if(isRunning()){
            animator.stop();
            glow = null;
        }
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }
}
