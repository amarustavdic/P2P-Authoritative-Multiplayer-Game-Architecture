package com.mygame.app.networking;

import com.mygame.app.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class RoutingTable {

        private static final int ID_SIZE = 8;        // 8 bits, keyspace is from 0 to 255
        private static final int BUCKET_SIZE = 3;    // 3 is size of buckets in routing table
        private static Node localNode;
        private static Node bootstrapNode;
        private static List<List<Node>> buckets;


        public static void init() throws UnknownHostException {
                Random rnd = new Random();
                bootstrapNode = new Node(
                        125,
                        InetAddress.getByName("172.17.0.2"),
                        5000,
                        true,
                        0
                );
                if (Boolean.parseBoolean(System.getenv("ARG1"))) {
                        localNode = bootstrapNode;
                } else {
                        localNode = new Node(
                                rnd.nextInt(256),
                                InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()),
                                5000,
                                false,
                                0
                        );
                }
                buckets = new ArrayList<>(ID_SIZE);
                for (int i = 0; i < ID_SIZE; i++) {
                        buckets.add(new ArrayList<>(BUCKET_SIZE));
                }
        }


        public static void add(Node node) {
                int bucketIndex = getBucketIndex(node);
                List<Node> bucket = buckets.get(bucketIndex);

                if (bucket.size() < BUCKET_SIZE) {
                        bucket.add(node);
                        System.out.println(Constants.NETWORK + "New node added!" + Constants.RESET);
                } else {
                        Node lrsNode = getLeastRecentlySeenNode(bucket);
                        if (lrsNode != null && !lrsNode.equals(localNode) && !lrsNode.equals(bootstrapNode)) {
                                bucket.remove(lrsNode);
                                bucket.add(node);
                                System.out.println(Constants.NETWORK + "New node added!" + Constants.RESET);
                        }
                }
        }

        private static int getBucketIndex(Node node) {
                int distance = localNode.getId() ^ node.getId();
                return Integer.numberOfLeadingZeros(distance) - (32 - ID_SIZE);
        }

        private static Node getLeastRecentlySeenNode(List<Node> bucket) {
                Node lrsNode = null;
                long lrsTimestamp = Long.MAX_VALUE;
                for (Node node : bucket) {
                        if (!node.equals(localNode) && !node.equals(bootstrapNode) && node.getLastSeenTimestamp() < lrsTimestamp) {
                                lrsNode = node;
                                lrsTimestamp = node.getLastSeenTimestamp();
                        }
                }
                return lrsNode;
        }

        public static List<Node> getClosestNodes(Node node, int k) {
                PriorityQueue<Node> queue = new PriorityQueue<>(k, (n1, n2) -> {
                        int dist1 = node.getId() ^ n1.getId();
                        int dist2 = node.getId() ^ n2.getId();
                        return Integer.compare(dist1, dist2);
                });
                for (List<Node> bucket : buckets) {
                        for (Node n : bucket) {
                                queue.offer(n);
                                if (queue.size() > k) {
                                        queue.poll();
                                }
                        }
                }
                List<Node> result = new ArrayList<>(k);
                while (!queue.isEmpty()) {
                        result.add(queue.poll());
                }
                return result;
        }







        // getters and setters bellow
        public static void setLocalNode(Node localNode) {
                RoutingTable.localNode = localNode;
        }

        public static Node getLocalNode() {
                return localNode;
        }

        public static Node getBootstrapNode() {
                return bootstrapNode;
        }
}
