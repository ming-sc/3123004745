import com.img.Main;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author : IMG
 * @create : 2025/3/8
 */
public class MainTest {

    @Test
    public void mainTest() {
        String originPath = "src/main/resources/origin.txt";
        String addPath = "src/main/resources/add.txt";
        String resultPath = "src/main/resources/result.txt";
        Main.main(new String[]{originPath, addPath, resultPath});
    }

    @Test
    public void readFileTest() throws IOException {
        String originPath = "src/main/resources/origin.txt";
        String addPath = "src/main/resources/add.txt";

        String origin = Main.readFile(originPath);
        String add = Main.readFile(addPath);

        System.out.println(origin);
        System.out.println(add);
    }

    @Test
    public void processStringTest() {
        String str = "一位真正的作家永远只为内心写作，只有内心才会真实地告诉他，他的自私、他的高尚是多么突出。";
        String[] strings = Main.processString(str);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void getWordFreqTest() {
        String str = "一位真正的作家永远只为内心写作，只有内心才会真实地告诉他，他的自私、他的高尚是多么突出。";
        Map<String, Float> wordFreq = Main.getWordFreq(str);
        for (Map.Entry<String, Float> entry : wordFreq.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    @Test
    public void cosineSimilarityTest() throws IOException {
        String origin = Main.readFile("src/main/resources/origin.txt");
        String add = Main.readFile("src/main/resources/add.txt");
        Map<String, Float> originWordFreq = Main.getWordFreq(origin);
        Map<String, Float> addWordFreq = Main.getWordFreq(add);
        float cosineSimilarity = Main.cosineSimilarity(originWordFreq, addWordFreq);
        System.out.println(cosineSimilarity);
    }
}
