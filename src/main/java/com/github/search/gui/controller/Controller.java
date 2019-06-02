package com.github.search.gui.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.github.search.gui.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller extends SearchTextInFiles {



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
    private AnchorPane anchorid;

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
            List<String> listWithFiles =  searchFiles(new File(directory.getText()),format.getText(),searchText.getText());
            System.out.println(listWithFiles);
            textArea.setPromptText(listWithFiles.toString());
        });



    }

}
