package anmao.mc.nu.amlib;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AM_EnchantHelp {
    public static final String ENCHANT_ID = "eid";
    public static final String ENCHANT_Lvl = "lvl";
    public static final String ENCHANT_SAVE_KEY = "am.enchants";
    public static Registry<Enchantment> getEnchantReg(){
        return BuiltInRegistries.ENCHANTMENT;
    }
    public static Optional<Enchantment> getEnchantRegWithOptional(ResourceLocation pRes){
        return getEnchantReg().getOptional(pRes);
    }
    public static Optional<Enchantment> getEnchantRegWithOptional(String id){
        return getEnchantRegWithOptional(ResourceLocation.tryParse(id));
    }
    public static Optional<Enchantment> getEnchantRegWithOptionalEid(CompoundTag pTag){
        return getEnchantRegWithOptional(pTag.getString(ENCHANT_ID));
    }
    public static Optional<Enchantment> getEnchantRegWithOptionalVid(CompoundTag pTag){
        return getEnchantRegWithOptional(pTag.getString("id"));
    }

    public static ResourceLocation getRegEnchantId(Enchantment enchantment){
        return EnchantmentHelper.getEnchantmentId(enchantment);
    }
    public static String getEnchantId(Enchantment enchantment){
        return String.valueOf(getRegEnchantId(enchantment));
    }
    public static CompoundTag EnchantToCompoundTag(Enchantment enchantment,int lvl){
        CompoundTag tag = new CompoundTag();
        tag.putString(ENCHANT_ID, getEnchantId(enchantment));
        tag.putInt(ENCHANT_Lvl,lvl);
        return tag;
    }
    public static ListTag EnchantsToListTag(HashMap<Enchantment,Integer> edt){
        ListTag tag = new ListTag();
        edt.forEach((enchantment, integer) -> {
            tag.add(EnchantToCompoundTag(enchantment,integer));
        });
        return tag;
    }
    public static CompoundTag EnchantsToCompoundTag(HashMap<Enchantment,Integer> edt){
        CompoundTag tag = new CompoundTag();
        tag.put(ENCHANT_SAVE_KEY,EnchantsToListTag(edt));
        return tag;
    }
    public static HashMap<Enchantment,Integer> ListTagToEnchants(ListTag tag){
        HashMap<Enchantment,Integer> enchants = new HashMap<>();
        for (int i = 0; i < tag.size(); i++){
            CompoundTag compoundtag = tag.getCompound(i);
            enchants.put(CompoundTagToEnchant(compoundtag), getEnchantmentLevel(compoundtag));
        }
        return enchants;
    }
    public static HashMap<Enchantment,Integer> CompoundTagToEnchants(CompoundTag tag){
        return ListTagToEnchants(tag.getList(ENCHANT_SAVE_KEY, Tag.TAG_COMPOUND));
    }
    public static Enchantment CompoundTagToEnchant(CompoundTag tag){
        return IdToEnchant(tag.getString(ENCHANT_ID));
    }
    public static Enchantment IdToEnchant(String s){
        return getEnchantRegWithOptional(s).orElseThrow(() -> new RuntimeException("Enchantment not found for ID: " + s));
    }
    public static Enchantment VanillaCompoundTagToEnchant(CompoundTag pCompoundTag){
        AtomicReference<Enchantment> enchant = null;
        getEnchantRegWithOptionalEid(pCompoundTag).ifPresent((enchantment) -> enchant.set(enchantment));
        return enchant.get();
    }
    public static int getEnchantmentLevel(CompoundTag pCompoundTag) {
        return Math.max(0,pCompoundTag.getInt(ENCHANT_Lvl));
    }
    public static ListTag getEnchantBookEnchants(ItemStack pItem){
        CompoundTag lEnchantBook = pItem.getTag();
        if (lEnchantBook != null) {
            return lEnchantBook.getList("StoredEnchantments", Tag.TAG_COMPOUND);
        }
        return null;
    }
    public static boolean isEnchantItem(ItemStack pItem){
        if (pItem == ItemStack.EMPTY){
            return false;
        }
        if (pItem.getItem() == Items.ENCHANTED_BOOK){
            return true;
        }
        return !pItem.getEnchantmentTags().isEmpty();
    }
}
