package anmao.mc.nu.screen.index;

import anmao.mc.amlib.constant$data$Table.PlayerCDT;
import anmao.mc.amlib.entity.EntityHelper;
import anmao.mc.nu.NU;
import anmao.mc.nu.block.NUBlocks;
import anmao.mc.nu.block.entity.index.IndexBlockEntity;
import anmao.mc.nu.datatype.EnchantDataTypeCore;
import anmao.mc.nu.screen.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class IndexMenu extends AbstractContainerMenu {
    private static final int invSlotX = 58;
    public final IndexBlockEntity index;
    private final Level level;
    private final ContainerData data;
    public IndexMenu(int pContainerId, Inventory inventory , FriendlyByteBuf ex) {
        this(pContainerId,inventory, EntityHelper.getBlockEntity(inventory.player,ex.readBlockPos()),new SimpleContainerData(6));
    }
    public IndexMenu(int cid, Inventory inv, BlockEntity ent, ContainerData dat){
        super(MenuTypes.INDEX_MENU.get(), cid);
        checkContainerSize(inv,2);
        index = (IndexBlockEntity) ent;
        this.level = inv.player.level();
        this.data = dat;
        addPlayerInventory(inv);
        addPlayerHotBar(inv);
        this.index.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler,0,112,37));
            this.addSlot(new SlotItemHandler(iItemHandler,1,170,37));
        });
        addDataSlots(dat);
    }

    @Override
    public void slotsChanged(@NotNull Container pContainer) {
        super.slotsChanged(pContainer);
    }

    public boolean isCrafting(){
        return data.get(0) >0;
    }

    public int getScaleProgress(){
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int progressSize = 24;
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
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int ind) {
        Slot sourceSlot = slots.get(ind);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (ind < PlayerCDT.VANILLA_FIRST_SLOT_INDEX + PlayerCDT.VANILLA_SLOT_COUNT){
            if (!moveItemStackTo(sourceStack,PlayerCDT.INVENTORY_FIRST_SLOT_INDEX,PlayerCDT.INVENTORY_FIRST_SLOT_INDEX+PlayerCDT.INVENTORY_SLOT_COUNT,false)){
                return ItemStack.EMPTY;
            }
        }else if (ind < PlayerCDT.INVENTORY_FIRST_SLOT_INDEX+PlayerCDT.INVENTORY_SLOT_COUNT){
            if (!moveItemStackTo(sourceStack,PlayerCDT.VANILLA_FIRST_SLOT_INDEX,PlayerCDT.VANILLA_FIRST_SLOT_INDEX + PlayerCDT.VANILLA_SLOT_COUNT,false)){
                return ItemStack.EMPTY;
            }
        }else {
            NU.LOG.error("Error Inventory slot index");
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0){
            sourceSlot.set(ItemStack.EMPTY);
        }else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player,sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level,index.getBlockPos()),player, NUBlocks.INDEX.get());
    }
    private void addPlayerInventory(Inventory playerInv){
        for (int i= 0; i<3;i++){
            for (int m = 0;m<9;m++){
                this.addSlot(new Slot(playerInv,m+i*9+9,invSlotX+m*18,84+i*18));
            }
        }
    }
    private void addPlayerHotBar(Inventory inventory){
        for (int i = 0;i<9;i++){
            this.addSlot(new Slot(inventory,i,invSlotX+i*18,142));
        }
    }

    public HashMap<Enchantment, EnchantDataTypeCore> getEnchantData() {
        return this.index.getSaveEnchants();
    }
    public ItemStack getInputItem(){
        return index.getSlotItem(0);
    }
}
