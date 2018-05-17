# java对医学图像算法的一些实现

- 主要实现了一些常见操作
- 主要测试代码如下所示
```
import com.peiwei.toolKit.toolOfBuff;
import com.peiwei.toolKit.toolOfFile;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
//        String name = "1.jpg";
//        String save = "2bc.jpg";
//        toolOfFile.hist(name,save);

        String name = "2.jpg";
        String save = "2cc.jpg";
        String type = "jpg";
        BufferedImage image = toolOfBuff.readImage(name);
//        image = toolOfBuff.logTransform(image,35,3) ; //非线性转换
//        image = toolOfBuff.indTransform(image,80,10,20);//指数转换
//        image = toolOfBuff.partGaryTransform(image,1,0);//部分灰度转换
//        image = toolOfBuff.Horizontal(image);//水平镜像
//        image = toolOfBuff.Vertical(image);//垂直镜像
//        image = toolOfBuff.verticalAndHorizontal(image);//水平垂直镜像
//        image = toolOfBuff.translation(image,-250,-250,true);//平移
//        image = toolOfBuff.rotateToBig(image,60);//旋转 原图-新图
//        image = toolOfBuff.rotateSame(image,60);//旋转 新图-原图
//        image = toolOfBuff.zoom(image, 0.5, 1);//放大 缩小
//        image = toolOfBuff.partAvgNarrow(image,0.5);//局部均值缩小
//        BufferedImage image1 = toolOfBuff.readImage("11.jpg");
//        BufferedImage image2 = toolOfBuff.readImage("12.jpg");
//        image = toolOfBuff.add(image1,image2,3);//图片相加

//        image = toolOfBuff.avgWaveFilter(image,7);//均值滤波 后面参数是选择 不同方法 或者算子
//        image = toolOfBuff.midWaveFilter(image,4);//中值滤波
//        image = toolOfBuff.edgeDetection(image,5);//边缘检测
        image =  toolOfBuff.grayFg(image,128,4);//灰度分割
        toolOfFile.saveImage(image, type, save);
        
// openCV 操作  
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        String imageFilePath = "2.jpg";
//        Mat mat = openCVtool.read(imageFilePath);
//        mat = openCVtool.gradient(mat);
//        openCVtool.save("111.jpg",mat);

    }
}
```
##  代码仅供参考 若有bug 欢迎指出 谢谢
