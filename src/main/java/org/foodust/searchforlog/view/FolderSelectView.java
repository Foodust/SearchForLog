package org.foodust.searchforlog.view;

import javafx.stage.DirectoryChooser;
import org.foodust.searchforlog.MainApplication;

import java.io.File;

public class FolderSelectView {
    private MainApplication mainApp;
    private DirectoryChooser directoryChooser;

    public FolderSelectView(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.directoryChooser = new DirectoryChooser();
    }

    public File showDirectoryChooser() {
        return directoryChooser.showDialog(mainApp.getPrimaryStage());
    }
}


