package com.nowcoder.community.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/8 下午 08:58
 * @description 敏感词过滤器 交给spring容器进行管理
 */
@Component
public class SensitivaWordsFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 敏感词替换符
     */
    public final String REPLACE_STR = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    /**
     * 初始化敏感词树，构建敏感词树形结构【构造函数后执行】
     */
    @PostConstruct
    public void init() {
        try (
                //自行关闭
                InputStream stream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            String keyword;
            //根节点
            while ((keyword = reader.readLine()) != null) {
                addSensitiveWord(keyword);
            }
        } catch (IOException e) {
            logger.error("初始化敏感词错误");
        }


    }

    //添加敏感词
    public void addSensitiveWord(String keyword) {
        TrieNode temp = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = temp.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                temp.setSubNode(c, subNode);
            }
            //移向下一个节点
            temp = subNode;

            //设置结束标识
            if (i == keyword.length() - 1) {
                temp.setEndNode(true);
            }
        }
    }


    /**
     * 过滤敏感词
     * @param originWords
     * @return
     */
    public String filterWord(String originWords) {
        if (StringUtils.isBlank(originWords)) {
            return null;
        }

        //指向敏感树trie的指针
        TrieNode node = rootNode;
        //确定头指针
        int index1 = 0;
        //确定尾指针
        int index2 = 0;
        //过滤后的敏感词信息
        StringBuilder sb = new StringBuilder();

        while (index1 <= originWords.length() - 1 && index2 <= originWords.length() - 1) {
            if (node.getSubNode(originWords.charAt(index1)) == null) {
                sb.append(originWords.charAt(index1));
                index1++;
                index2++;
            } else {
                //指向下一个节点
                node = rootNode.getSubNode(originWords.charAt(index1));
                //移动右指针
                index2++;
                TrieNode tempNode = node.getSubNode(originWords.charAt(index2));
                if (tempNode != null) {
                    //判断是否为根节点
                    if (tempNode.isEndNode) {
                        //匹配到敏感词
                        sb.append(REPLACE_STR);
                        //重新设置指针位置
                        node = rootNode;
                        index1 = index2 + 1;
                        index2 = index1;
                    } else {
                        while (index2 <= originWords.length() - 1) {
                            index2++;
                            node = tempNode.getSubNode(originWords.charAt(index2));

                        }
                    }
                }
            }
        }

        return sb.toString();
    }


    /**
     * 前缀树结构
     */
    private class TrieNode {

        /**
         * 是否为尾节点
         */
        private boolean isEndNode;

        /**
         * 当前节点下的子节点
         * key为子节点的字符，value为子节点
         */
        private Map<Character, TrieNode> subNode = new HashMap<>();


        public boolean isEndNode() {
            return isEndNode;
        }

        public void setEndNode(boolean endNode) {
            isEndNode = endNode;
        }

        /**
         * 根据当前字符key获得子节点
         *
         * @param character
         * @return
         */
        public TrieNode getSubNode(Character character) {
            return subNode.get(character);
        }


        public void setSubNode(Character key, TrieNode value) {
            subNode.put(key, value);
        }
    }
}
