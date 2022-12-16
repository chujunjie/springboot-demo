package com.example.springbootdemo.utils;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 二维码工具类
 *
 * @author chujunjie
 * @date create in 14:15 2022/12/15
 */
public class QrCodeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeUtils.class);

    private static final int BLACK = Color.BLACK.getRGB();
    private static final int WHITE = Color.WHITE.getRGB();

    private static final int DEFAULT_QR_SIZE = 183;

    private static final String DEFAULT_QR_FORMAT = "png";
    private static final String UTF8 = "utf-8";

    private static final byte[] EMPTY_BYTES = new byte[0];

    /**
     * 生成固定大小且格式为png的二维码
     *
     * @param content 二维码中要包含的信息
     * @return
     */
    public static byte[] createQrCode(String content) {
        return createQrCode(content, DEFAULT_QR_SIZE, DEFAULT_QR_FORMAT, null);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码中要包含的信息
     * @param size      size
     * @param extension 文件格式扩展
     * @return
     */
    public static byte[] createQrCode(String content, int size, String extension) {
        return createQrCode(content, size, extension, null);
    }

    /**
     * 生成带图片的二维码
     *
     * @param content   二维码中要包含的信息
     * @param size      大小
     * @param extension 文件格式扩展
     * @param insertImg 中间的logo图片
     * @return
     */
    public static byte[] createQrCode(String content, int size, String extension, Image insertImg) {
        ByteArrayOutputStream byteStream = null;
        try {
            Map<EncodeHintType, Object> hints = Maps.newHashMap();
            hints.put(EncodeHintType.CHARACTER_SET, UTF8);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            // 使用信息生成指定大小的点阵
            BitMatrix m = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);

            // 去掉白边
            m = updateBit(m, 0);

            int width = m.getWidth();
            int height = m.getHeight();

            // 将BitMatrix中的信息设置到BufferedImage中，形成黑白图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    image.setRGB(i, j, m.get(i, j) ? BLACK : WHITE);
                }
            }
            if (insertImg != null) {
                // 插入中间的logo图片
                insertImage(image, insertImg, m.getWidth());
            }
            // 将因为去白边而变小的图片再放大
            image = zoomInImage(image, size, size);
            byteStream = new ByteArrayOutputStream();
            ImageIO.write(image, extension, byteStream);

            return byteStream.toByteArray();
        } catch (Exception e) {
            LOGGER.error("生成二维码异常，e", e);
        } finally {
            if (byteStream != null) {
                try {
                    byteStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return EMPTY_BYTES;
    }

    /**
     * 自定义二维码白边宽度
     *
     * @param matrix
     * @param margin
     * @return
     */
    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        // 获取二维码图案的属性
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        // 按照自定义边框生成新的BitMatrix
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        // 循环，将二维码图案绘制到新的bitMatrix中
        for (int i = margin; i < resWidth - margin; i++) {
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * 缩放图片
     *
     * @param originalImage
     * @param width
     * @param height
     * @return
     */
    private static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    /**
     * 设置logo图片
     *
     * @param source
     * @param insertImg
     * @param size
     */
    private static void insertImage(BufferedImage source, Image insertImg, int size) {
        try {
            int width = insertImg.getWidth(null);
            int height = insertImg.getHeight(null);
            // logo设为二维码的六分之一大小
            width = width > size / 6 ? size / 6 : width;
            height = height > size / 6 ? size / 6 : height;
            Graphics2D graph = source.createGraphics();
            int x = (size - width) / 2;
            int y = (size - height) / 2;
            graph.drawImage(insertImg, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
