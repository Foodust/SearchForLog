package org.foodust.searchforlog.common;

import lombok.Getter;

// CommonString.java
@Getter
public enum CS {
    // 애플리케이션 타이틀
    APP_TITLE("SearchForLog"),

    // 메뉴
    MENU_FILE("파일"),
    MENU_EDIT("편집"),
    MENU_HELP("도움말"),
    MENU_OPTION("옵션"),
    MENU_ENCODING("인코딩"),

    // 메뉴 아이템
    MENU_ITEM_OPEN("열기"),
    MENU_ITEM_SAVE("저장"),
    MENU_ITEM_EXIT("종료"),
    MENU_ITEM_PREFERENCES("환경설정"),
    MENU_ITEM_ABOUT("프로그램 정보"),

    // 인코딩 옵션
    ENCODING_UTF8("UTF-8"),
    ENCODING_EUC_KR("EUC-KR"),
    ENCODING_ISO_8859_1("ISO-8859-1"),


    // 라벨
    LABEL_FOLDER_LIST("폴더 목록"),
    LABEL_SEARCH_RESULT("검색 결과"),
    LABEL_FILE_CONTENT("파일 내용"),

    // design
    DESIGN_BASE_FONT_SIZE("-fx-font-size: 14px; "),
    DESIGN_BASE_FONT_BOLD("-fx-font-weight: bold; "),

    // 버튼
    BUTTON_FOLDER_SELECT("폴더 선택"),
    BUTTON_SEARCH("검색"),

    // 입력 필드
    PROMPT_SEARCH_FIELD("검색할 문자열을 입력하세요"),

    // 오류 메시지
    ERROR_READING_FILE("Error reading file: ");

    private final String value;

    CS(String value) {
        this.value = value;
    }
}