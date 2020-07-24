package net.lomeli.trophyslots.client.screen;

public enum SlotRenderType {
    CROSS_ONLY(true, false), GREYED_OUT(false, true),
    GREY_CROSS(true, true), NONE(false, false);

    private final boolean drawCross;
    private final boolean greyOut;

    SlotRenderType(boolean drawCross, boolean greyOut) {
        this.drawCross = drawCross;
        this.greyOut = greyOut;
    }

    public boolean drawCross() {
        return drawCross;
    }

    public boolean isGreyOut() {
        return greyOut;
    }
}
