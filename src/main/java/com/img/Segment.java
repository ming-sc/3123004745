package com.img;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : IMG
 * @create  : 2025/3/8
 */
@SuppressWarnings("unused")
public class Segment {
    /**
     * 前缀树根节点
     */
    private static PrefixTreeNode<CharacterInfo> root;
    private static int totalFrequency;

    static {
        try {
            // 导入词典
            String dictPath = "src/main/resources/dict.txt";
            root = importDict(dictPath);
        } catch (IOException e) {
            System.out.println("导入词典失败");
        }
    }

    /**
     * 分词
     * @param str 待分词的字符串
     * @return 分词结果
     */
    public static String[] getSegments(String str) {

        if (str == null || str.isEmpty()) {
            return new String[0];
        }

        List<DAGInfo>[] dag = getDAG(str);

        RouteInfo[] route = getRoute(str, dag);
        List<String> segments = toResult(str, route);
        return segments.toArray(new String[0]);
    }

    /**
     * 将分词结果转换为字符串列表
     * @param str 待分词的字符串
     * @param route 分词概率最高路径
     * @return 分词结果
     */
    public static List<String> toResult(String str, RouteInfo[] route) {
        List<String> segments = new ArrayList<>();
        int x = 0;
        String buffer = "";
        while (x < str.length()) {
            int y = route[x].getTo() + 1;
            String word = str.substring(x, y);
            if (word.length() == 1 && isEnglish(word.charAt(0))) {
                buffer += word;
            } else {
                if (!buffer.isEmpty()) {
                    segments.add(buffer);
                    buffer = "";
                }
                segments.add(word);
            }
            x = y;
        }
        if (!buffer.isEmpty()) {
            segments.add(buffer);
        }
        return segments;
    }

    public static boolean isEnglish(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 获取分词概率最高路径
     * @param str 待分词的字符串
     * @param DAG 有向无环图
     * @return 分词概率最高路径
     */
    public static RouteInfo[] getRoute(String str, List<DAGInfo>[] DAG) {
        int n = str.length();
        RouteInfo[] route = new RouteInfo[n + 1];
        double logTotal = Math.log(totalFrequency);
        route[n] = new RouteInfo(0, 0);
        for (int i = n - 1; i >= 0; i--) {
            RouteInfo maxRoute = new RouteInfo(-1, Double.NEGATIVE_INFINITY);
            for (DAGInfo dagInfo : DAG[i]) {
                int w = dagInfo.getWeight() == 0 ? 1 : dagInfo.getWeight();
                // 计算当前分词加上后面的最高分词的概率
                double weight = Math.log(w) - logTotal + route[dagInfo.getTo() + 1].getWeight();
                if (weight > maxRoute.getWeight()) {
                    maxRoute.setTo(dagInfo.getTo());
                    maxRoute.setWeight(weight);
                }
            }
            route[i] = maxRoute;
        }
        return route;
    }

    /**
     * 获取DAG
     *
     * @param str 待分词的字符串
     * @return DAG
     */
    public static List[] getDAG(String str) {
        List[] DAG = new List[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            List<DAGInfo> tmp = new ArrayList<>();
            if (root.hasNext(c)) {
                PrefixTreeNode<CharacterInfo> node = root;
                int j = i;
                char nextChar = str.charAt(j);
                while (j < str.length() && node.hasNext(nextChar)) {
                    node = node.getNext().get(nextChar);
                    tmp.add(new DAGInfo(i, j, node.getData().getFrequency()));
                    j++;
                    // 防止越界
                    if (j < str.length()) {
                        nextChar = str.charAt(j);
                    }
                }
            } else {
                tmp.add(new DAGInfo(i, i, 0));
            }
            DAG[i] = tmp;
        }
        return DAG;
    }

    /**
     * 导入词典
     * @param dictPath 词典路径
     * @return 前缀树根节点
     * @throws IOException 读取文件异常
     */
    public static PrefixTreeNode<CharacterInfo> importDict(String dictPath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(dictPath));
        String line = null;
        PrefixTreeNode<CharacterInfo> root = new PrefixTreeNode<>();
        totalFrequency = 0;
        while ((line = bufferedReader.readLine()) != null) {
            // 以空格分割
            String[] words = line.split(" ");
            // 处理词和词频
            String word = words[0];

            int freq = Integer.parseInt(words[1]);
            // 插入到前缀树
            handleWord(root, word, freq);
        }
        return root;
    }

    /**
     * 处理词和词频
     * @param root 前缀树根节点
     * @param word 词
     * @param freq 词频
     */
    public static void handleWord(PrefixTreeNode<CharacterInfo> root, String word, int freq) {
        if (word == null || word.isEmpty()) {
            return;
        }
        PrefixTreeNode<CharacterInfo> node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!node.hasNext(c)) {
                PrefixTreeNode<CharacterInfo> nextNode = new PrefixTreeNode<>();
                if (i == word.length() - 1) {
                    totalFrequency += freq;
                    nextNode.setData(new CharacterInfo(c, freq));
                } else {
                    nextNode.setData(new CharacterInfo(c, 0));
                }
                node.addNext(c, nextNode);
            }
            node = node.getNext().get(c);
        }
    }
}
