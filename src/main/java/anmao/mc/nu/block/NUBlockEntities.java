package anmao.mc.nu.block;

import anmao.mc.nu.NU;
import anmao.mc.nu.block.NUBlocks;
import anmao.mc.nu.block.equipment_enhancer.EquipmentEnhancerBlockEntity;
import anmao.mc.nu.block.index.IndexBlockEntity;
import anmao.mc.nu.block.the_eight_trigrams.TheEightTrigramsBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NUBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NU.MOD_ID);

    public static final RegistryObject<BlockEntityType<IndexBlockEntity>> INDEX = BLOCK_ENTITIES.register("index",()->BlockEntityType.Builder.of(IndexBlockEntity::new, NUBlocks.INDEX.get()).build(null));
    public static final RegistryObject<BlockEntityType<TheEightTrigramsBlockEntity>> THE_EIGHT_TRIGRAMS = BLOCK_ENTITIES.register("the_eight_trigrams",()->BlockEntityType.Builder.of(TheEightTrigramsBlockEntity::new, NUBlocks.THE_EIGHT_TRIGRAMS.get()).build(null));
    public static final RegistryObject<BlockEntityType<EquipmentEnhancerBlockEntity>> EQUIPMENT_ENHANCER = BLOCK_ENTITIES.register("equipment_enhancer",()->BlockEntityType.Builder.of(EquipmentEnhancerBlockEntity::new, NUBlocks.EQUIPMENT_ENHANCER.get()).build(null));
    public static void reg(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
