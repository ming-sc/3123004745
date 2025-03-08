import com.img.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author : IMG
 * @create : 2025/3/8
 */
public class SegmentTest {
    @Test
    public void getSegmentsTest() {
        String str = "一位真正的作家永远只为内心写作";
        String[] segments = Segment.getSegments(str);
        for (String segment : segments) {
            System.out.println(segment);
        }
    }


    @Test
    public void importDictTest() {
        InputStream inputStream = Segment.class.getClassLoader().getResourceAsStream("dict.txt");
        try {
            Segment.importDict(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void handleWordTest() {
        PrefixTreeNode<CharacterInfo> root = new PrefixTreeNode<>();
        Segment.handleWord(root, "一", 1);
        Segment.handleWord(root, "一一", 2);
        Segment.handleWord(root, "一一道来", 3);
        System.out.println(root);
    }

    @Test
    public void getDAGTest() {
        String str = "一位真正的作家永远只为内心写作";
        List<DAGInfo>[] DAG = Segment.getDAG(str);
        for (List<DAGInfo> dagInfos : DAG) {
            for (DAGInfo dagInfo : dagInfos) {
                System.out.println(str.substring(dagInfo.getFrom(), dagInfo.getTo() + 1) + " : " + dagInfo.getWeight());
            }
            System.out.println();
        }
    }

    @Test
    public void getRouteTest() {
        String str = "一位真正的作家永远只为内心写作";
        List<DAGInfo>[] DAG = Segment.getDAG(str);
        RouteInfo[] route = Segment.getRoute(str, DAG);
        for (int i = 0; i < route.length - 1; i++) {
            System.out.println(str.substring(i, route[i].getTo() + 1) + " : " + route[i].getWeight());
        }
    }

    @Test
    public void toResultTest() {
        String str = "一位真正的作家永远只为内心写作";
        List<DAGInfo>[] DAG = Segment.getDAG(str);
        RouteInfo[] route = Segment.getRoute(str, DAG);
        List<String> segments = Segment.toResult(str, route);
        for (String segment : segments) {
            System.out.println(segment);
        }
    }
}
