import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Autocomplete {
    private static final int MAX_SUGGESTIONS = 10;

    public static void main(String[] args) {
        String[] strings = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "kiwi", "lemon", "mango"};
        int[] weights = {10, 8, 5, 2, 6, 4, 9, 7, 3, 1};
        Trie trie = new Trie();
        for (int i = 0; i < strings.length; i++) {
            trie.insert(strings[i], weights[i]);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a prefix: ");
            String prefix = scanner.nextLine();
            System.out.print("Enter k: ");
            int k = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            List<String> suggestions = trie.search(prefix, k);
            for (String suggestion : suggestions) {
                System.out.println(suggestion);
            }
        }
    }

    private static class Trie {
        private Node root;

        public Trie() {
            root = new Node();
        }

        public void insert(String word, int weight) {
            Node node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new Node());
                node = node.children.get(c);
            }
            node.isEndOfWord = true;
            node.weight = weight;
        }

        public List<String> search(String prefix, int k) {
            List<String> suggestions = new ArrayList<>();
            Node node = root;
            for (char c : prefix.toCharArray()) {
                node = node.children.get(c);
                if (node == null) {
                    return suggestions;
                }
            }
            findTopKSuggestions(node, prefix, k, suggestions);
            return suggestions;
        }

        private void findTopKSuggestions(Node node, String prefix, int k, List<String> suggestions) {
            if (node.isEndOfWord) {
                suggestions.add(prefix);
            }
            for (char c : node.children.keySet()) {
                Node child = node.children.get(c);
                findTopKSuggestions(child, prefix + c, k, suggestions);
            }
            suggestions.sort((s1, s2) -> Integer.compare(getWeight(s2), getWeight(s1)));
            while (suggestions.size() > k) {
                suggestions.remove(suggestions.size() - 1);
            }
        }

        private int getWeight(String word) {
            Node node = root;
            for (char c : word.toCharArray()) {
                node = node.children.get(c);
            }
            return node.weight;
        }

        private static class Node {
            private boolean isEndOfWord;
            private int weight;
            private final java.util.Map<Character, Node> children = new java.util.HashMap<>();
        }
    }
}
