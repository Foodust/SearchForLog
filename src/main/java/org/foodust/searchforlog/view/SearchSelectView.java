package org.foodust.searchforlog.view;

import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import org.foodust.searchforlog.MainApplication;

public class SearchSelectView {
    private MainApplication mainApp;

    @Getter
    private final ListView<String> searchResultListView;


    public SearchSelectView(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.searchResultListView = new ListView<>();
    }
}
