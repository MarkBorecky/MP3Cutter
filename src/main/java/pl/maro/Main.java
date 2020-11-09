/* mbor1 created on 06.11.2020
inside the package - pl.maro */

package pl.maro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    final static int second = 28302;

    public static void main(String... args) throws IOException {
        readParameters(args);
    }

    private static void readParameters(String... args) throws IOException {
        int from = 0;
        int to = 0;
        String source = null;
        String result = null;
        for (int i = args.length - 1; i >= 0; i--) {
            var arg = args[i];
            if (isNumber(arg)) {
                if (from == 0)
                    from = Integer.valueOf(arg);
                else
                    to = Integer.valueOf(arg);
            } else {
                if (source == null) {
                    source = arg;
                } else {
                    result = arg;
                }
            }
        }
        if (result == null)
            result = "result.mp3";

        ensureExtension(source);
        ensureExtension(result);
        cutMp3(source, result, from, to);
    }

    private static void ensureExtension(String result) {
        if (!result.contains(".mp3"))
            result += ".mp3";
    }

    private static boolean isNumber(String arg) {
        for (char c : arg.toCharArray())
            if (!Character.isDigit(c)) return false;
        return true;
    }

    private static void cutMp3(String source, String result, int from, int to) throws IOException {
        var bytes = new FileInputStream(new File(source)).readAllBytes();
        var newBytes = subArray(bytes, 7, 10);
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
