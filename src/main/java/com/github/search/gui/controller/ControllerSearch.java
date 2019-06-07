package com.github.search.gui.controller;

import com.github.search.gui.Main;
import com.github.search.gui.fileUtil.FileContentFilterImpl;
import com.github.search.gui.fileUtil.FilesFinderImpl;
import com.github.search.gui.fileUtil.FilesReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ControllerSearch {

    private final static FilesReader filesReader = new FilesReader(new FileContentFilterImpl(), new FilesFinderImpl());

    private List<Path> listWithFiles;
    private Path activityFile;
    private Integer numberStart = 0;
    private Integer numberEnd = 50;

    private TreeItem <Path> mainRoot;
    private Map<String,Path> mapWithFiles = new HashMap<>();




    @FXML
    private MenuItem newApplication;

    @FXML
    private MenuItem clearThisWindow;


    @FXML
    private TreeView<Path> treeWiew;

    @FXML
    private MenuBar menuBar;

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
    void initialize() {

        brose.setOnAction(event -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();

            Stage stage = (Stage) anchorid.getScene().getWindow();

            File file = directoryChooser.showDialog(stage);

            if (file != null) {

                directory.setText(file.getAbsolutePath());

            }
        });

        Search.setOnAction(event -> {



            if (directory != null && !searchText.getText().isEmpty() && !format.getText().isEmpty()) {
                if (listWithFiles != null) {
                    listWithFiles.clear();
                }
                textArea.clear();
                listWithFiles = filesReader.getFilesWithGivenTextInside(
                        Paths.get(directory.getText()),
                        searchText.getText(),
                        format.getText()
                );
                //listWithFiles = searchFiles(new File(directory.getText()), format.getText(), searchText.getText());

                mainRoot = new TreeItem<Path>(Paths.get(directory.getText()));
                mainRoot.setExpanded(true);

                listWithFiles.stream().forEach(x -> mapWithFiles.put(x.getFileName().toString(),x));

                listWithFiles.stream().forEach(x -> sortedMap(x,new TreeItem <Path> (Paths.get(x.getFileName().toString()))));

                treeWiew.setRoot(null);
                treeWiew.setRoot(mainRoot);
                if (listWithFiles.size() == 0) {
                    textArea.setText("Nothing found");

                }

            } else {

                treeWiew.setRoot(null);
                textArea.clear();
            }
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
            numberEnd += 50;

            try {
                textArea.setText(showTextFromFile(activityFile));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });


        previousPage.setOnAction(event -> {

            numberEnd = numberStart;
            numberStart -= 50;

            try {
                textArea.setText(showTextFromFile(activityFile));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });


        clearThisWindow.setOnAction(event -> {

            Main m = new Main();
            try {
                m.start((Stage) anchorid.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


    }

    @FXML
    private void mouseClicked(MouseEvent event) {

        TreeItem<Path> item = (TreeItem<Path>) treeWiew.getSelectionModel().getSelectedItem();

        if (item.getValue().toString().endsWith(format.getText())) {

            activityFile = mapWithFiles.get(item.getValue().toString());
            numberStart = 0;
            numberEnd = 50;
            try {
                textArea.setText(showTextFromFile(activityFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * The method reads the selected file and displays some text.
     *
     * @param file selected file from Tree
     * @return string with part of the text from the file
     * @throws IOException if an I/O error occurs reading from the file or a malformed or
     * unmappable byte sequence is read
     */

    private String showTextFromFile(Path file) throws IOException {

        String show = "";
        String a = "";

        List<String> rows = Files.readAllLines(Paths.get(file.toString()));

        if (numberStart > 0) {
            previousPage.setDisable(false);
        } else {
            numberStart = 0;
            previousPage.setDisable(true);
        }

        if (numberEnd > rows.size()) {
            numberEnd = rows.size();
            nextPage.setDisable(true);
        }

        if (numberEnd <= rows.size()) {
            if (numberEnd < rows.size()) {
                nextPage.setDisable(false);
            }
            for (int i = numberStart; i < numberEnd; i++) {
                a += rows.get(i) + '\n';
            }

            show = a;
            a = null;
        }

        return show;

    }

    /**
     *  the method completely reads the selected file and returns all the contents in the string
     * @param file selected file from Tree
     * @return string with all content from file
     * @throws IOException if an I/O error occurs reading from the file or a malformed or
     * unmappable byte sequence is read
     */
    private String copyAll(Path file) throws IOException {

        String text = "";

        List<String> rows = Files.readAllLines(Paths.get(file.toString()));


        for (int i = 0; i < rows.size(); i++) {
            text += rows.get(i) + '\n';
        }


        return text;

    }




    private void sortedMap (Path path, TreeItem <Path> item) {

        if (path.getParent().toString().equals(directory.getText())) {
            mainRoot.getChildren().add(item);
        } else {

            if (runOnRoot(mainRoot,new TreeItem(path.getParent().getFileName()),item) == false) {

                if (mainRoot.getChildren().size() == 0) {
                    setFirstRoot(path, item);
                }

            } else {
                TreeItem <Path> newRoot = new TreeItem<>(path.getParent().getFileName());
                newRoot.setExpanded(true);
                newRoot.getChildren().add(item);
                sortedMap(path.getParent(),newRoot);
            }


        }

    }


    private boolean runOnRoot ( TreeItem <Path> rootM, TreeItem <Path> itemParentName,TreeItem <Path> item) {

        if(mainRoot.getChildren().size() != 0) {

            if (!mainRoot.getValue().getFileName().equals(itemParentName.getValue())) {


                for (TreeItem<Path> a : rootM.getChildren()) {

                    if (a.getValue().equals(itemParentName.getValue())) {
                        a.setExpanded(true);
                        a.getChildren().add(item);
                        return false;
                    } else {
                        if(!a.getValue().toString().endsWith(format.getText())) {
                            runOnRoot(a, itemParentName, item);
                        } else {
                            return true;
                        }

                    }

                }

            } else {

                mainRoot.getChildren().add(item);
                return true;

            }



        } else {

            return false;

        }

        return true;

    }


    private void setFirstRoot (Path path,TreeItem <Path> item) {

        if (!mainRoot.getValue().getFileName().equals(path.getParent().getFileName())) {

            TreeItem <Path> newRoot = new TreeItem<>(path.getParent().getFileName());
            newRoot.setExpanded(true);
            newRoot.getChildren().add(item);
            setFirstRoot(path.getParent(),newRoot);

        } else {
            mainRoot.getChildren().add(item);
        }

    }




}
