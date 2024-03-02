package anmao.mc.nu.config;


import anmao.mc.nu.NU;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = NU.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configs {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.IntValue INDEX_MP_USE;
    public static ForgeConfigSpec.DoubleValue THE_EIGHT_TRIGRAMS_DAMAGE;
    public static final ForgeConfigSpec SPEC;


    static {
        setupConfig();
        SPEC = builder.build();
    }


    public static int indexMpUse;
    public static float theEightTrigramsDamage;
    private static void setupConfig() {
        builder.comment("===============================================");
        builder.comment("==================Neko Utility=================");
        builder.comment("===================Ver 1.0.0===================");
        builder.comment("===============================================");
        builder.comment(" ");
        builder.comment(" ");
        builder.comment("Index Block");
        builder.push("index");
        INDEX_MP_USE = builder
                .comment("MP consumption when enchanting (per enchantment level)")
                .comment("[default:5]")
                .defineInRange("MpUse", 5, 0, 100000000);
        builder.pop();
        builder.comment(" ");
        builder.comment("The Eight Trigrams Block");
        builder.push("TheEightTrigrams");
        THE_EIGHT_TRIGRAMS_DAMAGE = builder
                .comment("damage( v * (1 or 16) * maxHealth)")
                .comment("[default:0.05]")
                .defineInRange("Damage", 0.05, 0, 10);
        builder.pop();
    }
    @SubscribeEvent
    public static void onClientLoad(final ModConfigEvent event)
    {
        indexMpUse = INDEX_MP_USE.get();
        theEightTrigramsDamage = THE_EIGHT_TRIGRAMS_DAMAGE.get().floatValue();
    }
}

