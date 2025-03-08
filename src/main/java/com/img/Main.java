package com.img;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : IMG
 * @create : 2025/3/8
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("参数不足");
            return;
        }
        String originPath = args[0];
        String addPath = args[1];
        String resultPath = args[2];

        System.out.println("原文件: " + originPath);
        System.out.println("抄袭文件: " + addPath);
        System.out.println("结果文件: " + resultPath);

        String origin;
        String add;
        try {
            origin = readFile(originPath);
            add = readFile(addPath);
        } catch (IOException e) {
            System.out.println("Read file error.");
            throw new RuntimeException(e);
        }

        if (origin == null || add == null) {
            System.out.println("文件内容为空");
            return;
        }

        if (origin.isEmpty() || add.isEmpty()) {
            System.out.println("文件内容为空");
            return;
        }

        Map<String, Float> originWordFreq = getWordFreq(origin);
        Map<String, Float> addWordFreq = getWordFreq(add);

        float cosineSimilarity = cosineSimilarity(originWordFreq, addWordFreq);
        float jaccardSimilarity = jaccardSimilarity(originWordFreq, addWordFreq);
        double result = 0.5 * cosineSimilarity + 0.5 * jaccardSimilarity;

        System.out.format("%.2f\n", result);
        try {
            writeFile(resultPath, String.format("%.2f", result));
            System.out.println("结果保存成功");
        } catch (IOException e) {
            System.out.println("无法保存结果");
        }
    }

    /**
     * 计算两个字符串词向量的Jaccard相似度
     * @param originWordFreq 原始字符串词频
     * @param addWordFreq 新增字符串词频
     * @return Jaccard相似度
     */
    public static float jaccardSimilarity(Map<String, Float> originWordFreq, Map<String, Float> addWordFreq) {
        float numerator = 0;
        float denominator = 0;
        // 计算交集
        for (Map.Entry<String, Float> entry : originWordFreq.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            if (addWordFreq.containsKey(key)) {
                Float value2 = addWordFreq.get(key);
                numerator += Math.min(value, value2);
                denominator += Math.max(value, value2);
            }
        }
        // 计算并集
        for (Map.Entry<String, Float> entry : addWordFreq.entrySet()) {
            if (!originWordFreq.containsKey(entry.getKey())) {
                denominator += entry.getValue();
            }
        }
        return numerator / denominator;
    }

    /**
     * 计算两个字符串词向量的余弦相似度
     * @param originWordFreq 原始字符串词频
     * @param addWordFreq 新增字符串词频
     * @return 余弦相似度
     */
    public static float cosineSimilarity(Map<String, Float> originWordFreq, Map<String, Float> addWordFreq) {
        float numerator = 0;
        float denominator1 = 0;
        float denominator2 = 0;
        for (Map.Entry<String, Float> entry : originWordFreq.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            if (addWordFreq.containsKey(key)) {
                Float value2 = addWordFreq.get(key);
                denominator1 += value * value;
                numerator += value * value2;
            }
        }
        for (Map.Entry<String, Float> entry : addWordFreq.entrySet()) {
            denominator2 += entry.getValue() * entry.getValue();
        }
        return numerator / (float)(Math.sqrt(denominator1) * Math.sqrt(denominator2));
    }

    /**
     * 获取字符串中每个词的词频
     * @param str 输入字符串
     * @return 词频map
     */
    public static Map<String, Float> getWordFreq(String str) {
        HashMap<String, Float> wordFreq = new HashMap<>();
        String[] strings = processString(str);
        for (String string : strings) {
            // 获取分词
            String[] segments = Segment.getSegments(string);
            for (String segment : segments) {
                if (wordFreq.containsKey(segment)) {
                    wordFreq.put(segment, wordFreq.get(segment) + 1);
                } else {
                    wordFreq.put(segment, 1f);
                }
            }
        }
        return wordFreq;
    }

    /**
     * 处理字符串, 以空格,或者中英文标点符号分割
     * @param str 输入字符串
     * @return 分割后的字符串数组
     */
    public static String[] processString(String str) {
        // 切割之后的结果数组中不要空格
        return str.split("[\\s\\pP]");
    }

    /**
     * 读取文件
     * @param originPath 文件路径
     * @return 文件内容
     * @throws IOException 读取文件异常
     */
    public static String readFile(String originPath) throws IOException {
        FileReader reader = new FileReader(originPath);
        int len;
        char[] ch = new char[100];
        StringBuilder result = new StringBuilder();
        while((len = reader.read(ch)) != -1){
            result.append(new String(ch, 0, len));
        }
        reader.close();
        return result.toString();
    }

    public static void writeFile(String resultPath, String result) throws IOException {
        FileWriter writer = new FileWriter(resultPath);
        writer.write(result);
        writer.close();
    }
}
