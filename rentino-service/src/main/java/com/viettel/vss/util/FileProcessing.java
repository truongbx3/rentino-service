package com.viettel.vss.util;


import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileProcessing {

    public static void main(String[] args) throws Exception {
//        Path template = Paths.get("C:\\Users\\ADMIN\\Desktop\\remino\\hơp đồng\\[Mau]_Hop dong cam co_giay to co gia_20251031.docx");      // template đã đổi sang ${...}
        Path template = Paths.get("C:\\Users\\ADMIN\\Desktop\\remino\\tmpFile\\contract_123456785_1765729140750.docx");      // template đã đổi sang ${...}
        Path output   = Paths.get("C:\\Users\\ADMIN\\Desktop\\remino\\tmpFile\\output.docx");

        Map<String, String> vars = new HashMap<>();
        vars.put("${contract_no}", "001");
        vars.put("${pawn_party_name}", "CÔNG TY ABC");
        vars.put("${pawn_party_address}", "123 Nguyễn Trãi, Q1, TP.HCM");
        vars.put("${pawn_party_phone}", "0909123456");
        vars.put("${pawn_party_biz_no}", "0312345678");
        vars.put("${pawn_party_biz_issue_place}", "Sở KHĐT TP.HCM");
        vars.put("${pawn_party_biz_issue_date}", "31/10/2025");
        vars.put("${pawn_party_rep_name}", "Nguyễn Văn A");
        vars.put("${pawn_party_rep_title}", "Giám đốc");

        vars.put("${pawn_party_id_no}", "012345678901");
        vars.put("${pawn_party_id_issue_place}", "Cục CSQLHC");
        vars.put("${pawn_party_id_issue_date}", "01/01/2020");

        vars.put("${pawn_party_bank_account}", "123456789");
        vars.put("${pawn_party_bank_name}", "Vietcombank");

        vars.put("${rentino_phone}", "028-xxxxxxx");
        vars.put("${rentino_fax}", "028-yyyyyyy");
        vars.put("${rentino_rep_name}", "Trần Văn B");
        vars.put("${rentino_rep_title}", "Tổng giám đốc");
        vars.put("${rentino_po_no}", "UQ-01");
        vars.put("${rentino_po_date}", "31/10/2025");
        vars.put("${rentino_po_issuer}", "CÔNG TY CỔ PHẦN RENTINO");

        vars.put("${collateral_total_value}", "1.000.000.000");
        vars.put("${valuation_day}", "31");
        vars.put("${valuation_month}", "10");
        vars.put("${valuation_year}", "2025");
        vars.put("${max_loan_amount}", "700.000.000");

        vars.put("${pawn_party_sign_name}", "CÔNG TY ABC");
        vars.put("${rentino_sign_name}", "Trần Văn B");
        vars.put("${rentino_sign_title}", "Tổng giám đốc");
        vars.put("${secured_contracts_list}", "HĐ-01; HĐ-02; HĐ-03");

        generateFile(template,output,vars);
    }
    public static void generateFile(Path template,Path output, Map<String, String> vars) throws IOException {
        try (InputStream in = Files.newInputStream(template);
             XWPFDocument doc = new XWPFDocument(in)) {

            PoiDocxReplacer.replaceAll(doc, vars);

            try (OutputStream out = Files.newOutputStream(output)) {
                doc.write(out);
            }
        }
    }
    public static Path writeFile(ByteArrayInputStream in, Path outFile) throws Exception {
        Files.createDirectories(outFile.getParent());
        Files.copy(in, outFile, StandardCopyOption.REPLACE_EXISTING);
        return outFile;
    }

    public static Path downloadToTempDir(String fileUrl, String fileName,Path  tempDir) throws Exception {
        Files.createDirectories(tempDir);

        Path outFile = tempDir.resolve(fileName);
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, outFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return outFile;
    }

}
