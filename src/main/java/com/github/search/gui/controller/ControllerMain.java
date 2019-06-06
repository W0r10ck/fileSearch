package com.github.search.gui.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;


public class ControllerMain {


    private SingleSelectionModel<Tab> selectionModel;
    private Integer i = 1;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab mainTab;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem newApplication;

    @FXML
    void initialize() throws IOException {

        mainTab.setContent(FXMLLoader.load(getClass().getResource("/fxml/samp.fxml")));
        
       newApplication.setOnAction(event -> {
           Tab newTab = new Tab ();
           newTab.setText("Tab"+ " " + i);
           newTab.setClosable(true);
           newTab.setId("Tab" + i);
           try {
               newTab.setContent(FXMLLoader.load(getClass().getResource("/fxml/samp.fxml")));
           } catch (IOException e) {
               e.printStackTrace();
           }

           i++;

           tabPane.getTabs().add(newTab);
           selectionModel.selectLast();

       });

       selectionModel = tabPane.getSelectionModel();
        
    }





}
