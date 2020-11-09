/* mbor1 created on 06.11.2020 
inside the package - pl.maro */

package pl.maro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Test {
    final static int second = 16000;
    final static String source = "mp3/100SLOWEK.mp3";
    final static String result = "result3.mp3";

    public static void main(String[] args) throws IOException {
        var bytes = new FileInputStream(new File(source)).readAllBytes();
        var newBytes = subArray(bytes, 0, 15);
        Files.write(new File(result).toPath(), newBytes);
    }

    private static byte[] subArray(byte[] bytes, int fromSecond, int toSecond) {
        int from = fromSecond * second;
        int to = toSecond * second;
        if (from < to && from < bytes.length && to < bytes.length) {
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
