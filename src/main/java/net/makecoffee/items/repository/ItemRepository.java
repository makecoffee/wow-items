package net.makecoffee.items.repository;

import net.makecoffee.items.data.ItemData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ItemRepository {

    private final Map<String, ItemData> items = new HashMap<>();

    public void add(@NotNull ItemData data) {
        items.put(data.id(), data);
    }

    public @Nullable ItemData get(@NotNull String id) {
        return items.get(id);
    }

    public @NotNull @UnmodifiableView Collection<ItemData> all() {
        return Collections.unmodifiableCollection(items.values());
    }
}
