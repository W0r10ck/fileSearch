package com.github.search.gui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
         //File directory = new File("C:\\Users\\omoskale\\Downloads");
        File directory = new File("C:\\Users\\omoskale\\Desktop\\test");


        findFiles(directory);
        System.out.println(requiredFiles);


    }


    private static String text = "базука";

    // private static List<String> requiredFiles = new ArrayList<>();
    private static List<String> requiredFiles = Collections.synchronizedList(new ArrayList<>());



    private static void findFiles(File folder) {

       List<String> allFiles = new ArrayList<>();


        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                findFiles(file);
                continue;
            }

            if (file.getName().endsWith(".txt")) {
                allFiles.add(file.getAbsolutePath());

            }

        }

        try {
            processAll(allFiles,4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    /*
    *   Paths.get(file).toFile().length()  показывает размер файла в байтах
    *
    */



    private static void processAll (List<String> list,int numThreads) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(list.size());

        for (final String lis : list) {


            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    readFile(lis);
                    latch.countDown();
                }
            });

        }

        executorService.shutdown();
        latch.await();
    }





    private static void readFile (String file) {

        String a = "";
        try {
           // Files.lines(path).forEach(System.out::println);
            List <String> rows =  Files.readAllLines(Paths.get(file));
          for (int i = 0; i < rows.size()-1; i++ ) {
              a = rows.get(i) + rows.get(i+1);
              if (a.contains(text)){
                  requiredFiles.add(file);
                  i = rows.size();
              }
          }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
