package anmao.mc.nu.screen.widget;

import anmao.mc.amlib.color._ColorCDT;
import anmao.mc.amlib.debug.DeBug;
import anmao.mc.nu.datatype.DT_ListBoxData;
import anmao.mc.nu.datatype.DT_XYWH;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class DropDownListBox extends DropDownListBoxCore {
    private final DT_XYWH dt_xywh;
    private final List<DT_ListBoxData> dataList;
    private final Component msg;
    private int nowSelectIndex = -1;
    private boolean showList = false;
    private int line,lineHeight,linePosY;
    private int pages,nowPage;
    private int layerZ = 300;
    public DropDownListBox(DT_XYWH dt_xywh, Component pMessage, DT_ListBoxData... data) {
        this(dt_xywh,pMessage,Arrays.asList(data));
    }
    public DropDownListBox(DT_XYWH dt_xywh, Component pMessage,List<DT_ListBoxData> data) {
        super(dt_xywh.x(), dt_xywh.y(), dt_xywh.width(), dt_xywh.height(), pMessage);
        this.texture = null;

        setTextColor(_ColorCDT.black,_ColorCDT.black,_ColorCDT.blue);
        setBgColor(Color.LIGHT_GRAY.getRGB(),_ColorCDT.white, Color.GRAY.getRGB());

        this.dt_xywh = dt_xywh;
        this.dataList = data;
        this.msg = pMessage;
        setLine(7);
    }
    public void setLine(int line){
        this.line = line;
        lineHeight = this.dt_xywh.height() * line;
        int a = this.dt_xywh.height() - font.lineHeight;
        if (a > 0) {
            this.linePosY = a / 2;
        }else {
            this.linePosY = 0;
        }
        this.pages = getPages(this.dataList.size(),line);
        this.nowPage = 1;
    }
    public int getPages(int number , int line){
        int n = number / line;
        if (number % line != 0){
            n ++;
        }
        return n;
    }
    public List<DT_ListBoxData> getDataList() {
        return dataList;
    }
    public Component getSelectComponent(){
        DT_ListBoxData dropDownListBoxData = getSelectData();
        if (dropDownListBoxData != null){
            return dropDownListBoxData.getComponent();
        }
        return Component.literal("---NULL---");
    }
    public Component getComponent(int index){
        DT_ListBoxData dropDownListBoxData = getData(index);
        if (dropDownListBoxData != null){
            return dropDownListBoxData.getComponent();
        }
        return Component.literal("---NULL---");
    }
    public List<Component> getDataTooltip(int index){
        DT_ListBoxData dropDownListBoxData = getData(index);
        if (dropDownListBoxData != null){
            return dropDownListBoxData.getTooltip();
        }
        return List.of(Component.literal("---NULL---")) ;
    }
    public DT_ListBoxData getSelectData(){
        if (nowSelectIndex >= 0 && nowSelectIndex < dataList.size()){
            return dataList.get(nowSelectIndex);
        }
        return null;
    }
    public DT_ListBoxData getData(int index){
        int i = (nowPage - 1) * line + index;
        if (i >= 0 && i < dataList.size()){
            return dataList.get(i);
        }
        return null;
    }
    public Object getSelectValue(){
        if (nowSelectIndex > dataList.size() || nowSelectIndex < 0){
            DeBug.ThrowError("Error Select");
            return null;
        }else {
            return dataList.get(nowSelectIndex).getValue();
        }
    }
    public boolean setSelect(int index){
        if (index > dataList.size() || index < 0){
            return false;
        }
        this.nowSelectIndex = index;
        return true;
    }
    public int getNowSelectIndex(){
        return nowSelectIndex;
    }
    public void setLayerZ(int z){
        layerZ = z;
    }
    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        if (showList) {
            poseStack.translate(0, 0, layerZ+1);
        }else {
            poseStack.translate(0, 0, layerZ);
        }
        if (texture == null) {
            guiGraphics.fill(dt_xywh.x(), dt_xywh.y(), dt_xywh.x()+width, dt_xywh.y() + dt_xywh.height() ,bgSelectColor);
        }else {
            guiGraphics.blit(texture,getX(),getY(),0,0,width,height);
        }
        Component c = msg;
        if (nowSelectIndex > -1 || c == null || c.equals(Component.empty())){
            c = getSelectComponent();
        }
        drawStr(guiGraphics,c,dt_xywh.x(),dt_xywh.y()+linePosY,textSelectColor);
        //guiGraphics.drawString(font,c,dt_xywh.getX(),dt_xywh.getY()+linePosY,textSelectColor,false);
        if (showList){
            for (int i = 0; i < line;i++){
                int lineY = this.getY() + (i+1) * this.dt_xywh.height();
                Component select = getComponent(i);
                int bgc = bgUsualColor;
                int hc = textUsualColor;
                if (pMouseX > getX() && pMouseX < getX() + width && pMouseY > lineY && pMouseY < lineY + dt_xywh.height()){
                    bgc = bgHoverColor;
                    hc = textHoverColor;
                    guiGraphics.renderTooltip(font,getDataTooltip(i), Optional.empty(),pMouseX,pMouseY);
                }
                if (texture == null) {
                    guiGraphics.fill(dt_xywh.x(),  lineY,dt_xywh.x()+width, lineY+ dt_xywh.height(), bgc);
                }
                drawStr(guiGraphics,select,this.getX(),lineY+ linePosY,hc);
            }
        }
        poseStack.popPose();
    }
    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        showList = !showList;
        if (showList){
            setHeight(this.dt_xywh.height() + lineHeight);
        }else {
            setHeight(this.dt_xywh.height());
        }
        updateIndex(pMouseY);
    }
    public void updateIndex(double mouseY){
        int i = (int) ((mouseY - getY()) / this.dt_xywh.height());
        if (i > 0){
            nowSelectIndex = i - 1;
            nowSelectIndex += (nowPage - 1) * line;
            DT_ListBoxData dropDownListBoxData = getSelectData();
            if (dropDownListBoxData != null) {
                dropDownListBoxData.OnPress(getSelectValue());
            }
        }
    }
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (showList) {
            if (isInWidget(pMouseX,pMouseY)) {
                if (pDelta < 0 && nowPage < pages){
                    nowPage ++;
                }else if (pDelta > 0 && nowPage > 1){
                    nowPage --;
                }
                return true;
            }
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);

    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE,Component.translatable("block.nu.equipment_enhancer"));
    }
}