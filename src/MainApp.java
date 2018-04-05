import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

public class MainApp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {

		String imagePath = "1.png";
		int kernelSize = 3;

		Mat image = Imgcodecs.imread(imagePath);
		Mat kernel = ImageUtil.getCircularKernel(kernelSize);
		image = ImageUtil.erode(image, kernel);
		Imgcodecs.imwrite("eroded.png", image);
		kernel = ImageUtil.getCircularKernel(kernelSize + 1);
		image = ImageUtil.dilate(image, kernel);
		Imgcodecs.imwrite("eroded_dilated.png", image);
		Mat circles = ImageUtil.detectCircles(image);
		// get the two bottom circles
		ArrayList<Point> centers = ImageUtil.getCirclesCenters(circles, 20, 60, 1430);
		Point leftPoint = centers.get(0);
		Point rightPoint = centers.get(1);
		// swap circles if they are not ordered
		if (leftPoint.x > rightPoint.x) {
			Point temp = leftPoint;
			leftPoint = rightPoint;
			rightPoint = temp;
		}
		// System.out.println(leftPoint.toString());
		// System.out.println(rightPoint.toString());

		double xa = leftPoint.x, ya = leftPoint.y, xb = rightPoint.x, yb = rightPoint.y;
		final int h = 108; // vertical distance between center of first circle and the parallel line
		final int m = 118;
		final double A = 1190; // length of left line
		final double B = 992.395; // length of bottom line
		final double C = 1180.4; // length of right line
		final double D = 993.68; // length of top line

		double slope; // slope of the line connecting the centers of the two bottom black circles
		double theta; // angle that the line which connects the centers of the two circles make with the horizontal

		slope = (yb - ya) / (xb - xa);
		theta = Math.atan(slope); // angle between the line connecting centers of the two circles and horizintal in radians
		// (x1, y1) (x2, y2), (x3,y3) (x4,y4) are the coordinates of the corners
		// of the slant paper..
		double x3 = xa + h * Math.sin(theta) - m * Math.cos(theta);
		double y3 = ya - h * Math.cos(theta) - m * Math.sin(theta);

		double x1 = x3 + A * Math.sin(theta);
		double y1 = y3 - A * Math.cos(theta);

		double x4 = x3 + B * Math.cos(theta);
		double y4 = y3 + B * Math.sin(theta);

		double x2 = x1 + D * Math.cos(theta);
		double y2 = y1 + D * Math.sin(theta) + 10;

		// srcPoints array contains corner coordinates of the slant
		Point[] srcPoints = new Point[4];
		srcPoints[0] = new Point(x1, y1);
		srcPoints[1] = new Point(x2, y2);
		srcPoints[2] = new Point(x3, y3);
		srcPoints[3] = new Point(x4, y4);

		// dstPoints array contains the new coordinates which the slant paper corner
		// coordinates will be mapped to.
		Point[] dstPoints = new Point[4];

		dstPoints[0] = new Point(0, 0);
		dstPoints[1] = new Point(image.width() - 1, 0);
		dstPoints[2] = new Point(0, image.height() - 1);
		dstPoints[3] = new Point(image.width() - 1, image.height() - 1);

		// wrap the image from srcPoints to dstPoints
		image = ImageUtil.wrap(image, srcPoints, dstPoints);
		Imgcodecs.imwrite("eroded_dilated_wrapped.png", image);

		image = ImageUtil.erode(image, kernel);
		Imgcodecs.imwrite("eroded_dilated_wrapped_eroded.png", image);

		image = ImageUtil.thresholdImage(image);
		Imgcodecs.imwrite("eroded_dilated_wrapped_eroded_thresholded.png", image);
		
		Answer answer = new Answer();	
		ArrayList<String> studentAnswers = answer.getStudentAnswers(image);
		System.out.println("student grade = " + answer.getStudentGrade(studentAnswers) + " / 45");
		
		
	}
}