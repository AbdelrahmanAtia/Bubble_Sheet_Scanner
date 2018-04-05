import java.util.ArrayList;
import org.opencv.core.Mat;

public class Answer {

	private final static int radius = 16; // radius of answer circle
	private final static int width = 2 * radius; // width of square outside
													// circle
	private final static int height = 2 * radius; // height of square outside
													// circle
	private final static int white_circle_intenisity = 255 * width * height;
	private final static int black_circle_intenisity = 0 * width * height;
	private final static int avg_circle_intenisity = (white_circle_intenisity + black_circle_intenisity) * 90 / 100;
	private final static int horizontal_center_offset = 52;
	private final static int vertical_center_offset = 60;
	private static final int firstSectionX = 154; // x coordinate of center of
													// the top left circle of
													// first section
	private static final int secondSectionX = 563; // x coordinate of center of
													// the top left circle of
													// second section
	private static final int thirdSectionX = 973; // x coordinate of center of
													// the top left circle of
													// 3rd section
	private static final int sectionsY = 790;
	private ArrayList<String> studentAnswers = new ArrayList<>();
	private int studentGrade;
	private static final String[] modelAnswers = { "B", "C", "A", "A", "D", "A", "C", "C", "A", "C", "A", "B", "C", "C",
			"B", "A", "D", "B", "C", "B", "D", "C", "D", "B", "D", "C", "D", "D", "B", "C", "B", "B", "D", "C", "B",
			"C", "B", "C", "C", "A", "B", "B", "C", "C", "B" };

	public ArrayList<String> getStudentAnswers(Mat image) {
		ArrayList<String> firstSectionAnswers = getSectionAnswers(image, firstSectionX);
		ArrayList<String> secondSectionAnswers = getSectionAnswers(image, secondSectionX);
		ArrayList<String> thirdSectionAnswers = getSectionAnswers(image, thirdSectionX);
		studentAnswers.addAll(firstSectionAnswers);
		studentAnswers.addAll(secondSectionAnswers);
		studentAnswers.addAll(thirdSectionAnswers);
		return studentAnswers;
	}

	private ArrayList<String> getSectionAnswers(Mat image, int sectionXPos) {

		int x = sectionXPos; // x coordinate of center of the top left circle
								// section
		int y = sectionsY; // y coordinate of center of the top left circle
							// section
		ArrayList<String> sectionAnswers = new ArrayList<>();
		for (int s1 = 0; s1 < 15; s1++) {
			int count = 0; // number of shaded circles
			String ch = "";
			for (int m1 = 0; m1 < 4; m1++) {
				double intenisity = ImageUtil.calIntenisity(image, x - radius, y - radius, width, height);
				if (intenisity < avg_circle_intenisity) {
					if (m1 == 0) {
						ch = "A";
						count++;
					} else if (m1 == 1) {
						ch = "B";
						count++;
					} else if (m1 == 2) {
						ch = "C";
						count++;
					} else if (m1 == 3) {
						ch = "D";
						count++;
					}
				}
				x = x + horizontal_center_offset;
			}
			// for each question, only one circle should be shadded
			// otherwise, the student answer is wrong
			if (count == 1)
				sectionAnswers.add(ch);
			else
				sectionAnswers.add("error");

			x = sectionXPos; // returns x to first circle x value
			y = y + vertical_center_offset; // move y to the next raw
			count = 0; // reset the count
		}
		return sectionAnswers;
	}

	public int getStudentGrade(ArrayList<String> studentAnswers) {
		if (studentAnswers.size() != modelAnswers.length) {
			System.out.println("student answers array and model answer array are not equal is size");
			return -1;
		}
		for (int i = 0; i < studentAnswers.size(); i++) {
			if (studentAnswers.get(i).equals(modelAnswers[i]))
				studentGrade++;
		}
		return studentGrade;
	}

}
