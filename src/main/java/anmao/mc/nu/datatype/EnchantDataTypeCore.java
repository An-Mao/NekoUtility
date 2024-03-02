package anmao.mc.nu.datatype;

import java.math.BigInteger;

public class EnchantDataTypeCore {
    private String eid;
    private int maxLvl = 0;
    private BigInteger xp = new BigInteger("0");

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return eid;
    }
    public void setMaxLvl(int newLvl) {
        this.maxLvl = Math.max(this.maxLvl, newLvl);
    }
    public void setXp(String xp) {
        this.xp = new BigInteger(xp);
    }
    public void setXp(BigInteger xp) {
        this.xp = xp;
    }
    public void addMaxLvl(int na) {
        this.maxLvl += na;
    }

    public void addXp(BigInteger nb) {
        xp = xp.add(nb);
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public BigInteger getXp() {
        return xp;
    }

    @Override
    public String toString() {
        return "{eid:"+getEid()+"}{max:" + getMaxLvl() +"}{xp:"+ getXp().toString()+"}";
    }
}
