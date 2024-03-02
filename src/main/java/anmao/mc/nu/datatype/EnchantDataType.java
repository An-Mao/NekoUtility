package anmao.mc.nu.datatype;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.math.BigInteger;
import java.util.HashMap;

public class EnchantDataType {
    private final String SAVE_DAT_ENCHANT_ID = "eid";
    private final String SAVE_DAT_ENCHANT_MAX = "max";
    private final String SAVE_DAT_ENCHANT_XP = "xp";
    private HashMap<Enchantment, EnchantDataTypeCore> enchantData;
    public EnchantDataType(){
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
                EnchantDataTypeCore stringIntInt = new EnchantDataTypeCore();

                int lvl = Mth.clamp(compoundtag.getInt(SAVE_DAT_ENCHANT_MAX), 0, 255);
                stringIntInt.setEid(compoundtag.getString(SAVE_DAT_ENCHANT_ID));
                stringIntInt.setXp(compoundtag.getString(SAVE_DAT_ENCHANT_XP));
                stringIntInt.setMaxLvl(lvl);
                enchantData.put(enchantment, stringIntInt);
            });
        }

    }
    public CompoundTag setNewData(String eid, int max, BigInteger xp){
        CompoundTag dat = new CompoundTag();
        dat.putString(SAVE_DAT_ENCHANT_ID,eid);
        dat.putInt(SAVE_DAT_ENCHANT_MAX,max);
        dat.putString(SAVE_DAT_ENCHANT_XP,xp.toString());
        return dat;
    }
    public void addEnchant(String eid,int pLvl){
        Enchantment lEnchant = anmao.mc.amlib.enchantment.EnchantmentHelper.IdToEnchant(eid);
        EnchantDataTypeCore lEnchantData ;
        if (enchantData.containsKey(lEnchant)){
            lEnchantData = enchantData.get(lEnchant);
        }else {
            lEnchantData = new EnchantDataTypeCore();
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
                EnchantDataTypeCore stringIntInt ;
                if (enchantData.containsKey(enchantment)){
                    stringIntInt = enchantData.get(enchantment);
                }else {
                    stringIntInt = new EnchantDataTypeCore();
                }
                int lvl = EnchantmentHelper.getEnchantmentLevel(compoundtag);
                stringIntInt.setEid(compoundtag.getString("id"));
                stringIntInt.addXp(lvlToXp(lvl));
                stringIntInt.setMaxLvl(lvl);
                enchantData.put(enchantment, stringIntInt);
            });
        }
    }
    private BigInteger lvlToXp(int lvl){
        //return 1 << (lvl - 1);
        BigInteger base = new BigInteger("2");
        return base.pow(lvl - 1);
    }
    public HashMap<Enchantment, EnchantDataTypeCore> getEnchantData() {
        return enchantData;
    }

    public int getLvl(Enchantment pEnchantment){
        return enchantData.get(pEnchantment).getMaxLvl();
    }
    public BigInteger getXp(Enchantment pEnchantment){
        return enchantData.get(pEnchantment).getXp();
    }

    public boolean dimXp(Enchantment pEnchantment,int pLvl){
        BigInteger lXp = getXp(pEnchantment).subtract(lvlToXp(pLvl));
        if (lXp.compareTo(BigInteger.ZERO) < 0){
            return false;
        }
        enchantData.get(pEnchantment).setXp(lXp);
        return true;
    }
}
