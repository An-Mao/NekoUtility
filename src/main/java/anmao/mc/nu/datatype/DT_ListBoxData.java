package anmao.mc.nu.datatype;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DT_ListBoxData {
    private final Component component;
    private final Object value;
    private final Component tooltip;
    private final OnPress onPress;
    public DT_ListBoxData(Component component, Object value){
        this(component,value,component,null);
    }
    public DT_ListBoxData(Component component, Object value, OnPress onPress){
        this(component,value,component,onPress);
    }
    public DT_ListBoxData(Component component, Object value, Component tooltip, OnPress onPress){
        this.component = component;
        this.value = value;
        this.tooltip = tooltip;
        this.onPress = onPress;
    }

    public Component getComponent() {
        return component;
    }

    public Object getValue() {
        return value;
    }

    public Component getTooltip() {
        return tooltip;
    }

    public void OnPress(Object value) {
        if (onPress != null) {
            onPress.onPress(value);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(Object value);
    }
}
