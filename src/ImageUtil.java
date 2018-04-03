import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static Mat erode(Mat input, Mat kernel) {
		Mat output = new Mat();
		Imgproc.erode(input, output, kernel);
		return output;
	}

	public static Mat dilate(Mat input, Mat kernel) {
		Mat output = new Mat();
		Imgproc.dilate(input, output, kernel);
		return output;
	}

	public static Mat getCircularKernel(int kernelSize) {
		double kernelWidth = kernelSize * 2 + 1;
		double kernelHeight = kernelSize * 2 + 1;
		return Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(kernelWidth, kernelHeight),
				new Point(kernelSize, kernelSize));
	}

	public static Mat thresholdImage(Mat image) {
		Mat output = new Mat(image.rows(), image.cols(), image.type());
		Imgproc.threshold(image, output, 200, 255, Imgproc.THRESH_BINARY);
		return output;
	}

	public static Mat detectCircles(Mat image) {
		/*
		 * convert the image into a grayscale and blur it with a filter of
		 * kernel size 3
		 */
		Mat grayImage = new Mat();
		Imgproc.cvtColor(image.clone(), grayImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(grayImage, grayImage, new Size(3, 3));
		int lowThreshold = 30;
		Mat circles = new Mat();
		Imgproc.HoughCircles(grayImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1, grayImage.rows() / 8, 200, lowThreshold,
				0, 0);
		return circles;
	}

	public static ArrayList<Point> getCirclesCenters(Mat circles, int minRadius, int maxRadius, int minTopDistance) {
		ArrayList<Point> centers = new ArrayList<Point>();

		for (int i = 0; i < circles.cols(); i++) {
			Point center = new Point(circles.get(0, i)[0], circles.get(0, i)[1]);
			int radius = (int) Math.round(circles.get(0, i)[2]);
			if (radius > minRadius && radius < maxRadius && center.y > minTopDistance) {
				centers.add(center);
			}
		}
		return centers;
	}

	public static Mat wrap(Mat image, Point[] srcPoints, Point[] dstPoints) {
		MatOfPoint2f srcMat = new MatOfPoint2f(srcPoints[0], srcPoints[1], srcPoints[2], srcPoints[3]);
		MatOfPoint2f dstMat = new MatOfPoint2f(dstPoints[0], dstPoints[1], dstPoints[2], dstPoints[3]);
		Mat warpMat = Imgproc.getPerspectiveTransform(srcMat, dstMat);
		Imgproc.warpPerspective(image, image, warpMat, image.size());
		return image;
	}

	public static BufferedImage toBufferedImage(Mat matrix) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (matrix.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
		byte[] buffer = new byte[bufferSize];
		matrix.get(0, 0, buffer); // get all the pixels
		BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image;
	}

	/**
	 * @return the intenisity of the square at point(x,y) and has the given
	 *         width and height
	 */
	public static double calIntenisity(Mat image, int x, int y, int width, int height) {
		BufferedImage bufferedImage = toBufferedImage(image);
		double sum = 0;
		int yEnd = y + height;
		int xEnd = x + width;
		for (int i = y; i < yEnd; i++) {
			for (int j = x; j < xEnd; j++) {
				Color color = new Color(bufferedImage.getRGB(j, i));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				double avg = (red + green + blue) / 3.0;
				sum = sum + avg;
			}
		}
		return sum;
	}
}