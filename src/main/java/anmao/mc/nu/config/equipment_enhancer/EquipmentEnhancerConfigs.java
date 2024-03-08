package anmao.mc.nu.config.equipment_enhancer;

import anmao.mc.amlib.math._Math;
import anmao.mc.amlib.system._File;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.slf4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EquipmentEnhancerConfigs extends EquipmentEnhancerConfigDefaultData{
    public static final Logger LOG = LogUtils.getLogger();
    private static final String EquipmentEnhancerDir = _File.getFileFullPathWithRun("/config/NekoUtility/EquipmentEnhancer/");
    private static final String EnhancerItemFileName = EquipmentEnhancerDir + "/EnhancerItem.json";
    private static final String ItemFileName = EquipmentEnhancerDir + "/Item.json";
    private static final String AttributeFileName = EquipmentEnhancerDir + "/Attribute.json";
    public static Map<String , EnhancerItemConfig> enhancerItemConfig = new HashMap<>();
    public static Map<String , ItemConfig> itemConfig = new HashMap<>();
    public static Map<String , AttributeConfig> attributeConfigConfig = new HashMap<>();
    static {
        _File.checkAndCreateDir(EquipmentEnhancerDir);
    }
    public static void init(){
        LOG.debug("Start Load Equipment Enhancer");
        File file = new File(EnhancerItemFileName);
        if (!file.exists()){
            ResetEnhancerItemFile();
        }
        EnhancerItemLoad();
        file = new File(ItemFileName);
        if (!file.exists()){
            ResetItemFile();
        }
        ItemLoad();
        file = new File(AttributeFileName);
        if (!file.exists()){
            ResetAttributeFile();
        }
        AttributeLoad();
    }
    private static void ResetEnhancerItemFile(){
        try (FileWriter writer = new FileWriter(EnhancerItemFileName)) {
            writer.write(EquipmentEnhancerConfigDefaultData.EnhancerItemDefaultConfig);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
    private static void ResetItemFile(){
        try (FileWriter writer = new FileWriter(ItemFileName)) {
            writer.write(EquipmentEnhancerConfigDefaultData.ItemDefaultConfig);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
    private static void ResetAttributeFile(){
        try (FileWriter writer = new FileWriter(AttributeFileName)) {
            writer.write(EquipmentEnhancerConfigDefaultData.AttributeDefaultConfig);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
    private static void EnhancerItemLoad(){
        Gson gson = new Gson();
        try (Reader reader = new FileReader(EnhancerItemFileName)) {
            enhancerItemConfig = gson.fromJson(reader, new TypeToken<Map<String, EnhancerItemConfig>>() {}.getType());
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    private static void ItemLoad(){
        Gson gson = new Gson();
        try (Reader reader = new FileReader(ItemFileName)) {
            itemConfig = gson.fromJson(reader, new TypeToken<Map<String, ItemConfig>>() {}.getType());
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    private static void AttributeLoad(){
        Gson gson = new Gson();
        try (Reader reader = new FileReader(AttributeFileName)) {
            attributeConfigConfig = gson.fromJson(reader, new TypeToken<Map<String, AttributeConfig>>() {}.getType());
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    public static class EnhancerItemConfig {
        private String type;
        private double value;
        private String[] slots;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String[] getSlots() {
            return slots;
        }

        public void setSlots(String[] slots) {
            this.slots = slots;
        }
        public AttributeModifier.Operation getOperationType(){
            if (type.equals("MULTIPLY_BASE")) {
                return AttributeModifier.Operation.MULTIPLY_BASE;
            }else if (type.equals("MULTIPLY_TOTAL")) {
                return AttributeModifier.Operation.MULTIPLY_TOTAL;
            }
            return AttributeModifier.Operation.ADDITION;
        }
    }
    public static class ItemConfig {
        private int MaxEnhancer;
        private int MaxMainHandEnhancer;
        private int MaxOffHandEnhancer;
        private int MaxFeetEnhancer;
        private int MaxLegsEnhancer;
        private int MaxChestEnhancer;
        private int MaxHeadEnhancer;

        public int getMaxEnhancer() {
            return MaxEnhancer;
        }
        public void setMaxEnhancer(int maxEnhancer) {
            MaxEnhancer = maxEnhancer;
        }

        public int getMaxMainHandEnhancer() {
            return MaxMainHandEnhancer;
        }

        public void setMaxMainHandEnhancer(int maxMainHandEnhancer) {
            MaxMainHandEnhancer = maxMainHandEnhancer;
        }

        public int getMaxOffHandEnhancer() {
            return MaxOffHandEnhancer;
        }

        public void setMaxOffHandEnhancer(int maxOffHandEnhancer) {
            MaxOffHandEnhancer = maxOffHandEnhancer;
        }

        public int getMaxFeetEnhancer() {
            return MaxFeetEnhancer;
        }

        public void setMaxFeetEnhancer(int maxFeetEnhancer) {
            MaxFeetEnhancer = maxFeetEnhancer;
        }

        public int getMaxLegsEnhancer() {
            return MaxLegsEnhancer;
        }

        public void setMaxLegsEnhancer(int maxLegsEnhancer) {
            MaxLegsEnhancer = maxLegsEnhancer;
        }

        public int getMaxChestEnhancer() {
            return MaxChestEnhancer;
        }

        public void setMaxChestEnhancer(int maxChestEnhancer) {
            MaxChestEnhancer = maxChestEnhancer;
        }

        public int getMaxHeadEnhancer() {
            return MaxHeadEnhancer;
        }

        public void setMaxHeadEnhancer(int maxHeadEnhancer) {
            MaxHeadEnhancer = maxHeadEnhancer;
        }
        public int getSlotMaxEnhancer(String s){
            return switch (s) {
                case "mainhand" -> getMaxMainHandEnhancer();
                case "offhand" -> getMaxOffHandEnhancer();
                case "feet" -> getMaxFeetEnhancer();
                case "legs" -> getMaxLegsEnhancer();
                case "chest" -> getMaxChestEnhancer();
                case "head" -> getMaxHeadEnhancer();
                default -> 0;
            };
        }
    }
    public static class AttributeConfig{
        private String MaxAddition;
        private String MaxMultiplyBase;
        private String MaxMultiplyTotal;

        public String getMaxAddition() {
            return MaxAddition;
        }

        public void setMaxAddition(String maxAddition) {
            MaxAddition = maxAddition;
        }

        public String getMaxMultiplyBase() {
            return MaxMultiplyBase;
        }

        public void setMaxMultiplyBase(String maxMultiplyBase) {
            MaxMultiplyBase = maxMultiplyBase;
        }

        public String getMaxMultiplyTotal() {
            return MaxMultiplyTotal;
        }

        public void setMaxMultiplyTotal(String maxMultiplyTotal) {
            MaxMultiplyTotal = maxMultiplyTotal;
        }
        public double getMaxAmount(AttributeModifier.Operation operation,double def){
            String v = switch (operation){
                case ADDITION -> getMaxAddition();
                case MULTIPLY_BASE -> getMaxMultiplyBase();
                case MULTIPLY_TOTAL -> getMaxMultiplyTotal();
            };
            System.out.println(v.replace("default",String.valueOf(def)));
            return _Math.evaluate(v.replace("default",String.valueOf(def)));
        }
    }
}
