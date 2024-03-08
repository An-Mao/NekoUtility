package anmao.mc.nu.datatype;

import anmao.mc.amlib.color._ColorCDT;

public class DT_Color {
    private final int color;
    private final int selectColor;
    private final int hoverColor;
    public DT_Color(int color){
        this(color,color,color);
    }
    public DT_Color(int color, int selectColor, int hoverColor){
        this.color = color;
        this.selectColor = selectColor;
        this.hoverColor = hoverColor;
    }

    public int getColor() {
        return color;
    }

    public int getSelectColor() {
        return selectColor;
    }

    public int getHoverColor() {
        return hoverColor;
    }
}
