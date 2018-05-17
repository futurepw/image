package com.peiwei.entity;

import com.peiwei.Interface.Iimage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class photoFile implements Iimage {
    private int width;
    private int height;
    private BufferedImage image;
    private File file;

    public photoFile(String name) {//文件名
        File f = new File(name);
        this.file = f;
        this.width = 0;
        this.height = 0;
        this.image = null;
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

    public int getRGB(int r, int g, int b) {//重新组装RGB值
        return (r << 16) | (g << 8) | b;
    }

    public void readImage() throws IOException {//读取图片
        if (this.file != null) {
            this.image = ImageIO.read(this.file);
            this.width = image.getWidth();
            this.height = image.getHeight();
        } else {
            System.out.println("没有指定文件");
        }
    }

    public void saveImage(BufferedImage image, String type, String fileName) throws IOException {//存储新图片
        File f = new File(fileName);
        ImageIO.write(image, type, f);
    }

    public int getWidth() {//获取图片宽度
        return width;
    }

    public void setWidth(int width) {//设置图片宽度
        this.width = width;
    }

    public int getHeight() {//获取图片高度
        return height;
    }

    public void setHeight(int height) {//设置图片高度
        this.height = height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.file = null;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
