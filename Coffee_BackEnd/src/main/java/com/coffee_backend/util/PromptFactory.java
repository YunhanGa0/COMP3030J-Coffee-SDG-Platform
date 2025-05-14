package com.coffee_backend.util;

import java.util.List;

public class PromptFactory {
    public static String getSystemPrompt(String res, String mode) {
        switch (res + ":" + mode) {
            case "article:single":
                return "You are an article summary assistant. Please read the following and summarize the main points for the user:";
            case "article:list":
                return "You are an article classification assistant. Please select articles that meet user needs from the following article list:";
            case "farm:single":
                return "You are an agricultural data analyst. Please interpret the following farm information:";
            case "farm:list":
                return "You are a farm recommendation assistant. Please filter and sort the farms in the following list based on user needs:";
            case "course:single":
                return "You are a course consultant, helping users analyze the following course information:";
            case "course:list":
                return "You are a course recommendation assistant, please recommend the following courses:";
            default:
                return "Error"; // 可以扩展
        }
    }

    public static String buildPrompt(String sysPrompt, List dataList, String userQuery) {
        StringBuilder sb = new StringBuilder(sysPrompt).append("\n\n");
        dataList.forEach(item -> sb.append(item.toString()).append("\n---\n"));
        sb.append("User question: ：").append(userQuery);
        return sb.toString();
    }
}
