/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Jose Flores
 * jf33676
 * 16325
 * Nikhil Jalla
 * nj5473
 * 16325
 * Slip days used: <0>
 * Git URL: https://github.com/JoseFlores8882/EE422C_Project_3
 * Spring 2020
 */


package assignment3;
import java.util.*;
import java.io.*;
import java.lang.StringBuilder;

public class Main {
	
	public static Set <String> dictionary;
	public static ArrayList<String> dfsLadder;
	// static variables and constants only here.
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		while(true) 
		{
			ArrayList<String> input = parse(kb);
			if(input.size()==0)			//if empty array, input was quit
			{
				break;
			}
			ArrayList<String> ladder = getWordLadderBFS(input.get(0),input.get(1));
			printLadder(ladder);
			ladder = getWordLadderDFS(input.get(0),input.get(1));
			printLadder(ladder);
		}
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		dictionary = makeDictionary();
		dfsLadder = new ArrayList<>();
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String word;
		ArrayList<String> inWords = new ArrayList<String>();
		word = keyboard.next();
		if(word.contentEquals("/quit")) 
		{
			return inWords;
		}
		else 
		{
			inWords.add(word.toUpperCase());
			word = keyboard.next();
			inWords.add(word.toUpperCase());
			return inWords;
		}
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		boolean found;
		dfsLadder.add(start);
		Set<String> dictCopy = makeDictionary();
		dictCopy.remove(start.toUpperCase());
		for(int i = 0; i < start.length(); i++) {
			if(start.charAt(i) != end.charAt(i))
			{
				char endChar = end.charAt(i);
				StringBuilder next = new StringBuilder(start);
				next.setCharAt(i, endChar);
				if(dictCopy.remove(next.toString().toUpperCase())) {
					dfsLadder.add(next.toString());
					found = getWordLadderDFS(next.toString(), end, dictCopy, i);
					if(found) return dfsLadder;
					else dfsLadder.remove(1);
				}
			}
			for(char j = 'a'; j < 'z'; j++) {
				if(start.charAt(i) != j) {
					StringBuilder next = new StringBuilder(start);
					next.setCharAt(i, j);
					if(dictCopy.remove(next.toString().toUpperCase())) {
						dfsLadder.add(next.toString());
						found = getWordLadderDFS(next.toString(), end, dictCopy, 0);
						if(found) return dfsLadder;
						else dfsLadder.remove(1);
					}
				}
			}
		}
		if(dfsLadder.size() == 1) {
			dfsLadder.add(end);
		}
		return dfsLadder;
	}
	
	private static boolean getWordLadderDFS(String start, String end, Set<String> dictCopy, int index) {
		boolean found;
		if(start.contentEquals(end)) {
			return true;
		}
		int oneDiff = oneDiffChecker(start,end);
		if(oneDiff!=-1) {
			StringBuilder secondToLast = new StringBuilder(start);
			secondToLast.setCharAt(oneDiff, end.charAt(oneDiff));
			dfsLadder.add(secondToLast.toString());
			return true;
		}
		for(int i = 0; i < start.length(); i++) {
			if(i != index && start.charAt(i) != end.charAt(i))
			{
				char endChar = end.charAt(i);
				StringBuilder next = new StringBuilder(start);
				next.setCharAt(i, endChar);
				if(dictCopy.remove(next.toString().toUpperCase())) {
					dfsLadder.add(next.toString());
					found = getWordLadderDFS(next.toString(), end, dictCopy, i);
					if(found) return true;
					else dfsLadder.remove(dfsLadder.size()-1);
				}
			}
			for(char j = 'a'; j < 'z'; j++) {
				if(start.charAt(i) != j && i != index) {
					StringBuilder next = new StringBuilder(start);
					next.setCharAt(i, j);
					if(dictCopy.remove(next.toString().toUpperCase())) {
						dfsLadder.add(next.toString());
						found = getWordLadderDFS(next.toString(), end, dictCopy, i);
						if(found) return true;
						else dfsLadder.remove(dfsLadder.size()-1);
					}
				}
			}
		}
		return false;
	}
	public static int oneDiffChecker(String currentWord, String end) {
		int numSame = 0;
		int index = 0;
		for(int i =0;i < end.length();i++)
		{
			if(currentWord.charAt(i)==end.charAt(i))
			{
				numSame++;
			}
			else
			{
				index = i;
			}
		}
		if((end.length()-numSame)==1)
		{
			return index;
		}
		else 
		{
			return -1;
		}
		
	}
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	String startTemp = start.toUpperCase();
    	String endTemp = end.toUpperCase();
    	HashMap <String,String> graph = new HashMap<String, String>();
    	Iterator<String> it = dictionary.iterator();
		while(it.hasNext())
		{
			graph.put(it.next(), "");		//create unmapped graph of dictionary words
		}
		Queue<String> queue = new LinkedList<String>();
		graph.put(startTemp,"start");           //mark start as discovered
		queue.add(startTemp);                   //add to queue
		while(!queue.isEmpty())             //begin BFS
		{
			String checkParent = queue.remove();	//get head of queue
			if(checkParent.contentEquals(endTemp))      //if found a path to end
			{
				ArrayList<String> ladder = new ArrayList<String>();
				String path = endTemp;
				while(!path.equals("start"))    	//from end all the way to start
				{
					ladder.add(path);
					path = graph.get(path);			//traverse map by getting each string's parent string
				}
				Collections.reverse(ladder);        //reverse the ladder
				return ladder;
			}
			char[] parentWord = checkParent.toCharArray();
			for(int i=0;i < parentWord.length;i++)
			{
				char tempChar = parentWord[i];      //store character we are modifying
				for(char letter = 'A';letter <= 'Z';letter++)	//for all possible 1 letter change combinations at spot i
				{
					parentWord[i] = letter;                     //insert letter
					String checkWord = new String(parentWord);
					if(dictionary.contains(checkWord) && graph.get(checkWord).contentEquals("") ) 	//if not already mapped to graph(not visited = empty string) and valid word in the dictionary
					{
						graph.put(checkWord,checkParent);                                   //map to graph with parent word, this new word is now considered visited
						queue.add(checkWord);                                               //add to queue as a new parent word to check
					}
				}
				parentWord[i] = tempChar;           //restore character, move on to next
			}
		} 
		//BFS failed to find a ladder 
		ArrayList<String> fail = new ArrayList<String>();
		fail.add(start);
		fail.add(end);
		return fail; // replace this line later with real return
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		StringBuilder output;
        if(ladder.size() == 2) {
            output = new StringBuilder("no word ladder can be found between ");
        } else {
            output = new StringBuilder("a " + Integer.toString(ladder.size()-2) + "-rung ladder exists between ");
        }
        output.append(ladder.get(0) + " and " + ladder.get(ladder.size()-1) + ".");
        System.out.println(output.toString());
        if(ladder.size() != 2) {
            Iterator<String> iterator = ladder.iterator();
            while(iterator.hasNext())
            {
                System.out.println(iterator.next().toLowerCase());
            }
        }
	}
	// TODO
	// Other private static methods here


	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}

