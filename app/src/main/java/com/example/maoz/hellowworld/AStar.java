package com.example.maoz.hellowworld;
import java.util.*;


final class NodeData<T> {

    private final T nodeId;
    private final Map<T, Double> heuristic;
    private double g;  // g ���зҧ�ҡ�鹷ҧ
    private double h;  // h ��������ʵԡ�ͧ�˹�任��·ҧ
    private double f;  // f = g + h 

    public NodeData (T nodeId, Map<T, Double> heuristic) {
        this.nodeId = nodeId;
        this.g = Double.MAX_VALUE;
        this.heuristic = heuristic;
    }

    public T getNodeId() {
        return nodeId;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void calcF(T destination) {
        this.h = heuristic.get(destination);
        this.f = g + h;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return f;
    }
}

/**
 * The graph represents an undirected graph. 
 *
 * @author SERVICE-NOW\ameya.patil
 *
 * @param <T>
 */
final class GraphAStar<T> implements Iterable<T> {
    /*
     * A map from the nodeId to outgoing edge.
     * An outgoing edge is represented as a tuple of NodeData and the edge length
     */
    private final Map<T, Map<NodeData<T>, Double>> graph;
    /*
     * A map of heuristic from a node to each other node in the graph.
     */
    private final Map<T, Map<T, Double>> heuristicMap;
    /*
     * A map between nodeId and nodedata.
     */
    private final Map<T, NodeData<T>> nodeIdNodeData;

    public GraphAStar(Map<T, Map<T, Double>> heuristicMap) {
        if (heuristicMap == null) throw new NullPointerException("The huerisic map should not be null");
        graph = new HashMap<T, Map<NodeData<T>, Double>>();
        nodeIdNodeData = new HashMap<T, NodeData<T>>();
        this.heuristicMap = heuristicMap;
    }

    /**
     * �����˹������ѧ��ҿ
     *
     * @param nodeId ����˹����ж١����
     */
    public void addNode(T nodeId) {
        if (nodeId == null) throw new NullPointerException("The node cannot be null");
        if (!heuristicMap.containsKey(nodeId)) throw new NoSuchElementException("This node is not a part of hueristic map");

        graph.put(nodeId, new HashMap<NodeData<T>, Double>());
        nodeIdNodeData.put(nodeId, new NodeData<T>(nodeId, heuristicMap.get(nodeId)));
    }

    /**
     * �������������ҡ�˹��鹷ҧ��ѧ�˹����·ҧ
     *
     * @param nodeIdFirst   �˹��á�ͧ��������
     * @param nodeIdSecond  �˹�����ͧ�ͧ��������
     * @param length        ���зҧ�ͧ��������
     */
    public void addEdge(T nodeIdFirst, T nodeIdSecond, double length) {
        if (nodeIdFirst == null || nodeIdSecond == null) throw new NullPointerException("The first nor second node can be null.");

        if (!heuristicMap.containsKey(nodeIdFirst) || !heuristicMap.containsKey(nodeIdSecond)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of hueristic map");
        }
        if (!graph.containsKey(nodeIdFirst) || !graph.containsKey(nodeIdSecond)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of graph");
        }

        graph.get(nodeIdFirst).put(nodeIdNodeData.get(nodeIdSecond), length);
        graph.get(nodeIdSecond).put(nodeIdNodeData.get(nodeIdFirst), length);
    }

    /**
     * �׹������������ͧ�˹�
     *
     * @param nodeId    ����˹��������������
     * @return          ������������͡Ѻ�˹����
     */
    public Map<NodeData<T>, Double> edgesFrom (T nodeId) {
        if (nodeId == null) throw new NullPointerException("The input node should not be null.");
        if (!heuristicMap.containsKey(nodeId)) throw new NoSuchElementException("This node is not a part of hueristic map");
        if (!graph.containsKey(nodeId)) throw new NoSuchElementException("The node should not be null.");

        return Collections.unmodifiableMap(graph.get(nodeId));
    }

    /**
     * The nodedata corresponding to the current nodeId.
     *
     * @param nodeId    the nodeId to be returned
     * @return          the nodeData from the 
     */
    public NodeData<T> getNodeData (T nodeId) {
        if (nodeId == null) { throw new NullPointerException("The nodeid should not be empty"); }
        if (!nodeIdNodeData.containsKey(nodeId))  { throw new NoSuchElementException("The nodeId does not exist"); }
        return nodeIdNodeData.get(nodeId);
    }

