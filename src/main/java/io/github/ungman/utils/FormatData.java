package io.github.ungman.utils;

import io.github.ungman.pojo.Profile;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class FormatData {

    private static int countLineCharacter = 50;

    public static String getProfileFormatted(Profile profile,String messageError) {
        String[] profileToArrayString = profileToStringChecked(profile,messageError);
        String profileToString;
        if (profileToArrayString.length > 1) {
            String[] arrDataDelimiter = new String[]{"-", "*", "-"};
            String[] arrSideDelimiter = new String[]{"|"};
            profileToString = getDataFormatted(profileToArrayString, countLineCharacter, arrSideDelimiter, arrDataDelimiter);
        } else {
            profileToString = getDataFormatted(profileToArrayString, countLineCharacter, null, null);
        }
        return profileToString;
    }

    public static String getProfileFormatted(Profile profile) {
        return getProfileFormatted(profile,null);
    }

    public static String showLinesFormatted(String data, int countAllLineCharacter, String sideDelimiter, String dataDelimiter) {
        StringBuilder formattedDataStringBuilder = new StringBuilder();
        int countLineCharacter = countAllLineCharacter - sideDelimiter.length() * 2 - 2;
        String[] arrDataToLines = splitToNChar(data, countLineCharacter);
        formattedDataStringBuilder.append(getNCopyData(countAllLineCharacter, dataDelimiter));
        for (String arrDataToLine : arrDataToLines) {
            formattedDataStringBuilder.append("\n")
                    .append(sideDelimiter + " ")
                    .append(arrDataToLine)
                    .append(" "+sideDelimiter).append("\n");

        }
        formattedDataStringBuilder.append(getNCopyData(countAllLineCharacter, dataDelimiter)).append("\n");
        return formattedDataStringBuilder.append("\n").toString();
    }

    public static String showLinesFormatted(String data, String sideDelimiter, String dataDelimiter) {
        return showLinesFormatted(data, countLineCharacter, sideDelimiter, dataDelimiter);
    }

    private static String getDataFormatted(String[] arrData, int countAllLineCharacter, String[] arrSideDelimiter, String[] arrDataDelimiter) {
        String defaultSideDelimiter = "|";
        String defaultDataDelimiter = "-";
        int indexSideDelimiter = -1;
        int indexDataDelimiter = -1;
        int countLineCharacter;
        StringBuilder formattedDataStringBuilder = new StringBuilder();
        indexDataDelimiter = getIndexDelimiter(arrDataDelimiter, indexDataDelimiter);
        String dataDel = getDelimiter(arrDataDelimiter, defaultDataDelimiter, indexDataDelimiter);
        String dataDelimiter = getNCopyData(countAllLineCharacter, dataDel);
        formattedDataStringBuilder
                .append("\n")
                .append(dataDelimiter)
                .append("\n");
        for (String data : arrData) {
            indexSideDelimiter = getIndexDelimiter(arrSideDelimiter, indexSideDelimiter);
            String sideDelimiter = getDelimiter(arrSideDelimiter, defaultSideDelimiter, indexSideDelimiter);
            countLineCharacter = countAllLineCharacter - sideDelimiter.length() * 2 - 2;
            String[] lines = splitToNChar(data, countLineCharacter);
            for (String line : lines) {
                formattedDataStringBuilder
                        .append(sideDelimiter)
                        .append(" ")
                        .append(line)
                        .append(" ")
                        .append(sideDelimiter)
                        .append("\n");
            }
            indexDataDelimiter = getIndexDelimiter(arrDataDelimiter, indexDataDelimiter);
            dataDelimiter = getNCopyData(countAllLineCharacter, getDelimiter(arrDataDelimiter, defaultDataDelimiter, indexDataDelimiter));
            formattedDataStringBuilder
                    .append(dataDelimiter)
                    .append("\n");

        }
        return formattedDataStringBuilder.toString();
    }


    private static String[] profileToStringChecked(Profile profile, String messageError) {
        List<String> profileToString = new ArrayList<>();
        if (profile.getIdUser() < 0) {
            if (messageError==null) {
                profileToString.add("Низайше кланемся, пользователи не найдены.");
                profileToString.add("Вы можете стать первым/ой!");
            } else {
                profileToString = Arrays.asList(splitToNChar(messageError, (countLineCharacter-4)));
            }
        } else {
            profileToString.add(profile.getName());
            profileToString.add(profile.getDescription());
        }

        return profileToString.toArray(new String[0]);
    }

    private static String getDelimiter(String[] arrDelimiter, String defaultDelimiter, int index) {
        if (index > 0) {
            return arrDelimiter[index];
        } else {
            return defaultDelimiter;
        }
    }

    private static int getIndexDelimiter(String[] arrDelimiter, int index) {
        if (arrDelimiter == null || arrDelimiter.length < 1) {
            return -1;
        } else {
            index++;
            if (arrDelimiter.length <= index) {
                index = 0;
            }
            return index;
        }
    }

    private static String getNCopyData(int count, String data) {
        return String.join("", Collections.nCopies(count, data));
    }

    private static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        int lastIndex = parts.size() - 1;
        if (parts.get(lastIndex).length() < size) {
            int diff = size - parts.get(lastIndex).length();
            String part = parts.get(lastIndex) + getNCopyData(diff, " ");
            parts.set(lastIndex, part);
        }
        return parts.toArray(new String[0]);
    }

}