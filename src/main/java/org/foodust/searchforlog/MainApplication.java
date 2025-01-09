package org.foodust.searchforlog;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.foodust.searchforlog.common.CS;
import org.foodust.searchforlog.controller.SearchController;
import org.foodust.searchforlog.view.FolderSelectView;

import java.io.File;
import java.util.Arrays;

@Getter
public class MainApplication extends Application {
    private Stage primaryStage;
    private FolderSelectView folderSelectView;
    private SearchController searchController;
    private ListView<String> folderContentListView;
    private ListView<String> searchResultListView;
    private TextArea selectedFileContent;
    private File currentFolder;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(CS.APP_TITLE.getValue());
        this.primaryStage.setResizable(false);

        initializeViews();
        showMainView();
    }

    private void initializeViews() {
        folderSelectView = new FolderSelectView(this);
        searchController = new SearchController(this);
        folderContentListView = new ListView<>();
        searchResultListView = new ListView<>();
        selectedFileContent = new TextArea();
        selectedFileContent.setEditable(false);
        selectedFileContent.setWrapText(true);
    }

    private void showMainView() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue()); // 기본 폰트 크기 설정

        // 왼쪽 영역 (폴더 관련)
        VBox leftArea = new VBox(15);  // 간격 증가
        leftArea.setPrefWidth(600);     // 왼쪽 영역 고정 너비

        // 폴더 목록 라벨
        Label folderLabel = new Label(CS.LABEL_FOLDER_LIST.getValue());
        folderLabel.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue() + CS.DESIGN_BASE_FONT_BOLD.getValue());
        folderLabel.setMaxWidth(Double.MAX_VALUE);
        folderLabel.setAlignment(Pos.CENTER);

        // 폴더 경로 및 선택 버튼
        HBox folderSelectionBox = new HBox(10);
        folderSelectionBox.setAlignment(Pos.CENTER);
        TextField folderPathField = new TextField();
        folderPathField.setEditable(false);
        folderPathField.setPrefWidth(450);
        folderPathField.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());

        Button selectFolderBtn = new Button(CS.BUTTON_FOLDER_SELECT.getValue());
        selectFolderBtn.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());
        selectFolderBtn.setPrefWidth(120);

        folderSelectionBox.getChildren().addAll(folderPathField, selectFolderBtn);

        // 폴더 내용 목록
        folderContentListView.setPrefSize(580, 500);
        folderContentListView.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());

        leftArea.getChildren().addAll(folderLabel, folderSelectionBox, folderContentListView);

        // 중앙/오른쪽 영역
        VBox rightArea = new VBox(15);  // 간격 증가
        rightArea.setPrefWidth(600);     // 오른쪽 영역 고정 너비
        rightArea.setPadding(new Insets(0, 0, 0, 20)); // 왼쪽 영역과의 간격

        // 검색 영역
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPrefWidth(450);
        searchField.setPromptText(CS.PROMPT_SEARCH_FIELD.getValue());
        searchField.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());

        Button searchBtn = new Button(CS.BUTTON_SEARCH.getValue());
        searchBtn.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());
        searchBtn.setPrefWidth(120);

        searchBox.getChildren().addAll(searchField, searchBtn);

        // 검색 결과 및 파일 내용 영역
        HBox resultArea = new HBox(20);  // 간격 증가

        // 검색 결과 목록
        VBox searchResultBox = new VBox(10);
        searchResultBox.setPrefWidth(580);
        Label searchResultLabel = new Label(CS.LABEL_SEARCH_RESULT.getValue());
        searchResultLabel.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue() + CS.DESIGN_BASE_FONT_BOLD.getValue());
        searchResultListView.setPrefSize(580, 220);
        searchResultListView.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());
        searchResultBox.getChildren().addAll(searchResultLabel, searchResultListView);

        // 파일 내용
        VBox fileContentBox = new VBox(10);
        fileContentBox.setPrefWidth(580);
        Label fileContentLabel = new Label(CS.LABEL_FILE_CONTENT.getValue());
        fileContentLabel.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue() + CS.DESIGN_BASE_FONT_BOLD.getValue());
        selectedFileContent.setPrefSize(580, 220);
        selectedFileContent.setStyle(CS.DESIGN_BASE_FONT_SIZE.getValue());
        fileContentBox.getChildren().addAll(fileContentLabel, selectedFileContent);

        VBox bottomArea = new VBox(15);
        bottomArea.getChildren().addAll(searchResultBox, fileContentBox);

        rightArea.getChildren().addAll(searchBox, bottomArea);

        // 레이아웃 배치
        mainLayout.setLeft(leftArea);
        mainLayout.setRight(rightArea);

        // 이벤트 핸들러
        selectFolderBtn.setOnAction(e -> {
            File selectedFolder = folderSelectView.showDirectoryChooser();
            if (selectedFolder != null) {
                currentFolder = selectedFolder;
                folderPathField.setText(selectedFolder.getAbsolutePath());
                updateFolderContent(selectedFolder);
                searchResultListView.getItems().clear();
                selectedFileContent.clear();
            }
        });

        searchBtn.setOnAction(e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty() && currentFolder != null) {
                searchController.searchInFolder(currentFolder, searchTerm);
            }
        });

        searchResultListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null && !searchField.getText().isEmpty()) {
                        searchController.showFileContent(
                                new File(newValue),
                                searchField.getText()
                        );
                    }
                }
        );

        Scene scene = new Scene(mainLayout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateFolderContent(File folder) {
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
}