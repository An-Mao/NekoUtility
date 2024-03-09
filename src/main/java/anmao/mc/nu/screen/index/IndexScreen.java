package anmao.mc.nu.screen.index;

import anmao.mc.amlib.amlib.network.Net;
import anmao.mc.amlib.amlib.network.easy_net.EasyNetCTS;
import anmao.mc.amlib.color._ColorCDT;
import anmao.mc.amlib.enchantment.EnchantmentHelper;
import anmao.mc.nu.NU;
import anmao.mc.nu.datatype.DT_ListBoxData;
import anmao.mc.nu.datatype.DT_XYWH;
import anmao.mc.nu.datatype.EnchantDataTypeCore;
import anmao.mc.nu.network.NetReg;
import anmao.mc.nu.screen.widget.ListBox;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexScreen extends AbstractContainerScreen<IndexMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(NU.MOD_ID,"textures/gui/index.png");
    private final int textureWidth = 320,textureHeight = 256;
    private ImageButton enchantItem;
    private int x,y;
    private final Font font = Minecraft.getInstance().font;
    private HashMap<Enchantment, EnchantDataTypeCore> enchantData;
    private final HashMap<Enchantment,Integer> selectEnchants = new HashMap<>();
    private int needMp = 0;
    private ListBox enchantments;
    public IndexScreen(IndexMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        enchantData = menu.getEnchantData();
    }
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        imageHeight = 222;
        imageWidth = 311;
        leftPos = x = (width - imageWidth)/2;
        topPos = y = (height - imageHeight)/2;
        addEnchantItemButton();
        getEnchantments();
        addRenderableWidget(this.enchantments);
    }
    private void getEnchantments() {
        this.enchantments = new ListBox(new DT_XYWH(x+7,y+17,297,108),50,12,Component.literal("enchants"),
                getAllEnchantment()
        );
        this.enchantments.setBgColor(0x00000000);
        this.enchantments.setLeft(7);
        this.enchantments.setTop(7);
        this.enchantments.setBgUsualColor(0x55ffffff);
        this.enchantments.setBgSelectColor(0x55000000);
    }
    private List<DT_ListBoxData> getAllEnchantment() {
        List<DT_ListBoxData> data = new ArrayList<>();
        if (!enchantData.isEmpty()){
            enchantData.forEach((enchantment, enchantDataTypeCore) -> data.add(new DT_ListBoxData(
                    Component.translatable(enchantment.getDescriptionId()).withStyle(getEnchantColor(enchantment)),
                    enchantment,
                    List.of(
                            Component.translatable(enchantment.getDescriptionId()).withStyle(getEnchantColor(enchantment)),
                            EnchantmentHelper.getEnchantmentDescString(enchantment),
                            Component.translatable("tooltip.nu.index.max_lvl").append(String.valueOf(enchantDataTypeCore.getMaxLvl())),
                            Component.translatable("tooltip.nu.index.xp").append(enchantDataTypeCore.getXp().toString())
                    ),
                    this::addEnchantmentToSelect)));
        }
        return data;
    }
    public ChatFormatting getEnchantColor(Enchantment enchantment){
        if (enchantment.isCurse()) {
            return ChatFormatting.RED;
        } else {
            return ChatFormatting.GRAY;
        }
    }
    private ImageButton creatImageButton(int x, int y, int width, int height, int startX, int startY, int yDiffTex, ResourceLocation res, Button.OnPress onPress,Component component){
        return new ImageButton(x,y,width,height,startX,startY,yDiffTex,res,textureWidth,textureHeight,onPress,component);
    }
    private void addEnchantItemButton(){
        enchantItem = creatImageButton(x + 112, y + 165, 16, 16, 9, 222, 0, TEXTURE, (pButton -> sendSelectEnchant()), Component.empty());
        this.addRenderableWidget(enchantItem);
    }
    private void sendSelectEnchant(){
        if (selectEnchants.isEmpty()){
            return;
        }
        CompoundTag lDat = EnchantmentHelper.EnchantsToCompoundTag(selectEnchants,"am.enchants");
        lDat.putString(Net.EASY_NET_KEY, NetReg.INDEX_BLOCK.getId().toString());
        lDat.putInt("be.x",menu.getX());
        lDat.putInt("be.y",menu.getY());
        lDat.putInt("be.z",menu.getZ());
        Net.EasyNetCTS(new EasyNetCTS(lDat));
    }
    public void addEnchantmentToSelect(Object v){
        if (v instanceof Enchantment enchantment) {
            int lvl = selectEnchants.getOrDefault(enchantment, 0);
            if (lvl < enchantData.get(enchantment).getMaxLvl()) {
                selectEnchants.put(enchantment, lvl + 1);
                needMp += 5;
            }
        }
    }
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.setShaderTexture(0,TEXTURE);
        guiGraphics.blit(TEXTURE,x,y,imageWidth,imageHeight,0,0,imageWidth,imageHeight,textureWidth,textureHeight);
        renderProgress(guiGraphics);
        renderMP(guiGraphics);
    }
    private void renderProgress(GuiGraphics guiGraphics){
        if (menu.isCrafting()){
            guiGraphics.blit(TEXTURE,x+86,y+159,9,menu.getScaleProgress(),0,222,9,menu.getScaleProgress(),textureWidth,textureHeight);
        }
    }
    private void renderMP(GuiGraphics guiGraphics){
        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("tooltip.nu.index.mp").append(String.valueOf(menu.getMP())),x+9,y+157, _ColorCDT.cyan,false);

    }
    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);


        pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.save.all").append(String.valueOf(enchantData.size())),x+9,y+141,_ColorCDT.green,false);
        renderSelectEnchantTooltip(pGuiGraphics,pMouseX,pMouseY);
    }
    private void renderSelectEnchantTooltip(GuiGraphics guiGraphics, int pMouseX, int pMouseY){
        if (enchantItem.isHovered()) {
            List<Component> tip = new ArrayList<>();
            tip.add(Component.translatable("tooltip.nu.index.select").append(String.valueOf(selectEnchants.size())));
            selectEnchants.forEach((enchantment, integer) -> tip.add(
                    enchantment.getFullname(integer)
            ));
            ChatFormatting color = ChatFormatting.GREEN;
            if (needMp > menu.getMP()){
                color = ChatFormatting.RED;
            }
            tip.add(Component.translatable("tooltip.nu.index.need_mp").append(String.valueOf(needMp)).withStyle(color));
            guiGraphics.renderTooltip(font, tip, java.util.Optional.empty(), pMouseX, pMouseY);
        }
    }
    public void handlePacket(CompoundTag msg) {
        this.enchantData = new HashMap<>();
        ListTag es = msg.getList("index.enchants", Tag.TAG_COMPOUND);
        for (int i = 0; i < es.size(); i++){
            CompoundTag compoundtag = es.getCompound(i);
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(compoundtag.getString("eid")));
            if (enchantment != null){
                EnchantDataTypeCore stringIntInt = new EnchantDataTypeCore();
                int lvl = Mth.clamp(compoundtag.getInt("max"), 0, 255);
                stringIntInt.setEid(compoundtag.getString("eid"));
                stringIntInt.setXp(compoundtag.getString("xp"));
                stringIntInt.setMaxLvl(lvl);
                enchantData.put(enchantment, stringIntInt);
            }
        }
        this.enchantments.setData(getAllEnchantment());
    }
}
