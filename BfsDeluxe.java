/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class BfsDeluxe {
    private int minDist;
    private int shortestCommonAncestor;
    private final int vCount;
    private final Digraph digraph;
    private Queue<Integer> q = new Queue<>();
    private boolean[] markedFromW;
    private boolean[] markedFromV;
    private int[] distTo;


    public BfsDeluxe(Digraph g, int v, int w) {
        vCount = g.V();
        minDist = vCount;
        digraph = g;

        markedFromW = new boolean[vCount];
        markedFromV = new boolean[vCount];
        distTo = new int[vCount];


        markedFromV[v] = true;
        markedFromW[w] = true;

        q = new Queue<>();
        q.enqueue(v);
        q.enqueue(w);

        bfs();
    }

    public BfsDeluxe(Digraph g, Iterable<Integer> v, Iterable<Integer> w) {
        vCount = g.V();
        minDist = vCount;
        digraph = g;

        markedFromW = new boolean[vCount];
        markedFromV = new boolean[vCount];
        distTo = new int[vCount];

        for (int v1 : v) {
            markedFromV[v1] = true;
            q.enqueue(v1);
        }

        for (int w1 : w) {
            if (markedFromV[w1]) {
                shortestCommonAncestor = w1;
                minDist = 0;
                break;
            }
            markedFromW[w1] = true;
            q.enqueue(w1);
        }

        bfs();

    }

    private void bfs() {
        int dist;
        int child;
        while (!q.isEmpty()) {

            // deque an int from queue -  child
            child = q.dequeue();
            for (int ancestor : digraph.adj(child)) {
                // if unmarked, mark ancestor
                if (!markedFromW[ancestor] && !markedFromV[ancestor]) {
                    markedFromW[ancestor] = markedFromW[child];
                    markedFromV[ancestor] = markedFromV[child];
                    distTo[ancestor] = distTo[child] + 1;
                    q.enqueue(ancestor);
                }

                else if ((markedFromW[ancestor] != markedFromW[child])
                            || (markedFromV[ancestor] != markedFromV[child]))  {
                        markedFromW[ancestor] = true;
                        markedFromV[ancestor] = true;
                    }

                if (markedFromW[ancestor] && markedFromV[ancestor]) {
                    dist = distTo[ancestor] + distTo[child] + 1;
                    if (dist < minDist) {
                        minDist = dist;
                        shortestCommonAncestor = ancestor;
                    }


                }

            }
        }
    }

    public int getMinDist() {
        if (minDist == vCount) return -1;
        else return minDist;
    }

    public int getShortestCommonAncestor() {
        if (minDist == vCount) return -1;
        else return shortestCommonAncestor;
    }

}
