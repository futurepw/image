package com.peiwei.toolKit;

import com.peiwei.entity.photoBuff;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.util.Arrays;

public class toolOfBuff {

    public static BufferedImage readImage(String name) {//读取图片
        File f = new File(name);
        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage colorContrary(BufferedImage bufferedImage) {//图片颜色相反
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = 255 - photoBuff.getR(p);
                int g = 255 - photoBuff.getG(p);
                int b = 255 - photoBuff.getB(p);
                int x = photoBuff.getRGB(r, g, b);
                nbi.setRGB(i, j, x);
            }
        return nbi;
    }

    public static BufferedImage grayImage(BufferedImage bufferedImage) {//加权灰度处理
/*  灰度处理一般有三种算法：
*    1 最大值法：即新的颜色值R＝G＝B＝Max(R，G，B)，这种方法处理后的图片看起来亮度值偏高。
*    2 平均值法：即新的颜色值R＝G＝B＝(R＋G＋B)／3，这样处理的图片十分柔和
*    3 加权平均值法：即新的颜色值R＝G＝B＝(R ＊ Wr＋G＊Wg＋B＊Wb)，一般由于人眼对不同颜色的敏感度不一样，所以三种颜色值的权重不一样，
*    一般来说绿色最高，红色其次，蓝色最低，最合理的取值分别为Wr ＝ 30％，Wg ＝ 59％，Wb ＝ 11％
*/
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
//                    int x = (int)(0.3*r+0.59*g+0.11*b);
                int x = (int) (0.114 * r + 0.587 * g + 0.299 * b);
                int y = photoBuff.getRGB(x, x, x);
                nbi.setRGB(i, j, y);
            }
        return nbi;
    }

    public static BufferedImage blackWhite(BufferedImage bufferedImage) {//黑白效果
        /* 求RGB平均值Avg ＝ (R + G + B) / 3，如果Avg >= 100，则新的颜色值为R＝G＝B＝255；
        *  如果Avg < 100，则新的颜色值为R＝G＝B＝0；255就是白色，0就是黑色；
        */
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
                int avg = (r + g + b) / 3;
                int pixel;
                if (avg >= 100)
                    pixel = 255;
                else
                    pixel = 0;
                int x = photoBuff.getRGB(pixel, pixel, pixel);
                nbi.setRGB(i, j, x);
            }
        return nbi;
    }

    public static int[] hist(BufferedImage bufferedImage, String save) {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        int[] hist = new int[256];
        try {
            hist = toolOfFile.count(photoBuff.getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int max = max(hist);
        return hist;
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

    public static BufferedImage lineTransform(BufferedImage bufferedImage, int a, double c) {//线性灰度转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
                int x = photoBuff.getRGB(select(liner(c, a, r)), select(liner(c, a, g)), select(liner(c, a, b)));
                nbi.setRGB(i, j, x);
            }
        return nbi;
    }

    public static int liner(double c, int a, int p) {
        return (int) (p * c + a);
    }

    public static int logarithm(double a, double b, int p) {
        double x = Math.log1p(p + 1) * a + b;
        return (int) x;
    }

    public static int index(double a, double b, double c, int p) {
        double x = (p - a) * c;
        x = Math.pow(b, x) - 1;
        return (int) x;
    }

    public static int part(int W, int C, int p) {
        int x = p;
        try {
            x = 255 * (p - C + W / 2) / W;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    public static int select(int p) {
        if (p > 255)
            return 255;
        else if (p < 0)
            return 0;
        else return p;
    }

    public static BufferedImage logTransform(BufferedImage bufferedImage, double a, double c) {//线性灰度转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
                int x = photoBuff.getRGB(select(logarithm(a, c, r)), select(logarithm(a, c, g)), select(logarithm(a, c, b)));
                nbi.setRGB(i, j, x);
            }
        return nbi;
    }

    public static BufferedImage indTransform(BufferedImage bufferedImage, double a, double bb, double c) {//线性灰度转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
                int x = photoBuff.getRGB(select(index(a, bb, c, r)), select(index(a, bb, c, g)), select(index(a, bb, c, b)));
                nbi.setRGB(i, j, x);
            }
        return nbi;
    }

    public static BufferedImage partGaryTransform(BufferedImage bufferedImage, int W, int C) {//线性灰度转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
                int x = photoBuff.getRGB(select(part(W, C, r)), select(part(W, C, g)), select(part(W, C, b)));
                nbi.setRGB(i, j, x);
            }
        return nbi;
    }

    public static BufferedImage translation(BufferedImage bufferedImage, int W, int H, boolean zhe) {//平移转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        int WHITE = photoBuff.getRGB(255, 255, 255);
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int x = i - W;
                int y = j - H;
                if (zhe) {
                    x = x % photoBuff.getWidth();
                    y = y % photoBuff.getHeight();
                    if (x < 0)
                        x = W + x;
                    if (y < 0)
                        y = H + y;
                    int p = photoBuff.getImage().getRGB(x, y);
                    nbi.setRGB(i, j, p);

                } else {
                    if (x >= 0 && x < photoBuff.getWidth() && y >= 0 && y < photoBuff.getHeight()) {
                        int p = photoBuff.getImage().getRGB(x, y);
                        nbi.setRGB(i, j, p);
                    } else
                        nbi.setRGB(i, j, WHITE);
                }
            }
        return nbi;
    }

    public static BufferedImage rotateToBig(BufferedImage bufferedImage, double angle) {//平移转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        int newPhono = photoBuff.getWidth() + photoBuff.getHeight();
        nbi = new BufferedImage(newPhono, newPhono, BufferedImage.TYPE_3BYTE_BGR);
        int cw = photoBuff.getWidth() / 2;
        int ch = photoBuff.getWidth() / 2;
        angle = Math.PI * angle / 180;
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        int WHITE = photoBuff.getRGB(255, 255, 255);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int x = (int) ((i - cw) * cosA - (j - ch) * sinA + newPhono / 2);
                int y = (int) ((i - cw) * sinA + (j - ch) * cosA + newPhono / 2);
                int p = photoBuff.getImage().getRGB(i, j);
                nbi.setRGB(x, y, p);
            }
        return nbi;
    }

    public static BufferedImage rotateSame(BufferedImage bufferedImage, double angle) {//平移转换
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        int cw = photoBuff.getWidth() / 2;
        int ch = photoBuff.getWidth() / 2;
        angle = Math.PI * angle / 180;
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        int WHITE = photoBuff.getRGB(255, 255, 255);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int x = (int) ((i - cw) * cosA - (j - ch) * sinA + cw);
                int y = (int) ((i - cw) * sinA + (j - ch) * cosA + ch);
                if (x >= 0 && x < photoBuff.getWidth() && y >= 0 && y < photoBuff.getHeight()) {
                    int p = photoBuff.getImage().getRGB(x, y);
                    nbi.setRGB(i, j, p);
                }
            }
        return nbi;
    }

    public static BufferedImage zoom(BufferedImage bufferedImage, double zoom, int function) {//缩放
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        int cw = (int) (photoBuff.getWidth() * zoom);
        int ch = (int) (photoBuff.getWidth() * zoom);
        nbi = new BufferedImage(cw, ch, BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < nbi.getHeight(); j++)
            for (int i = 0; i < nbi.getWidth(); i++) {
                int x = i, y = j;
                if (function == 1) {
                    try {
                        x = (int) (i / zoom);
                        y = (int) (j / zoom);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        x = (int) ((i + 1) / zoom - 1);
                        y = (int) ((j + 1) / zoom - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (x >= 0 && x < photoBuff.getWidth() && y >= 0 && y < photoBuff.getHeight()) {
                    int p = photoBuff.getImage().getRGB(x, y);
                    nbi.setRGB(i, j, p);
                }
            }
        return nbi;
    }

    public static BufferedImage partAvgNarrow(BufferedImage bufferedImage, double zoom) {//缩小
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        int cw = (int) (photoBuff.getWidth() * zoom);
        int ch = (int) (photoBuff.getWidth() * zoom);
        nbi = new BufferedImage(cw, ch, BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < nbi.getHeight(); j++)
            for (int i = 0; i < nbi.getWidth(); i++)
                nbi.setRGB(i, j, avg(bufferedImage, i, j, zoom));
        return nbi;
    }

//    public static BufferedImage lineEnlarge(BufferedImage bufferedImage, double zoom) {//双线性插值放大
//        photoBuff photoBuff = new photoBuff(bufferedImage);
//        BufferedImage nbi = null;
//        int cw = (int) (photoBuff.getWidth() * zoom);
//        int ch = (int) (photoBuff.getWidth() * zoom);
//        nbi = new BufferedImage(cw, photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        BufferedImage nbi2 = new BufferedImage(cw, ch, BufferedImage.TYPE_3BYTE_BGR);
//        for (int j = 0; j < bufferedImage.getHeight(); j++)
//            for (int i = 0; i < bufferedImage.getWidth() - 1; i++) {
//                int p1 = photoBuff.getImage().getRGB(i, j);
//                int p2 = photoBuff.getImage().getRGB(i + 1, j);
//                for (int i1 = (int) ((i + 1) * zoom - 1); i1 < (i + 2) * zoom; i1++) {
//                    int r1 = photoBuff.getR(p1);
//                    int g1 = photoBuff.getG(p1);
//                    int b1 = photoBuff.getB(p1);
//                    int r2 = photoBuff.getR(p2);
//                    int g2 = photoBuff.getG(p2);
//                    int b2 = photoBuff.getB(p2);
//                    int r = (int) (Math.abs(r1 - r2) / (zoom + 1)) * i1 + Math.min(r1, r2);
//                    int g = (int) (Math.abs(g1 - g2) / (zoom + 1)) * i1 + Math.min(g1, g2);
//                    int b = (int) (Math.abs(b1 - b2) / (zoom + 1)) * i1 + Math.min(b1, b2);
//                    int p = photoBuff.getRGB(r,g,b);
//                    nbi.setRGB(i1, j, p);
//                }
//
//            }
//
//        for (int i = 0; i < nbi.getWidth(); i++)
//            for (int j = 0; j < nbi.getHeight() - 1; j++) {
//                int p3 = nbi.getRGB(i, j);
//                int p4 = nbi.getRGB(i, j + 1);
//                for (int j1 = (int) ((j + 1) * zoom - 1); j1 < (j + 2) * zoom; j1++) {
//                    int r3 = photoBuff.getR(p3);
//                    int g3 = photoBuff.getG(p3);
//                    int b3 = photoBuff.getB(p3);
//                    int r4 = photoBuff.getR(p4);
//                    int g4 = photoBuff.getG(p4);
//                    int b4 = photoBuff.getB(p4);
//                    int r = (int) (Math.abs(r3 - r4) / (zoom + 1)) * j1 + Math.min(r3, r4);
//                    int g = (int) (Math.abs(g3 - g4) / (zoom + 1)) * j1 + Math.min(g3, g4);
//                    int b = (int) (Math.abs(b3 -b4) / (zoom + 1)) * j1 + Math.min(b3,b4);
//                    int p = photoBuff.getRGB(r,g,b);
//                    nbi2.setRGB(i, j1, p);
//                }
//            }
//        return nbi2;
//    }

    public static int avg(BufferedImage image, int i, int j, double zoom) {
        photoBuff photoBuff = new photoBuff(image);
        int x0 = (int) (i / zoom);
        int y0 = (int) (j / zoom);
        int x1 = (int) ((i + 1) / zoom - 1);
        int y1 = (int) ((j + 1) / zoom - 1);
        int count = 0;
        int Rsum = 0, Gsum = 0, Bsum = 0;
        for (int x = x0; x < x1; x++)
            for (int y = y0; y < y1; y++) {
                int p = image.getRGB(x, y);
                Rsum += photoBuff.getR(p);
                Gsum += photoBuff.getG(p);
                Bsum += photoBuff.getB(p);
                count++;
            }
        if (count == 0)
            return photoBuff.getRGB(Rsum, Gsum, Bsum);
        else
            return photoBuff.getRGB(Rsum / count, Gsum / count, Bsum / count);
    }

    public static BufferedImage Horizontal(BufferedImage bufferedImage) {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                nbi.setRGB(photoBuff.getWidth() - i - 1, j, p);
            }
        return nbi;
    }

    public static BufferedImage Vertical(BufferedImage bufferedImage) {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                nbi.setRGB(i, photoBuff.getHeight() - j - 1, p);
            }
        return nbi;
    }

    public static BufferedImage verticalAndHorizontal(BufferedImage bufferedImage) {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                nbi.setRGB(photoBuff.getWidth() - i - 1, photoBuff.getHeight() - j - 1, p);
            }
        return nbi;
    }

    public static BufferedImage add(BufferedImage bufferedImage1, BufferedImage bufferedImage2, int selct) {//平移转换
        photoBuff photoBuff1 = new photoBuff(bufferedImage1);
        photoBuff photoBuff2 = new photoBuff(bufferedImage2);
        int w = Math.min(photoBuff1.getWidth(), photoBuff2.getWidth());
        int h = Math.min(photoBuff1.getHeight(), photoBuff2.getHeight());
        BufferedImage nbi = null;
        nbi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < nbi.getHeight(); j++)
            for (int i = 0; i < nbi.getWidth(); i++) {
                int p1 = photoBuff1.getImage().getRGB(i, j);
                int p2 = photoBuff2.getImage().getRGB(i, j);
                int p = function(p1, p2, selct);
                nbi.setRGB(i, j, p);
            }
        return nbi;
    }

    public static int function(int p1, int p2, int select) {
        photoBuff photoBuff = new photoBuff();
        int r1 = photoBuff.getR(p1);
        int g1 = photoBuff.getG(p1);
        int b1 = photoBuff.getB(p1);
        int r2 = photoBuff.getR(p2);
        int g2 = photoBuff.getG(p2);
        int b2 = photoBuff.getB(p2);
        int r = r1, g = g1, b = b1;
        if (select == 1) {
            r = (r1 + r2) / 2;
            g = (g1 + g2) / 2;
            b = (b1 + b2) / 2;
        }
        if (select == 2) {
            r = Math.abs(r1 - r2);
            g = Math.abs(g1 - g2);
            b = Math.abs(b1 - b2);
        }
        if (select == 3) {
            r = (int) Math.sqrt(r1 * r2);
            g = (int) Math.sqrt(g1 * g2);
            b = (int) Math.sqrt(b1 * b2);
        }
        return photoBuff.getRGB(r, g, b);
    }

    public static BufferedImage midWaveFilter(BufferedImage bufferedImage, int version) {//version 选择不同方式 支持1-4
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        int end = 0;
        if (version == 1)
            end = 1;
        else
            end = 3;
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int[] z;
                int count = 0;
                if ((i < end) || (j < end) || (j >= (photoBuff.getHeight() - end)) || (i >= (photoBuff.getHeight() - end))) {
                    int p = photoBuff.getImage().getRGB(i, j);
                    nbi.setRGB(i, j, p);
                } else {
                    if (version == 1) {
                        z = new int[9];
                        for (int y = -1; y < 2; y++)
                            for (int x = -1; x < 2; x++) {
                                z[count++] = photoBuff.getImage().getRGB(i + x, j + y);
                            }
                        int p = midPixel(z);
                        nbi.setRGB(i, j, p);
                    } else if (version == 2) {
                        z = new int[7];
                        for (int x = -3; x < 4; x++) {
                            z[count++] = photoBuff.getImage().getRGB(i + x, j + x);
                        }
                        int p = midPixel(z);
                        nbi.setRGB(i, j, p);
                    } else if (version == 3) {
                        z = new int[13];
                        for (int x = -3; x < 4; x++) {
                            if (x != 0) {
                                z[count++] = photoBuff.getImage().getRGB(i + x, j);
                                z[count++] = photoBuff.getImage().getRGB(i, j + x);
                            } else {
                                z[count++] = photoBuff.getImage().getRGB(i, j);
                            }

                        }
                        int p = midPixel(z);
                        nbi.setRGB(i, j, p);
                    } else {
                        z = new int[37];
                        for (int y = -2; y < 3; y++)
                            for (int x = -2; x < 3; x++) {
                                z[count++] = photoBuff.getImage().getRGB(i + x, j + y);
                            }
                        for (int x = -1; x < 2; x++) {
                            z[count++] = photoBuff.getImage().getRGB(i + x, j - 3);
                            z[count++] = photoBuff.getImage().getRGB(i + x, j + 3);
                            z[count++] = photoBuff.getImage().getRGB(i - 3, j + x);
                            z[count++] = photoBuff.getImage().getRGB(i + 3, j + x);
                        }
                        int p = midPixel(z);
                        nbi.setRGB(i, j, p);
                    }
                }
            }
        return nbi;
    }

    public static BufferedImage avgWaveFilter(BufferedImage bufferedImage, int k) {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth() - 1; i++) {
                int[] z = new int[9];
                int[] a = kernel(k);
                int count = 0;
                for (int y = -1; y < 2; y++)
                    for (int x = -1; x < 2; x++) {
                        if (((i + x) < 0) || ((j + y) < 0) || ((i + x) >= photoBuff.getWidth()) || ((j + y) >= photoBuff.getHeight()))
                            z[count++] = 0;
                        else
                            z[count++] = photoBuff.getImage().getRGB(i + x, j + y);
                    }
                int p = avgValue(z, a, k);
                nbi.setRGB(i, j, p);
            }
        return nbi;
    }

    public static int avgValue(int[] a, int[] b, int k) {
        photoBuff photoBuff = new photoBuff();
        int p = 0, R = 0, G = 0, B = 0;
        int div = 1;
        if (k == 1)
            div = 9;
        else if (k == 2)
            div = 16;
        else
            div = 1;
        for (int i = 0; i < a.length; i++) {
            int r1 = photoBuff.getR(a[i]) * b[i];
            int g1 = photoBuff.getG(a[i]) * b[i];
            int b1 = photoBuff.getB(a[i]) * b[i];
            R += r1;
            G += g1;
            B += b1;
        }
        R = R / div;
        G = G / div;
        B = B / div;
        return photoBuff.getRGB(select(R), select(G), select(B));
    }

    public static BufferedImage edgeDetection(BufferedImage bufferedImage, int select) {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth() - 1; i++) {
                int[] z = new int[9];
                int[] a = edgeKernel(select);
                int count = 0;
                for (int y = -1; y < 2; y++)
                    for (int x = -1; x < 2; x++) {
                        if (((i + x) < 0) || ((j + y) < 0) || ((i + x) >= photoBuff.getWidth()) || ((j + y) >= photoBuff.getHeight()))
                            z[count++] = 0;
                        else
                            z[count++] = photoBuff.getImage().getRGB(i + x, j + y);
                    }
                int p = detection(z, a, select);
                nbi.setRGB(i, j, p);
            }
        return nbi;
    }

    public static int detection(int[] a, int[] b, int k) {//a 像素  b 核函数
        photoBuff photoBuff = new photoBuff();
        int R = 0, G = 0, B = 0;
        if (k == 1) {
            R = (int) Math.sqrt(Math.pow(photoBuff.getR(a[0]) * b[0] - photoBuff.getR(a[8]) * b[8], 2) + Math.pow(photoBuff.getR(a[2]) * b[2] - photoBuff.getR(a[6]) * b[6], 2));
            G = (int) Math.sqrt(Math.pow(photoBuff.getG(a[0]) * b[0] - photoBuff.getG(a[8]) * b[8], 2) + Math.pow(photoBuff.getG(a[2]) * b[2] - photoBuff.getG(a[6]) * b[6], 2));
            B = (int) Math.sqrt(Math.pow(photoBuff.getB(a[0]) * b[0] - photoBuff.getB(a[8]) * b[8], 2) + Math.pow(photoBuff.getB(a[2]) * b[2] - photoBuff.getB(a[6]) * b[6], 2));
        } else if (k == 2) {
            int rr = 0, gg = 0, bb = 0;
            int r1 = 0, g1 = 0, b1 = 0;
            for (int i = 0; i < 9; i++) {
                r1 += photoBuff.getR(a[i]) * b[i];
                g1 += photoBuff.getG(a[i]) * b[i];
                b1 += photoBuff.getB(a[i]) * b[i];
                rr += photoBuff.getR(a[i]) * b[i + 9];
                gg += photoBuff.getG(a[i]) * b[i + 9];
                bb += photoBuff.getB(a[i]) * b[i + 9];
            }
            R = Math.abs(r1) + Math.abs(rr);
            G = Math.abs(g1) + Math.abs(gg);
            B = Math.abs(b1) + Math.abs(bb);
        } else if (k == 5) {
            for (int i = 0; i < 8; i++) {
                int r1 = 0, g1 = 0, b1 = 0;
                for (int j = 0; j < 9; j++) {
                    r1 += photoBuff.getR(a[j]) * b[j + i * 9];
                    g1 += photoBuff.getG(a[j]) * b[j + i * 9];
                    b1 += photoBuff.getB(a[j]) * b[j + i * 9];
                }
                r1 = Math.abs(r1);
                g1 = Math.abs(g1);
                b1 = Math.abs(b1);
                R = Math.max(R, r1);
                G = Math.max(G, g1);
                B = Math.max(B, b1);
            }
        } else {
            for (int i = 0; i < a.length; i++) {
                int r1 = photoBuff.getR(a[i]) * b[i];
                int g1 = photoBuff.getG(a[i]) * b[i];
                int b1 = photoBuff.getB(a[i]) * b[i];
                R += r1;
                G += g1;
                B += b1;
            }
        }
        return photoBuff.getRGB(select(R), select(G), select(B));
    }

    public static int[] edgeKernel(int k) {
        int[] z;
        if (k == 1) {//robert
            z = new int[]{1, 0, 1, 0, 0, 0, 1, 0, 1};
        } else if (k == 2) {//sobel
            z = new int[]{-1, 0, 1, -2, 0, 2, -1, 0, 1,
                    -1, -2, -1, 0, 0, 0, 1, 2, 1};
        } else if (k == 3) {//laplacian
            z = new int[]{1, 1, 1, 1, -8, 1, 1, 1, 1};
//            z = new int[]{0, 1, 0, 1, -4, 1, 0, 1, 0};
        } else if (k == 4) {//prewitt
            z = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
        } else if (k == 5) {//kirsch
            z = new int[]{5, 5, 5, -3, 0, -3, -3, -3, -3,
                    -3, 5, 5, -3, 0, 5, -3, -3, -3,
                    -3, -3, 5, -3, 0, 5, -3, -3, 5,
                    -3, -3, -3, -3, 0, 5, 3, 5, 5,
                    -3, -3, -3, -3, 0, -3, 5, 5, 5,
                    -3, -3, -3, 5, 0, -3, 5, 5, -3,
                    5, -3, -3, 5, 0, -3, 5, -3, -3,
                    5, 5, -3, 5, 0, -3, -3, -3, -3
            };
        } else {
            z = new int[]{0, 0, 0, 0, 1, 0, 0, 0, 0};  //啥也不做
        }
        return z;
    }

    public static int[] kernel(int k) {
        int[] z = new int[9];
        if (k == 1) {
            z = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};//均值滤波
        } else if (k == 2) {
            z = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};//均值滤波
        } else if (k == 3) {
            z = new int[]{-1, -1, 0, -1, 0, 1, 0, 1, 1};//浮雕
        } else if (k == 4) {
            z = new int[]{1, 1, 1, 1, -7, 1, 1, 1, 1};//边缘检测
//            z= new int[]{-1,0,-1,0,4,0,-1,0,-1};
//            z= new int[]{-1,-1,-1,-1,8,-1,-1,-1,-1};
//            z= new int[]{1,-2,1,-2,4,-2,1,-2,1};
        } else if (k == 5) {
            z = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};//高通滤波
        } else if (k == 6) {
//            z= new int[]{0,0,0,-1,1,0,0,0,0};
//            z= new int[]{-1,0,0,0,1,0,0,0,0};
            z = new int[]{0, -1, 0, 0, 1, 0, 0, 0, 0};//平移和差分边缘检测
        } else if (k == 7) {
            z = new int[]{1, 1, 1, 1, -2, 1, -1, -1, -1};//梯度方向边缘检测
//            z= new int[]{1,1,1,-1,-2,1,-1,-1,1};
//            z= new int[]{-1,1,1,-1,-2,1,-1,1,1};
//            z= new int[]{-1,-1,1,-1,-2,1,1,1,1};
//            z= new int[]{-1,-1,-1,1,-2,1,1,1,1};
//            z= new int[]{1,-1,-1,1,-2,-1,1,1,1};
//            z= new int[]{1,1,-1,1,-2,-1,1,1,-1};
//            z= new int[]{1,1,1,1,-2,-1,1,-1,-1};
        } else {
            z = new int[]{0, 0, 0, 0, 1, 0, 0, 0, 0};  //啥也不做
        }
        return z;
    }

    public static int midPixel(int[] z) {
        Arrays.sort(z);
        return z[(int) (Math.ceil(z.length / 2) - 1)];
    }

    public static BufferedImage grayFg(BufferedImage bufferedImage, int yz, int select) throws IOException {
        photoBuff photoBuff = new photoBuff(bufferedImage);
        BufferedImage nbi = null;
        nbi = new BufferedImage(photoBuff.getWidth(), photoBuff.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        int[] count = count(bufferedImage);
        if (select == 1)
            yz = dieDai(bufferedImage, yz);
        else if (select == 2) {
            double max = shang(count, 0);
            int index = 0;
            for (int i = 1; i < count.length; i++) {
                double s = shang(count, i);
                if (max < s) {
                    index = i;
                    max = s;
                }
            }
            yz = index;
        } else if (select == 3) {
            double max = ostu(count, 0);
            int index = 0;
            for (int i = 1; i < count.length; i++) {
                double s = ostu(count, i);
                if (max < s) {
                    index = i;
                    max = s;
                }
            }
            yz = index;
        } else if (select == 4) {
            yz = momentPreserving(count, 0);
        }
        System.out.println(yz);
        for (int j = 0; j < photoBuff.getHeight(); j++)
            for (int i = 0; i < photoBuff.getWidth(); i++) {
                int p = photoBuff.getImage().getRGB(i, j);
                int r = photoBuff.getR(p);
                int g = photoBuff.getG(p);
                int b = photoBuff.getB(p);
                int x = (int) (0.114 * r + 0.587 * g + 0.299 * b);
                if (x >= yz)
                    nbi.setRGB(i, j, photoBuff.getRGB(255, 255, 255));
            }
        return nbi;
    }

    public static int dieDai(BufferedImage bufferedImage, int yz) throws IOException {
        boolean flag = true;
        int t = 0;
        int nyz = findYz(count(bufferedImage), yz);
        while (flag && t <= 20) {
            if (yz == nyz) {
                flag = false;
            } else {
                yz = nyz;
                nyz = findYz(count(bufferedImage), yz);
            }
            t++;
        }
        return yz;
    }

    public static int findYz(int[] count, int yz) {
        int m0 = 0, m1 = 0;
        int t = 1;
        for (int i = 0; i < yz; i++) {
            t += count[i];
            m0 += i * count[i];
        }
        m0 /= t;
        t = 1;
        for (int i = yz; i < count.length; i++) {
            t += count[i];
            m1 += i * count[i];
        }
        m1 /= t;
        return (m1 + m0) / 2;
    }

    public static double ostu(int[] count, int t) {
        double w0 = 0;
        double w1 = 0;
        double u0 = 0;
        double u1 = 0;
        double ut = 0;
        double s0 = 0;
        double s1 = 0;
        for (int i = 0; i < count.length; i++) {
            if (i <= t)
                w0 += count[i];
            else
                w1 += count[i];
        }
        if (w0 == 0)
            w0 = 1;
        if (w1 == 0)
            w1 = 1;
        for (int i = 0; i < count.length; i++) {
            if (i <= t)
                u0 += i * count[i] / w0;
            else
                u1 += i * count[i] / w1;
            ut += i * count[i];
        }
        for (int i = 0; i < count.length; i++) {
            s0 += (i - ut) * (i - ut) * count[i];
        }
        s1 = w1 * w0 * (u0 - u1) * (u0 - u1);
        return s1 / s0;
    }

    public static int momentPreserving(int[] count, int t) {
        double N = 0;
        double[] p = new double[count.length];
        double m0 = 0, m1 = 0, m2 = 0, m3 = 0;
        m0 = 1;
        for (int i = 0; i < count.length; i++) {
            N += count[i];
        }
        for (int i = 0; i < count.length; i++) {
            p[i] = (double) count[i] / N;
        }
        for (int i = 0; i < count.length; i++) {
            m1 += p[i] * i;
            m2 += p[i] * i * i;
            m3 += p[i] * i * i * i;
        }
        double c0 = (m1 * m3 - m2*m2) / (m2 - m1 * m1);
        double c1 = (m1 * m2 - m3) / (m2 - m1 * m1);
        double d = (Math.sqrt(c1 * c1 - 4 * c0) - c1) / 2;
        double p0=0,p1=0;
        int index = 0;
        p0=(d - m1) / (2 * d + c1);
        for (int i = 0; i <count.length; i++) {
            p1+=p[i];
            if(p1==p0) {
                index=i;
                break;
            }
            else if(p1>p0) {
                index=i-1;
                break;
            }
        }
        return index;
    }

    public static double shang(int[] count, int t) {
        int pt = 0;
        for (int i = 0; i <= t; i++) {
            pt += count[i];
        }
        double h0 = 0, p;
        if (pt == 0)
            pt = 1;
        for (int i = 0; i <= t; i++) {
            p = (double) count[i] / pt;
            if (p != 0) {
                h0 += p * Math.log(p);
            }
        }
        double h1 = 0;
        pt = 0;
        for (int i = t + 1; i < count.length; i++) {
            pt += count[i];
        }
        for (int i = t + 1; i < count.length; i++) {
            p = (double) count[i] / pt;
            if (p != 0) {
                h1 += p * Math.log(p);
            }
        }
        return -(h0 + h1);
    }
}
