import java.util.*;

public class Solution {
    private static final long INF = 1e157;
    private long[][] dist;
    private int K;
    private int[] pickups, deliveries;
    private int[] nodes;
    private Long[][][] memo;

    public long solve(int N, int[][] edges, int[][] tasks) {
        this.K = tasks.length;
        this.dist = new long[N][N];
        for (long[] row : dist) Arrays.fill(row, INF);
        for (int i = 0; i < N; i++) dist[i][i] = 0;
        for (int[] edge : edges) {
            dist[edge[0]][edge[1]] = Math.min(dist[edge[0]][edge[1]], (long)edge[2]);
            dist[edge[1]][edge[0]] = Math.min(dist[edge[1]][edge[0]], (long)edge[2]);
        }

        // Floyd-Warshall for all-pairs shortest paths
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        pickups = new int[K];
        deliveries = new int[K];
        nodes = new int[2 * K + 1];
        nodes[0] = 0; // Starting point
        for (int i = 0; i < K; i++) {
            pickups[i] = tasks[i][0];
            deliveries[i] = tasks[i][1];
            nodes[i + 1] = pickups[i];
            nodes[i + 1 + K] = deliveries[i];
        }

        // memo[picked_mask][delivered_mask][current_node_index_in_nodes_array]
        memo = new Long[1 << K][1 << K][2 * K + 1];
        long result = dp(0, 0, 0);
        return result >= INF ? -1 : result;
    }

    private long dp(int picked, int delivered, int currIdx) {
        if (delivered == (1 << K) - 1) {
            return dist[nodes[currIdx]][0];
        }
        if (memo[picked][delivered][currIdx] != null) {
            return memo[picked][delivered][currIdx];
        }

        long minVisit = INF;

        for (int i = 0; i < K; i++) {
            // Try picking up
            if ((picked & (1 << i)) == 0) {
                long d = dist[nodes[currIdx]][pickups[i]];
                if (d < INF) {
                    minVisit = Math.min(minVisit, d + dp(picked | (1 << i), delivered, i + 1));
                }
            }
            // Try delivering
            if ((picked & (1 << i)) != 0 && (delivered & (1 << i)) == 0) {
                long d = dist[nodes[currIdx]][deliveries[i]];
                if (d < INF) {
                    minVisit = Math.min(minVisit, d + dp(picked, delivered | (1 << i), i + 1 + K));
                }
            }
        }

        return memo[picked][delivered][currIdx] = minVisit;
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        int[][] edges = {{0,1,10}, {1,2,10}};
        int[][] tasks = {{1,2}};
        System.out.println(sol.solve(3, edges, tasks)); // Expected: 40
    }
}