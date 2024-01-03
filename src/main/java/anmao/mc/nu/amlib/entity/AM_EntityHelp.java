package anmao.mc.nu.amlib.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.swing.text.html.parser.Entity;

public class AM_EntityHelp {
    public static Level getLevel(LivingEntity pLivingEntity){
        return pLivingEntity.level();
    }
    public static ServerLevel getServerLevel(LivingEntity pLivingEntity){
        return (ServerLevel) pLivingEntity.level();
    }
    public static BlockEntity getBlockEntity(Level pLevel, BlockPos pPos){
        return pLevel.getBlockEntity(pPos);
    }
    public static BlockEntity getBlockEntity(LivingEntity pLivingEntity, BlockPos pPos){
        return getLevel(pLivingEntity).getBlockEntity(pPos);
    }
}
