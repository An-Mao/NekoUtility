package anmao.mc.nu.recipe;

import anmao.mc.nu.NU;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NURecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, NU.MOD_ID);
    public static final RegistryObject<RecipeSerializer<EquipmentEnhancerRecipe>> RECIPE_SERIALIZER_REGISTRY_OBJECT = SERIALIZERS.register("equipment_enhancer",()->EquipmentEnhancerRecipe.Serializer.INSTANCE);
    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
