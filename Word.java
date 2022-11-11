package Assignment;

public class Word {
	String eng;
	String kor;
	int asked = 0; // 출제회수
	int wrong = 0; // 오답회수
	
	public Word(String eng, String kor)
	{
		super();
		this.eng = eng;
		this.kor = kor;
	}

	@Override
	public String toString() {
		return eng+" : "+kor; 
	}
	
	public String getEng() {
		return eng;
	}

	public String getKor() {
		return kor;
	}

	public void setEng(String eng) {
		this.eng = eng;
	}

	public void setKor(String kor) {
		this.kor = kor;
	}
}

