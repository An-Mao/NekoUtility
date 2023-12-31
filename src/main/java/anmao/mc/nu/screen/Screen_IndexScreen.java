package anmao.mc.nu.screen;

import anmao.mc.nu.NU;
import anmao.mc.nu.amlib.datatype._DataType_EnchantData;
import anmao.mc.nu.amlib.datatype._DataType_StringIntInt;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Screen_IndexScreen extends AbstractContainerScreen<Screen_IndexMenu> {
    private ImageButton[] enchantButton = new ImageButton[7];
    private int x,y;
    private final Font font = Minecraft.getInstance().font;
    private static final ResourceLocation TEXTURE = new ResourceLocation(NU.MOD_ID,"textures/gui/index.png");
    private final HashMap<Enchantment, _DataType_StringIntInt> enchantData;
    private final ArrayList<Enchantment> enchants = new ArrayList<>();
    private final ArrayList<_DataType_StringIntInt> enchantInfo = new ArrayList<>();
    private int ROW = 0;
    private int[] buttonIndex = new int[7];

    private ImageButton Button_Left,Button_right;
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
        addEnchantButton();
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
        System.out.println("enchant::"+enchant);
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
        //renderEnchant(guiGraphics);
    }

    private void renderEnchant(GuiGraphics guiGraphics) {
        //HashMap<Enchantment, _DataType_StringIntInt> enchantData = menu.getEnchantData();
        //System.out.println("ed::"+enchantData.size());
        ItemStack inputItem = menu.getInputItem();
        AtomicInteger a = new AtomicInteger();
        int nx = x+5;
        enchantData.forEach((enchantment, dataTypeStringIntInt) -> {
            if (!(inputItem == ItemStack.EMPTY)) {
                if (!enchantment.canEnchant(inputItem)){
                    return;
                }
            }
            a.getAndIncrement();
            int ny = y+ a.get() *20;
            //guiGraphics.blit(TEXTURE,nx,ny,88,18,279,61,88,18,367,166);
           // guiGraphics.drawString(font,enchantment.getFullname(dataTypeStringIntInt.getMaxLvl()),nx+3,ny+3,0x336699);
            this.addRenderableWidget(new ImageButton(nx,ny,88,18,279,61,0,TEXTURE,367,166,(pButton -> {
                System.out.println("text");
            }),enchantment.getFullname(1)));
        });

    }

    private void renderProgress(GuiGraphics guiGraphics){
        if (menu.isCrafting()){
            //guiGraphics.blit(TEXTURE,x+185,y+44,308,0,menu.getScaleProgress(),17);
            guiGraphics.blit(TEXTURE,x+185,y+38,menu.getScaleProgress(),17,308,0,menu.getScaleProgress(),17,367,166);
            //guiGraphics.blit(TEXTURE,x+185,y+44,308,0,100,17);
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
            //System.out.println("eii::"+m);
            if (m < enchants.size()){
                boolean show = true;
                if (inputItem  != ItemStack.EMPTY){
                    //System.out.println(":item:"+inputItem);
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
}
