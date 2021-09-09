import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

import java.util.StringJoiner;


public class WordNet {
    private final ST<String, SET<Integer>> wordsST = new ST<>();
    private final ST<Integer, SET<String>> synSetST = new ST<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("input files cannot be null");
        }
        In synSetIn = new In(synsets);
        String[] line;
        String[] words;
        SET<String> synSet;
        int index;

        // create a symbol table that has words as key and its synset vertices as values.
        // key - string value - set of integers
        while (!synSetIn.isEmpty()) {

            synSet = new SET<>();
            line = synSetIn.readLine().split(",");
            index = Integer.parseInt(line[0]);
            words = line[1].split(" ");

            // if namesST contains name add the integer to the set
            // else add new name to the symbol table
            for (String word : words) {
                if (!wordsST.contains(word)) {
                    wordsST.put(word, new SET<>());
                }
                wordsST.get(word).add(index);
                synSet.add(word);
            }

            // create a set of all words in the synset and create a
            // symbol table that has keys as integers and values as synsets.
            synSetST.put(index, synSet);

        }

        In hypernymIn = new In(hypernyms);

        // create a digraph with the length of the second symbol table.
        Digraph digraph = new Digraph(synSetST.size());
        int v, w;

        // read each line from hypernym.txt and add edges from first vertex to all of the vertices in the line after the comma.
        while (!hypernymIn.isEmpty()) {
            line = hypernymIn.readLine().split(",");
            v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                w = Integer.parseInt(line[i]);
                digraph.addEdge(v, w);
            }
        }

        assertRootedDag(digraph);
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordsST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordsST.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Nouns are not wordnet nouns");
        }
        if (nounA.equals(nounB)) return 0;
        return sap.length(wordsST.get(nounA), wordsST.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Nouns are not wordnet nouns");
        }
        int vertex;
        if (nounA.equals(nounB)) {
            vertex = wordsST.get(nounA).min();
        } else {
            vertex = sap.ancestor(wordsST.get(nounA), wordsST.get(nounB));
        }
        StringJoiner sj = new StringJoiner(" ");
        for (String word : synSetST.get(vertex)) {
            sj.add(word);
        }

        return sj.toString();
    }

    private void assertRootedDag(Digraph g) {
        DirectedCycle directedCycle = new DirectedCycle(g);
        // DirectedCycleFinder cycleFinder = new DirectedCycleFinder(g);
        if (!directedCycle.hasCycle()) {
            System.out.println("No cycles in digraph...");
            int rootCount = 0;

            for (int v = 0; v < g.V(); v++) {
                if (g.outdegree(v) == 0) {
                    if (rootCount == 0) {
                        rootCount++;
                    } else {
                        throw new IllegalArgumentException("Multiple roots");
                    }
                }
            }
            System.out.println("Digraph has single root... ");
        } else throw new IllegalArgumentException("Graph has a directed cycle");
    }


    public static void main(String[] args) {
        WordNet wordNet = new WordNet("src/main/wordnet/synsets100-subgraph.txt", "src/main/wordnet/hypernyms100-subgraph.txt");
        for (String word : wordNet.nouns()) {
            System.out.println(word + " ");
        }

        System.out.println("------------------------------");
        System.out.println(wordNet.sap("gluten", "whopper"));
    }


}
