package net.lomeli.trophyslots.client.screen.special;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;

import java.util.Calendar;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SpecialScreenRenderer {
    private static final int MAX_FLAKES = 250;
    private static float gravity = 2;
    private Wind wind;
    private List<SnowFlake> flurry;

    public SpecialScreenRenderer() {
        wind = new Wind();
        flurry = Lists.newArrayList();
    }

    public void tick(Screen screen) {
        wind.tick();
        manageFlurry(screen.width, screen.height);
        edgeWrapFlakes(screen.width, screen.height);
        updateFlakePos();
        drawFlakes(screen);
    }

    private void manageFlurry(int screenWidth, int screenHeight) {
        flurry.removeIf(flake -> flake.getYPos() > screenHeight);
        int newFlakeCount = (int) Math.sqrt(MAX_FLAKES - flurry.size()) / 8;
        for (int i = 0; i < newFlakeCount; i++)
            flurry.add(new SnowFlake(screenWidth));
    }

    private void drawFlakes(Screen screen) {
        flurry.forEach(flake -> flake.render(screen));
    }

    private void edgeWrapFlakes(int screenWidth, int screenHeight) {
        flurry.forEach(flake -> {
            if (flake.getXPos() > screenWidth + 16)
                flake.setXPos(-15);
            else if (flake.getXPos() < -16)
                flake.setXPos(screenWidth + 15);
        });
    }

    private void updateFlakePos() {
        flurry.forEach(flake -> flake.update(wind, gravity));
    }

    public void clearFlakes() {
        flurry.clear();
    }

    public boolean isSpecialDay() {
        return Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 25;
    }
}
