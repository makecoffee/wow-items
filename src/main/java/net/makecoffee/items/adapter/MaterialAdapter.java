package net.makecoffee.items.adapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minestom.server.item.Material;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class MaterialAdapter extends TypeAdapter<Material> {

    public static final MaterialAdapter INSTANCE = new MaterialAdapter();

    private MaterialAdapter() {}

    @Override
    public void write(JsonWriter writer, Material material) throws IOException {
        if (material == null) {
            writer.nullValue();
            return;
        }

        writer.value(material.key().value());
    }

    @Override
    public @NotNull Material read(@NotNull JsonReader reader) throws IOException {
        @Subst("stone")
        String key = reader.nextString();
        Material material = Material.fromKey(key);
        if (material == null) {
            throw new JsonParseException("Unknown material with id: " + key);
        }

        return material;
    }
}
