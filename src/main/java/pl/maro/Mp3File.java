/* mbor1 created on 06.11.2020 
inside the package - pl.maro */

package pl.maro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.maro.Test2.cutOff;

class Mp3File {
    final static int step = 1600;
    private Map<Double, NoughtPointOneSecondRecord> chunks;
    private List<byte[]> subFiles;

    public Map<Double, NoughtPointOneSecondRecord> getChunks() {
        return chunks;
    }

    public List<byte[]> getSubFiles() {
        return subFiles;
    }

    public Mp3File(byte[] bytes) {
        this.chunks = splitBytes(bytes);
        this.subFiles = splitFile(bytes);
    }

    private List<byte[]> splitFile(byte[] bytes) {
        List<byte[]> subFiles = new ArrayList<>();
        int len = bytes.length;
        int progressBar = 0;
        int start = 0;
        boolean isRecording = false;
        while (progressBar < len) {
            byte[] part = getPart(bytes, progressBar, progressBar + step);
            int noise = countNoise(part);
            if (noise < cutOff && !isRecording) {
                isRecording = true;
                start = progressBar;
            }
            if (isRecording && noise > cutOff) {
                isRecording = false;
                byte[] subFile = getPart(
                        bytes,
                        start-(step*2),
                        progressBar+(step*2)
                );
                subFiles.add(subFile);
            }
            progressBar += step;
            if (progressBar > len)
                progressBar = len;
        }
        return subFiles;
    }

    private byte[] getPart(byte[] bytes, int from, int to) {
        if (from < 0)
            from = 0;
        if (to > bytes.length)
            to = bytes.length;
        if (from < to && from < bytes.length) {
            if (to > bytes.length)
                to = bytes.length;
            byte[] newArray = new byte[to - from];
            int index = 0;
            for (int i = from; i < to; i++) {
                newArray[index] = bytes[i];
                index++;
            }
            return newArray;
        }
        return null;
    }

    private Map<Double, NoughtPointOneSecondRecord> splitBytes(byte[] bytes) {
        int noughtPointOneSeconds = bytes.length / step;
        if (bytes.length % noughtPointOneSeconds != 0)
            noughtPointOneSeconds++;

        Map<Double, NoughtPointOneSecondRecord> map = new HashMap<>();
        for (int i = 0; i < noughtPointOneSeconds; i++) {
            map.put(i / 10.0, new NoughtPointOneSecondRecord(subArray(bytes, i, i + 1)));

        }
        return map;
    }

    public static int countNoise(byte[] sound) {
        int sum = 0;
        for (byte b : sound) {
            sum += Double.valueOf(b);
        }
        return sum;
    }

    private byte[] subArray(byte[] bytes, int fromSecond, int toSecond) {
        int from = fromSecond * step;
        int to = toSecond * step;
        if (from < to && from < bytes.length) {
            if (to > bytes.length)
                to = bytes.length;
            byte[] newArray = new byte[to - from];
            int index = 0;
            for (int i = from; i < to; i++) {
                newArray[index] = bytes[i];
                index++;
            }
            return newArray;
        } else {
            throw new IllegalArgumentException("from or to is incorrect!!!");
        }
    }

}
