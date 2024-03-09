package anmao.mc.nu.screen.equipment_enhancer;

import anmao.mc.amlib.amlib.network.Net;
import anmao.mc.amlib.amlib.network.easy_net.EasyNetCTS;
import anmao.mc.amlib.amlib.network.easy_net.EasyNetSTC;
import anmao.mc.nu.NU;
import anmao.mc.nu.datatype.DT_ListBoxData;
import anmao.mc.nu.datatype.DT_XYWH;
import anmao.mc.nu.datatype.DT_XYWHUV;
import anmao.mc.nu.network.NetReg;
import anmao.mc.nu.screen.widget.DropDownListBox;
import anmao.mc.nu.screen.widget.ImageButton;
import anmao.mc.nu.screen.widget.ListBox;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EquipmentEnhancerScreen extends AbstractContainerScreen<EquipmentEnhancerMenu> {
    private static final String[] EFFECTIVE_SLOTS = {"mainhand","offhand","feet","legs","chest","head"};
    private static final ResourceLocation TEXTURE = new ResourceLocation(NU.MOD_ID,"textures/gui/equipment_enhancer.png");
    private int startX,startY;
    private DropDownListBox AttritubeListBox;
    private DropDownListBox EnhancerSlotListBox;
    private ResourceLocation att;
    private String effectiveSlot;
    private ImageButton send;
    public EquipmentEnhancerScreen(EquipmentEnhancerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void resize(@NotNull Minecraft pMinecraft, int pWidth, int pHeight) {
        super.resize(pMinecraft, pWidth, pHeight);
        init();
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 256;
        this.imageHeight = 256;
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        this.leftPos = this.startX = (width - 176) / 2;
        this.topPos = this.startY = (height - 166) / 2;
        this.AttritubeListBox = new DropDownListBox(
                new DT_XYWH(startX + 52,startY+40,72,12),
                Component.translatable("screen.nu.equipment_enhancer.drop_down_list.tip"),
                getAllAtt()
                );
        AttritubeListBox.setLine(5);
        this.addRenderableWidget(this.AttritubeListBox);

        this.EnhancerSlotListBox = new DropDownListBox(
                new DT_XYWH(startX + 52,startY+53,72,12),
                Component.translatable("screen.nu.equipment_enhancer.enhancer_slot_list.tip"),
                getAllSlot()
        );
        EnhancerSlotListBox.setLine(7);
        this.addRenderableWidget(this.EnhancerSlotListBox);
        DT_XYWH sendPos = new DT_XYWH(startX + 132,startY+52,18,18);
        this.send = new ImageButton(sendPos,Component.literal("send"),this::sendPack);
        this.send.setTexture(TEXTURE);
        this.send.setBgSelect(new DT_XYWHUV(sendPos,2,188));
        this.send.setBgNormal(new DT_XYWHUV(sendPos,2,188));
        this.addRenderableWidget(this.send);
    }
    public void sendPack(){
        if (this.att != null && this.effectiveSlot != null){
            CompoundTag dat = new CompoundTag();
            dat.putString(Net.EASY_NET_KEY, NetReg.EQUIPMENT_ENHANCER.getId().toString());
            dat.putString("attribute.name",this.att.getNamespace());
            dat.putString("attribute.path",this.att.getPath());
            dat.putString("effective_slot",this.effectiveSlot);
            dat.putInt("block_entity.x",menu.getX());
            dat.putInt("block_entity.y",menu.getY());
            dat.putInt("block_entity.z",menu.getZ());
            Net.EasyNetCTS(new EasyNetCTS(dat));
        }
    }
    public void ddlbClick(Object v){
        if (v instanceof Attribute attribute){
            this.att = ForgeRegistries.ATTRIBUTES.getKey(attribute);
        }
    }
    @Override
    public void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.setShaderTexture(0,TEXTURE);
        guiGraphics.blit(TEXTURE,startX,startY,imageWidth,imageHeight,176,166,imageWidth,imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);
    }

    public List<DT_ListBoxData> getAllAtt(){
        List<DT_ListBoxData> data = new ArrayList<>();
        ForgeRegistries.ATTRIBUTES.getValues().forEach(attribute -> data.add(
                new DT_ListBoxData(Component.translatable(attribute.getDescriptionId()),attribute,this::ddlbClick)
        ));
        return data;
    }
    public List<DT_ListBoxData> getAllSlot(){
        List<DT_ListBoxData> data = new ArrayList<>();
        for (String es : EFFECTIVE_SLOTS){
            data.add(
            new DT_ListBoxData(Component.translatable("screen.nu.equipment_enhancer.enhancer_slot_list."+es),es,this::esClick)
            );
        }
        return data;
    }
    private void esClick(Object v){
        if (v instanceof String s){
            this.effectiveSlot = s;
        }
    }
}
