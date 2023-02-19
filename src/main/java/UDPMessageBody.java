import java.util.List;

public class UDPMessageBody {

    private List<Node> nodes;

    public UDPMessageBody(List<Node> nodes) {
        this.nodes = nodes;
    }


    public List<Node> getNodes() {
        return nodes;
    }
}
