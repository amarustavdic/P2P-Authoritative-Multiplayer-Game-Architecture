package com.myproject.game.network.kademlia;

public enum KademliaMessageType {
    /**
     *  PING: probes a node to see if it’s online
     *  PING_REPLY: reply from a node, signaling that it's still online
     */
    PING,
    PING_REPLY,

    /**
     *  STORE: instructs a node to store a key-value pair
     */
    STORE,
    STORE_REPLY,

    /**
     *  FIND_NODE: returns information about the k nodes closest to the target id
     */
    FIND_NODE,
    FIND_NODE_REPLY,

    /**
     *  FIND_VALUE: similar to the FIND_NODE RPC, but if the recipient has received a STORE
     *              for the given key, it just returns the stored value
     */
    FIND_VALUE,
    FIND_VALUE_REPLY
}
