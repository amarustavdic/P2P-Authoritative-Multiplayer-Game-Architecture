package com.myproject.game.network.kademlia;

import java.util.Scanner;

public class DebugCLI implements Runnable {

    private final Scanner scanner;
    private final RoutingTable routingTable;


    public DebugCLI(RoutingTable routingTable) {
        this.scanner = new Scanner(System.in);
        this.routingTable = routingTable;
    }

    @Override
    public void run() {
        while (true) {
            String input = scanner.nextLine();
            handleCommand(input);
        }
    }

    private void handleCommand(String command) {
        switch (command) {
            case "/print":
                routingTable.print();
                break;
            case "/help":
                System.out.println(
                        "DEBUG_CLI AVAILABLE COMMANDS:       \n" +
                        "/print  -> prints out routing table content  \n"
                );
                break;
            default:
                System.out.println("Invalid command. Try /help");
                break;
        }
    }
}
