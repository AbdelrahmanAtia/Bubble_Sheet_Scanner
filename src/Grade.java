import java.util.ArrayList;
import org.opencv.core.Mat;

public class Grade {

	final static int radius = 16; // radius of answer circle
	final static int width = 2 * radius; // width of square outside circle
	final static int height = 2 * radius; // height of square outside circle
	final static int white_circle_intenisity = 255 * width * height;
	final static int black_circle_intenisity = 0 * width * height;
	final static int avg_circle_intenisity = (white_circle_intenisity + black_circle_intenisity) * 90 / 100;
	final static int horizontal_center_offset = 52;
	final static int vertical_center_offset = 60;

	private static final String[] modelAnswers = { "B", "C", "A", "A", "D", "A", "C", "C", "A", "C", "A", "B", "C", "C",
			"B", "A", "D", "B", "C", "B", "D", "C", "D", "B", "D", "C", "D", "D", "B", "C", "B", "B", "D", "C", "B",
			"C", "B", "C", "C", "A", "B", "B", "C", "C", "B" };

	ArrayList<Integer> degrees = new ArrayList<>();

	public static void calculateGrade(Mat image) {
		// first answer section
		int x = 154; // x coordinate of center
		int y = 790; // y coordinate of center

		ArrayList<String> studentAnswers = new ArrayList<>();

		for (int s1 = 0; s1 < 15; s1++) {
			int count = 0; // number of shaded circles
			String ch = "";
			for (int m1 = 0; m1 < 4; m1++) {
				double intenisity = ImageUtil.calIntenisity(image, x - radius, y - radius, width, height);
				if (intenisity < avg_circle_intenisity) {
					if (m1 == 0) {
						ch = "A";
						count++;
					}
					else if (m1 == 1) {
						ch = "B";
						count++;
					}
					else if (m1 == 2) {
						ch = "C";
						count++;
					}
					else if (m1 == 3) {
						ch = "D";
						count++;
					}
				}
				x = x + horizontal_center_offset;
			}
			// for each question, only one circle should be shadded
			//otherwise, the student answer is wrong
			if (count == 1)
				studentAnswers.add(ch);
			else
				studentAnswers.add("error");

			x = 154; // returns x to first circle x value
			y = y + vertical_center_offset; // move y to the next raw
			count = 0; // reset the count
		}
	}

}
