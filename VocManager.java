package Assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map.Entry;

import java.util.*;

public class VocManager {
	private String userName;
	private Map<String, Word> voc = new HashMap<>();
	private ArrayList<String> korArray = new ArrayList<>();
	private int number = 0;
	static Scanner scan = new Scanner(System.in);
	
	public VocManager(String userName) {
		super();
		this.userName = userName;
	}
	
	public void addWord(Word word)
	{
		String temp = word.getEng();
		voc.put(temp, word);
		korArray.add(word.kor);
		number++;
	}
	
	public void makeVoc(String fileName) {
	
		try(Scanner scan = new Scanner(new File(fileName))) {
			while(scan.hasNextLine())
			{
				String str = scan.nextLine();
				String[] temp = str.split("\t");
				this.addWord(new Word(temp[0].trim(), temp[1].trim()));
			}
			System.out.println(userName+"의 단어장이 생성되었습니다.");
//			System.out.println(voc.size());
//			System.out.println(voc);
			this.menu();
		}catch(FileNotFoundException e) {
			System.out.println(userName+"의 단어장이 생성되지 않았습니다.\n파일명을 확인하세요.");
		}
	}
	
	public void menu()
	{
		int choice = 0;
		while(choice != 2)
		{
			System.out.println("\n-----"+userName+"의 영단어 퀴즈 -----");
			System.out.println("1) 주관식 퀴즈 2) 객관식 퀴즈 3) 오답노트 4) 단어검색 5) 종료");
			System.out.print("메뉴를 선택하세요 : ");
			choice = scan.nextInt();
			scan.nextLine();
			System.out.println();
			
			switch(choice)
			{
			case 1:
				subjective();
				break;
			case 2:
				objective();
				break;
			case 3:
				break;
			case 4:
				searchVoc();
				break;
			case 5:
				System.out.println(userName+"의 단어장 프로그램을 종료합니다.");
				break;
			}
		}
	}
	
	public void subjective()
	{	
		// 중복 없는 정수 값 저장
		int randArray[] = new int[10];
		Random r = new Random();
		for(int i=0; i<10; i++)
		{
			randArray[i] = r.nextInt(number);
			for(int j=0; j<i; j++)
			{
				if(randArray[i] == randArray[j]) i--;
			}
		}
		
		int right = 0;
		long starttime = System.nanoTime();
		for(int i=0; i<10; i++)
		{
			System.out.println("----- 주관식 퀴즈 " + (i+1) + "번 -----");
			
			// randArray 배열(중복 없음)에 저장된 인덱스 번째를 출제함.
			Set<Map.Entry<String, Word>> entrySet = voc.entrySet();
			Iterator<Map.Entry<String, Word>> entryIterator = entrySet.iterator();
			Map.Entry<String, Word> entry = null;
			int entrycount = 0;
			while(entryIterator.hasNext())
			{
				entry = entryIterator.next();
//				System.out.println("randArray index : " + randArray[i] + " entrycount : " + entrycount);
				if(++entrycount == randArray[i]) break;
			}
			
			String tmp = entry.getValue().kor;
			System.out.println(tmp + "의 뜻은 무엇입니까?");
			System.out.print("답을 입력하세요 : ");
			String answer = scan.next();
			if(voc.containsKey(answer) && answer.equals(entry.getValue().eng)) // 시간 줄이기 위해
			{
				System.out.println("정답입니다.");
				right++;
			}
			else
			{
				// 뜻이 같은 영단어 검사
				Set<Map.Entry<String, Word>> entrySet2 = voc.entrySet();
				Iterator<Map.Entry<String, Word>> entryIterator2 = entrySet2.iterator();
				Map.Entry<String, Word> entry2 = null;
				boolean flag = false;
				while(entryIterator2.hasNext())
				{
					entry2 = entryIterator2.next();
					if(answer.equals(entry2.getValue().kor))
					{
						System.out.println("정답입니다.");
						right++;
						flag = true;
						break;
					}
				}
				if(!flag)
					System.out.println("틀렸습니다. 정답은 " + entry.getValue().eng + "입니다.");
			}
			scan.nextLine();
		}
		long endtime = System.nanoTime();
		System.out.printf(userName + "님 10문제 중 %d" + "개 맞추셨고, 총 %d" + "초 소요되었습니다.", right, (endtime-starttime)/1000000000);
	}
	
