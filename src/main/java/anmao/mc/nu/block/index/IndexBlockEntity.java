package anmao.mc.nu.block.index;

import anmao.mc.amlib.enchantment.EnchantmentHelper;
import anmao.mc.nu.block.NUBlockEntities;
import anmao.mc.nu.block.MenuBlockEntityCore;
import anmao.mc.nu.config.Configs;
import anmao.mc.nu.datatype.EnchantDataType;
import anmao.mc.nu.datatype.EnchantDataTypeCore;
import anmao.mc.nu.network.index.NetCore;
import anmao.mc.nu.network.index.packet.IndexPacketSTC;
import anmao.mc.nu.screen.index.IndexMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class IndexBlockEntity extends MenuBlockEntityCore {
    private final String enchantDatSaveKey ="index.enchants", progressSaveKey = "index.progress", mpSaveKey = "index.mp";
    private int progress = 0,maxProgress = 70,mp = 0;
    private final int aInputSlotIndex = 0, aOutputSlotIndex = 1;

    protected final ContainerData data;
    private final BlockPos blockPos;
    private Player player;
    private final EnchantDataType enchantData;
    public IndexBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(NUBlockEntities.INDEX.get(), pPos, pBlockState,2);
        enchantData = new EnchantDataType();
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
    public void drops(){
        SimpleContainer inventory = new SimpleContainer(getItemStackHandler().getSlots());
        for (int i= 0;i < getItemStackHandler().getSlots();i++){
            inventory.setItem(i,getItemStackHandler().getStackInSlot(i));
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
    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag dat = super.getUpdateTag();
        dat.put(enchantDatSaveKey,enchantData.formatEnchants());
        return dat;
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
            mp+= getInputItem().getCount() * base;
            //getItemStackHandler().extractItem(aInputSlotIndex, 1, false);

            getItemStackHandler().setStackInSlot(aInputSlotIndex, ItemStack.EMPTY);
            return true;
        }
        return false;
    }
    private void outPutItem() {
        ItemStack inItem = getInputItem().copy();
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
            ListTag lEnchant = EnchantmentHelper.getEnchantBookEnchants(inItem);
            if (lEnchant != null) {
                for (int i = 0; i < lEnchant.size(); ++i) {
                    CompoundTag lCompoundtag = lEnchant.getCompound(i);
                    enchantData.addEnchant(lCompoundtag.getString("id"), lCompoundtag.getInt("lvl"));
                }
                getItemStackHandler().extractItem(aInputSlotIndex, 1, false);
                getItemStackHandler().setStackInSlot(aOutputSlotIndex, new ItemStack(Items.BOOK, lOutItem.getCount() + 1));
            }else {
                System.out.println("error enchant book");
                getItemStackHandler().extractItem(aInputSlotIndex, 1, false);
                getItemStackHandler().setStackInSlot(aOutputSlotIndex,inItem);
            }
        }else if (!(inItem.getEnchantmentTags().isEmpty()) && lOutItem == ItemStack.EMPTY) {
            ItemStack in = inItem.copy();
            enchantData.addToEnchantData(inItem);
            //this.itemStackHandler.extractItem(aInputSlotIndex, 1, false);
            getItemStackHandler().setStackInSlot(aInputSlotIndex, ItemStack.EMPTY);
            in.getEnchantmentTags().clear();
            getItemStackHandler().setStackInSlot(aOutputSlotIndex, in);

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
    public HashMap<Enchantment, EnchantDataTypeCore> getSaveEnchants(){
        return enchantData.getEnchantData();
    }
    public void handlePacket(CompoundTag msg){
        ItemStack newItem = getInputItem().copy();
        if (newItem != ItemStack.EMPTY && (newItem.getEnchantmentTags().isEmpty() || newItem.getItem() == Items.BOOK)){

            if (getOutputItem().isEmpty()){


                HashMap<Enchantment, Integer> selectEnchants = EnchantmentHelper.CompoundTagToEnchants(msg,"am.enchants");
                if (newItem.getItem() == Items.BOOK){
                    newItem = new ItemStack(Items.ENCHANTED_BOOK);
                }
                ItemStack finalItem = newItem.copy();
                if (Configs.indexMpUse >0) {
                    int[] needMp = {0};
                    selectEnchants.forEach((enchantment, integer) -> {
                        if (enchantment.canEnchant(finalItem) || finalItem.getItem() == Items.ENCHANTED_BOOK) {
                            int lvl = Math.min(integer, enchantData.getLvl(enchantment));
                            needMp[0] += lvl * Configs.indexMpUse;
                        }
                    });
                    if (needMp[0] == 0 || needMp[0] > mp) {
                        return;
                    }
                    mp -= needMp[0];
                }
                selectEnchants.forEach((enchantment, integer) -> {
                    if (enchantment.canEnchant(finalItem) || finalItem.getItem()==Items.ENCHANTED_BOOK){
                        int lvl = Math.min(integer, enchantData.getLvl(enchantment));
                        if (enchantData.dimXp(enchantment,lvl)){
                            if (finalItem.getItem()==Items.ENCHANTED_BOOK){
                                finalItem.getOrCreateTag();
                                if (!finalItem.getTag().contains("StoredEnchantments", 9)) {
                                    finalItem.getTag().put("StoredEnchantments", new ListTag());
                                }
                                ListTag listtag = finalItem.getTag().getList("StoredEnchantments", 10);
                                listtag.add(EnchantmentHelper.EnchantToCompoundTag(enchantment,lvl));
                            }else {
                                finalItem.enchant(enchantment, lvl);
                            }
                        }
                    }
                });
                getItemStackHandler().setStackInSlot(aInputSlotIndex,ItemStack.EMPTY);
                getItemStackHandler().setStackInSlot(aOutputSlotIndex,finalItem);
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
            NetCore.sendToPlayer(new IndexPacketSTC(getUpdateTag()), (ServerPlayer) this.player);
        }
    }
    public boolean shouldRenderFace(Direction pFace) {
        return Block.shouldRenderFace(this.getBlockState(), this.level, this.getBlockPos(), pFace, this.getBlockPos().relative(pFace));
    }
}
