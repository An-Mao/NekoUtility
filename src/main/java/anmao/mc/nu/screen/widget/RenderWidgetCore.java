package anmao.mc.nu.screen.widget;

import anmao.mc.nu.datatype.DT_XYWH;
import anmao.mc.nu.datatype.DT_XYWHUV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

public abstract class RenderWidgetCore extends AbstractWidget implements Renderable {
    protected ResourceLocation texture;
    protected Font font;
    protected float fontWidth;
    protected final DT_XYWH widget_xywh;
    protected int bgUsualColor, bgSelectColor;
    protected DT_XYWHUV bgSelect = null , bgNormal = null;
    protected int layerZ = 1000;
    public RenderWidgetCore(DT_XYWH dt_xywh, Component pMessage) {
        this(Minecraft.getInstance().font,dt_xywh,pMessage);
    }
    public RenderWidgetCore(Font font, DT_XYWH dt_xywh, Component pMessage) {
        super(dt_xywh.x(), dt_xywh.y(), dt_xywh.width(), dt_xywh.height(), pMessage);
        this.font = font;
        this.fontWidth = this.font.width("a");
        this.widget_xywh = dt_xywh;
        this.bgUsualColor = Color.gray.getRGB();
        this.bgSelectColor = Color.LIGHT_GRAY.getRGB();
    }

    public void setLayerZ(int layerZ) {
        this.layerZ = layerZ;
    }

    public void setBgNormal(DT_XYWHUV bgNormal) {
        this.bgNormal = bgNormal;
    }

    public void setBgSelect(DT_XYWHUV bgSelect) {
        this.bgSelect = bgSelect;
    }

    public void setBgUsualColor(int bgUsualColor) {
        this.bgUsualColor = bgUsualColor;
    }

    public void setBgSelectColor(int bgSelectColor) {
        this.bgSelectColor = bgSelectColor;
    }

    public void setFont(Font font) {
        this.font = font;
        this.fontWidth = this.font.width("a");
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    public void drawImage(GuiGraphics guiGraphics,DT_XYWHUV xywhuv){
        guiGraphics.blit(texture,xywhuv.getX(),xywhuv.getY(),xywhuv.getUOffset(),xywhuv.getVOffset(),xywhuv.getWidth(),xywhuv.getHeight());
    }
    public void drawString(GuiGraphics guiGraphics,Font font,int x,int y,int color,boolean shadow, Component component){
        guiGraphics.drawString(font,component,x,y,color,shadow);
    }
    public void drawString(GuiGraphics guiGraphics,int x,int y,int color,boolean shadow, Component component){
        drawString(guiGraphics,font,x,y,color,shadow,component);
    }
    public void drawString(GuiGraphics guiGraphics,int x,int y,int color,Component component){
        drawString(guiGraphics,font,x,y,color,false,component);
    }
    public void drawString(GuiGraphics guiGraphics,int x,int y,int color,String s){
        drawString(guiGraphics,font,x,y,color,false,Component.literal(s));
    }
    public void drawString(GuiGraphics guiGraphics,int x,int y,Component component){
        drawString(guiGraphics,font,x,y,bgUsualColor,false,component);
    }
    public void drawSquare(GuiGraphics guiGraphics,int x,int y,int width,int height,int color){
        guiGraphics.fill(x,y,x+width,y+height,color);
    }
    public void drawSquare(GuiGraphics guiGraphics,DT_XYWH dt_xywh,int color){
        drawSquare(guiGraphics,dt_xywh.x(),dt_xywh.y(),dt_xywh.width(),dt_xywh.height(),color);
    }
    public void drawSquare(GuiGraphics guiGraphics,int color){
        drawSquare(guiGraphics,widget_xywh.x(),widget_xywh.y(),widget_xywh.width(),widget_xywh.height(),color);
    }

    @Override
    protected abstract void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick);
    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {}
    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress();
    }
}
