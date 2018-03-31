import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static Mat erode(Mat input, Mat kernel) {
		Mat outputImageMat = new Mat();
		Imgproc.erode(input, outputImageMat, kernel);
		return outputImageMat;
	}
	
	public static Mat dilate(Mat input, Mat kernel) {
		Mat outputImageMat = new Mat();
		Imgproc.dilate(input, outputImageMat, kernel);
		return outputImageMat;
	}

	public static Mat getCircularKernel(int kernelSize) {
		double kernelWidth = kernelSize * 2 + 1;
		double kernelHeight = kernelSize * 2 + 1;
		return Imgproc.getStructuringElement(
						Imgproc.MORPH_ELLIPSE, 
						new Size(kernelWidth, kernelHeight),
						new Point(kernelSize, kernelSize));
	}

}
