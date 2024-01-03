package anmao.mc.nu.amlib;

import anmao.mc.nu.NU;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AM_InventoryHelp {
    public static final int HOT_BAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOT_BAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;

    public static final int INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    public static final int INVENTORY_SLOT_COUNT = 2;
}
