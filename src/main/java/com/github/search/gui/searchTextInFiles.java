package com.github.search.gui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class searchTextInFiles {

    /*
     * Когда то мне поставили задачу парсить гигабайтных размеров текстовый файл со статистикой.
     * Для анализа больших файлов можно разбивать их на куски (тут действительно поможет RandomAccessFile),
     * а каждый кусок анализировать в отдельном потоке, т. е. создать многопоточное приложение.
     * Правда здесь возникают другие проблемы, лимитом скорости обработки становится не жесткий диск,
     * а недостаток мощности процессора, т. е. достигается почти 100% загрузка процессора.
     *
     *
     * */

    public static void main(String[] args) {
        // File directory = new File("C:\\Users\\omoskale\\Downloads");
        File directory = new File("C:\\Users\\omoskale\\Desktop\\test");


        try {


            findFiles(directory);

            findTextInFile();


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(requiredFiles);
        try {
            System.out.println(readFile(new File(requiredFiles.get(0))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static RandomAccessFile file;
    private static String text = "повтор";
    private static List<String> allFiles = new ArrayList<String>();
    private static List<String> requiredFiles = new ArrayList<String>();



    private static void findFiles(File folder) {

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                findFiles(file);
                continue;
            }
//            if (FilenameUtils.getExtension(file.getName()).equals("docx")) {
//                System.out.println(file.getAbsolutePath());
//            }

            if (file.getName().endsWith(".txt")) {
                allFiles.add(file.getAbsolutePath());

            }

        }


    }

    private static String readFile(File currentlyFile) throws IOException {


        file = new RandomAccessFile(currentlyFile, "r");

        String res = "";

        String b = file.readLine();

        while (b != null) {
            res = res + new String(b.getBytes("ISO-8859-1"), "UTF-8") + "\n";
            b = file.readLine();
        }

        file.close();

        return res;

    }

    private static void findTextInFile() throws IOException {


       for (String file : allFiles) {
           if (readFile(new File(file)).contains(text)) {
               requiredFiles.add(file);
           }
       }
    }


}
