package com.example.chatproject.utils;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static List<String> history = new ArrayList<>();

    public static List<String> getHistory() {
        return history;
    }
    public static void setHistory(List<String> h1) {
        history = h1;
    }
}
