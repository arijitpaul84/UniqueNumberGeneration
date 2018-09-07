package com.test;

import java.util.*;

public class Main {

    public static void main(String[] args)  {
        test1();
        test2();
        test3();

    }

    private static void test1() {
        List<String> trackingNumbers1 = new ArrayList<>();
        trackingNumbers1.add("1ZV98F55YW20633378");
        trackingNumbers1.add("1Z7642XA1224505518");
        trackingNumbers1.add("1Z3000W8YW82826727");

        String uniqueTrackingNumber = getUniqueTrackingNumber(trackingNumbers1);

        String uniqueShipmentNumber = generateUniqueEncodedValue("1414716593",
                "9019380881277",uniqueTrackingNumber);

        System.out.println("Unique shipment number generated test1:: "+uniqueShipmentNumber);
    }

    private static void test2() {
        List<String> trackingNumbers = new ArrayList<>();
        trackingNumbers.add("1ZV98F55YW20633378");
        trackingNumbers.add("1Z3000W8YW82826727");
        trackingNumbers.add("1Z7642XA1224505518");

        String uniqueTrackingNumber = getUniqueTrackingNumber(trackingNumbers);

        String uniqueShipmentNumber = generateUniqueEncodedValue("1414716593",
                "9019380881277",uniqueTrackingNumber);

        System.out.println("Unique shipment number generated test2:: "+uniqueShipmentNumber);
    }

    private static void test3() {
        List<String> trackingNumbers = new ArrayList<>();
        trackingNumbers.add("1Z3000W8YW82826727");
        trackingNumbers.add("1Z7642XA1224505518");
        trackingNumbers.add("1ZV98F55YW20633378");

        String uniqueTrackingNumber = getUniqueTrackingNumber(trackingNumbers);

        String uniqueShipmentNumber = generateUniqueEncodedValue("1414716593",
                "9019380881277",uniqueTrackingNumber);

        System.out.println("Unique shipment number generated test3:: "+uniqueShipmentNumber);
    }



    private static String generateUniqueEncodedValue(String shipAdviceNumber,
                                                     String orderNumber,
                                                     String encodedTrackingNumber) {
        final Hashids hashids = new Hashids("Target");
        final String response = hashids.encode(Long.parseLong(shipAdviceNumber),
                Long.parseLong(orderNumber),
                encodedTrackingNumber == null ? 0L : Long.parseLong(encodedTrackingNumber));
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

    private static String getUniqueTrackingNumber(List<String> trackingNumbers) {
        List<Long> numericTrackingNumbers = new ArrayList<>();
        for(String trackingNumber : trackingNumbers) {
            numericTrackingNumbers.add(Long.parseLong(generateUniqueAschii(trackingNumber)));
        }
        Long min = numericTrackingNumbers.get(0);
        for(int i = 1; i < numericTrackingNumbers.size(); i++){
            if(numericTrackingNumbers.get(i) < min) {
                min = numericTrackingNumbers.get(i);
            }
        }
        return min.toString();
    }
}