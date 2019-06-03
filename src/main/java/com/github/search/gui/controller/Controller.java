package com.github.search.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class Controller extends SearchTextInFiles {


    private List<Path> listWithFiles;
    private Path activityFile;
    private Integer numberStart = 0;
    private Integer numberEnd = 20;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private TreeView<?> treeWiew;

    @FXML
    private TextArea textArea;

    @FXML
    private Button nextPage;

    @FXML
    private Button previousPage;

    @FXML
    private Button copyAll;

    @FXML
    private AnchorPane anchorid;

    @FXML
    private TextField directory;

    @FXML
    private TextField format;

    @FXML
    private TextField searchText;

    @FXML
    private Button Search;

    @FXML
    private Button brose;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void initialize() {


        brose.setOnAction(event -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();

            Stage stage = (Stage) anchorid.getScene().getWindow();

            File file = directoryChooser.showDialog(stage);

            if(file!= null) {

                directory.setText(file.getAbsolutePath());

            }
        });

        Search.setOnAction(event -> {
            listWithFiles =  searchFiles(new File(directory.getText()),format.getText(),searchText.getText());
            treeWiew.setRoot(buildingTree());
        });


        copyAll.setOnAction(event -> {

            try {
                Toolkit.getDefaultToolkit()
                        .getSystemClipboard()
                        .setContents(
                                new StringSelection(copyAll(activityFile)),
                                null
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        nextPage.setOnAction(event -> {

            numberStart = numberEnd;
            numberEnd += 20;

            try {
                textArea.setText(showTextFromFile(activityFile));
            } catch (IOException e) {
                e.printStackTrace();
            }



        });


        previousPage.setOnAction(event -> {

            numberEnd = numberStart;
            numberStart -=20;

            try {
                textArea.setText(showTextFromFile(activityFile));
            } catch (IOException e) {
                e.printStackTrace();
            }



        });



    }

    @FXML
    private void mouseClicked (MouseEvent event){

        TreeItem<Path> item = (TreeItem<Path>) treeWiew.getSelectionModel().getSelectedItem();
        activityFile= item.getValue();
        numberStart=0;
        numberEnd=20;
        try {
            textArea.setText(showTextFromFile(activityFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private TreeItem buildingTree () {
        TreeItem rootItem = new TreeItem("Files");
        rootItem.setExpanded(true);
        for (Path p : listWithFiles) {

            if (!rootItem.getChildren().contains(p)) {
                rootItem.getChildren().add(new TreeItem(p));
            }
        }
        return rootItem;
    }


    private String showTextFromFile (Path file) throws IOException {

    String show = "";
    String a = "";

        List <String> rows =  Files.readAllLines(Paths.get(file.toString()));

        if(numberStart > 0){
            previousPage.setDisable(false);
        } else{
            numberStart = 0;
            previousPage.setDisable(true);
        }

        if(numberEnd > rows.size()) {
            numberEnd=rows.size();
            nextPage.setDisable(true);
        }

        if (numberEnd <= rows.size()) {
            if (numberEnd < rows.size()){
                nextPage.setDisable(false);
            }
            for (int i = numberStart; i < numberEnd; i++) {
                a += rows.get(i) + '\n';
            }

            show = a;
            a=null;
        }




        return show;

    }


    private String copyAll (Path file) throws IOException {

        String text = "";

        List <String> rows =  Files.readAllLines(Paths.get(file.toString()));


            for (int i = 0; i < rows.size(); i++) {
                text += rows.get(i) + '\n';
            }


        return text;

    }





}
