package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		
		HashMap<String,Occurrence> keyWords = new HashMap<String,Occurrence>();
		
		Scanner sc = new Scanner(new File(docFile));
		
		while(sc.hasNext()) {
			String word = getKeyWord(sc.next());
			if (word != null) {
				if (!keyWords.containsKey(word)) {
					Occurrence o = new Occurrence (docFile, 1);
					keyWords.put(word, o);
				}
				else if (keyWords.containsKey(word)) {
					keyWords.get(word).frequency++;
				}
			}
			
		}
		
		return keyWords;
	}
	
	private void printHashMap (HashMap<String, ArrayList<Occurrence>> hash) {
		for (String key : hash.keySet()) {
			System.out.println(key + " " + hash.get(key));
		}
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		
		for (String key: kws.keySet()) {
			Occurrence o = kws.get(key);
			ArrayList<Occurrence> arr = keywordsIndex.get(key);
			
			if (arr == null) 
				arr = new ArrayList<Occurrence>();
			arr.add(o);
			insertLastOccurrence(arr);
			
			keywordsIndex.put(key, arr);
			
		}
		
	//	for (String key : keywordsIndex.keySet() ) {
	//		System.out.println(key + " " + keywordsIndex.get(key));
		//}
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		
		//make whole word lowercase
		//Iterate over every character in string
		//If not a letter, check character following it. If character following is a letter, return null
		//Else make a new word that is a substring from start of word to i
		//If substring is a noise word, return null, else return word
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		String newWord = word.toLowerCase();
		
		for (int i = 0; i < newWord.length(); i++) {
			if (!Character.isLetter(newWord.charAt(i))) {
				if (!isPunctuation(newWord.charAt(i)) || (i+1 < newWord.length() && Character.isLetter(newWord.charAt(i+1))))
					return null;
				else if (isPunctuation(newWord.charAt(i))){
					for (int j = i; j < newWord.length(); j++) {
						if (Character.isLetter(newWord.charAt(j)))
							return null;
					}
					newWord = newWord.substring(0, i);
					break;
				}
			}
		}
		if (noiseWords.containsKey(newWord) || newWord.isEmpty())
			return null;
		else
			return newWord;
	}
	
	private boolean isPunctuation (char ch) {
		if (ch == '.' || ch == ',' || ch == '?' || ch == ':' || ch == ';' || ch == '!')
			return true;
		return false;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
				
		Occurrence o = occs.get(occs.size()-1);
		int lo = 0;
		int hi = occs.size() -2;
		int mid = (lo + hi) / 2;
		
		while (lo <= hi) {
			mid = (lo + hi) / 2;
			
			if (o.frequency > occs.get(mid).frequency) {
				indices.add(mid);
				hi = mid - 1;
			}
			else if (o.frequency < occs.get(mid).frequency) {
				indices.add(mid);
				lo = mid + 1;
			}
			else if (o.frequency == occs.get(mid).frequency) {
				indices.add(mid);
				break;
			}
						
		}
		
		if(occs.get(mid).frequency <= o.frequency) {
			for (int i = occs.size()-1; i > mid; i--) {
				occs.set(i, occs.get(i-1));
			}
		
			occs.set(mid, o);
		}
		return indices;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		//Get the ordered arraylists for both keywords
		//iterate thru both of them
		//If doc1 > doc2 - if topfive doesn't contain doc1, add doc1. 
		//increment doc1
		//If doc2 > doc1 -- if topfive doesn't contain doc2, add doc2.
		//increment doc2
		//If doc1 == doc2 -- if topfive doesn't contain doc1, add doc1.
		//increment both
		//Do this until arraylists are exhausted or new arraylist has 5 items
		//Return new arraylist
		
		ArrayList<String> topFive = new ArrayList<String>();
		
		ArrayList<Occurrence> arrOne = keywordsIndex.get(kw1.toLowerCase());
		ArrayList<Occurrence> arrTwo = keywordsIndex.get(kw2.toLowerCase());
		int i = 0;
		int j = 0;
		
		if (arrOne == null && arrTwo == null)
			return topFive;
		else if (arrOne == null) {
			while (j < arrTwo.size() && topFive.size() < 5)	{
				topFive.add(arrTwo.get(j).document);
				j++;
			}
				
		}
		else if (arrTwo == null) {
			while (i < arrOne.size() && topFive.size() < 5) {
				topFive.add(arrOne.get(i).document);
				i++;
			}
			
		}
		else {	
		
			while ((i < arrOne.size() && j  < arrTwo.size()) && topFive.size() < 5) {
			
				if (arrOne.get(i).frequency > arrTwo.get(j).frequency) {
					if (!topFive.contains(arrOne.get(i).document))
					topFive.add(arrOne.get(i).document);
					i++;
				}
				else if (arrTwo.get(j).frequency > arrOne.get(i).frequency) {
					if (!topFive.contains(arrTwo.get(j).document))
					topFive.add(arrTwo.get(j).document);
				j++;
				}
				else if (arrOne.get(i).frequency == arrTwo.get(j).frequency) {
					if (!topFive.contains(arrOne.get(i).document))
					topFive.add(arrOne.get(i).document);
				j++;
			}
			
			}
		
			if (i != arrOne.size()) {
			while (i < arrOne.size() && topFive.size() < 5) {
				if(!topFive.contains(arrOne.get(i).document))
					topFive.add(arrOne.get(i).document);
				i++;
			}
			}
			else if (j != arrTwo.size()) {
				while (j < arrTwo.size() && topFive.size() < 5) {
				if(!topFive.contains(arrTwo.get(j).document))
					topFive.add(arrTwo.get(j).document);
				j++;
				}
			}
		}
		
		return topFive;
	}
}
