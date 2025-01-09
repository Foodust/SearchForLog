package org.foodust.searchforlog.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import org.foodust.searchforlog.MainApplication;
import org.foodust.searchforlog.common.CS;

import java.io.File;
import java.util.Arrays;

public class FolderSelectView {
    private MainApplication mainApp;
    private DirectoryChooser directoryChooser;
    @Getter
    private final ListView<String> folderContentListView;

    @Getter
    private final Label folderLabel;
    @Getter
    private final HBox folderSelectionBox;
    @Getter
    private final TextField folderPathField;
    @Getter
    private final Button selectFolderButton;

    public FolderSelectView(MainApplication mainApp) {
        this.mainApp = mainApp;
        this.directoryChooser = new DirectoryChooser();
        this.folderContentListView = new ListView<>();
        this.folderLabel = new Label(CS.LABEL_FOLDER_LIST.getValue());
        this.folderSelectionBox = new HBox(10);
        this.folderPathField = new TextField();
        this.selectFolderButton = new Button(CS.BUTTON_FOLDER_SELECT.getValue());

        // 폴더 목록 라벨
        folderLabel.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue() + CS.DESIGN_BASE_FONT_BOLD.getValue());
        folderLabel.setMaxWidth(Double.MAX_VALUE);
        folderLabel.setAlignment(Pos.CENTER);

        folderSelectionBox.setAlignment(Pos.CENTER);

        folderPathField.setEditable(false);
        folderPathField.setPrefWidth(450);
        folderPathField.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());

        selectFolderButton.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());
        selectFolderButton.setPrefWidth(120);
    }

    public void updateFolderContent(File folder) {
        folderContentListView.getItems().clear();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                Arrays.sort(files);  // 파일 이름순 정렬
                for (File file : files) {
                    folderContentListView.getItems().add(file.getName());
                }
            }
        }
    }

    public File showDirectoryChooser() {
        return directoryChooser.showDialog(mainApp.getPrimaryStage());
    }
}


