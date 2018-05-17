package com.peiwei.toolKit;

import com.peiwei.entity.photoFile;
import com.peiwei.entity.photoBuff;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class toolOfFile {
    public static BufferedImage colorContrary(String name, String save) {//图片颜色相反
        photoFile photoOfFile = new photoFile(name);
        BufferedImage nbi = null;
        try {
            photoOfFile.readImage();
            nbi = new BufferedImage(photoOfFile.getWidth(), photoOfFile.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            for (int j = 0; j < photoOfFile.getHeight(); j++)
                for (int i = 0; i < photoOfFile.getWidth(); i++) {
                    int p = photoOfFile.getImage().getRGB(i, j);
                    int r = 255 - photoOfFile.getR(p);
                    int g = 255 - photoOfFile.getG(p);
                    int b = 255 - photoOfFile.getB(p);
                    int x = photoOfFile.getRGB(r, g, b);
                    nbi.setRGB(i, j, x);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbi;
    }

    public static BufferedImage grayImage(String name, String save) {//加权灰度处理
/*  灰度处理一般有三种算法：
*    1 最大值法：即新的颜色值R＝G＝B＝Max(R，G，B)，这种方法处理后的图片看起来亮度值偏高。
*    2 平均值法：即新的颜色值R＝G＝B＝(R＋G＋B)／3，这样处理的图片十分柔和
*    3 加权平均值法：即新的颜色值R＝G＝B＝(R ＊ Wr＋G＊Wg＋B＊Wb)，一般由于人眼对不同颜色的敏感度不一样，所以三种颜色值的权重不一样，
*    一般来说绿色最高，红色其次，蓝色最低，最合理的取值分别为Wr ＝ 30％，Wg ＝ 59％，Wb ＝ 11％
*/
        photoFile photoOfFile = new photoFile(name);
        BufferedImage nbi = null;
        try {
            photoOfFile.readImage();
            nbi = new BufferedImage(photoOfFile.getWidth(), photoOfFile.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            for (int j = 0; j < photoOfFile.getHeight(); j++)
                for (int i = 0; i < photoOfFile.getWidth(); i++) {
                    int p = photoOfFile.getImage().getRGB(i, j);
                    int r = photoOfFile.getR(p);
                    int g = photoOfFile.getG(p);
                    int b = photoOfFile.getB(p);
//                    int x = (int)(0.3*r+0.59*g+0.11*b);
                    int x = (int) (0.114 * r + 0.587 * g + 0.299 * b);
                    int y = photoOfFile.getRGB(x, x, x);
                    nbi.setRGB(i, j, y);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbi;
    }

    public static BufferedImage blackWhite(String name, String save) {//黑白效果
        /* 求RGB平均值Avg ＝ (R + G + B) / 3，如果Avg >= 100，则新的颜色值为R＝G＝B＝255；
        *  如果Avg < 100，则新的颜色值为R＝G＝B＝0；255就是白色，0就是黑色；
        */
        photoFile photoOfFile = new photoFile(name);
        BufferedImage nbi = null;
        try {
            photoOfFile.readImage();
            nbi = new BufferedImage(photoOfFile.getWidth(), photoOfFile.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            for (int j = 0; j < photoOfFile.getHeight(); j++)
                for (int i = 0; i < photoOfFile.getWidth(); i++) {
                    int p = photoOfFile.getImage().getRGB(i, j);
                    int r = photoOfFile.getR(p);
                    int g = photoOfFile.getG(p);
                    int b = photoOfFile.getB(p);
                    int avg = (r + g + b) / 3;
                    int pixel;
                    if (avg >= 100)
                        pixel = 255;
                    else
                        pixel = 0;
                    int x = photoOfFile.getRGB(pixel, pixel, pixel);
                    nbi.setRGB(i, j, x);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbi;
    }

    public static void hist(String name, String save) {
        photoFile photoOfFile = new photoFile(name);
        int[] hist = new int[256];
        try {
            photoOfFile.readImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            hist = toolOfFile.count(photoOfFile.getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int max = max(hist);
        drawHist(hist, max, save);
    }

    public static int max(int[] count) {//统计数字中最大值
        int max = count[0];
        for (int i = 0; i < count.length; i++) {
            if (max < count[i])
                max = count[i];
        }
        return max;
    }

    public static void drawHist(int[] hist, int max, String save) {//对数值hist进行绘制灰度直方图
        int size = 300;
        BufferedImage pic = new BufferedImage(size, size, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics2D = pic.createGraphics();
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, size, size);
        graphics2D.setPaint(Color.black);
        graphics2D.drawLine(24, 40, 24, 275);
        graphics2D.setPaint(Color.black);
        graphics2D.drawLine(24, 276, 280, 276);
        graphics2D.setFont(new Font("隶书", Font.BOLD, 20));
        graphics2D.drawString("灰度直方图", 100, 35);
        graphics2D.setPaint(Color.green);
        float rate = 200.0f / ((float) max);
        for (int i = 0; i < hist.length; i++) {
            int frequency = (int) (hist[i] * rate);
            graphics2D.drawLine(25 + i, 275, 25 + i, 275 - frequency);
        }
        try {
            ImageIO.write(pic, "jpg", new File(save));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] count(BufferedImage image) throws IOException {//统计图片中的灰度值
        photoBuff photo = new photoBuff(image);
        int[] hist = new int[256];
        for (int j = 0; j < photo.getHeight(); j++)
            for (int i = 0; i < photo.getWidth(); i++) {
                int p = photo.getImage().getRGB(i, j);
                int r = photo.getR(p);
                int g = photo.getG(p);
                int b = photo.getB(p);
                int x = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                hist[x]++;
            }
        return hist;
    }

    public static void saveImage(BufferedImage image, String type, String fileName) {//存储新图片
        File f = new File(fileName);
        try {
            ImageIO.write(image, type, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage grayTransform(String name, String save, int a, double c) {//线性灰度转换
        photoFile photoOfFile = new photoFile(name);
        BufferedImage nbi = null;
        try {
            photoOfFile.readImage();
            nbi = new BufferedImage(photoOfFile.getWidth(), photoOfFile.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            for (int j = 0; j < photoOfFile.getHeight(); j++)
                for (int i = 0; i < photoOfFile.getWidth(); i++) {
                    int p = photoOfFile.getImage().getRGB(i, j);
                    int r = photoOfFile.getR(p);
                    int g = photoOfFile.getG(p);
                    int b = photoOfFile.getB(p);
                    int x = photoOfFile.getRGB(select(liner(c, a, r)), select(liner(c, a, g)), select(liner(c, a, b)));
                    nbi.setRGB(i, j, x);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbi;
    }

    public static int liner(double c, int a, int p) {
        return (int) (p * c + a);
    }

    public static int select(int p) {
        if (p > 255)
            return 255;
        else if (p < 0)
            return 0;
        else return p;
    }
}