	public void objective()
	{	
		// 중복 없는 정수 값 저장
		int randArray[] = new int[10];
		Random r= new Random();
		for(int i=0; i<10; i++)
		{
			randArray[i] = r.nextInt(number);
			for(int j=0; j<i; j++)
			{
				if(randArray[i] == randArray[j]) i--;
			}
		}
		
		long starttime = System.nanoTime();
		int right = 0;
		String outArray[] = new String[4]; // 선지 배열
		String[] krArray = korArray.toArray(new String[korArray.size()]); // 한글 뜻 배열
		
		for(int i=0; i<10; i++)
		{
			System.out.println("----- 객관식 퀴즈 " + (i+1) + "번 -----");
			Set<Map.Entry<String, Word>> entrySet = voc.entrySet();
			Iterator<Map.Entry<String, Word>> entryIterator = entrySet.iterator();
			Map.Entry<String, Word> entry = null;
			int entrycount = 0;

			while(entryIterator.hasNext())
			{
				entry = entryIterator.next();
				if(++entrycount == randArray[i]) break;
			}
			String tmp = entry.getKey();
			System.out.println(tmp + "의 뜻은 무엇일까요?");
			for(int q=0; q<4; q++)
			{
				outArray[q] = null;
			}
			
			outArray[0] = entry.getValue().kor;
			for(int j=1; j<4; j++)
			{
				outArray[j] = krArray[r.nextInt(number)];
				for(int k=0; k<j; k++)
				{
					for(int l=0; l<k; l++)
					{
						if(outArray[l].equals(outArray[k])) j--;
					}
				}
			}
			List<String> outList = new ArrayList<String>(Arrays.asList(outArray));
			Collections.shuffle(outList);
			outArray = outList.toArray(new String[outList.size()]);
			
			for(int l=0; l<outArray.length; l++)
			{
				System.out.println(l+1 + ") " + outArray[l]);
			};
			int answer = 0;
			try {
				answer = scan.nextInt();
			}
			catch(InputMismatchException e) {
				scan.nextLine();
			}
			
			if(answer <1 || answer>4)
			{
				System.out.println("유효하지 않은 입력입니다.");
				i--;
			}
			else
			{
				if(outArray[answer-1].equals(entry.getValue().kor))
				{
					System.out.println("정답입니다.");
					right++;
				}
				else
				{
					int ans = 0;
					for(int k=0; k<4; k++)
					{
						if(outArray[answer-1].equals(entry.getValue().kor))
						{
							ans = k;
							break;
						}
					}
					ans += 1;
					System.out.println("틀렸습니다. 정답은 " + ans + "번입니다.");
				}
				scan.nextLine();
			}	
		}
		
		long endtime = System.nanoTime();
		System.out.printf(userName + "님 10문제 중 %d" + "개 맞추셨고, 총 %d" + "초 소요되었습니다.", right, (endtime-starttime)/1000000000);
	}
	
	public void wrongAnswers()
	{
		
	}
	
	public void searchVoc()
	{
		System.out.println("----- 단어 검색 -----");
		System.out.print("검색할 단어를 입력하세요 : ");
		String sWord = scan.nextLine();
		sWord = sWord.trim();
		if(voc.containsKey(sWord))
		{
			System.out.println(sWord + "뜻 : " + voc.get(sWord));
			System.out.println("출제회수 : " + "오답회수 : ");
		}
		else
		{
			System.out.println("단어장에 등록된 단어가 아닙니다.");
		}
	}
}
