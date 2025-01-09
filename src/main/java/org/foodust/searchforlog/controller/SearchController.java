package org.foodust.searchforlog.controller;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.RequiredArgsConstructor;
import org.foodust.searchforlog.MainApplication;
import org.foodust.searchforlog.file.FileAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class SearchController {
    private final MainApplication mainApp;
    private ScrollPane contentScrollPane;

    public void searchInFolder(File folder, String searchTerm) {
        FileAnalyzer analyzer = new FileAnalyzer();
        List<File> matchedFiles = analyzer.searchFilesWithTerm(folder, searchTerm);
        updateSearchResults(matchedFiles);
    }

    public void showFileContent(File file, String searchTerm) {
        try {
            String content = FileAnalyzer.readFileContent(file);
            highlightAndDisplay(content, searchTerm);
        } catch (IOException e) {
            mainApp.getSelectedFileContent().setText("Error reading file: " + e.getMessage());
        }
    }

    private void updateSearchResults(List<File> files) {
        ListView<String> searchResultListView = mainApp.getSearchResultListView();
        searchResultListView.getItems().clear();
        files.forEach(file -> searchResultListView.getItems().add(file.getAbsolutePath()));
    }

    private void highlightAndDisplay(String content, String searchTerm) {
        TextFlow textFlow = new TextFlow();
        textFlow.setLayoutX(0);
        textFlow.setLayoutY(0);

        // 자동 줄바꿈 비활성화를 위한 설정
        textFlow.setPrefWidth(Region.USE_COMPUTED_SIZE);
        textFlow.setMinWidth(Region.USE_COMPUTED_SIZE);
        textFlow.setMaxWidth(Region.USE_COMPUTED_SIZE);

        String[] lines = content.split("\n");
        int lineNumber = 1;
        Text firstHighlight = null;

        for (String line : lines) {
            // 라인 번호는 항상 추가
            Text lineNumberText = new Text(String.format("%4d: ", lineNumber));
            lineNumberText.setFill(Color.GRAY);
            textFlow.getChildren().add(lineNumberText);

            if (line.contains(searchTerm)) {
                int currentIndex = 0;
                while (true) {
                    int index = line.indexOf(searchTerm, currentIndex);
                    if (index == -1) {
                        // 남은 텍스트 처리
                        if (currentIndex < line.length()) {
                            Text remainingText = new Text(line.substring(currentIndex));
                            textFlow.getChildren().add(remainingText);
                        }
                        break;
                    }

                    // 검색어 이전 텍스트
                    if (index > currentIndex) {
                        Text beforeMatch = new Text(line.substring(currentIndex, index));
                        textFlow.getChildren().add(beforeMatch);
                    }

                    // 검색어 하이라이트
                    Text match = new Text(line.substring(index, index + searchTerm.length()));
                    match.setFill(Color.RED);
                    match.setStyle("-fx-background-color: yellow; -fx-fill: red;");
                    textFlow.getChildren().add(match);

                    // 첫 번째 하이라이트 텍스트 저장
                    if (firstHighlight == null) {
                        firstHighlight = match;
                    }

                    currentIndex = index + searchTerm.length();
                }
            } else {
                // 검색어가 없는 경우 전체 라인 추가
                Text normalText = new Text(line);
                textFlow.getChildren().add(normalText);
            }

            // 줄바꿈 추가
            Text newline = new Text("\n");
            textFlow.getChildren().add(newline);
            lineNumber++;
        }

        if (contentScrollPane == null) {
            // 최초 생성 시
            contentScrollPane = new ScrollPane(textFlow);
            contentScrollPane.setFitToWidth(false);
            contentScrollPane.setFitToHeight(false);
            contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            contentScrollPane.setPrefViewportWidth(400);
            contentScrollPane.setPrefViewportHeight(250);

            // ScrollPane 내부의 콘텐츠가 자동으로 줄바꿈되지 않도록 설정
            contentScrollPane.setContent(textFlow);
            ((Region) contentScrollPane.getContent()).setMinWidth(Region.USE_COMPUTED_SIZE);
            ((Region) contentScrollPane.getContent()).setPrefWidth(Region.USE_COMPUTED_SIZE);

            VBox parent = (VBox) mainApp.getSelectedFileContent().getParent();
            int index = parent.getChildren().indexOf(mainApp.getSelectedFileContent());
            parent.getChildren().set(index, contentScrollPane);
        } else {
            // 이후 업데이트 시
            contentScrollPane.setContent(textFlow);
        }

        // 첫 번째 하이라이트로 스크롤
        if (firstHighlight != null) {
            Text finalFirstHighlight = firstHighlight;
            Platform.runLater(() -> {
                Bounds bounds = finalFirstHighlight.getBoundsInLocal();
                Point2D scrollOffset = getScrollOffset(textFlow, finalFirstHighlight);
                contentScrollPane.setVvalue(scrollOffset.getY() / (textFlow.getBoundsInLocal().getHeight() - contentScrollPane.getViewportBounds().getHeight()));
                contentScrollPane.setHvalue(scrollOffset.getX() / (textFlow.getBoundsInLocal().getWidth() - contentScrollPane.getViewportBounds().getWidth()));
            });
        }
    }

    private Point2D getScrollOffset(TextFlow textFlow, Text targetNode) {
        double offsetY = 0;
        double offsetX = 0;

        for (Node node : textFlow.getChildren()) {
            if (node == targetNode) {
                break;
            }
            Bounds bounds = node.getBoundsInLocal();
            offsetY += bounds.getHeight();
            if (node instanceof Text text) {
                if (text.getText().endsWith("\n")) {
                    offsetX = 0;
                } else {
                    offsetX += bounds.getWidth();
                }
            }
        }

        // 화면 중앙에 위치하도록 오프셋 조정
        offsetY -= contentScrollPane.getViewportBounds().getHeight() / 2;
        if (offsetY < 0) offsetY = 0;

        return new Point2D(offsetX, offsetY);
    }
}