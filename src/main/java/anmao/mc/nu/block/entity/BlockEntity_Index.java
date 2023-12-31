package anmao.mc.nu.block.entity;

import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import anmao.mc.nu.amlib.datatype._DataType_StringIntInt;
import anmao.mc.nu.network.index.Net_Index_Core;
import anmao.mc.nu.network.index.packet.Packet_Index_ServerToClient;
import anmao.mc.nu.screen.Screen_IndexMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BlockEntity_Index extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(3);
    private static final int SLOT_INPUT_ITEM = 0;
    private static final int SLOT_OUTPUT = 1;
    private static final int SLOT_INPUT_MATERIAL = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 70;
    //true : in
    //false : out
    private boolean TYPE = true;
    private Player player;
    private final _DataType_EnchantData enchantData;
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.empty();
    public BlockEntity_Index(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntities.INDEX.get(), pPos, pBlockState);
        enchantData = new _DataType_EnchantData();
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> BlockEntity_Index.this.progress;
                    case 1 -> BlockEntity_Index.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i){
                    case 0 -> BlockEntity_Index.this.progress = i1;
                    case 1 -> BlockEntity_Index.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyOptional = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOptional.invalidate();
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i= 0;i < itemStackHandler.getSlots();i++){
            inventory.setItem(i,itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level,this.worldPosition,inventory);
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.nu.index");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        this.player = player;
        return new Screen_IndexMenu(i,inventory,this,this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory",itemStackHandler.serializeNBT());
        pTag.putInt("index.progress",progress);
        pTag.put("index.enchants",enchantData.formatEnchants());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("index.progress");
        enchantData.loadEnchants(pTag.getList("index.enchants", Tag.TAG_COMPOUND));
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (checkItem()){
            addProgress();
            setChanged(level,blockPos,blockState);
            if (isDone()) {
                outPutItem();
                resetProgress();
            }
        }else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void outPutItem() {
        ItemStack inItem = this.itemStackHandler.getStackInSlot(SLOT_INPUT_ITEM);
        if (!(inItem.getEnchantmentTags().isEmpty()) && this.itemStackHandler.getStackInSlot(SLOT_OUTPUT) == ItemStack.EMPTY){
            ItemStack in = inItem.copy();
            enchantData.addToEnchantData(inItem);
            this.itemStackHandler.extractItem(SLOT_INPUT_ITEM,1,false);
            in.getEnchantmentTags().clear();
            this.itemStackHandler.setStackInSlot(SLOT_OUTPUT,new ItemStack(in.getItem(),1));
            //saveAdditional(this.getPersistentData());
            Net_Index_Core.sendToPlayer(new Packet_Index_ServerToClient(getUpdateTag()), (ServerPlayer) this.player);
        }
    }

    private boolean isDone() {
        return progress >= maxProgress;
    }

    private void addProgress() {
        progress++;
    }

    private boolean checkItem(){
        ItemStack in = this.itemStackHandler.getStackInSlot(SLOT_INPUT_ITEM);
        if (in == ItemStack.EMPTY){
            return false;
        }
        if (TYPE){
            return !in.getEnchantmentTags().isEmpty();
        }
        return this.itemStackHandler.getStackInSlot(SLOT_OUTPUT) == ItemStack.EMPTY;
    }
    public ItemStack getSlotItem(int ind){
        return this.itemStackHandler.getStackInSlot(ind);
    }
    public HashMap<Enchantment,_DataType_StringIntInt> getSaveEnchants(){
        return enchantData.getEnchantData();
    }

    public void setTYPE(boolean TYPE) {
        this.TYPE = TYPE;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag dat = new CompoundTag();
        dat.put("index.enchants",enchantData.formatEnchants());
        return dat;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
