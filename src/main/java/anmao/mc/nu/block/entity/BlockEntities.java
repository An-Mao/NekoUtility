package anmao.mc.nu.block.entity;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NU.MOD_ID);

    public static final RegistryObject<BlockEntityType<IndexBlockEntity>> INDEX = BLOCK_ENTITIES.register("index",()->BlockEntityType.Builder.of(IndexBlockEntity::new, Blocks.INDEX.get()).build(null));
    public static void reg(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
