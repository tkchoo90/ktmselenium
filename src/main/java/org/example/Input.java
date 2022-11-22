package org.example;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {

    protected static String getOrigin() throws IOException {
        System.out.println("Select Origin");
        System.out.println("1) JB SENTRAL");
        System.out.println("2) WOODLANDS CIQ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();

        if (entry.equals("1")) {
            return "JB SENTRAL";
        } else if (entry.equals("2")) {
            return "WOODLANDS CIQ";
        } else {
            System.out.println("Invalid start point selection. Exiting now...");
        }
        throw new RuntimeException("Invalid start point selection. Exiting now...");
    }

    protected static String getDate() throws IOException {
        System.out.println("Enter date: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();
        if (!StringUtils.isEmpty(entry)) {
            return entry;
        } else {
            throw new RuntimeException("Invalid date entry. Exiting now...");
        }
    }

    protected static String getYear() throws IOException {
        System.out.println("Enter year: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();
        if (!StringUtils.isEmpty(entry)) {
            return entry;
        } else {
            throw new RuntimeException("Invalid year entry. Exiting now...");
        }
    }

    protected static String getStartTime() throws IOException {
        System.out.println("Enter start time in 24hr format: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();
        if (!StringUtils.isEmpty(entry)) {
            return entry;
        } else {
            throw new RuntimeException("Invalid start time. Exiting now...");
        }
    }

    protected static String getEndTime() throws IOException {
        System.out.println("Enter end time in 24jr fpr,at: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();
        if (!StringUtils.isEmpty(entry)) {
            return entry;
        } else {
            throw new RuntimeException("Invalid end time. Exiting now...");
        }
    }

    protected static String getMonth() throws IOException {
        System.out.println("Select Month: ");
        System.out.println(
                "0) January \n" +
                        "1) February \n" +
                        "2) March \n" +
                        "3) April \n" +
                        "4) May \n" +
                        "5) June \n" +
                        "6) July \n" +
                        "7) August \n" +
                        "8) September \n" +
                        "9) October \n" +
                        "10) November \n" +
                        "11) December"
        );
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entry = br.readLine();
        return entry;
//        switch (entry) {
//            case "1":
//                return "January";
//            case "2":
//                return "February";
//            case "3":
//                return "March";
//            case "4":
//                return "April";
//            case "5":
//                return "May";
//            case "6":
//                return "June";
//            case "7":
//                return "July";
//            case "8":
//                return "August";
//            case "9":
//                return "September";
//            case "10":
//                return "October";
//            case "11":
//                return "November";
//            case "12":
//                return "December";
//        }
//        throw new RuntimeException("Invalid month selection. Exiting now...");
    }

}
