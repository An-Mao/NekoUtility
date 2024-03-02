package anmao.mc.nu.datagen;

import anmao.mc.nu.NU;
import anmao.mc.nu.item.NUItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class DataGen_ItemModelProvider extends ItemModelProvider {
    public DataGen_ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(NUItems.YANG);
        simpleItem(NUItems.YIN);

        simpleItem(NUItems.QIAN);
        simpleItem(NUItems.KUN);
        simpleItem(NUItems.DUI);
        simpleItem(NUItems.GEN);
        simpleItem(NUItems.KAN);
        simpleItem(NUItems.LI);
        simpleItem(NUItems.XUN);
        simpleItem(NUItems.ZHEN);
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),new ResourceLocation("item/generated")).texture("layer0",new ResourceLocation(NU.MOD_ID,"item/"+item.getId().getPath()));
    }
}
