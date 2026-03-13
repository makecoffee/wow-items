package net.makecoffee.items.service;

import com.google.common.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.makecoffee.items.Constants;
import net.makecoffee.items.data.ItemData;
import net.makecoffee.items.instance.ItemInstance;
import net.makecoffee.items.repository.ItemRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public final class ItemService {

    private static final Type TYPE = new TypeToken<List<ItemData>>(){}.getType();
    private static final String ITEMS_FILE_NAME = "items.json";
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void loadFromJson() {
        InputStream is = getItemsInputStream();
        assert is != null;
        try (InputStreamReader isr = new InputStreamReader(is)) {
            JsonReader reader = new JsonReader(isr);
            List<ItemData> items = Constants.GSON.fromJson(reader, TYPE);
            for (ItemData data : items) {
                itemRepository.add(data);
            }

            Constants.LOGGER.info("Loaded {} items", itemRepository.all().size());
        } catch (Exception ex) {
            Constants.LOGGER.error("Failed to load {}", ITEMS_FILE_NAME, ex);
        }
    }

    private InputStream getItemsInputStream() {
        return ItemService.class.getClassLoader().getResourceAsStream(ITEMS_FILE_NAME);
    }

    public @Nullable ItemInstance createInstanceFromId(@NotNull String id) {
        return Optional.ofNullable(itemRepository.get(id))
                .map(ItemInstance::new)
                .orElse(null);
    }
}
