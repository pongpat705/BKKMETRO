package com.example.maoz.hellowworld;
import java.util.*;


final class NodeData<T> {

    private final T nodeId;
    private final Map<T, Double> heuristic;
    private double g;  // g ระยะทางจากต้นทาง
    private double h;  // h ค่าฮิวริสติกของโหนดไปปลายทาง
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
     * เพิ่มโหนดเข้าไปยังกราฟ
     *
     * @param nodeId คือโหนดที่จะถูกเพิ่ม
     */
    public void addNode(T nodeId) {
        if (nodeId == null) throw new NullPointerException("The node cannot be null");
        if (!heuristicMap.containsKey(nodeId)) throw new NoSuchElementException("This node is not a part of hueristic map");

        graph.put(nodeId, new HashMap<NodeData<T>, Double>());
        nodeIdNodeData.put(nodeId, new NodeData<T>(nodeId, heuristicMap.get(nodeId)));
    }

    /**
     * เพิ่มเส้นเชื่อมจากโหนดต้นทางไปยังโหนดปลายทาง
     *
     * @param nodeIdFirst   โหนดแรกของเส้นเชื่อม
     * @param nodeIdSecond  โหนดที่สองของเส้นเชื่อม
     * @param length        ระยะทางของเส้นเชื่อม
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
     * คืนค่าเส้นเชื่อมของโหนด
     *
     * @param nodeId    คือโหนดที่มีเส้นเชื่อม
     * @return          เส้นเชื่อมที่ต่อกับโหนดนี้
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
     * หาคู่อันดับโหนดจาก ต้นทางไปปลายทาง ส่งให้ path จากนั้นรีเทินเป็น โหนดต้นทางไปยังโหนดปลายทาง
     *
     * @param source        ต้นทาง
     * @param destination   ปลายทาง
     * @return              ลำดับการเดินทางจากต้นทาง ไป ปลายทาง
     */
    public List<T> astar(T source, T destination) {
        /**
         * http://stackoverflow.com/questions/20344041/why-does-priority-queue-has-default-initial-capacity-of-11
         */
        final Queue<NodeData<T>> openQueue = new PriorityQueue<NodeData<T>>(11, new NodeComparator());

        NodeData<T> sourceNodeData = graph.getNodeData(source);// sourceNodeData = ข้อมูลของต้นทาง
        sourceNodeData.setG(0);
        sourceNodeData.calcF(destination);
        openQueue.add(sourceNodeData);// ใส่ต้นทางให้คิว

        final Map<T, T> path = new HashMap<T, T>();
        final Set<NodeData<T>> closedList = new HashSet<NodeData<T>>();

        while (!openQueue.isEmpty()) {
            final NodeData<T> nodeData = openQueue.poll();//เอาต้นทางในคิวย้ายลงไป nodeData คิวว่าง

            if (nodeData.getNodeId().equals(destination)) {//ถ้าต้นทาง = ปลายทาง เอาค่าเส้นทางที่ลงคู่อันดับไว้ไปเมธอด path
                return path(path, destination);
            }

            closedList.add(nodeData);// เพิ่มต้นทางลงไปใน closedList

            for (Map.Entry<NodeData<T>, Double> neighborEntry : graph.edgesFrom(nodeData.getNodeId()).entrySet()) {//ลูปโหนดนี้มีเส้นทางอะไรบ้าง
                NodeData<T> neighbor = neighborEntry.getKey();

                if (closedList.contains(neighbor)) continue;//ถ้า closedList มีข้อมูลโหนดที่ค้นพบในลูปอยู่แล้ว ไปต่อไม่ต้องทำ
                //ถ้ายังไม่มีก็ทำปายยย
                double distanceBetweenTwoNodes = neighborEntry.getValue();// ระยะทางมายังโหนดนี้
                double tentativeG = distanceBetweenTwoNodes + nodeData.getG();// ระยะทางทดลอง ถ้าเลือกโหนดนี้(เดินทางแล้ว + โหนดนี้)

                if (tentativeG < neighbor.getG()) {//ถ้า tentativeG < neighbor.getG()
                    neighbor.setG(tentativeG);
                    distance = tentativeG;
                    neighbor.calcF(destination);

                    path.put(neighbor.getNodeId(), nodeData.getNodeId());//เพิ่มคู่อันดับของเส้นเชื่อม ทุกโหนดที่ค้น
                    if (!openQueue.contains(neighbor)) {// ถ้าคิวยังไม่มีโหนด สุดท้ายใน openQueue จะเหลือโหนดที่ไม่ได้ใช้งาน
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    /**
     * จากคู่อันดับ เลือกเฉพาะอันที่เกี่ยวข้องกับปลายทาง เก็บไว้ในลิส และกลับข้างเพื่อให้ เป็น ต้นทางไปปลายทาง
     *
     * @param path        คู่อันดับที่ algorithm หาได้
     * @param destination   ปลายทาง
     * @return              รายการลำดับการเดินทางจากต้นทาง ไป ปลายทาง
     */
    private List<T> path(Map<T, T> path, T destination) {
        assert path != null;
        assert destination != null;

        final List<T> pathList = new ArrayList<T>();
        pathList.add(destination);//เพิ่มปลายทางไว้ในรายการก่อนเลย
        while (path.containsKey(destination)) {//ลูปเมื่อยังมี คีย์ที่ชื่อ destination
            destination = path.get(destination);//เอาค่าของคีย์ destination ไปใส่ destination เหมือนสลับ ค่าไปเป็นคีย์
            pathList.add(destination);//ลิสเพิ่มค่าของ destination ลงไป
        }
        Collections.reverse(pathList);// จบแล้วได้ลำดับการเดินทาง รีเวิสซะจะได้เป็น ต้นทางไปปลายทาง
        return pathList;
    }


}