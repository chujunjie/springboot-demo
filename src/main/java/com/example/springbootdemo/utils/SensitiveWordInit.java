package com.example.springbootdemo.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 敏感词
 *
 * @author chujunjie
 * @date Create in 22:02 2020/7/8
 */
public class SensitiveWordInit {

    @SuppressWarnings("rawtypes")
    private static HashMap sensitiveWordMap;

    /**
     * 初始化词库
     *
     * @param data 敏感词集合
     */
    private static void init(List<String> data) {
        addSensitiveWord(data);
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型
     *
     * @param data 敏感词
     */
    @SuppressWarnings("unchecked")
    private static void addSensitiveWord(List<String> data) {
        sensitiveWordMap = new HashMap<>(data.size());
        Iterator<String> iterator = data.iterator();
        Map<String, Object> now;
        Map now2;
        while (iterator.hasNext()) {
            now2 = sensitiveWordMap;
            String word = iterator.next().trim();
            for (int i = 0; i < word.length(); i++) {
                char keyWord = word.charAt(i);
                Object obj = now2.get(keyWord);
                if (obj != null) {
                    now2 = (Map) obj;
                } else {
                    now = new HashMap<>();
                    now.put("isEnd", "0");
                    now2.put(keyWord, now);
                    now2 = now;
                }
                if (i == word.length() - 1) {
                    now2.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 获取内容中的敏感词
     *
     * @param text      内容
     * @param matchType 匹配规则 1=不最佳匹配，2=最佳匹配
     * @return List<String>
     */
    private static List<String> getSensitiveWord(String text, int matchType) {
        List<String> words = new ArrayList<>();
        Map now = sensitiveWordMap;
        // 初始化敏感词长度
        int count = 0;
        // 标志敏感词开始的下标
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            char key = text.charAt(i);
            now = (Map) now.get(key);
            if (now != null) {
                count++;
                if (count == 1) {
                    start = i;
                }
                // 敏感词结束
                if ("1".equals(now.get("isEnd"))) {
                    // 重新获取敏感词库
                    now = sensitiveWordMap;
                    // 取出敏感词，添加到集合
                    words.add(text.substring(start, start + count));
                    // 初始化敏感词长度
                    count = 0;
                }
            } else {
                // 重新获取敏感词库
                now = sensitiveWordMap;
                if (count == 1 && matchType == 1) {
                    count = 0;
                } else if (count == 1 && matchType == 2) {
                    words.add(text.substring(start, start + count));
                    count = 0;
                }
            }
        }
        return words;
    }

    /**
     * 从文件中读取敏感词库中的内容，将内容添加到set集合中
     *
     * @param file 需读取的文件
     * @return Set<String>
     * @throws Exception e
     */
    @SuppressWarnings("resource")
    private static List<String> readSensitiveWordFile(File file) throws Exception {
        Set<String> set;
        try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            if (file.isFile() && file.exists()) {
                set = new HashSet<>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt;
                while ((txt = bufferedReader.readLine()) != null) {
                    // 读取文件，将文件内容放入到set中
                    // 文本保存时包含了BOM，需去除
                    if (txt.startsWith("\uFEFF")) {
                        txt = txt.replace("\uFEFF", "");
                    }
                    set.add(txt);
                }
            } else {
                throw new Exception("敏感词库文件不存在");
            }
        }
        return new ArrayList<>(set);
    }

    public static void main(String[] args) throws Exception {
        List<String> list = readSensitiveWordFile(new ClassPathResource("sensitiveWord.txt").getFile());
        System.out.println(list);
        init(list);
        System.out.println(getSensitiveWord("hello, word", 2));
        System.out.println(getSensitiveWord(", 法1轮功，法轮功，Fuck", 1));
    }
}
