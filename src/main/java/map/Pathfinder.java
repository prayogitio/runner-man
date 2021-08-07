package map;

import core.Position;

import java.util.*;

public class Pathfinder {

    public static List<Position> findPath(Position start, Position target, GameMap gameMap) {
        final PriorityQueue<Node> open = new PriorityQueue<>();
        final Set<Node> closed = new HashSet<>();
        final Node[][] nodeMap = new Node[gameMap.getTiles().length][gameMap.getTiles()[0].length];
        Node current;

        for (int x = 0; x < nodeMap.length; x++) {
            for (int y = 0; y < nodeMap[0].length; y++) {
                int heuristic = Math.abs(x - target.gridX()) + Math.abs(y - target.gridY());
                Node node = new Node(10, heuristic, x, y);

                if (!gameMap.tileIsAvailable(x, y)) {
                    closed.add(node);
                }

                nodeMap[x][y] = node;
            }
        }

        Node startNode = nodeMap[start.gridX()][start.gridY()];
        Node targetNode = nodeMap[target.gridX()][target.gridY()];

        open.add(startNode);

        do {
            current = open.poll();
            closed.add(current);

            if (current.equals(targetNode)) {
                return extractPath(current);
            }

            for (int x = current.gridX - 1; x < current.gridX + 2; x++) {
                for (int y = current.gridY - 1; y < current.gridY + 2; y++) {
                    if (gameMap.gridWithinBounds(x, y)) {
                        if ((x == current.gridX - 1 && y == current.gridY - 1)
                            || (x == current.gridX - 1 && y == current.gridY + 1)
                            || (x == current.gridX + 1 && y == current.gridY - 1)
                            || (x == current.gridX + 1 && y == current.gridY + 1)
                        ) continue;
                        Node neighbor = nodeMap[x][y];

                        if (closed.contains(neighbor)) {
                            continue;
                        }

                        int calculatedCost = neighbor.heuristic + neighbor.moveCost + current.totalCost;

                        if (calculatedCost < neighbor.totalCost || !open.contains(neighbor)) {
                            neighbor.totalCost = calculatedCost;
                            neighbor.parent = current;

                            if (!open.contains(neighbor)) {
                                open.add(neighbor);
                            }
                        }
                    }
                }
            }
        } while (!open.isEmpty());

        return new ArrayList<>(Arrays.asList(start));
    }

    private static List<Position> extractPath(Node current) {
        List<Position> path = new ArrayList<>();
        while (current.parent != null) {
//            path.add(Position.ofGridPosition(current.getPosition().gridX(), current.getPosition().gridY()));
            path.add(current.getPosition());
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node implements Comparable<Node> {
        private int moveCost;
        private int heuristic;
        private int gridX;
        private int gridY;
        private int totalCost;
        private Node parent;

        public Node(int moveCost, int heuristic, int gridX, int gridY) {
            this.moveCost = moveCost;
            this.heuristic = heuristic;
            this.gridX = gridX;
            this.gridY = gridY;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.totalCost, that.totalCost);
        }

        public Position getPosition() {
            return Position.ofGridPosition(gridX, gridY);
        }
    }
}
