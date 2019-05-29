package com.github.search.gui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

       // checkFiles(directory);

        // findTextInFile();
        System.out.println(requiredFiles);


    }


  //  private static RandomAccessFile file;
    private static String text = "Олег";
  //  private static List<String> allFiles = new ArrayList<String>();
    private static List<String> requiredFiles = Collections.synchronizedList(new ArrayList<>());



    private static void findFiles(File folder) {

       List<String> allFiles = new ArrayList<>();


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

        processAll(allFiles,4);


    }



    /*
    *   Paths.get(file).toFile().length()  показывает размер файла в байтах
    *
    */



    private static void processAll (List<String> list,int numThreads) {

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (final String lis : list) {

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    readFile(lis);
                }
            });

        }

        executorService.shutdown();

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
              }
          }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private static void checkFiles (File folder) {
//        findFiles(folder);
//
//        for (String file : allFiles) {
//
//
//           readFile(file);
//       }
//
//    }

}
