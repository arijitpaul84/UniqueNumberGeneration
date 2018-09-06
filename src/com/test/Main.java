package com.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args)  {
        testWithSingleRecords();
        //testWithMultipleRecords();
    }

    private static void testWithSingleRecords() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ship advice number ::");
        String shipadviceNumber = scanner.next();
        System.out.println("Enter order number ::");
        String orderNumber = scanner.next();
        System.out.println("Enter comma separated tracking numbers ::");
        String trackingNumber = scanner.next();
        String[] trackingNumbers = trackingNumber.split(",");
        /*String encodedTrackingNumber = generateUniqueAschii(
                "1ZV98F55YW20633378","1Z7642XA1224505518","1Z3000W8YW82826727");
        String uniqueShipmentNumber = generateUniqueEncodedValue("1414716593",
                "9019380881277",encodedTrackingNumber);*/

        String encodedTrackingNumber = generateUniqueAschii(
                trackingNumbers);
        String uniqueShipmentNumber = generateUniqueEncodedValue(shipadviceNumber,
                orderNumber,encodedTrackingNumber);
        System.out.println("Unique shipment number generated :: "+uniqueShipmentNumber);
    }

    private static void testWithMultipleRecords() {
        String csvFile = "/Users/z0035yw/export.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            Set<String> set = new HashSet<>();
            Set<String> shipAdviceSet = new HashSet<>();
            Set<String> orderNumberSet = new HashSet<>();
            Map<String,String> map = new HashMap<>();
            Map<String,String> map1 = new HashMap<>();
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                shipAdviceSet.add(values[0].trim());
                orderNumberSet.add(values[1].trim());
                String uniqueNum = generateUniqueEncodedValue(
                        values[0].trim().substring(1,values[0].trim().length()-1),
                        values[1].trim().substring(1,values[1].trim().length()-1),null);
                set.add(uniqueNum);
                /*if(set.add(uniqueNum)) {
                    map.put(uniqueNum, values[0].trim());
                    map1.put(uniqueNum,values[1].trim());
                } else {
                    System.out.println("Current ship advice number --"+values[0].trim());
                    System.out.println("Existing ship advice number --"+map.get(uniqueNum));
                    System.out.println("Current order number --"+values[1].trim());
                    System.out.println("Existing order number --"+map1.get(uniqueNum));
                    System.out.println("Duplicated unique number is "+uniqueNum);
                    System.out.println("---------------------------------------");
                }*/
                i++;
            }
            System.out.println(set.size());
            System.out.println(i);

            System.out.println("shipAdviceSet "+shipAdviceSet.size());
            System.out.println("orderNumberSet "+orderNumberSet.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String generateUniqueEncodedValue(String shipAdviceNumber,
                                                     String orderNumber,
                                                     String encodedTrackingNumber) {
        final Hashids hashids = new Hashids("Target");
        final String response = hashids.encode(Long.parseLong(shipAdviceNumber),
                Long.parseLong(orderNumber),
                encodedTrackingNumber== null ? 0L : Long.parseLong(encodedTrackingNumber));
        return generateUniqueAschii(response);
    }

    private static String generateUniqueAschii(String... responses) {
        StringBuilder builder = new StringBuilder();
        for(String response : responses) {
            for (Character ch : response.toCharArray()) {
                int i = (int) ch;
                builder.append(i);
            }
        }

        int len = builder.toString().length();
        StringBuilder builder1 = new StringBuilder();
        if (len > 20) {
            int divisor = 0;
            if (len / 20 != 0) {
                divisor = len / 20 + 1;
            } else {
                divisor = len / 20;
            }
            String str = builder.toString();
            int start = 0;
            int end = divisor;
            while (end < len) {
                createFinalUniqueCode(builder1, str, start, end);
                start = end;
                end = end + divisor;
            }
            if (end > len) {
                end = len;
                createFinalUniqueCode(builder1, str, start, end);
            }
        }
        return builder1.toString();
    }

    private static void createFinalUniqueCode(StringBuilder builder1,
                                              String str,
                                              int start,
                                              int end) {
        String str1 = str.substring(start,end);
        int sum = 0;
        for(Character ch : str1.toCharArray()) {
            sum += ch;
        }
        builder1.append(sum%10);
    }
}