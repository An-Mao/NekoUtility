package anmao.mc.nu.datatype;

public class DT_XYWHUV {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int UOffset;
    private final int VOffset;
    public DT_XYWHUV(DT_XYWH dt_xywh, int uOffset, int vOffset) {
        this.x = dt_xywh.x();
        this.y = dt_xywh.y();
        this.width = dt_xywh.width();
        this.height = dt_xywh.height();
        UOffset = uOffset;
        VOffset = vOffset;
    }
    public DT_XYWHUV(int x, int y, int width, int height, int uOffset, int vOffset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        UOffset = uOffset;
        VOffset = vOffset;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getUOffset() {
        return UOffset;
    }

    public int getVOffset() {
        return VOffset;
    }
}
