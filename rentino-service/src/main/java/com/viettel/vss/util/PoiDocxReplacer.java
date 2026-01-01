package com.viettel.vss.util;

import org.apache.poi.xwpf.usermodel.*;
import java.util.*;

public class PoiDocxReplacer {
    public static void replaceAll(XWPFDocument doc, Map<String, String> vars) {
        // body
        replaceInParagraphs(doc.getParagraphs(), vars);

        // tables
        for (XWPFTable t : doc.getTables()) replaceInTable(t, vars);

        // header/footer (nếu có)
        for (XWPFHeader h : doc.getHeaderList()) replaceInParagraphs(h.getParagraphs(), vars);
        for (XWPFFooter f : doc.getFooterList()) replaceInParagraphs(f.getParagraphs(), vars);
    }

    private static void replaceInTable(XWPFTable table, Map<String, String> vars) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                replaceInParagraphs(cell.getParagraphs(), vars);
                for (XWPFTable nested : cell.getTables()) replaceInTable(nested, vars);
            }
        }
    }

    private static void replaceInParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> vars) {
        for (XWPFParagraph p : paragraphs) replaceInParagraphKeepFormat(p, vars);
    }

    // Giữ format: chỉ sửa text của các run liên quan, không xoá run
    private static void replaceInParagraphKeepFormat(XWPFParagraph p, Map<String, String> vars) {
        List<XWPFRun> runs = p.getRuns();
        if (runs == null || runs.isEmpty()) return;

        boolean changed;
        do {
            changed = false;

            StringBuilder full = new StringBuilder();
            int n = runs.size();
            int[] runStart = new int[n];
            int[] runEnd = new int[n];

            for (int i = 0; i < n; i++) {
                runStart[i] = full.length();
                full.append(getRunText(runs.get(i)));
                runEnd[i] = full.length();
            }

            String fullText = full.toString();
            if (fullText.isEmpty()) return;

            for (var e : vars.entrySet()) {
                String key = e.getKey();                 // ví dụ "${contractNo}"
                String val = e.getValue() == null ? "" : e.getValue();

                int idx = fullText.indexOf(key);
                if (idx < 0) continue;

                int start = idx;
                int end = idx + key.length(); // exclusive

                int startRun = findRun(runStart, runEnd, start);
                int endRun = findRun(runStart, runEnd, end - 1);
                if (startRun < 0 || endRun < 0) continue;

                XWPFRun rStart = runs.get(startRun);
                XWPFRun rEnd = runs.get(endRun);

                String startText = getRunText(rStart);
                String endText = getRunText(rEnd);

                int startOffset = start - runStart[startRun];
                int endOffsetInEndRun = end - runStart[endRun];

                String prefix = startText.substring(0, Math.max(0, startOffset));
                String suffix = endText.substring(Math.min(endText.length(), Math.max(0, endOffsetInEndRun)));

                if (startRun == endRun) {
                    setRunText(rStart, prefix + val + suffix);
                } else {
                    setRunText(rStart, prefix + val);
                    for (int i = startRun + 1; i < endRun; i++) setRunText(runs.get(i), "");
                    setRunText(rEnd, suffix);
                }

                changed = true;
                break; // rebuild lại mapping rồi thay tiếp
            }

        } while (changed);
    }

    private static int findRun(int[] runStart, int[] runEnd, int pos) {
        for (int i = 0; i < runStart.length; i++) {
            if (pos >= runStart[i] && pos < runEnd[i]) return i;
        }
        return -1;
    }

    private static String getRunText(XWPFRun run) {
        String t = run.getText(0);
        return t == null ? "" : t;
    }

    private static void setRunText(XWPFRun run, String text) {
        run.setText(text == null ? "" : text, 0);
    }
}
