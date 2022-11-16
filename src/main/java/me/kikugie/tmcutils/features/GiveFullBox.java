package me.kikugie.tmcutils.features;

import me.kikugie.tmcutils.config.Configs;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;

public class GiveFullBox {
    public static void giveBox() {
        var player = MinecraftClient.getInstance().player;
        if (!player.isCreative()) {
            player.sendMessage(Text.of("§cNot in creative mode!"), true);
            return;
        }
        var item = player.getInventory().getMainHandStack();
        if (item == ItemStack.EMPTY || item.getTranslationKey().contains("shulker_box")) {
            player.sendMessage(Text.of("§cInvalid item!"), true);
            return;
        }
        var box = composeBox(item.getItem(), DyeColor.byName(Configs.FeatureConfigs.BOX_COLOR.getStringValue(), null));
        MinecraftClient.getInstance().interactionManager.clickCreativeStack(box, 36 + player.getInventory().selectedSlot);
    }

    public static ItemStack composeBox(Item item, @Nullable DyeColor color) {
        var box = ShulkerBoxBlock.getItemStack(color);

        // Setup item
        var itemCompound = new NbtCompound();
        itemCompound.putString("id", item.toString());
        itemCompound.putByte("Count", (byte) item.getMaxCount());

        // Setup box contents
        var items = new NbtList();
        for (byte i = 0; i < 27; i++) {
            var localItem = itemCompound.copy();
            localItem.putByte("Slot", i);
            items.add(localItem);
        }

        // Setup box
        var nbt = new NbtCompound();
        nbt.put("BlockEntityTag", new NbtCompound());
        nbt.getCompound("BlockEntityTag").put("Items", items);
        box.writeNbt(nbt);

        return box;
    }
}
