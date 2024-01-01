package anmao.mc.nu.amlib.datatype;

import anmao.mc.nu.amlib.AM_EnchantHelp;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.HashMap;

public class _DataType_EnchantData {
    private final String SAVE_DAT_ENCHANT_ID = "eid";
    private final String SAVE_DAT_ENCHANT_MAX = "max";
    private final String SAVE_DAT_ENCHANT_XP = "xp";
    private HashMap<Enchantment, _DataType_StringIntInt> enchantData;
    public _DataType_EnchantData(){
        enchantData = new HashMap<>();
    }
    public ListTag formatEnchants(){
        ListTag es = new ListTag();
        if (!enchantData.isEmpty()){
            enchantData.forEach((eid,intInt)-> es.add(setNewData(intInt.getEid(),intInt.getMaxLvl(),intInt.getXp())));
        }
        return es;
    }
    public void loadEnchants(ListTag es){
        for (int i = 0; i < es.size(); i++){
            CompoundTag compoundtag = es.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundtag.getString("eid"))).ifPresent((enchantment) -> {
                _DataType_StringIntInt stringIntInt = new _DataType_StringIntInt();

                int lvl = Mth.clamp(compoundtag.getInt(SAVE_DAT_ENCHANT_MAX), 0, 255);
                stringIntInt.setEid(compoundtag.getString(SAVE_DAT_ENCHANT_ID));
                stringIntInt.setXp(compoundtag.getInt(SAVE_DAT_ENCHANT_XP));
                stringIntInt.setMaxLvl(lvl);
                enchantData.put(enchantment, stringIntInt);
            });
        }

    }
    public CompoundTag setNewData(String eid,int max,int xp){
        CompoundTag dat = new CompoundTag();
        dat.putString(SAVE_DAT_ENCHANT_ID,eid);
        dat.putInt(SAVE_DAT_ENCHANT_MAX,max);
        dat.putInt(SAVE_DAT_ENCHANT_XP,xp);
        return dat;
    }
    public void addEnchant(String eid,int pLvl){
        Enchantment lEnchant = AM_EnchantHelp.IdToEnchant(eid);
        _DataType_StringIntInt lEnchantData ;
        if (enchantData.containsKey(lEnchant)){
            lEnchantData = enchantData.get(lEnchant);
        }else {
            lEnchantData = new _DataType_StringIntInt();
        }
        lEnchantData.setEid(eid);
        lEnchantData.addXp(lvlToXp(pLvl));
        lEnchantData.setMaxLvl(pLvl);
        enchantData.put(lEnchant,lEnchantData);
    }
    public void addToEnchantData(ItemStack item){
        ListTag es = item.getEnchantmentTags();
        for(int i = 0; i < es.size(); ++i) {
            CompoundTag compoundtag = es.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundtag)).ifPresent((enchantment) -> {
                _DataType_StringIntInt stringIntInt ;
                if (enchantData.containsKey(enchantment)){
                    stringIntInt = enchantData.get(enchantment);
                }else {
                    stringIntInt = new _DataType_StringIntInt();
                }
                int lvl = EnchantmentHelper.getEnchantmentLevel(compoundtag);
                stringIntInt.setEid(compoundtag.getString("id"));
                stringIntInt.addXp(lvlToXp(lvl));
                stringIntInt.setMaxLvl(lvl);
                enchantData.put(enchantment, stringIntInt);
            });
        }
    }
    private int lvlToXp(int lvl){
        return 1 << (lvl - 1);
    }
    public HashMap<Enchantment, _DataType_StringIntInt> getEnchantData() {
        return enchantData;
    }

    public int getLvl(Enchantment pEnchantment){
        return enchantData.get(pEnchantment).getMaxLvl();
    }
    public int getXp(Enchantment pEnchantment){
        return enchantData.get(pEnchantment).getXp();
    }

    public boolean dimXp(Enchantment pEnchantment,int pLvl){
        int lXp = getXp(pEnchantment) - lvlToXp(pLvl);
        if (lXp < 0){
            return false;
        }
        enchantData.get(pEnchantment).setXp(lXp);
        return true;
    }
}
