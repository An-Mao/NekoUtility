package anmao.mc.nu.datagen;

import anmao.mc.nu.block.Blocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class DataGen_Recipe extends RecipeProvider implements IConditionBuilder {
    public DataGen_Recipe(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.INDEX.get())
                .pattern("ABA")
                .pattern("ACA")
                .pattern("DDD")
                .define('A', Items.REDSTONE_BLOCK)
                .define('B', Items.BOOKSHELF)
                .define('C', Items.NETHER_STAR)
                .define('D', Items.OBSIDIAN)
                .unlockedBy(getHasName(Items.NETHER_STAR),has(Items.NETHER_STAR))
                .save(consumer);
    }
}
