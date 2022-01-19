package net.lomeli.trophyslots.client.screen.special;

import static java.lang.Math.*;

class Wind {
    private static final float maxWind = 6;
    private static final int maxDuration = 5000;
    private static final int minDuration = 2000;
    private float targetSpeed;
    private float speed;
    private long windStopTime;

    void tick() {
        long now = System.currentTimeMillis();
        if (now > windStopTime)
            calculateNewWind();
        else
            interpolateWindSpeed();
    }

    private void calculateNewWind() {
        float newWindSpeed = (float) sqrt(random() * maxWind * maxWind);
        if (random() > 0.5)
            newWindSpeed = -newWindSpeed;
        int newDuration = (int) (random() * (maxDuration - minDuration)) + minDuration;

        targetSpeed = newWindSpeed;
        windStopTime = newDuration + System.currentTimeMillis();
    }

    private void interpolateWindSpeed() {
        float speedDiff = targetSpeed - speed;
        float speedChange = (float) (sqrt(abs(speedDiff)) / 15);

        speed += speedChange * signum(speedDiff);
    }

    float getSpeed() {
        return speed;
    }
}
