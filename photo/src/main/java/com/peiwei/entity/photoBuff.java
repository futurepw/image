package com.peiwei.entity;

import com.peiwei.Interface.Iimage;

import java.awt.image.BufferedImage;

public class photoBuff implements Iimage {
    private BufferedImage image;
    private int width;
    private int height;

    public photoBuff(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    public photoBuff() {
    }
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRGB(int r, int g, int b) {//重新组装RGB值
        return (r << 16) | (g << 8) | b;
    }

    public int getR(int pixel) {//获取R通道值
        return (pixel >> 16) & 0xff;
    }

    public int getG(int pixel) {//获取G通道值
        return (pixel >> 8) & 0xff;
    }

    public int getB(int pixel) {//获取B通道值
        return pixel & 0xff;
    }
}
