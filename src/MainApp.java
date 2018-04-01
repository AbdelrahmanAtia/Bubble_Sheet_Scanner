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
		//Imgcodecs.imwrite("eroded.png", image);
		kernel = ImageUtil.getCircularKernel(kernelSize + 1);
		image = ImageUtil.dilate(image, kernel);
		//Imgcodecs.imwrite("eroded_dilated.png", image);
		Mat circles = ImageUtil.detectCircles(image);
		// get the two bottom circles
		ArrayList<Point> centers = ImageUtil.getCirclesCenters(circles, 20, 60, 1430);
		Point leftPoint = centers.get(0);
		Point rightPoint = centers.get(1);
		//swap circles if they are not ordered
		if (leftPoint.x > rightPoint.x) {
			Point temp = leftPoint;
			leftPoint = rightPoint;
			rightPoint = temp;
		}
		//System.out.println(leftPoint.toString());
		//System.out.println(rightPoint.toString());
		
		double xa = leftPoint.x, ya = leftPoint.y, xb = rightPoint.x, yb = rightPoint.y;
	
	}
	
	

}