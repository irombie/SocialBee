package havelsan.staj.SocialNetwork;

public class AnalyzeNode {

	private String title;
	private double degree;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getScore() {
		return degree;
	}
	public void setScore(Object object) {
		this.degree = (Double) object;
	}
	
	
}
