package com.cj.sms.utils;

public class Solution {

    public static void main(String[] args) {
        addBinary("1010", "1011");
    }


    public static String addBinary(String a, String b) {
        int x = Integer.parseInt(a, 2);
        int y = Integer.parseInt(b, 2);
        while (y != 0) {
            int answer = x ^ y;
            int carry = (x & y) << 1;
            x = answer;
            y = carry;
        }
        return Integer.toBinaryString(x);
    }
}
