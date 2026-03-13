package net.makecoffee.items.adapter;

import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.makecoffee.items.data.DataComponentEntry;
import net.minestom.server.codec.Transcoder;
import net.minestom.server.component.DataComponent;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

public final class DataComponentEntryAdapter extends TypeAdapter<DataComponentEntry> {

    public static final DataComponentEntryAdapter INSTANCE = new DataComponentEntryAdapter();
    private static final Transcoder<JsonElement> JSON = Transcoder.JSON;

    private static final String KEY_TYPE = "type";
    private static final String KEY_VALUE = "value";

    private DataComponentEntryAdapter() {}

    @Override
    public void write(JsonWriter writer, DataComponentEntry entry) throws IOException {
        if (entry == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();
        DataComponent<?> component = entry.type();
        writer.name(KEY_TYPE).value(component.key().value());

        writer.name(KEY_VALUE);
        writeValue(writer, component, entry.value());
        writer.endObject();
    }

    @Override
    public @NotNull DataComponentEntry read(@NotNull JsonReader reader) throws IOException {
        reader.beginObject();

        DataComponent<?> component = null;
        Object value = null;
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case KEY_TYPE -> {
                    @Subst("use_cooldown")
                    String key = reader.nextString();
                    component = DataComponent.fromKey(key);
                }

                case KEY_VALUE -> {
                    JsonElement element = Streams.parse(reader);
                    if (component == null) {
                        throw new IllegalStateException("Component type must be read before value");
                    }

                    value = component
                            .decode(JSON, element)
                            .orElseThrow();
                }

                default -> reader.skipValue();
            }
        }

        reader.endObject();
        assert component != null;
        return new DataComponentEntry(component, value);
    }

    @SuppressWarnings("unchecked")
    private static <T> void writeValue(JsonWriter writer, @NonNull DataComponent<T> component, Object value)
            throws IOException {
        JsonElement encoded = component.encode(JSON, (T) value).orElseThrow();
        Streams.write(encoded, writer);
    }
}
