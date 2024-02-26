package anmao.mc.nu.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class IndexBlockEntityRender implements BlockEntityRenderer<IndexBlockEntity> {
    public IndexBlockEntityRender(BlockEntityRendererProvider.Context context){
    }
    @Override
    public void render(@NotNull IndexBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.40625,0.6875,0.40625);
        Matrix4f matrix4f = pPoseStack.last().pose();
        this.renderCube(pBlockEntity, matrix4f, pBuffer.getBuffer(this.renderType()));
        pPoseStack.popPose();
    }
    private void renderCube(IndexBlockEntity pBlockEntity, Matrix4f pPose, VertexConsumer pConsumer) {
        float size = 0.1875F;
        float f = this.getOffsetDown();
        float f1 = this.getOffsetUp();
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, size, 0.0F, size, size, size, size, size, Direction.SOUTH);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, size, size, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(pBlockEntity, pPose, pConsumer, size, size, size, 0.0F, 0.0F, size, size, 0.0F, Direction.EAST);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 0.0F, 0.0F, size, 0.0F, size, size, 0.0F, Direction.WEST);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, size, f, f, 0.0F, 0.0F, size, size, Direction.DOWN);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, size, f1, f1, size, size, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(IndexBlockEntity pBlockEntity, Matrix4f pPose, VertexConsumer pConsumer, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        if (pBlockEntity.shouldRenderFace(pDirection)) {
            pConsumer.vertex(pPose, pX0, pY0, pZ0).endVertex();
            pConsumer.vertex(pPose, pX1, pY0, pZ1).endVertex();
            pConsumer.vertex(pPose, pX1, pY1, pZ2).endVertex();
            pConsumer.vertex(pPose, pX0, pY1, pZ3).endVertex();
        }

    }

    protected float getOffsetUp() {
        return 0.1875F;
    }

    protected float getOffsetDown() {
        return 0.0F;
    }

    protected RenderType renderType() {
        return RenderType.endPortal();
    }
}
