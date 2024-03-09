package anmao.mc.nu.screen.index;

import anmao.mc.amlib.entity.EntityHelper;
import anmao.mc.nu.block.NUBlocks;
import anmao.mc.nu.block.index.IndexBlockEntity;
import anmao.mc.nu.datatype.EnchantDataTypeCore;
import anmao.mc.nu.screen.AbstractContainerMenuCore;
import anmao.mc.nu.screen.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class IndexMenu extends AbstractContainerMenuCore {
    private static final int invSlotX = 58;
    public final IndexBlockEntity index;
    private final Level level;
    private final ContainerData data;
    public IndexMenu(int pContainerId, Inventory inventory , FriendlyByteBuf ex) {
        this(pContainerId,inventory, EntityHelper.getBlockEntity(inventory.player,ex.readBlockPos()),new SimpleContainerData(6));
    }
    public IndexMenu(int cid, Inventory inv, BlockEntity ent, ContainerData dat){
        super(MenuTypes.INDEX_MENU.get(), cid,2);
        checkContainerSize(inv,2);
        index = (IndexBlockEntity) ent;
        this.level = inv.player.level();
        this.data = dat;
        addPlayerInventory(inv,143,140,18);
        addPlayerHotBar(inv,143,198,18);
        this.index.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler,0,83,140){

                @Override
                public int getMaxStackSize(@NotNull ItemStack itemStack) {
                    if (itemStack.getItem() == Items.LAPIS_BLOCK || itemStack.getItem() == Items.LAPIS_LAZULI){
                        return 64;
                    }
                    return 1;
                }
            });
            this.addSlot(new SlotItemHandler(iItemHandler,1,82,193){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });
        addDataSlots(dat);
    }

    public boolean isCrafting(){
        return data.get(0) >0;
    }

    public int getScaleProgress(){
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int progressSize = 28;
        return maxProgress != 0 && progress != 0 ? progress * progressSize / maxProgress : 0;
    }
    public int getX(){
        return data.get(2);
    }
    public int getY(){
        return data.get(3);
    }
    public int getZ(){
        return data.get(4);
    }

    public int getMP(){
        return data.get(5);
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level,index.getBlockPos()),player, NUBlocks.INDEX.get());
    }

    public HashMap<Enchantment, EnchantDataTypeCore> getEnchantData() {
        return this.index.getSaveEnchants();
    }
    public ItemStack getInputItem(){
        return index.getSlotItem(0);
    }
}
