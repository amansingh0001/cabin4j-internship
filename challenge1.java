import java.util.Scanner;
import edu.stanford.nlp.simple.*;

public class IntentIdentification {
    
    // define the function to identify the intent of a sentence
    public static String identifyIntent(String sentence) {
        String[] keywordsPositive = {"good", "great", "happy", "excited"};
        String[] keywordsNegative = {"bad", "terrible", "sad", "angry"};
        int polarity = 0;
        Document doc = new Document(sentence);
        for (Sentence sent : doc.sentences()) {
            for (Token token : sent.tokens()) {
                if (token.lemma().contains("not") || token.lemma().contains("n't")) {
                    polarity *= -1;
                } else if (token.tag().startsWith("JJ") || token.tag().startsWith("RB")) {
                    if (contains(keywordsPositive, token.lemma())) {
                        polarity += 1;
                    } else if (contains(keywordsNegative, token.lemma())) {
                        polarity -= 1;
                    }
                }
            }
        }
        if (polarity > 0) {
            return "Positive";
        } else if (polarity < 0) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }
    
    // utility function to check if an array contains a given string
    public static boolean contains(String[] arr, String str) {
        for (String s : arr) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    // main method to take user input and call the function to identify the intent
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a sentence: ");
        String sentence = scanner.nextLine();
        String intent = identifyIntent(sentence);
        System.out.println("Intent: " + intent);
        scanner.close();
    }
}
