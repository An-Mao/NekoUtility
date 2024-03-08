package anmao.mc.nu.config.equipment_enhancer;

public class EquipmentEnhancerConfigDefaultData {
    protected static String EnhancerItemDefaultConfig = """
            {
              "item.minecraft.nether_star": {
                "type": "ADDITION",
                "value": 1,
                "slots": ["mainhand","offhand","feet","legs","chest","head"]
              }
            }""";
    protected static String ItemDefaultConfig = """
            {
              "default": {
                "MaxEnhancer": 32,
                "MaxMainHandEnhancer": 10,
                "MaxOffHandEnhancer": 10,
                "MaxFeetEnhancer": 10,
                "MaxLegsEnhancer": 10,
                "MaxChestEnhancer": 10,
                "MaxHeadEnhancer": 10
              },
              "item.minecraft.netherite_sword": {
                "MaxEnhancer": 32,
                "MaxMainHandEnhancer": 10,
                "MaxOffHandEnhancer": 10,
                "MaxFeetEnhancer": 10,
                "MaxLegsEnhancer": 10,
                "MaxChestEnhancer": 10,
                "MaxHeadEnhancer": 10
              }
            }""";
    protected static String AttributeDefaultConfig = """
            {
              "default": {
                "MaxAddition": "2*default",
                "MaxMultiplyBase": "1",
                "MaxMultiplyTotal": "1"
              },
              "attribute.name.generic.attack_damage": {
                "MaxAddition": "10",
                "MaxMultiplyBase": "3",
                "MaxMultiplyTotal": "3"
              }
            }""";
}
