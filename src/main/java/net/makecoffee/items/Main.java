package net.makecoffee.items;

import net.minestom.server.MinecraftServer;

public class Main {

    static void main() {
        new WowItems().start();
        Runtime.getRuntime().addShutdownHook(new Thread(MinecraftServer::stopCleanly));
    }
}
