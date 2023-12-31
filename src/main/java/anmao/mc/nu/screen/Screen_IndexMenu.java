package anmao.mc.nu.screen;

import anmao.mc.nu.NU;
import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import anmao.mc.nu.amlib.datatype._DataType_StringIntInt;
import anmao.mc.nu.block.Blocks;
import anmao.mc.nu.block.entity.BlockEntity_Index;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.SlotItemHandler;

import java.util.HashMap;

public class Screen_IndexMenu extends AbstractContainerMenu {
    public final BlockEntity_Index index;
    private final Level level;
    private final ContainerData data;
    public Screen_IndexMenu(int pContainerId, Inventory inventory , FriendlyByteBuf ex) {
        this(pContainerId,inventory,inventory.player.level().getBlockEntity(ex.readBlockPos()),new SimpleContainerData(2));
    }
    public Screen_IndexMenu(int cid, Inventory inv, BlockEntity ent,ContainerData dat){
        super(Screen_MenuTypes.INDEX_MENU.get(), cid);
        /*
        try {
            Thread.dumpStack();
        }catch (Exception e){
            //
        }
         */
        //System.out.println("Constructing Screen_IndexMenu instance: " + this.hashCode());
        checkContainerSize(inv,2);
        index = (BlockEntity_Index) ent;
        this.level = inv.player.level();
        this.data = dat;
        addPlayerInventory(inv);
        addPlayerHotBar(inv);
        this.index.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler,0,86,37));
            //this.addSlot(new SlotItemHandler(iItemHandler,1,112,37));
            this.addSlot(new SlotItemHandler(iItemHandler,1,170,37));
        });
        addDataSlots(dat);
        //System.out.println(":dat:"+ getEnchantData());
    }

    @Override
    public void slotsChanged(Container pContainer) {
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
    private static final int HOT_BAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOT_BAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;

    private static final int INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int INVENTORY_SLOT_COUNT = 2;
    @Override
    public ItemStack quickMoveStack(Player player, int ind) {
        Slot sourceSlot = slots.get(ind);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (ind < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT){
            if (!moveItemStackTo(sourceStack,INVENTORY_FIRST_SLOT_INDEX,INVENTORY_FIRST_SLOT_INDEX+INVENTORY_SLOT_COUNT,false)){
                return ItemStack.EMPTY;
            }
        }else if (ind < INVENTORY_FIRST_SLOT_INDEX+INVENTORY_SLOT_COUNT){
            if (!moveItemStackTo(sourceStack,VANILLA_FIRST_SLOT_INDEX,VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,false)){
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
        return stillValid(ContainerLevelAccess.create(level,index.getBlockPos()),player, Blocks.INDEX.get());
    }
    private void addPlayerInventory(Inventory playerInv){
        for (int i= 0; i<3;i++){
            for (int m = 0;m<9;m++){
                this.addSlot(new Slot(playerInv,m+i*9+9,58+m*18,84+i*18));
            }
        }
    }
    private void addPlayerHotBar(Inventory inventory){
        for (int i = 0;i<9;i++){
            this.addSlot(new Slot(inventory,i,58+i*18,142));
        }
    }

    public HashMap<Enchantment, _DataType_StringIntInt> getEnchantData() {
        return this.index.getSaveEnchants();
    }
    public ItemStack getInputItem(){
        return index.getSlotItem(0);
    }
}