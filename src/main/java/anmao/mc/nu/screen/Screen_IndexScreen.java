package anmao.mc.nu.screen;

import anmao.mc.nu.NU;
import anmao.mc.nu.amlib.AM_EnchantHelp;
import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import anmao.mc.nu.amlib.datatype._DataType_StringIntInt;
import anmao.mc.nu.network.index.Net_Index_Core;
import anmao.mc.nu.network.index.packet.Packet_Index_ClientToServer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Screen_IndexScreen extends AbstractContainerScreen<Screen_IndexMenu> {
    private ImageButton[] enchantButton = new ImageButton[7];
    private int x,y;
    private final Font font = Minecraft.getInstance().font;
    private static final ResourceLocation TEXTURE = new ResourceLocation(NU.MOD_ID,"textures/gui/index.png");
    private HashMap<Enchantment, _DataType_StringIntInt> enchantData;
    private final ArrayList<Enchantment> enchants = new ArrayList<>();
    private final ArrayList<_DataType_StringIntInt> enchantInfo = new ArrayList<>();
    private int ROW = 0;
    private int[] buttonIndex = new int[7];

    private ImageButton Button_Left,Button_right,Button_Mode,Button_EnchantItem;
    private HashMap<Enchantment,Integer> selectEnchants = new HashMap<>();
    public Screen_IndexScreen(Screen_IndexMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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
    private void addMustButton(){
        addEnchantButton();
        addPageButton();
        addModeButton();
        addEnchantItemButton();
    }
    private void addPageButton(){
        Button_Left = new ImageButton(x+103,y+64,
                12,
                19,
                279,
                41,
                0,TEXTURE,
                367,
                166,(pButton -> subROW()),Component.empty());
        this.addRenderableWidget(Button_Left);
        Button_right = new ImageButton(x+155,y+64,
                12,
                19,
                279,
                22,
                0,TEXTURE,
                367,
                166,(pButton -> addROW()),Component.empty());
        this.addRenderableWidget(Button_right);
    }
    private void addModeButton(){
        Button_Mode = new ImageButton(x,y,12,12,200,100,0,TEXTURE,367,166,(pButton -> {}),Component.empty());
        this.addRenderableWidget(Button_Mode);
    }
    private void addEnchantItemButton(){
        Button_EnchantItem = new ImageButton(x+90,y+20,12,12,200,100,0,TEXTURE,367,166,(pButton -> sendSelectEnchant()),Component.empty());
        this.addRenderableWidget(Button_EnchantItem);
    }
    private void sendSelectEnchant(){
        if (selectEnchants.isEmpty()){
            return;
        }
        CompoundTag lDat = AM_EnchantHelp.EnchantsToCompoundTag(selectEnchants);

        lDat.putInt("be.x",menu.getX());
        lDat.putInt("be.y",menu.getY());
        lDat.putInt("be.z",menu.getZ());
        Net_Index_Core.sendToServer(new Packet_Index_ClientToServer(lDat));
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
        int ax = x + 5, ay = y + 19;
        for (int i = 0; i < enchantButton.length ;i++){
            int finalI = i;
            enchantButton[i] = new ImageButton(ax,ay,
                    88,
                    18,
                    279,
                    61,
                    0,TEXTURE,
                    367,
                    166,(pButton -> getSelectEnchant(finalI)),Component.empty());
            this.addRenderableWidget(enchantButton[i] );
            ay += 20;
        }
    }
    private void getSelectEnchant(int button){
        Enchantment enchant = enchants.get(buttonIndex[button]);
        //System.out.println("enchant::"+enchant);
        //
        selectEnchants.put(enchant,selectEnchants.getOrDefault(enchant,0)+1);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.setShaderTexture(0,TEXTURE);

        guiGraphics.blit(TEXTURE,x,y,imageWidth,imageHeight,0,0,imageWidth,imageHeight,367,imageHeight);
        renderProgress(guiGraphics);
    }

    private void renderProgress(GuiGraphics guiGraphics){
        if (menu.isCrafting()){
            guiGraphics.blit(TEXTURE,x+185,y+38,menu.getScaleProgress(),17,308,0,menu.getScaleProgress(),17,367,166);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);
        renderEnchantsButtonText(pGuiGraphics,pMouseX,pMouseY);
        pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.save.all").append(String.valueOf(enchants.size())),x+25,y+5,0x336699);
        pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.select").append(String.valueOf(selectEnchants.size())),x+150,y+5,0x336699);
        pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.page").append(String.valueOf(ROW)),x+117,y+70,0x336699);
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
                        List<Component> tip = List.of(Component.translatable(enchants.get(m).getDescriptionId()),
                                Component.translatable("tooltip.nu.index.max_lvl").append(String.valueOf(enchantInfo.get(m).getMaxLvl())),
                                Component.translatable("tooltip.nu.index.xp").append(String.valueOf(enchantInfo.get(m).getXp())));

                        guiGraphics.renderTooltip(font, tip, java.util.Optional.empty(), pMouseX, pMouseY);
                        color = 0x009900;
                    }
                    guiGraphics.drawString(font, Component.translatable(enchants.get(m).getDescriptionId()), button.getX() + 2, button.getY() + 2, color);
                }
            }
            m ++;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderLabels(pGuiGraphics, pMouseX, pMouseY);
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
