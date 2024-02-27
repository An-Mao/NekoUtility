package anmao.mc.nu.screen;

import anmao.mc.amlib.color._ColorCDT;
import anmao.mc.amlib.enchantment.EnchantmentHelper;
import anmao.mc.nu.NU;
import anmao.mc.nu.amlib.AM_EnchantHelp;
import anmao.mc.nu.datatype._DataType_StringIntInt;
import anmao.mc.nu.network.index.NetCore;
import anmao.mc.nu.network.index.packet.IndexPacketCTS;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexScreen extends AbstractContainerScreen<IndexMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(NU.MOD_ID,"textures/gui/index_gui.png");
    private final int textureWidth = 367,textureHeight = 166;
    private final ImageButton[] enchantButton = new ImageButton[7];
    private ImageButton enchantItem;
    private int x,y;
    private final Font font = Minecraft.getInstance().font;
    private HashMap<Enchantment, _DataType_StringIntInt> enchantData;
    private final ArrayList<Enchantment> enchants = new ArrayList<>();
    private final ArrayList<_DataType_StringIntInt> enchantInfo = new ArrayList<>();
    private int ROW = 0;
    private final int[] buttonIndex = new int[7];

    private final HashMap<Enchantment,Integer> selectEnchants = new HashMap<>();
    private int needMp = 0;
    public IndexScreen(IndexMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        enchantData = menu.getEnchantData();
        breakEnchantData();
    }
    private void breakEnchantData(){
        enchants.clear();
        enchantInfo.clear();
        enchantData.forEach(((enchantment, dataTypeStringIntInt) -> {
            enchants.add(enchantment);
            enchantInfo.add(dataTypeStringIntInt);
        }));
    }
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        imageHeight = 166;
        imageWidth = 276;
        x = (width - imageWidth)/2;
        y = (height - imageHeight)/2;
        addMustButton();
    }
    private ImageButton creatImageButton(int x, int y, int width, int height, int startX, int startY, int yDiffTex, ResourceLocation res, Button.OnPress onPress,Component component){
        return new ImageButton(x,y,width,height,startX,startY,yDiffTex,res,textureWidth,textureHeight,onPress,component);
    }
    private void addMustButton(){
        addEnchantButton();
        addPageButton();
        addEnchantItemButton();
    }
    private void addPageButton(){
        this.addRenderableWidget(creatImageButton(x + 106, y + 42, 12, 19, 279, 22, 0, TEXTURE, (pButton -> subROW()), Component.empty()));
        this.addRenderableWidget(creatImageButton(x + 136, y + 42, 12, 19, 279, 3, 0, TEXTURE, (pButton -> addROW()), Component.empty()));
    }
    private void addEnchantItemButton(){
        enchantItem = creatImageButton(x + 180, y + 50, 16, 16, 279, 112, 0, TEXTURE, (pButton -> sendSelectEnchant()), Component.empty());
        this.addRenderableWidget(enchantItem);
    }
    private void sendSelectEnchant(){
        if (selectEnchants.isEmpty()){
            return;
        }
        CompoundTag lDat = AM_EnchantHelp.EnchantsToCompoundTag(selectEnchants);

        lDat.putInt("be.x",menu.getX());
        lDat.putInt("be.y",menu.getY());
        lDat.putInt("be.z",menu.getZ());
        NetCore.sendToServer(new IndexPacketCTS(lDat));
    }
    private void addROW(){
        if (ROW < enchants.size() / 7){
            ROW++;
        }
    }
    private void subROW(){
        if (ROW > 0 ){
            ROW--;
        }
    }
    private void addEnchantButton(){
        int ax = x + 7, ay = y + 19;
        for (int i = 0; i < enchantButton.length ;i++){
            int finalI = i;
            enchantButton[i] = creatImageButton(ax,ay, 88, 18, 279, 45, 0,TEXTURE,(pButton -> getSelectEnchant(finalI)),Component.empty());
            this.addRenderableWidget(enchantButton[i] );
            ay += 20;
        }
    }
    private void getSelectEnchant(int button){
        int i = buttonIndex[button];
        if (i < enchants.size()) {
            Enchantment enchant = enchants.get(i);
            int lvl = selectEnchants.getOrDefault(enchant, 0);
            if (lvl < enchantInfo.get(i).getMaxLvl()) {
                selectEnchants.put(enchant, lvl + 1);
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
        renderSeletEnchant(guiGraphics);
    }
    private void renderSeletEnchant(GuiGraphics guiGraphics){
        guiGraphics.renderItem(new ItemStack(Items.ENCHANTED_BOOK),x + 180, y + 50);
    }
    private void renderProgress(GuiGraphics guiGraphics){
        if (menu.isCrafting()){
            guiGraphics.blit(TEXTURE,x+185,y+38,menu.getScaleProgress(),17,279,66,menu.getScaleProgress(),17,textureWidth,textureHeight);
        }
    }
    private void renderMP(GuiGraphics guiGraphics){
        guiGraphics.drawString(Minecraft.getInstance().font,"MP:"+menu.getMP(),x+106,y+18, _ColorCDT.black,false);

    }
    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);
        renderEnchantsButtonText(pGuiGraphics,pMouseX,pMouseY);
        pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.save.all").append(String.valueOf(enchants.size())),x+25,y+5,0x336699);
        pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.page"),x+106,y+33,_ColorCDT.black,false);
        pGuiGraphics.drawString(font,Component.literal(String.valueOf(ROW)),x+119,y+48,_ColorCDT.black,false);
        renderSelectEnchantTooltip(pGuiGraphics,pMouseX,pMouseY);
    }
    private void renderEnchantsButtonText(GuiGraphics guiGraphics, int pMouseX, int pMouseY){
        int m = ROW * 7;
        ItemStack inputItem = menu.getInputItem();
        for (int i = 0;i<enchantButton.length;i++){
            ImageButton button = enchantButton[i];
            if (m < enchants.size()){
                boolean show = true;
                if (inputItem  != ItemStack.EMPTY && inputItem.getItem() != Items.BOOK){
                    while (!(enchants.get(m).canEnchant(inputItem))){
                        m++;
                        if (m >= enchants.size()){
                            show = false;
                            break;
                        }
                    }
                }
                if (show && m < enchants.size()) {
                    buttonIndex[i] = m;
                    int color = 0x9900ff;
                    if (enchants.get(m).isCurse()) {
                        color = 0xff0000;
                    }
                    if (button.isHovered()) {

                        List<Component> tip = List.of(
                                Component.translatable(enchants.get(m).getDescriptionId()),
                                Component.translatable("tooltip.nu.index.max_lvl").append(String.valueOf(enchantInfo.get(m).getMaxLvl())),
                                Component.translatable("tooltip.nu.index.xp").append(String.valueOf(enchantInfo.get(m).getXp())),
                                EnchantmentHelper.getEnchantmentDescString(enchants.get(m))
                        );

                        guiGraphics.renderTooltip(font, tip, java.util.Optional.empty(), pMouseX, pMouseY);
                        color = 0x009900;
                    }
                    guiGraphics.drawString(font, Component.translatable(enchants.get(m).getDescriptionId()), button.getX() + 2, button.getY() + 2, color);
                }
            }
            m ++;
        }
    }
    private void renderSelectEnchantTooltip(GuiGraphics guiGraphics, int pMouseX, int pMouseY){
        if (enchantItem.isHovered()) {
            List<Component> tip = new ArrayList<>();
            tip.add(Component.translatable("tooltip.nu.index.select").append(String.valueOf(selectEnchants.size())));
            selectEnchants.forEach((enchantment, integer) -> {
                tip.add(
                        enchantment.getFullname(integer)
                );
            });
            ChatFormatting color = ChatFormatting.GOLD;
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

            BuiltInRegistries.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundtag.getString("eid"))).ifPresent((enchantment) -> {
                _DataType_StringIntInt stringIntInt = new _DataType_StringIntInt();

                int lvl = Mth.clamp(compoundtag.getInt("max"), 0, 255);
                stringIntInt.setEid(compoundtag.getString("eid"));
                stringIntInt.setXp(compoundtag.getInt("xp"));
                stringIntInt.setMaxLvl(lvl);
                enchantData.put(enchantment, stringIntInt);
            });
        }
        breakEnchantData();
    }
}
