package com.github.search.gui.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchTextInFiles {

    private static List<Path> requiredFiles = new CopyOnWriteArrayList<>();

    public List<Path> searchFiles (File dir, String format, String textForSearch ) {
        findFiles(dir,format,textForSearch);

        return requiredFiles;
    }

    private static void findFiles(File folder, String format, String textForSearch) {

       List<String> allFiles = new ArrayList<>();


        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                findFiles(file,format,textForSearch);
                continue;
            }

            if (file.getName().endsWith(format)) {
                allFiles.add(file.getAbsolutePath());

            }

        }

        try {
            processAll(allFiles,4,textForSearch);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    /*
    *   Paths.get(file).toFile().length()  показывает размер файла в байтах
    *
    */



    private static void processAll (List<String> list,int numThreads, String text) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(list.size());

        for (final String lis : list) {


            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    readFile(lis,text);
                    latch.countDown();
                }
            });

        }

        executorService.shutdown();
        latch.await();
    }





    private static void readFile (String file, String text) {

        String a = "";
        try {
           // Files.lines(path).forEach(System.out::println);
            List <String> rows =  Files.readAllLines(Paths.get(file));
          for (int i = 0; i < rows.size()-1; i++ ) {
              a = rows.get(i) + rows.get(i+1);
              if (a.contains(text)){
                  requiredFiles.add(Paths.get(file));
                  i = rows.size();
              }
          }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
