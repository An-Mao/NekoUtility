package anmao.mc.nu.amlib.datatype;

public class _DataType_StringIntInt {
    private String eid;
    private int maxLvl = 0;
    private int xp = 0;

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return eid;
    }
    public void setMaxLvl(int newLvl) {
        this.maxLvl = Math.max(this.maxLvl, newLvl);
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
    public void addMaxLvl(int na) {
        this.maxLvl += na;
    }

    public void addXp(int nb) {
        this.xp += nb;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public String toString() {
        return "{eid:"+getEid()+"}{max:" + getMaxLvl() +"}{xp:"+ getXp()+"}";
    }
}
