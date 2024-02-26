package anmao.mc.nu.block.entity;

import anmao.mc.nu.amlib.AM_EnchantHelp;
import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import anmao.mc.nu.amlib.datatype._DataType_StringIntInt;
import anmao.mc.nu.network.index.Net_Index_Core;
import anmao.mc.nu.network.index.packet.Packet_Index_ServerToClient;
import anmao.mc.nu.screen.IndexMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

public class IndexBlockEntity extends BlockEntity implements MenuProvider {
    private final String inventorySaveKey = "inventory", enchantDatSaveKey ="index.enchants", progressSaveKey = "index.progress", mpSaveKey = "index.mp";
    private int progress = 0,maxProgress = 70,mp = 0;
    private final int aInputSlotIndex = 0, aOutputSlotIndex = 1;
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            /*
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
             */
        }
    };
    protected final ContainerData data;
    private final BlockPos blockPos;
    private Player player;
    private final _DataType_EnchantData enchantData;
    private LazyOptional<IItemHandler> lazyOptional = LazyOptional.empty();
    public IndexBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntities.INDEX.get(), pPos, pBlockState);
        enchantData = new _DataType_EnchantData();
        this.blockPos = pPos;
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> IndexBlockEntity.this.progress;
                    case 1 -> IndexBlockEntity.this.maxProgress;
                    case 2 -> IndexBlockEntity.this.blockPos.getX();
                    case 3 -> IndexBlockEntity.this.blockPos.getY();
                    case 4 -> IndexBlockEntity.this.blockPos.getZ();
                    case 5 -> IndexBlockEntity.this.mp;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i){
                    case 0 -> IndexBlockEntity.this.progress = i1;
                    case 1 -> IndexBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    //------------------------------------------------------------------------------------------
    public ItemStack getSlotItem(int ind){
        return this.itemStackHandler.getStackInSlot(ind);
    }
    public ItemStack getInputItem(){
        return getSlotItem(aInputSlotIndex);
    }
    public ItemStack getOutputItem(){
        return getSlotItem(aOutputSlotIndex);
    }
    private void resetProgress() {
        progress = 0;
    }
    private boolean isDone() {
        return progress >= maxProgress;
    }
    private void addProgress() {
        progress++;
    }
    //---------------------------------------------------------------------------------------------
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
        if (this.level != null) {
            Containers.dropContents(this.level,this.worldPosition,inventory);
        }
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.nu.index");
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        this.player = player;
        return new IndexMenu(i,inventory,this,this.data);
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        saveDat(pTag);
    }
    private CompoundTag saveDat(CompoundTag tag){
        tag.put(enchantDatSaveKey,enchantData.formatEnchants());
        tag.putInt(progressSaveKey,progress);
        tag.putInt(mpSaveKey,mp);
        tag.put(inventorySaveKey,itemStackHandler.serializeNBT());
        return tag;
    }
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        loadDat(pTag);
    }
    private void loadDat(CompoundTag tag){
        enchantData.loadEnchants(tag.getList(enchantDatSaveKey, Tag.TAG_COMPOUND));
        progress = tag.getInt(progressSaveKey);
        mp = tag.getInt(mpSaveKey);
        itemStackHandler.deserializeNBT(tag.getCompound(inventorySaveKey));
    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag dat = new CompoundTag();
        dat.put(enchantDatSaveKey,enchantData.formatEnchants());
        super.getUpdateTag();
        return dat;
    }
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (addMP())return;
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

    private boolean addMP(){
        int base = 0;
        if (getInputItem().getItem() == Items.LAPIS_LAZULI){
            base = 1;
        }else if (getInputItem().getItem() == Items.LAPIS_BLOCK){
            base = 9;
        }
        if (base > 0){
            mp+= base;
            this.itemStackHandler.extractItem(aInputSlotIndex, 1, false);
            return true;
        }
        return false;
    }
    private void outPutItem() {
        ItemStack inItem = getInputItem();
        if (inItem.isEmpty()){
            return;
        }

        ItemStack lOutItem = getOutputItem();
        if (inItem.getItem() == Items.ENCHANTED_BOOK && (lOutItem == ItemStack.EMPTY || lOutItem.getItem() == Items.BOOK)){
            if (lOutItem.getItem() == Items.BOOK){
                if (lOutItem.getCount() >= lOutItem.getMaxStackSize()){
                    return;
                }
            }
            ListTag lEnchant = AM_EnchantHelp.getEnchantBookEnchants(inItem);
            if (lEnchant != null) {
                for (int i = 0; i < lEnchant.size(); ++i) {
                    CompoundTag lCompoundtag = lEnchant.getCompound(i);
                    enchantData.addEnchant(lCompoundtag.getString("id"), lCompoundtag.getInt("lvl"));
                }
                this.itemStackHandler.extractItem(aInputSlotIndex, 1, false);
                this.itemStackHandler.setStackInSlot(aOutputSlotIndex, new ItemStack(Items.BOOK, lOutItem.getCount() + 1));
            }else {
                System.out.println("error enchant book");
            }
        }else if (!(inItem.getEnchantmentTags().isEmpty()) && lOutItem == ItemStack.EMPTY) {
            ItemStack in = inItem.copy();
            enchantData.addToEnchantData(inItem);
            this.itemStackHandler.extractItem(aInputSlotIndex, 1, false);
            in.getEnchantmentTags().clear();
            this.itemStackHandler.setStackInSlot(aOutputSlotIndex, new ItemStack(in.getItem(), 1));

        }
        updateToClient();
    }
    private boolean checkItem(){
        ItemStack in = getInputItem();
        if (in == ItemStack.EMPTY){
            return false;
        }
        if (getOutputItem() != ItemStack.EMPTY){
            return in.getItem() == Items.ENCHANTED_BOOK && getOutputItem().getItem() == Items.BOOK;
        }
        return !in.getEnchantmentTags().isEmpty() || in.getItem() == Items.ENCHANTED_BOOK;
    }
    public HashMap<Enchantment,_DataType_StringIntInt> getSaveEnchants(){
        return enchantData.getEnchantData();
    }
    public void handlePacket(CompoundTag msg){
        ItemStack newItem = getInputItem().copy();
        if (newItem != ItemStack.EMPTY && (newItem.getEnchantmentTags().isEmpty() || newItem.getItem() == Items.BOOK)){

            if (getOutputItem().isEmpty()){


                HashMap<Enchantment, Integer> selectEnchants = AM_EnchantHelp.CompoundTagToEnchants(msg);
                int[] needMp = {0};
                selectEnchants.values().forEach(integer -> needMp[0] += integer * 5);
                if (needMp[0] > mp){
                    return;
                }
                mp -= needMp[0];
                if (newItem.getItem() == Items.BOOK){
                    newItem = new ItemStack(Items.ENCHANTED_BOOK);
                }
                ItemStack finalItem = newItem.copy();
                selectEnchants.forEach((enchantment, integer) -> {
                    if (enchantment.canEnchant(finalItem) || finalItem.getItem()==Items.ENCHANTED_BOOK){
                        int lvl = Math.min(integer, enchantData.getLvl(enchantment));
                        if (enchantData.dimXp(enchantment,lvl)){
                            finalItem.enchant(enchantment,lvl);
                        }
                    }
                });
                itemStackHandler.extractItem(aInputSlotIndex,1,false);
                itemStackHandler.setStackInSlot(aOutputSlotIndex,finalItem);
                updateToClient();
            }
        }
    }
    private void updateToClient(){
        setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
        if (this.player != null) {
            Net_Index_Core.sendToPlayer(new Packet_Index_ServerToClient(getUpdateTag()), (ServerPlayer) this.player);
        }
    }
    public boolean shouldRenderFace(Direction pFace) {
        return Block.shouldRenderFace(this.getBlockState(), this.level, this.getBlockPos(), pFace, this.getBlockPos().relative(pFace));
    }
}
