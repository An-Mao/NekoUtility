package anmao.mc.nu.block.entity;

import anmao.mc.nu.amlib.AM_EnchantHelp;
import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import anmao.mc.nu.amlib.datatype._DataType_StringIntInt;
import anmao.mc.nu.network.index.Net_Index_Core;
import anmao.mc.nu.network.index.packet.Packet_Index_ServerToClient;
import anmao.mc.nu.screen.Screen_IndexMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import java.util.Map;

public class BlockEntity_Index extends BlockEntity implements MenuProvider {
    /**
     * slot
     * 0 -> input item
     * 1 -> output item
     * 2 -> material item
     */
    private static final int[] SLOTS = {0,1,2};
    private int getInputSlot (){
        return SLOTS[0];
    };
    private int getOutputSlot (){
        return SLOTS[1];
    }
    private int getMaterialSlot(){
        return SLOTS[2];
    }
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(SLOTS.length);
    protected final ContainerData data;
    private final BlockPos blockPos;
    private int progress = 0;
    private int maxProgress = 70;
    //0 : in
    //1 : out
    private int TYPE = 0;
    private boolean isInMode(){
        return TYPE == 0;
    }
    private void setMode(int t){
        TYPE = t;
    }
    private Player player;
    private final _DataType_EnchantData enchantData;
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.empty();
    public BlockEntity_Index(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntities.INDEX.get(), pPos, pBlockState);
        enchantData = new _DataType_EnchantData();
        this.blockPos = pPos;
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> BlockEntity_Index.this.progress;
                    case 1 -> BlockEntity_Index.this.maxProgress;
                    case 2 -> BlockEntity_Index.this.blockPos.getX();
                    case 3 -> BlockEntity_Index.this.blockPos.getY();
                    case 4 -> BlockEntity_Index.this.blockPos.getZ();
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
                return 5;
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
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
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
        ItemStack inItem = getSlotItem(getInputSlot());
        if (inItem.isEmpty()){
            return;
        }
        ItemStack lOutItem = getSlotItem(getOutputSlot());
        if (inItem.getItem() == Items.ENCHANTED_BOOK && (lOutItem == ItemStack.EMPTY || lOutItem.getItem() == Items.BOOK)){
            if (lOutItem.getItem() == Items.BOOK){
                if (lOutItem.getCount() >= lOutItem.getMaxStackSize()){
                    return;
                }
            }
            CompoundTag lEnchantBook = inItem.getTag();
            ListTag lEnchant;
            System.out.println(lEnchantBook);
            if (lEnchantBook != null) {
                lEnchant = lEnchantBook.getList("StoredEnchantments",Tag.TAG_COMPOUND);
                for(int i = 0; i < lEnchant.size(); ++i) {
                    CompoundTag lCompoundtag = lEnchant.getCompound(i);
                    enchantData.addEnchant(lCompoundtag.getString("id"),lCompoundtag.getInt("lvl"));
                }
                this.itemStackHandler.extractItem(getInputSlot(), 1, false);
                this.itemStackHandler.setStackInSlot(getOutputSlot(), new ItemStack(Items.BOOK, lOutItem.getCount()+1));
            }
        }else if (!(inItem.getEnchantmentTags().isEmpty()) && lOutItem == ItemStack.EMPTY) {
            ItemStack in = inItem.copy();
            enchantData.addToEnchantData(inItem);
            this.itemStackHandler.extractItem(getInputSlot(), 1, false);
            in.getEnchantmentTags().clear();
            this.itemStackHandler.setStackInSlot(getOutputSlot(), new ItemStack(in.getItem(), 1));

        }
        Net_Index_Core.sendToPlayer(new Packet_Index_ServerToClient(getUpdateTag()), (ServerPlayer) this.player);
    }

    private boolean isDone() {
        return progress >= maxProgress;
    }

    private void addProgress() {
        progress++;
    }

    private boolean checkItem(){
        ItemStack in = getSlotItem(getInputSlot());
        if (in == ItemStack.EMPTY){
            return false;
        }
        if (isInMode()){
            if (getSlotItem(getOutputSlot()) != ItemStack.EMPTY){
                return in.getItem() == Items.ENCHANTED_BOOK && getSlotItem(getOutputSlot()).getItem() == Items.BOOK;
            }
            return !in.getEnchantmentTags().isEmpty() || in.getItem() == Items.ENCHANTED_BOOK;
        }else {
            return in.getEnchantmentTags().isEmpty();
        }
    }
    public ItemStack getSlotItem(int ind){
        return this.itemStackHandler.getStackInSlot(ind);
    }
    public HashMap<Enchantment,_DataType_StringIntInt> getSaveEnchants(){
        return enchantData.getEnchantData();
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

    public void handlePacket(CompoundTag msg){
        HashMap<Enchantment, Integer> lSelectEnchants = AM_EnchantHelp.CompoundTagToEnchants(msg);
        ItemStack lItem = getSlotItem(getInputSlot()).copy();
        if (lItem != ItemStack.EMPTY && (lItem.getEnchantmentTags().isEmpty() || lItem.getItem() == Items.BOOK)){
            if (getSlotItem(getOutputSlot()).isEmpty()){
                if (lItem.getItem() == Items.BOOK){
                    lItem = new ItemStack(Items.ENCHANTED_BOOK);
                }
                ItemStack finalLItem = lItem.copy();
                lSelectEnchants.forEach((enchantment, integer) -> {
                    if (enchantment.canEnchant(finalLItem) || finalLItem.getItem()==Items.ENCHANTED_BOOK){
                        int lLvl = Math.min(integer, enchantData.getLvl(enchantment));
                        if (enchantData.dimXp(enchantment,lLvl)){
                            finalLItem.enchant(enchantment,lLvl);
                        }
                    }
                });
                itemStackHandler.extractItem(getInputSlot(),1,false);

                itemStackHandler.setStackInSlot(getOutputSlot(),finalLItem);
                Net_Index_Core.sendToPlayer(new Packet_Index_ServerToClient(getUpdateTag()), (ServerPlayer) this.player);
            }
        }
    }
}
