package anmao.mc.nu.screen.equipment_enhancer;

import anmao.mc.amlib.entity.EntityHelper;
import anmao.mc.nu.block.NUBlocks;
import anmao.mc.nu.block.equipment_enhancer.EquipmentEnhancerBlockEntity;
import anmao.mc.nu.screen.AbstractContainerMenuCore;
import anmao.mc.nu.screen.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class EquipmentEnhancerMenu extends AbstractContainerMenuCore {
    private final EquipmentEnhancerBlockEntity enhancerBlock;
    private final Level level;
    private final ContainerData data;
    public EquipmentEnhancerMenu(int pContainerId, Inventory inventory , FriendlyByteBuf ex) {
        this(pContainerId,inventory, EntityHelper.getBlockEntity(inventory.player,ex.readBlockPos()),new SimpleContainerData(3));
    }
    public EquipmentEnhancerMenu(int pContainerId, Inventory inventory , BlockEntity blockEntity,ContainerData data) {
        super(MenuTypes.EQUIPMENT_ENHANCER_MENU.get(), pContainerId,EquipmentEnhancerBlockEntity.SlotSize);
        checkContainerSize(inventory,EquipmentEnhancerBlockEntity.SlotSize);
        this.enhancerBlock = (EquipmentEnhancerBlockEntity) blockEntity;
        this.level = inventory.player.level();
        this.data = data;
        addPlayerHotBar(inventory,8,142,18);
        addPlayerInventory(inventory,8,84,18);
        this.enhancerBlock.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler,0,26,16));
            this.addSlot(new SlotItemHandler(iItemHandler,1,75,16));
            this.addSlot(new SlotItemHandler(iItemHandler,2,133,16));
        });
        addDataSlots(data);
    }

    public int getX(){
        return data.get(0);
    }
    public int getY(){
        return data.get(1);
    }
    public int getZ(){
        return data.get(2);
    }
    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level,enhancerBlock.getBlockPos()),pPlayer, NUBlocks.EQUIPMENT_ENHANCER.get());
    }

}
