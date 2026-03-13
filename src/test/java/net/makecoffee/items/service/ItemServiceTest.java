package net.makecoffee.items.service;

import net.kyori.adventure.text.Component;
import net.makecoffee.items.data.DataComponentEntry;
import net.makecoffee.items.data.ItemData;
import net.makecoffee.items.data.ItemDisplayData;
import net.makecoffee.items.instance.ItemInstance;
import net.makecoffee.items.repository.ItemRepository;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.DyeColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @BeforeAll
    static void setup() {
        MinecraftServer.init();
    }

    @AfterAll
    static void shutdown() {
        MinecraftServer.stopCleanly();
    }

    @Test
    void shouldLoadItemsFromJson() {
        itemService.loadFromJson();
        Mockito.verify(itemRepository, Mockito.times(1))
                .add(Mockito.any(ItemData.class));
    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void createInstanceFromId_returnsExpected(String id, @Nullable ItemData data) {
        Mockito.when(itemRepository.get(id)).thenReturn(data);
        ItemInstance result = itemService.createInstanceFromId(id);
        Assertions.assertEquals(
                Optional.ofNullable(data).map(ItemInstance::new).orElse(null),
                result
        );
    }

    static @NotNull Stream<Arguments> provideIds() {
        ItemData item1 = new ItemData(
                "item1",
                Material.STONE,
                new ItemDisplayDataBuilder()
                        .name(Component.text("Rock"))
                        .description(Component.text("Line1"), Component.text("Line2"))
                        .component(new DataComponentEntry(DataComponents.DAMAGE, 10))
                        .build(),
                10
        );

        ItemData item2 = new ItemData(
                "item2",
                Material.STONE,
                new ItemDisplayDataBuilder()
                        .name(Component.text("Beautiful rock"))
                        .description(Component.text("Line1"))
                        .component(new DataComponentEntry(DataComponents.DYED_COLOR, DyeColor.LIME))
                        .build(),
                0
        );

        return Stream.of(
                Arguments.of("item1", item1),
                Arguments.of("item2", item2),
                Arguments.of("nonexistent", null)
        );
    }

    static class ItemDisplayDataBuilder {
        private Component name = Component.empty();
        private List<Component> description = new ArrayList<>();
        private final List<DataComponentEntry> components = new ArrayList<>();

        ItemDisplayDataBuilder name(Component name) {
            this.name = name;
            return this;
        }

        ItemDisplayDataBuilder description(Component... lines) {
            description = Arrays.asList(lines);
            return this;
        }

        ItemDisplayDataBuilder component(DataComponentEntry entry) {
            components.add(entry);
            return this;
        }

        ItemDisplayData build() {
            return new ItemDisplayData(name, description, components);
        }
    }
}
