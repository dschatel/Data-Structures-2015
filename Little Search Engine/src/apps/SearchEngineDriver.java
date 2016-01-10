package apps;

import search.*;


import java.io.*;

public class SearchEngineDriver {
	
	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		System.out.print("Enter document file name => ");
		String document = keyboard.readLine();
		System.out.print("Enter noisewords file name => ");
		String noiseWords = keyboard.readLine();
		
		LittleSearchEngine engine = new LittleSearchEngine();
		
		engine.makeIndex(document, noiseWords);
		
		String quit = "";
		
		while(!quit.equals("quit")) {
		System.out.print("Enter keyword one => ");
		String keyOne = keyboard.readLine();
		System.out.print("Enter keyword two => ");
		String keyTwo = keyboard.readLine();
		
		System.out.println("Result: " + engine.top5search(keyOne, keyTwo));
		System.out.print("Type 'quit' to quit, otherwise hit enter =>");
		quit = keyboard.readLine();
		}
		
	}

}
