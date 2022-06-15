package com.example.reeco;

import java.util.ArrayList;

public class FindText {
    String mainString;
    String findString;
    Integer[] failArray;

    FindText(String mainString, String findString) {
        this.mainString = mainString;
        this.findString = findString;
        findFailArray();
    }

    FindText() {
        this.mainString = null;
        this.findString = null;
    }

    void setMainString(String mainString) {
        this.mainString = mainString;
    }

    void setFindString(String findString) {
        this.findString = findString;
        findFailArray();
    }

    ArrayList<Integer> search() throws Exception {
        if (mainString == null || findString == null) {
            throw new Exception("string is null");
        }

        ArrayList<Integer> result = new ArrayList<>();
        int index = 0;

        while (index < mainString.length()) {
            int findIndex = kmp(index);
            if (findIndex == -1) {
                return result;
            }
            index = findIndex + 1;
        }

        return result;
    }

    /*
    KMP 알고리즘의 fail array를 구하는 함수입니다.
     */
    void findFailArray() {
        failArray = new Integer[findString.length()];

        // fail array를 구하기 위한 메인 인덱스입니다.
        // index 0의 원소는 0인 것이 확정되어 있기 때문에 1부터 검사를 수행합니다.
        int mainIndex = 1;
        // mainIndex의 위치로부터 추가적인 비교연산을 수행하기 위한 보조 인덱스입니다.
        int subIndex = 0;

        // find의 마지막 인덱스까지 비교를 수행합니다.
        while (mainIndex + subIndex < findString.length()) {
            // 접미사의 위치 (mainIndex + subIndex)와 접두사의 위치(subIndex)를 비교하여 일치할 시
            // result의 값을 변경하면서 계속 이동합니다.
            if (findString.charAt(mainIndex + subIndex) == findString.charAt(subIndex)) {
                subIndex++;
                failArray[mainIndex + subIndex - 1] = subIndex;
                continue;
            }

            // 일치하는 문자의 개수가 0일 경우, 메인 인덱스를 1 이동하여 비교를 수행합니다.
            if (subIndex == 0) {
                mainIndex++;
                continue;
            }

            // fail array 정보를 이용하여 index를 이동합니다.
            mainIndex += subIndex - failArray[subIndex - 1];
            subIndex = failArray[subIndex - 1];
        }
    }


    int kmp(int mainIndex) {
        // 패턴 문자열의 크기가 원 문자열의 크기보다 클 경우, 절대 일치할 수 없습니다.
        if (mainString.length() - mainIndex > findString.length()) {
            return -1;
        }

        // 탐색을 위한 find index입니다.
        int findIndex = 0;

        while (mainIndex + findIndex < mainString.length()) {
            if (mainString.charAt(mainIndex + findIndex) == findString.charAt(findIndex)) {
                findIndex++;
                if (findIndex >= findString.length()) {
                    return mainIndex;
                }
                continue;
            }

            if (findIndex == 0) {
                mainIndex++;
                continue;
            }

            mainIndex += findIndex - failArray[findIndex - 1];
            findIndex = failArray[findIndex - 1];
        }

        // 문자를 찾지 못한 경우
        return -1;
    }
}
