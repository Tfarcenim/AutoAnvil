package tfar.autoanvil.util;

public enum SideConfig {

    ALL(0xffffffff), BOTH_INPUT(0x0000ffff),
    OUTPUT(0xff7f00ff), INPUT1(0x00ff00ff),
    INPUT2(0x7f00ffff), NONE(0x000000ff);
    //rgba

    public final int color;
    public final float[] splitColor;

    SideConfig(int color) {
        this.color = color;
        splitColor = new float[4];
        splitColor[0] = (color >> 24 & 0xff)/(float)0xff;
        splitColor[1] = (color >> 16 & 0xff)/(float)0xff;
        splitColor[2] = (color >> 8 & 0xff)/(float)0xff;
        splitColor[3] = (color >> 0 & 0xff)/(float)0xff;
    }

    public boolean allowsOutput() {
        return this == ALL || this == OUTPUT;
    }

    public boolean allowsInput(int slot) {

        return this != NONE && (slot == 0 || slot == 1) &&
                (this == ALL || this == BOTH_INPUT || slot == 0 &&
                        this == INPUT1 || slot == 1 && this == INPUT2);

    }
}
