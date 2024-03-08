package anmao.mc.nu.block.equipment_enhancer;

import anmao.mc.nu.block.EntityBlockCore;
import anmao.mc.nu.block.index.IndexBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EquipmentEnhancerBlock extends EntityBlockCore {
    public EquipmentEnhancerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).lightLevel(value -> 9).noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide){
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof EquipmentEnhancerBlockEntity){
                NetworkHooks.openScreen(((ServerPlayer) pPlayer),(EquipmentEnhancerBlockEntity)entity,pPos);
            }else {
                throw new IllegalStateException("Missing");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new EquipmentEnhancerBlockEntity(pPos,pState);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
