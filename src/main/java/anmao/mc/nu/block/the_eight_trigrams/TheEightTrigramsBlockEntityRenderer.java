package anmao.mc.nu.block.the_eight_trigrams;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class TheEightTrigramsBlockEntityRenderer implements BlockEntityRenderer<TheEightTrigramsBlockEntity> {
    private final double a = 135.0 * Math.PI / 180.0;
    private final double test = 45.0 * Math.PI / 180.0;
    private final double[][] xyzs = {
            {0.5f,1,1.9f,0,1},//1
            {1.5f,1,1.5f,45.0F,1},//2
            {1.9f,1,0.5f,90.0F,1},//3
            {1.5f,1,-0.5f,135.0F,1},//4
            {0.5f,1,-0.9f,180.0F,1},//5
            {-0.5f,1,-0.5f,-135.0F,1},//6
            {-0.9f,1,0.5f, -90.0F,1},//7
            {-0.5f,1,1.5f, -45.0F,1}//8
            //{0.5f,1.5f,0.5f, 0,2}
    };


    private final ItemStack[] itemStacks ={
            new ItemStack(Items.WOODEN_SWORD),
            new ItemStack(Items.WOODEN_SWORD),
            new ItemStack(Items.STONE_SWORD),
            new ItemStack(Items.STONE_SWORD),
            new ItemStack(Items.GOLDEN_SWORD),
            new ItemStack(Items.GOLDEN_SWORD),
            new ItemStack(Items.IRON_SWORD),
            new ItemStack(Items.IRON_SWORD),
            new ItemStack(Items.IRON_SWORD),
            new ItemStack(Items.DIAMOND_SWORD),
            new ItemStack(Items.DIAMOND_SWORD),
            new ItemStack(Items.DIAMOND_SWORD),
            new ItemStack(Items.NETHERITE_SWORD),
            new ItemStack(Items.NETHERITE_SWORD),
            new ItemStack(Items.NETHERITE_SWORD),
            new ItemStack(Items.NETHERITE_SWORD)
    };
    public TheEightTrigramsBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }
    @Override
    public void render(TheEightTrigramsBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.getState()) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack itemStack = new ItemStack(Items.NETHERITE_SWORD);
            int t = pBlockEntity.getT();
            renderSword(pBlockEntity, poseStack, pBuffer, new ItemStack(Items.WOODEN_SWORD));

            poseStack.pushPose();
            poseStack.translate(0.5f, 1.5f, 0.5f);
            poseStack.scale(2.0f, 2.0f, 2.0f);
            poseStack.mulPose(new Quaternionf(new AxisAngle4d(getR(t * 22.5), 0, 1, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4d(a, 0, 0, 1)));
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, pBuffer, pBlockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }
    private void renderSword(TheEightTrigramsBlockEntity pBlockEntity,PoseStack poseStack,MultiBufferSource pBuffer,ItemStack itemStack){
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (double[] xyz : xyzs) {
            poseStack.pushPose();
            poseStack.translate(xyz[0], xyz[1] - 0.7, xyz[2]);
            poseStack.scale((float) xyz[4], (float) xyz[4], (float) xyz[4]);
            poseStack.mulPose(new Quaternionf(new AxisAngle4d(getR(xyz[3]), 0, 1, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4d(a, 0, 0, 1)));
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, pBuffer, pBlockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }
    private void renderer(TheEightTrigramsBlockEntity blockEntity , float pPartialTick, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay){
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = new ItemStack(Items.NETHERITE_SWORD);
        int t = blockEntity.getT();
        for (int i = 0; i < t; i++){
            if (i < xyzs.length) {
                double[] xyz = xyzs[i];
                poseStack.pushPose();
                poseStack.translate(xyz[0], xyz[1]-getH(t,i), xyz[2]);
                poseStack.scale((float) xyz[4], (float) xyz[4], (float) xyz[4]);


                //Quaternionf camera = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
                //poseStack.mulPose(Axis.YP.rotation(xyz[3]));
                poseStack.mulPose(new Quaternionf(new AxisAngle4d(getR(xyz[3]), 0, 1, 0)));

                poseStack.mulPose(new Quaternionf(new AxisAngle4d(a, 0, 0, 1)));


                itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, pBuffer, blockEntity.getLevel(), 1);
                poseStack.popPose();
            }
        }

    }
    int getLightLevel(Level level, BlockPos blockPos){
        int bLight = level.getBrightness(LightLayer.BLOCK,blockPos);
        int sLight = level.getBrightness(LightLayer.SKY,blockPos);
        return LightTexture.pack(bLight,sLight);
    }
    private float getH(float t,int i){
        float h = t - i;
        h = h * 0.1f;
        return Math.min(0.8f,h);
    }
    private double getR(double f){
        return f * Math.PI / 180.0;
    }
}
