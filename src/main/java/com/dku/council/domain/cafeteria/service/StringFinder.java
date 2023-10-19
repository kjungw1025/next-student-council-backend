package com.dku.council.domain.cafeteria.service;

import java.util.ArrayList;
import java.util.List;

public class StringFinder {
    /**
     * KMP Algorithm
     * Time complexity : O(N + M)
     */
    public static List<Integer> getPartialMatch(String N) {
        int m = N.length();
        List<Integer> pi = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            pi.add(0);
        }

        int begin = 1, matched = 0;
        while (begin + matched < m) {
            if (N.charAt(begin + matched) == N.charAt(matched)) {
                matched++;
                pi.set(begin + matched - 1, matched);
            } else {
                if (matched == 0) {
                    begin++;
                } else {
                    begin += matched - pi.get(matched - 1);
                    matched = pi.get(matched - 1);
                }
            }
        }
        return pi;
    }

    public static List<Integer> kmpSearch(String H, String N) {
        int n = H.length(), m = N.length();
        List<Integer> ret = new ArrayList<>();
        List<Integer> pi = getPartialMatch(N);

        int begin = 0, matched = 0;
        while (begin <= n - m) {
            if (matched < m && H.charAt(begin + matched) == N.charAt(matched)) {
                matched++;
                if (matched == m) {
                    ret.add(begin);
                }
            } else {
                if (matched == 0) {
                    begin++;
                } else {
                    begin += matched - pi.get(matched - 1);
                    matched = pi.get(matched - 1);
                }
            }
        }
        return ret;
    }
}
