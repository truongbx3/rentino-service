package com.viettel.vss.service;

import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

//@Service
public class WordToPdfService {
    @Value("${jod.libreOffice}")
    private static String libreOffice;


    public  static Path convert(Path wordFile, Path pdfFile) throws Exception {
        LocalOfficeManager localOfficeManager = LocalOfficeManager.builder()
                .officeHome(libreOffice)
//                .workingDir(new File("/app/tmp"))
//                .templateProfileDir(new File("/opt/lo-template"))
//                .officeHome("C:\\Program Files\\LibreOffice")
                .install()
                .build();
        try {
            localOfficeManager.start();

            LocalConverter.builder()
                    .officeManager(localOfficeManager)
                    .build()
                    .convert(wordFile.toFile())
                    .to(pdfFile.toFile())
                    .execute();

        } finally {
            localOfficeManager.stop();
        }
        return pdfFile;
    }
//    public static void main(String[] args) throws Exception {
//        Path input   = Paths.get("C:\\Users\\ADMIN\\Desktop\\remino\\tmpFile\\contract_123456785_1765730460441generate.docx");
//        Path output   = Paths.get("C:\\Users\\ADMIN\\Desktop\\remino\\tmpFile\\contract_123456785_1765730460441generate.pdf");
//        convert(input, output);
//    }
}
