package client.ui.effects;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

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

        BufferedImageOp filter = getGaussianBlurFilter(20, true);
        glow = filter.filter(glow, null);

        filter = new ColorTintFilter(Color.LIGHT_GRAY, 1.0f);
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

    public ConvolveOp getGaussianBlurFilter(int radius, boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }
}