    /**
     * Returns an iterator that can traverse the nodes of the graph
     *
     * @return an Iterator.
     */
    @Override public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }
}

public class AStar<T> {
    public double distance;
    private final GraphAStar<T> graph;


    public AStar (GraphAStar<T> graphAStar) {
        this.graph = graphAStar;
    }

    // extend comparator.
    public class NodeComparator implements Comparator<NodeData<T>> {
        public int compare(NodeData<T> nodeFirst, NodeData<T> nodeSecond) {
            if (nodeFirst.getF() > nodeSecond.getF()) return 1;
            if (nodeSecond.getF() > nodeFirst.getF()) return -1;
            return 0;
        }
    }

    /**
     * �Ҥ���ѹ�Ѻ�˹��ҡ �鹷ҧ任��·ҧ ����� path �ҡ������Թ�� �˹��鹷ҧ��ѧ�˹����·ҧ
     *
     * @param source        �鹷ҧ
     * @param destination   ���·ҧ
     * @return              �ӴѺ����Թ�ҧ�ҡ�鹷ҧ � ���·ҧ
     */
    public List<T> astar(T source, T destination) {
        /**
         * http://stackoverflow.com/questions/20344041/why-does-priority-queue-has-default-initial-capacity-of-11
         */
        final Queue<NodeData<T>> openQueue = new PriorityQueue<NodeData<T>>(11, new NodeComparator());

        NodeData<T> sourceNodeData = graph.getNodeData(source);// sourceNodeData = �����Ţͧ�鹷ҧ
        sourceNodeData.setG(0);
        sourceNodeData.calcF(destination);
        openQueue.add(sourceNodeData);// ���鹷ҧ�����

        final Map<T, T> path = new HashMap<T, T>();
        final Set<NodeData<T>> closedList = new HashSet<NodeData<T>>();

        while (!openQueue.isEmpty()) {
            final NodeData<T> nodeData = openQueue.poll();//��ҵ鹷ҧ㹤������ŧ� nodeData �����ҧ

            if (nodeData.getNodeId().equals(destination)) {//��ҵ鹷ҧ = ���·ҧ ��Ҥ����鹷ҧ���ŧ����ѹ�Ѻ�������ʹ path
                return path(path, destination);
            }

            closedList.add(nodeData);// �����鹷ҧŧ�� closedList

            for (Map.Entry<NodeData<T>, Double> neighborEntry : graph.edgesFrom(nodeData.getNodeId()).entrySet()) {//�ٻ�˹��������鹷ҧ���ú�ҧ
                NodeData<T> neighbor = neighborEntry.getKey();

                if (closedList.contains(neighbor)) continue;//��� closedList �բ������˹����鹾���ٻ�������� 仵������ͧ��
                //����ѧ����ա�ӻ����
                double distanceBetweenTwoNodes = neighborEntry.getValue();// ���зҧ���ѧ�˹����
                double tentativeG = distanceBetweenTwoNodes + nodeData.getG();// ���зҧ���ͧ ������͡�˹����(�Թ�ҧ���� + �˹����)

                if (tentativeG < neighbor.getG()) {//��� tentativeG < neighbor.getG()
                    neighbor.setG(tentativeG);
                    distance = tentativeG;
                    neighbor.calcF(destination);

                    path.put(neighbor.getNodeId(), nodeData.getNodeId());//��������ѹ�Ѻ�ͧ�������� �ء�˹�����
                    if (!openQueue.contains(neighbor)) {// ��Ҥ���ѧ������˹� �ش����� openQueue ��������˹�����������ҹ
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }


    private List<T> path(Map<T, T> path, T destination) {
        assert path != null;
        assert destination != null;

        final List<T> pathList = new ArrayList<T>();
        pathList.add(destination);//�������·ҧ������¡�á�͹���
        while (path.containsKey(destination)) {//�ٻ������ѧ�� ��������� destination
            destination = path.get(destination);//��Ҥ�Ңͧ���� destination ���� destination ����͹��Ѻ �����繤���
            pathList.add(destination);//���������Ңͧ destination ŧ�
        }
        Collections.reverse(pathList);// ���������ӴѺ����Թ�ҧ �����ʫШ����� �鹷ҧ任��·ҧ
        return pathList;
    }


}