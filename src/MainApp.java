import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class MainApp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {

		// read image and convert it into Matrix
		String srcImagePath = "1.png";
		Mat srcImageMat = Imgcodecs.imread(srcImagePath);

		// create a circular structuring element
		int kernelSize = 3;
		Mat kernel = ImageUtil.getCircularKernel(kernelSize);

		// erode the image
		Mat erodedImageMat = ImageUtil.erode(srcImageMat, kernel);

		Imgcodecs.imwrite("2.png", erodedImageMat);

	}

}