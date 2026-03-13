package net.makecoffee.items.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.makecoffee.items.Constants;
import net.makecoffee.items.instance.ItemInstance;
import net.makecoffee.items.repository.ItemRepository;
import net.makecoffee.items.service.ItemService;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.jspecify.annotations.NonNull;

public final class ItemCommand extends Command {

    private final ItemService itemService;
    private final ArgumentString idArgument;
    private final Argument<Integer> amountArgument;

    public ItemCommand(ItemRepository itemRepository, ItemService itemService) {
        super("item", "items", "i");
        this.itemService = itemService;

        setCondition(Conditions::playerOnly);
        setDefaultExecutor((sender, _) -> {
            sender.sendMessage(Component.text("Usage: /item <id> [amount]", NamedTextColor.DARK_RED));
        });

        idArgument = ArgumentType.String("id");
        idArgument.setSuggestionCallback((_, _, suggestion) -> {
            itemRepository.all()
                    .stream()
                    .map(data -> new SuggestionEntry(data.id(), data.display().name()))
                    .forEach(suggestion::addEntry);
        });

        amountArgument = ArgumentType.Integer("amount")
                .between(1, Constants.ITEM_MAX_STACK_SIZE)
                .setDefaultValue(1);

        addSyntax(this::give, idArgument, amountArgument);
    }

    public void give(CommandSender sender, @NonNull CommandContext context) {
        if (!context.has(idArgument)) {
            return;
        }

        String id = context.get(idArgument);
        ItemInstance instance = itemService.createInstanceFromId(id);
        if (instance == null) {
            sender.sendMessage(Component.text("Item with id", NamedTextColor.DARK_RED)
                    .appendSpace()
                    .append(Component.text(id, NamedTextColor.RED))
                    .appendSpace()
                    .append(Component.text("not found.", NamedTextColor.DARK_RED)));
            return;
        }

        int amount = context.get(amountArgument);
        ((Player) sender).getInventory().addItemStack(instance.generate(amount));
    }
}