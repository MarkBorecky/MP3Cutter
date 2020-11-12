/* mbor1 created on 06.11.2020 
inside the package - pl.maro */

package pl.maro;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Test2 {
    final static String source = "mp3/2part";
    final static String destination = "result/";
    final static double cutOff = 64_000;

    public static void main(String[] args) throws IOException {
        List<String> words = Files.readAllLines(Paths.get(source+".txt"), StandardCharsets.UTF_8);
        Mp3File file = new Mp3File(new FileInputStream(new File(source+".mp3")).readAllBytes());
        createChart(file);
        List<byte[]> subFiles = file.getSubFiles();
        subFiles.forEach(x-> System.out.println("length "+x.length));
        saveFiles(subFiles, words);
    }


    private static void saveFiles(List<byte[]> files, List<String> words) throws IOException {
        File dir = new File(destination);
        if (!dir.exists())
            dir.mkdir();
        System.out.printf("files amount %d\n", files.size());
        for (int i = 0; i < files.size(); i++) {
            var path = destination + words.get(i) + ".mp3";
            Files.write(new File(path).toPath(), files.get(i));
        }
    }

    private static byte[] appendTab(List<byte[]> currentFile) {
        int sum = currentFile.stream().mapToInt(tab -> tab.length).sum();
        var tab = new byte[sum];
        int i = 0;
        for (byte[] bytes : currentFile) {
            for (byte b : bytes) {
                tab[i++] = b;
            }
        }
        return tab;
    }

    private static void createChart(Mp3File file) throws IOException {
        var series1 = new XYSeries("sound");
        var series2 = new XYSeries("cut off");

        Map<Double, NoughtPointOneSecondRecord> chunks = file.getChunks();
        List<Double> keys = new ArrayList<>();
        chunks.keySet().forEach(x -> keys.add(x));
        Collections.sort(keys);
        for (double key : keys) {
            var value = chunks.get(key).getNoise();
//            System.out.printf("%f\t, %f\n", key, value);
            series1.add(key, value);
            series2.add(key, cutOff);
        }

        var dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "mp3",
                "seconds",
                "sum of bytes in one seconds",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );

        ChartUtils.saveChartAsPNG(new File("sound analyze with 0.1 second precision.png"), chart, 2000, 1000);
    }
}
