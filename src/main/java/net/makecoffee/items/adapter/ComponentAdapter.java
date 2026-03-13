package net.makecoffee.items.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.text.Component;
import net.makecoffee.items.Constants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ComponentAdapter extends TypeAdapter<Component> {

    public static final ComponentAdapter INSTANCE = new ComponentAdapter();

    private ComponentAdapter() {
    }

    @Override
    public void write(JsonWriter writer, Component component) throws IOException {
        if (component == null) {
            writer.nullValue();
            return;
        }

        writer.value(Constants.MM.serialize(component));
    }

    @Override
    public @NotNull Component read(@NotNull JsonReader reader) throws IOException {
        return Constants.MM.deserialize(reader.nextString());
    }
}
