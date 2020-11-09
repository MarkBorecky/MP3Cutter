/* mbor1 created on 06.11.2020 
inside the package - pl.maro */

package pl.maro;

import java.util.Arrays;

import static pl.maro.Mp3File.countNoise;

class NoughtPointOneSecondRecord {
    private double noise;
    private byte[] sound;

    public NoughtPointOneSecondRecord(byte[] sound) {
        this.sound = sound;
        this.noise = countNoise(sound);
    }


    public double getNoise() {
        return noise;
    }

    public boolean isPerceptible(){
        return noise < 100_000;
    }

    public byte[] getSound() {
        return sound;
    }
}