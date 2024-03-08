package anmao.mc.nu.block.the_eight_trigrams;

import anmao.mc.amlib.entity.EntityHelper;
import anmao.mc.nu.block.NUBlockEntities;
import anmao.mc.nu.config.Configs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TheEightTrigramsBlockEntity extends BlockEntity{
    private int t = 0;
    protected final ContainerData data;
    private boolean open;
    public TheEightTrigramsBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(NUBlockEntities.THE_EIGHT_TRIGRAMS.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return t;
            }

            @Override
            public void set(int i, int i1) {
                t = i1;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public int getT() {
        return t;
    }
    public void setState(){
        this.open = !this.open ;
        //System.out.println("type :: "+open);
    }
    public boolean getState(){
        return this.open;
    }
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level != null && open) {
            if (t > 16) {
                t = 0;
            }
            t++;
            List<Entity> entities = level.getEntities(null, new AABB(blockPos).inflate(64));
            entities.forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity) {
                    if (!(EntityHelper.isPlayerOrNpcOrAnimal(livingEntity))) {
                        int d = livingEntity.getPersistentData().getInt("tet.death");
                        if (d > 99){
                            livingEntity.die(livingEntity.damageSources().genericKill());
                            livingEntity.remove(Entity.RemovalReason.KILLED);
                        }else {
                            d++;
                            livingEntity.getPersistentData().putInt("tet.death",d);
                            livingEntity.hurt(livingEntity.damageSources().genericKill(), livingEntity.getMaxHealth() * (t * Configs.theEightTrigramsDamage));
                        }
                    }
                }
            });
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);

        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("tick",t);
        pTag.putBoolean("open",open);
    }

    @Override
    public void load(CompoundTag pTag) {
        t = pTag.getInt("tick");
        open = pTag.getBoolean("open");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("tick",t);
        tag.putBoolean("open",open);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
