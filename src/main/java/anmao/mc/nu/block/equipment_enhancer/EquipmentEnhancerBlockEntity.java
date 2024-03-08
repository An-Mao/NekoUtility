package anmao.mc.nu.block.equipment_enhancer;

import anmao.mc.nu.block.MenuBlockEntityCore;
import anmao.mc.nu.block.NUBlockEntities;
import anmao.mc.nu.config.equipment_enhancer.EquipmentEnhancerConfigs;
import anmao.mc.nu.recipe.EquipmentEnhancerRecipe;
import anmao.mc.nu.screen.equipment_enhancer.EquipmentEnhancerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class EquipmentEnhancerBlockEntity extends MenuBlockEntityCore {
    public static final int SlotSize = 17;
    private Player player;

    private final BlockPos blockPos;

    protected final ContainerData data;

    private final int SlotIndex_ItemInput = 0;
    private final int SlotIndex_EnhancerItem = 1;
    private final int SlotIndex_ItemOutput = SlotSize - 1;

    public EquipmentEnhancerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(NUBlockEntities.EQUIPMENT_ENHANCER.get(), pPos, pBlockState, SlotSize);

        blockPos = pPos;
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> EquipmentEnhancerBlockEntity.this.blockPos.getX();
                    case 1 -> EquipmentEnhancerBlockEntity.this.blockPos.getY();
                    case 2 -> EquipmentEnhancerBlockEntity.this.blockPos.getZ();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.nu.equipment_enhancer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        player = pPlayer;
        return new EquipmentEnhancerMenu(pContainerId,pPlayerInventory,this,data);
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return super.getUpdatePacket();
    }
    @Override
    public CompoundTag getUpdateTag() {
        return super.getUpdateTag();
    }
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    public void handlePacket(ResourceLocation resourceLocation,String effectiveSlot){
        @Nullable Attribute att = ForgeRegistries.ATTRIBUTES.getValue(resourceLocation);
        if (att != null && getSlotItem(SlotIndex_ItemOutput).isEmpty()){
            ItemStack itemStack = getSlotItem(SlotIndex_ItemInput).copy();
            itemStack.setCount(1);
            if (checkItem(att,itemStack,getSlotItem(SlotIndex_EnhancerItem),effectiveSlot)) {
                itemStackHandler.extractItem(SlotIndex_ItemInput,1 ,false);
                itemStackHandler.extractItem(SlotIndex_EnhancerItem,1 ,false);
                itemStackHandler.setStackInSlot(SlotIndex_ItemOutput, itemStack);
                setChanged();
                if (level != null) {
                    level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
                }
            }
        }
    }
    public boolean checkItem(Attribute attribute, ItemStack itemStack,ItemStack enhancerItem,String effectiveSlot){
        if (itemStack.isEmpty() || enhancerItem.isEmpty()) return false;
        EquipmentEnhancerConfigs.EnhancerItemConfig enhancerItemConfig = EquipmentEnhancerConfigs.enhancerItemConfig.get(enhancerItem.getItem().getDescriptionId());
        if (enhancerItemConfig == null) return false;
        if (!Arrays.asList(enhancerItemConfig.getSlots()).contains(effectiveSlot)) return false;
        EquipmentEnhancerConfigs.ItemConfig itemConfig = EquipmentEnhancerConfigs.itemConfig.get(itemStack.getItem().getDescriptionId());
        if (itemConfig == null) itemConfig = EquipmentEnhancerConfigs.itemConfig.get("default");
        if (itemConfig == null) return false;
        itemStack.getOrCreateTag();
        if (itemStack.getTag().getInt("enhancer.nu.slot." + effectiveSlot) < itemConfig.getSlotMaxEnhancer(effectiveSlot)) {
            AttributeModifier attributeModifier = new AttributeModifier("attribute.nu.enhancer."+effectiveSlot+"." + enhancerItemConfig.getType(), enhancerItemConfig.getValue(), enhancerItemConfig.getOperationType());
            //addItemAttributeModifier(itemStack, att, add[0], null);
            //itemStack.addAttributeModifier(att,add,null);
            return addItemAttributeModifier(itemStack, attribute, attributeModifier, getEquipmentSlot(effectiveSlot));
        }
        return false;
    }
    public EquipmentSlot getEquipmentSlot(String s){
        return EquipmentSlot.byName(s);
    }
    public boolean addItemAttributeModifier(ItemStack itemStack, Attribute pAttribute, AttributeModifier pModifier,EquipmentSlot pSlot) {
        itemStack.getOrCreateTag();
        if (!itemStack.getTag().contains("AttributeModifiers", 9)) {
            itemStack.getTag().put("AttributeModifiers", new ListTag());
        }
        ListTag listtag = itemStack.getTag().getList("AttributeModifiers", Tag.TAG_COMPOUND);
        if (!listtag.isEmpty()) {
            for (int i = 0; i < listtag.size(); i++) {
                CompoundTag compoundTag = listtag.getCompound(i);
                if (
                        compoundTag.getString("AttributeName").equals(ForgeRegistries.ATTRIBUTES.getKey(pAttribute).toString()) &&
                                compoundTag.getString("Name").equals(pModifier.getName()) &&
                                compoundTag.getInt("Operation") == pModifier.getOperation().toValue()
                ) {
                    double amount = compoundTag.getDouble("Amount");
                    EquipmentEnhancerConfigs.AttributeConfig attributeConfig = EquipmentEnhancerConfigs.attributeConfigConfig.get(pAttribute.getDescriptionId());
                    if (attributeConfig == null) attributeConfig = EquipmentEnhancerConfigs.attributeConfigConfig.get("default");
                    if (attributeConfig == null) return false;
                    double maxAmount = attributeConfig.getMaxAmount(pModifier.getOperation(),pAttribute.getDefaultValue());
                    System.out.println("maxAmount :: " + maxAmount);
                    if (amount < maxAmount) {
                        amount += pModifier.getAmount();
                        amount = Math.min(amount,maxAmount);
                        compoundTag.putDouble("Amount", amount);
                        return true;
                    }
                    return false;
                }
            }
        }
        listtag.add(getAttributeModifierCompoundTag(pAttribute,pModifier,pSlot));
        return true;
    }
    public CompoundTag getAttributeModifierCompoundTag(Attribute attribute, AttributeModifier modifier,EquipmentSlot slot){

        CompoundTag compoundtag = modifier.save();
        compoundtag.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(attribute).toString());
        if (slot != null) {
            compoundtag.putString("Slot", slot.getName());
        }
        return compoundtag;
    }

    public boolean hasRecipe(){
        Optional<EquipmentEnhancerRecipe> recipe = getRecipe();
        if (recipe.isEmpty()){
            return false;
        }
        ItemStack result = recipe.get().getResultItem(null);
        return true;
    }

    private Optional<EquipmentEnhancerRecipe> getRecipe() {
        SimpleContainer inv = new SimpleContainer(this.itemStackHandler.getSlots());
        for (int i = 0;i<itemStackHandler.getSlots();i++){
            inv.setItem(i,this.itemStackHandler.getStackInSlot(i));
        }
        return this.level.getRecipeManager().getRecipeFor(EquipmentEnhancerRecipe.Type.INSTANCE,inv,level);
    }
}
