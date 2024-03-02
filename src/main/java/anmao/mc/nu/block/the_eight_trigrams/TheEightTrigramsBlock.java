package anmao.mc.nu.block.the_eight_trigrams;

import anmao.mc.nu.block.EntityBlockCore;
import anmao.mc.nu.block.entity.NUBlockEntities;
import anmao.mc.nu.block.entity.the_eight_trigrams.TheEightTrigramsBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TheEightTrigramsBlock extends EntityBlockCore {

    public static final VoxelShape SHAPE = Block.box(-16,0,-16,32,1,32);
    public TheEightTrigramsBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).lightLevel(value -> 9).noOcclusion());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide){
            return null;
        }
        return createTickerHelper(pBlockEntityType, NUBlockEntities.THE_EIGHT_TRIGRAMS.get(), ((level, blockPos, blockState, blockEntityIndex) -> blockEntityIndex.tick(level,blockPos,blockState)));
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new TheEightTrigramsBlockEntity(pPos,pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide){
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof TheEightTrigramsBlockEntity theEightTrigramsBlockEntity){
                theEightTrigramsBlockEntity.setState();
            }else {
                throw new IllegalStateException("Missing");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("tooltip.nu.the_eight_trigrams.item.desc").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
