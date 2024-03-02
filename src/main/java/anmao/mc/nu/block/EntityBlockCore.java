package anmao.mc.nu.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityBlockCore extends BaseEntityBlock {
    public ItemStack dropItem;
    protected EntityBlockCore(Properties pProperties) {
        super(pProperties);
    }
    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState);
    @Override
    public void playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
        if (!pLevel.isClientSide){
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (pPlayer.hasCorrectToolForDrops(pState) && entity != null){
                dropItem = new ItemStack(pState.getBlock(),1);
                entity.saveToItem(dropItem);
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }
    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        List<ItemStack> drops = new ArrayList<>();
        if(dropItem != null){
            drops.add(dropItem);
            return drops;
        }else{
            return super.getDrops(pState, pParams);
        }
    }
}
