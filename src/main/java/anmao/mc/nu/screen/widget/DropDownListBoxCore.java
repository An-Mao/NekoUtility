package anmao.mc.nu.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class DropDownListBoxCore  extends AbstractWidget implements Renderable {

    protected ResourceLocation texture;
    protected Font font = Minecraft.getInstance().font;
    protected int textUsualColor, textSelectColor, textHoverColor;
    protected int bgUsualColor, bgSelectColor, bgHoverColor;
    protected boolean textShadow = false;
    public DropDownListBoxCore(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
    }
    public void setFont(Font font) {
        this.font = font;
    }
    public void setTexture(ResourceLocation texture){
        this.texture = texture;
    }
    public void setTextColor(int textColor,int textSelectColor,int textHoverColor) {
        setTextUsualColor(textColor);
        setTextSelectColor(textSelectColor);
        setTextHoverColor(textHoverColor);
    }
    public void setTextUsualColor(int textUsualColor) {
        this.textUsualColor = textUsualColor;
    }
    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
    }
    public void setTextHoverColor(int textHoverColor) {
        this.textHoverColor = textHoverColor;
    }
    public void setBgColor(int bgUsualColor,int bgSelectColor,int bgHoverColor){
        setBgUsualColor(bgUsualColor);
        setBgSelectColor(bgSelectColor);
        setBgHoverColor(bgHoverColor);
    }
    public void setBgUsualColor(int bgUsualColor) {
        this.bgUsualColor = bgUsualColor;
    }
    public void setBgSelectColor(int bgSelectColor) {
        this.bgSelectColor = bgSelectColor;
    }
    public void setBgHoverColor(int bgHoverColor) {
        this.bgHoverColor = bgHoverColor;
    }
    public boolean isInWidget(double pMouseX,double pMouseY){
        return pMouseX > getX() && pMouseX < getX() + width && pMouseY > getY() && pMouseY < getY() + height;
    }
    public void setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
    }

    public void drawStr(GuiGraphics guiGraphics,Component component,int x,int y,int color){
        guiGraphics.drawString(font,component,x,y,color,textShadow);
    }
    @Override
    protected abstract void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick);

    @Override
    protected abstract void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput);
}
