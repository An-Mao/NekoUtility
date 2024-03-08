package anmao.mc.nu.screen.widget;

import anmao.mc.nu.datatype.DT_XYWH;
import anmao.mc.nu.datatype.DT_XYWHUV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ImageButton extends RenderWidgetCore {
    private final OnPress onPress;
    public ImageButton(Font font, DT_XYWH dt_xywh, Component pMessage, OnPress onPress) {
        super(dt_xywh, pMessage);
        this.onPress = onPress;
        this.font = font;
    }
    public ImageButton(DT_XYWH dt_xywh, Component pMessage,OnPress onPress) {
        this(Minecraft.getInstance().font,dt_xywh,pMessage,onPress);
    }
    @Override
    public void onClick(double pMouseX, double pMouseY) {
        this.onPress.onPress();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (isMouseOver(pMouseX,pMouseY)){
            if (texture != null){
                DT_XYWHUV dt_xywhuv = bgSelect;
                if (dt_xywhuv != null) {
                    drawImage(pGuiGraphics,dt_xywhuv);
                }
            }else {
                drawSquare(pGuiGraphics,bgSelectColor);
            }
            pGuiGraphics.renderTooltip(font,getMessage(),pMouseX,pMouseY);
        }else {
            if (texture != null) {
                DT_XYWHUV dt_xywhuv = bgNormal;
                if (dt_xywhuv != null) {
                    drawImage(pGuiGraphics,dt_xywhuv);
                }
            }else {
                drawSquare(pGuiGraphics,bgUsualColor);
            }
        }
    }
}
