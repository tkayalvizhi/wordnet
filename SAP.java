import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;
    private final int vCount;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Digraph argument to SAP is null");
        digraph = G;
        this.vCount = G.V();

    }

    private void validate(int v) {
        if (v < 0 || v >= vCount) throw new IllegalArgumentException("vertex index: " + v + " is not valid");
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validate(v);
        validate(w);
        if (v == w) return 0;

        BfsDeluxe bfsDeluxe = new BfsDeluxe(digraph, v, w);
        return bfsDeluxe.getMinDist();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);
        if (v == w) return v;

        BfsDeluxe bfsDeluxe = new BfsDeluxe(digraph, v, w);
        return bfsDeluxe.getShortestCommonAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Null arguments passed to length() in SAP");

        for (int i : v) validate(i);
        for (int j : w) validate(j);

        BfsDeluxe bfsDeluxe = new BfsDeluxe(digraph, v, w);
        return bfsDeluxe.getMinDist();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("Null arguments passed to ancestor() in SAP");
        for (int i : v) validate(i);
        for (int j : w) validate(j);

        BfsDeluxe bfsDeluxe = new BfsDeluxe(digraph, v, w);
        return bfsDeluxe.getShortestCommonAncestor();
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        int v = 5;
        int w = 0;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

    }

}
