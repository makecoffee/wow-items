package net.makecoffee.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.makecoffee.items.adapter.*;
import net.makecoffee.items.data.DataComponentEntry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.slf4j.Logger;

public final class Constants {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Material.class, MaterialAdapter.INSTANCE)
            .registerTypeAdapter(DataComponentEntry.class, DataComponentEntryAdapter.INSTANCE)
            .registerTypeAdapter(Component.class, ComponentAdapter.INSTANCE)
            .setPrettyPrinting()
            .create();

    public static final MiniMessage MM = MiniMessage.miniMessage();
    public static final Logger LOGGER = MinecraftServer.LOGGER;
    public static final int ITEM_MAX_STACK_SIZE = 64;
    public static final String ITEM_ID_KEY = "id";
    public static final Tag<BinaryTag> ITEM_TAG = Tag.NBT("wow-item-tag");

    private Constants() {}
}
