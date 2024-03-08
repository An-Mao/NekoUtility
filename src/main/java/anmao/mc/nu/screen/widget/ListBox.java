package anmao.mc.nu.screen.widget;

import anmao.mc.amlib.color._ColorCDT;
import anmao.mc.amlib.debug.DeBug;
import anmao.mc.nu.datatype.DT_ListBoxData;
import anmao.mc.nu.datatype.DT_XYWH;
import anmao.mc.nu.datatype.DT_XYWHUV;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class ListBox extends RenderWidgetCore{
    private final List<DT_ListBoxData> data;
    private final int dataSize;
    private int line ,row,index,startIndex;
    private int elementalWidth, elementalHeight;
    private int hSpace, vSpace;
    private DT_XYWHUV bg;
    private int bgColor,textSelectColor,textColor;
    public ListBox(DT_XYWH dt_xywh,int elementalWidth,int elementalHeight, Component pMessage,DT_ListBoxData... data) {
        this(dt_xywh,elementalWidth, elementalHeight,pMessage, Arrays.asList(data));
    }
    public ListBox(DT_XYWH dt_xywh,int elementalWidth,int elementalHeight, Component pMessage,List<DT_ListBoxData> data) {
        super(dt_xywh,pMessage);
        this.data = data;
        this.dataSize = this.data.size();
        this.elementalWidth = elementalWidth;
        this.elementalHeight = elementalHeight;
        this.index = -1;
        this.startIndex = 0;
        this.bgColor = _ColorCDT.black;
        this.textColor = _ColorCDT.white;
        this.textSelectColor = _ColorCDT.yellow;
        setVSpace(2);
        setHSpace(2);
    }

    public void setBg(DT_XYWHUV bg) {
        this.bg = bg;
    }

    public void setElementalHeight(int elementalHeight) {
        this.elementalHeight = elementalHeight;
    }

    public void setElementalWidth(int elementalWidth) {
        this.elementalWidth = elementalWidth;
    }
    public void setVSpace(int vSpace) {
        this.vSpace = vSpace;
        this.line = widget_xywh.height() / (elementalHeight + vSpace);
    }

    public void setHSpace(int hSpace) {
        this.hSpace = hSpace;
        this.row = widget_xywh.width() / (elementalWidth + hSpace);
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (texture != null){
            drawImage(pGuiGraphics,bg);
            for (int i = 0;i < line;i++){
                int dy = getY() + i * (elementalHeight + vSpace);
                for (int r = 0; r < row; r++){
                    if (startIndex < dataSize){
                        int ni =startIndex + i * row + r;
                        if (ni < dataSize) {
                            int dx = getX() + r * (elementalWidth + hSpace);
                            DT_XYWHUV bg = new DT_XYWHUV(dx,dy,elementalWidth,elementalHeight,bgNormal.getUOffset(),bgNormal.getVOffset());
                            int tc = textColor;
                            if (pMouseX > dx
                                    && pMouseX < dx + elementalWidth
                                    && pMouseY > dy
                                    && pMouseY < dy+ elementalHeight) {
                                bg = new DT_XYWHUV(dx,dy,elementalWidth,elementalHeight,bgSelect.getUOffset(),bgSelect.getVOffset());
                                tc = textSelectColor;
                                index = ni;
                                pGuiGraphics.renderTooltip(font,getData(index).getTooltip(),pMouseX,pMouseY);
                            }
                            drawImage(pGuiGraphics,bg);
                            drawString(pGuiGraphics,dx,dy,tc,FixStrWidth(getDataComponent(ni)));
                        }
                    }else {
                        break;
                    }
                }
            }
        }else {
            drawSquare(pGuiGraphics,widget_xywh,bgColor);
            for (int i = 0;i < line;i++){
                int dy = getY() + i * (elementalHeight + vSpace);
                for (int r = 0; r < row; r++){
                    if (startIndex < dataSize){
                        int ni =startIndex + i * row + r;
                        if (ni < dataSize) {
                            int dx = getX() + r * (elementalWidth + hSpace);
                            int bgc = bgUsualColor;
                            int tc = textColor;
                            if (pMouseX > dx
                                    && pMouseX < dx + elementalWidth
                                    && pMouseY > dy
                                    && pMouseY < dy+ elementalHeight) {
                                bgc = bgSelectColor;
                                tc = textSelectColor;
                                index = ni;
                                pGuiGraphics.renderTooltip(font,getData(index).getTooltip(),pMouseX,pMouseY);
                            }
                            drawSquare(pGuiGraphics, dx, dy, elementalWidth, elementalHeight, bgc);
                            drawString(pGuiGraphics, dx, dy, tc, FixStrWidth(getDataComponent(ni)));
                        }
                    }else {
                        break;
                    }
                }
            }
        }
    }
    public void renderImageElemental(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY){

    }
    public DT_ListBoxData getData(int index){
        if (index < this.data.size()){
            return this.data.get(index);
        }
        DeBug.ThrowError("error index");
        return null;
    }
    public Component getDataComponent(int index){
        DT_ListBoxData d = getData(index);
        if (d != null){
            return d.getComponent();
        }
        DeBug.ThrowError("error data");
        return Component.literal("Error :: Null");
    }
    public boolean isSelectElemental(int pMouseX, int pMouseY){
        if (index < 0){
            return false;
        }
        return pMouseX > getX() + (index - 1) * (elementalWidth + hSpace)
                && pMouseX < getX() + (elementalWidth + hSpace) * index
                && pMouseY > getY() + (elementalHeight + vSpace) * (index - 1)
                && pMouseY < getY() + (elementalHeight + vSpace) * index;
    }
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pDelta < 0 && startIndex < dataSize - row){
            startIndex = startIndex + row;
        }else if (startIndex >= row){
            startIndex = startIndex - row;
        }
        index = -1;
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if (isMouseOver(pMouseX,pMouseY)){
            DT_ListBoxData d = getData(index);
            if (d != null){
                d.OnPress(d.getValue());
            }
        }
    }

    public String FixStrWidth(String s){
        return font.plainSubstrByWidth(s,elementalWidth);
    }
    public String FixStrWidth(Component s){
        return FixStrWidth(s.getString());
    }
}
