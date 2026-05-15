# The Optimized Courier Route

**Language:** Java | **Date:** 2026-05-15 16:23

## Description
A courier needs to deliver K packages in a city represented as a weighted undirected graph with N nodes and M edges. Each package i has a pickup location U[i] and a delivery location V[i]. The courier starts at node 0 and must return to node 0 after all packages have been delivered. The courier can carry any number of packages at the same time, but a package must be picked up at U[i] before it can be delivered at V[i]. Find the minimum total distance the courier must travel.

Constraints:
- N <= 100
- M <= 2000
- K <= 8
- Edge weights are positive integers <= 10^6.

Example:
Input: N=3, M=2, K=1, edges=[[0,1,10], [1,2,10]], tasks=[[1,2]]
Output: 40
Explanation: 0 -> 1 (pickup), 1 -> 2 (delivery), 2 -> 1 -> 0 (return). Total = 10 + 10 + 10 + 10 = 40.

## Explanation
The problem is a variation of the Traveling Salesman Problem (TSP) with precedence constraints (pickup must precede delivery). First, we use the Floyd-Warshall algorithm to precompute the shortest path between all pairs of nodes in O(N^3). Then, we define a DP state using bitmasks: dp(picked_mask, delivered_mask, current_node). The 'picked_mask' tracks which packages have been picked up, and 'delivered_mask' tracks which have been delivered. Since K is small (up to 8), the number of states is roughly 2^K * 2^K * (2K+1), which is approximately 65,536 * 17 = 1,114,112. In each state, we transition by either picking up a remaining package or delivering a package currently in hand. The complexity is O(N^3 + 2^(2K) * K^2).
