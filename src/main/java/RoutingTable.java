import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutingTable {

        private static final int idSize = 8;
        private static final int bucketSize = 3;
        private static int localID;
        private static boolean isBootstrapNode = false;
        private static String bootstrapNodeIp = "172.17.0.2";
        private static int port = 5000;
        private static List<List<Client>> buckets;


        public static void init() {
                SecureRandom random = new SecureRandom();
                byte[] idInBytes = new byte[idSize / 4];
                random.nextBytes(idInBytes);
                BigInteger bigInt = new BigInteger(1, idInBytes);
                localID = bigInt.intValue();

                buckets = new ArrayList<>(idSize);
                for (int i = 0; i < idSize; i++) {
                        buckets.add(new ArrayList<>(bucketSize));
                }
        }


        public static void add(Client node) {
                if (node.getID() == localID) return;
                int i = getBucketIndex(node);

                List<Client> bucket = buckets.get(i);
                bucket.remove(node);
                if (bucket.size() < bucketSize) {
                        bucket.add(node);
                } else {
                        System.out.println("There is no more space for nodes in this bucket!");
                }
        }

        private static int getBucketIndex(Client node) {
                return node.getID() >> 5;
        }

        public static boolean allBucketsEmpty() {
                for (List<Client> bucket : buckets) {
                        if (!bucket.isEmpty()) {
                                return false;
                        }
                }
                return true;
        }




        // getters and setters bellow
        public static int getLocalID() {
                return localID;
        }

        public static boolean isIsBootstrapNode() {
                return isBootstrapNode;
        }

        public static String getBootstrapNodeIp() {
                return bootstrapNodeIp;
        }

        public static int getPort() {
                return port;
        }

        public static void setIsBootstrapNode(boolean isBootstrapNode) {
                RoutingTable.isBootstrapNode = isBootstrapNode;
        }
}
