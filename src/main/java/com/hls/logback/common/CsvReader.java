package com.hls.logback.common;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;

public class CsvReader {  
    public static void main(String[] args) {

        File dir = new File("D:\\work\\eclipse-rcp\\thinktech\\RuntimeData");
        File[] files = dir.listFiles();
        int i = -5;
        boolean flag = false;
        System.out.println("*********************************************  新");
        if(files!=null) {
            for (File file : files) {
                if (i >= 0) {
                    if (i % 10 == 0) {
                        flag = !flag;
                    }
                    if (i % 5 == 0) {
                        if (flag) {
                            System.out.println("*********************************************   旧");
                        } else {
                            System.out.println("*********************************************   新");
                        }
                    }
                }
                check(file);
                i++;
            }
        }
    }

    private static void check(File file) {
        String csvFile = file.getAbsolutePath();
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            long temp = 0;
            int count = 2;
            int num = 0;
            while ((line = reader.readNext()) != null) {
                try {
                    long l = Long.parseLong(line[0]);
                    long l1 = l - temp;
                    if(l1 > 8000){
                        num++;
                        System.out.println(line[0] + " - " + line[1] + "-" + count);
                    }
                    temp = l;
                    count++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            reader.close();
            System.out.println(csvFile + "超出范围的次数：" + num);
//            System.out.println(csvFile + "平均值：" + (float)num/count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}