package com.myproject.game.utils;

import com.myproject.game.network.kademlia.Node;

import java.util.List;

public class JsonHelper {



    public static String createJsonString(Node node) {
        StringBuilder sb = new StringBuilder();

        // Start of JSON object
        sb.append("{");

        // Add "isBootstrap" field
        sb.append("\"isBootstrap\":").append(node.isBootstrap()).append(",");

        // Add "nodeId" field
        sb.append("\"nodeId\":{");
        sb.append("\"id\":\"").append(node.getNodeId().getID()).append("\"");
        sb.append("},");

        // Add "address" field
        sb.append("\"address\":{");
        sb.append("\"address\":\"").append(node.getAddress().getAddress().getHostAddress()).append("\",");
        sb.append("\"port\":").append(node.getAddress().getPort());
        sb.append("},");

        // Add "lastSeen" field
        sb.append("\"lastSeen\":{");
        sb.append("\"seconds\":").append(node.getLastSeen().getEpochSecond()).append(",");
        sb.append("\"nanos\":").append(node.getLastSeen().getNano());
        sb.append("}");

        // End of JSON object
        sb.append("}");

        return sb.toString();
    }



    public static String createJsonString(List<Node> nodeList) {
        StringBuilder sb = new StringBuilder();

        // Start of JSON array
        sb.append("[");

        // Iterate over the nodeList
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);

            // Convert the node to JSON string using the previous function
            String nodeJsonString = createJsonString(node);

            // Append the JSON string to the result
            sb.append(nodeJsonString);

            // Add comma if not the last element
            if (i < nodeList.size() - 1) {
                sb.append(",");
            }
        }

        // End of JSON array
        sb.append("]");

        return sb.toString();
    }
}
