package warehouse;
import java.util.*;
public class WarehouseGraph {
	
	    private Map<String, List<Edge>> adjList = new HashMap<>();

	    static class Edge {
	        String dest;
	        int cost;

	        Edge(String d, int c) {
	            dest = d;
	            cost = c;
	        }
	    }

	    public void addOrUpdateRoute(String src, String dest, int cost) {
	        adjList.putIfAbsent(src, new ArrayList<>());
	        adjList.putIfAbsent(dest, new ArrayList<>());

	        boolean updated = false;

	        for (Edge e : adjList.get(src)) {
	            if (e.dest.equals(dest)) {
	                System.out.println("Route UPDATED successfully:");
	                System.out.println("Old Cost: " + e.cost + " → New Cost: " + cost);
	                e.cost = cost;
	                updated = true;
	            }
	        }

	        if (!updated) {
	            adjList.get(src).add(new Edge(dest, cost));
	            adjList.get(dest).add(new Edge(src, cost));
	            System.out.println(" Route added successfully between " + src + " and " + dest + " with cost " + cost);
	        }
	    }

	    public void deleteRoute(String src, String dest) {
	        if (adjList.containsKey(src))
	            adjList.get(src).removeIf(e -> e.dest.equals(dest));
	        if (adjList.containsKey(dest))
	            adjList.get(dest).removeIf(e -> e.dest.equals(src));

	        System.out.println(" Route between " + src + " and " + dest + " deleted successfully");
	    }

	    public void checkWarehouse(String name) {
	        if (adjList.containsKey(name))
	            System.out.println(" Warehouse " + name + " exists in the network");
	        else
	            System.out.println(" Warehouse " + name + " does NOT exist in the network");
	    }

	    public void display() {
	        System.out.println("\n CURRENT WAREHOUSE NETWORK:\n");
	        for (String w : adjList.keySet()) {
	            System.out.println("Warehouse " + w + " connections:");
	            for (Edge e : adjList.get(w)) {
	                System.out.println("   → " + e.dest + " (Cost: " + e.cost + ")");
	            }
	            System.out.println();
	        }
	    }

	    
	    public void shortestPathBetween(String start, String end) {
	        Map<String, Integer> dist = new HashMap<>();
	        Map<String, String> parent = new HashMap<>();
	        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.cost));

	        for (String node : adjList.keySet())
	            dist.put(node, Integer.MAX_VALUE);

	        dist.put(start, 0);
	        pq.add(new Edge(start, 0));

	        System.out.println("\n FINDING SHORTEST PATH USING DIJKSTRA...\n");

	        while (!pq.isEmpty()) {
	            Edge current = pq.poll();

	            for (Edge neighbor : adjList.get(current.dest)) {
	                int newDist = dist.get(current.dest) + neighbor.cost;

	                if (newDist < dist.get(neighbor.dest)) {
	                    dist.put(neighbor.dest, newDist);
	                    parent.put(neighbor.dest, current.dest);
	                    pq.add(new Edge(neighbor.dest, newDist));
	                }
	            }
	        }

	        if (dist.get(end) == Integer.MAX_VALUE) {
	            System.out.println("No path available.");
	            return;
	        }

	        System.out.println("SHORTEST PATH RESULT:\n");
	        System.out.print("Path: ");
	        printPath(end, parent);
	        System.out.println("\nTotal Minimum Cost: " + dist.get(end));
	    }

	   
	    public void printAllPaths(String src, String dest) {
	        System.out.println("\n FINDING ALL POSSIBLE PATHS USING DFS...\n");
	        dfs(src, dest, new HashSet<>(), new ArrayList<>(), 0);
	    }

	    private void dfs(String current, String dest, Set<String> visited, List<String> path, int cost) {
	        visited.add(current);
	        path.add(current);

	        if (current.equals(dest)) {
	            System.out.print("Path: ");
	            for (int i = 0; i < path.size(); i++) {
	                System.out.print(path.get(i));
	                if (i != path.size() - 1) System.out.print(" → ");
	            }
	            System.out.println(" | Total Cost: " + cost);
	        } else {
	            for (Edge e : adjList.get(current)) {
	                if (!visited.contains(e.dest)) {
	                    dfs(e.dest, dest, visited, path, cost + e.cost);
	                }
	            }
	        }

	        path.remove(path.size() - 1);
	        visited.remove(current);
	    }

	    private void printPath(String node, Map<String, String> parent) {
	        if (!parent.containsKey(node)) {
	            System.out.print(node);
	            return;
	        }
	        printPath(parent.get(node), parent);
	        System.out.print(" → " + node);
	    }
	
	public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    WarehouseGraph graph = new WarehouseGraph();

    System.out.println("========= LOGISTICS MANAGEMENT SYSTEM =========");

    while (true) {
        System.out.println("\n===== LOGISTICS MENU =====");
        System.out.println("1. Add/Update Route");
        System.out.println("2. Delete Route");
        System.out.println("3. Display Network");
        System.out.println("4. Check Warehouse");
        System.out.println("5. Shortest Path");
        System.out.println("6. All Paths (DFS)");
        System.out.println("7. Exit");

        System.out.print("Enter choice: ");
        int ch = sc.nextInt();

        switch (ch) {
            case 1:
                System.out.print("Enter source, destination, cost: ");
                graph.addOrUpdateRoute(sc.next(), sc.next(), sc.nextInt());
                break;

            case 2:
                System.out.print("Enter source and destination: ");
                graph.deleteRoute(sc.next(), sc.next());
                break;

            case 3:
                graph.display();
                break;

            case 4:
                System.out.print("Enter warehouse name: ");
                graph.checkWarehouse(sc.next());
                break;

            case 5:
                System.out.print("Enter source and destination: ");
                graph.shortestPathBetween(sc.next(), sc.next());
                break;

            case 6:
                System.out.print("Enter source and destination: ");
                graph.printAllPaths(sc.next(), sc.next());
                break;

            case 7:
                System.out.println("\n Exiting Logistics Management System...");
                return;

            default:
                System.out.println("Invalid choice!");
        }
    }
}
}