/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nadim Aseq A Arman (process()) Coursera ID: 6484785
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

public class MP1 {

    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
        "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
        "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
        "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
        "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
        "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
        "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
        "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
        "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
        "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        //System.out.println(this.getIndexes()[50]);
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(this.inputFileName)));
        String str = new String();
        ArrayList<String> lines = new ArrayList<String>();
        Map<String, String> setTitleIndex = new TreeMap<String, String>();
        Map<String, Integer> words = new TreeMap<String, Integer>();
        Integer count = 0;
        //Read lines from the input.text
        while ((str = in.readLine()) != null) {
            setTitleIndex.put(Integer.toString(count), str);
            count++;
        }
        /* Generates Map with the words taken from all titles*/
        for (int i = 0; i < this.getIndexes().length; i++) {
            String linesSel = this.getIndexes()[i].toString();
            StringTokenizer st = new StringTokenizer(setTitleIndex.get(linesSel), this.delimiters);
            while (st.hasMoreTokens()) {
                String token = st.nextToken().toLowerCase();
                if (!Arrays.asList(this.stopWordsArray).contains(token)) {
                    if (words.containsKey(token)) {
                        words.put(token, words.get(token) + 1);
                    } else {
                        words.put(token, 1);
                    }
                }
            }
        }

        count = 0;
        for (Entry<String, Integer> entry : entriesSortedByValues(words)) {
            if (count == 20) {
                break;
            }
            ret[count] = entry.getKey();
            count++;
        }

        return ret;
    }
    /* This class compares values and values of the map to sort */

    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());

                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("MP1 <User ID>");
        } else {
            String userName = args[0];
            String inputFileName = "input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item : topItems) {
                System.out.println(item);
            }
        }
    }

}
