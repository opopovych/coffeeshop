package com.example.coffeeshop.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

@Service
public class PdfService {
    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generatePdf(Object order, Object settings, Object discount) {
        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("settings", settings);
        context.setVariable("discount", discount);

        // 1. Безпечне читання логотипа (працює і в JAR)
        try (InputStream logoStream = new ClassPathResource("static/images/favicon.png").getInputStream()) {
            byte[] logoBytes = logoStream.readAllBytes();
            String logoBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(logoBytes);
            context.setVariable("logoBase64", logoBase64);
        } catch (Exception e) {
            context.setVariable("logoBase64", "");
        }

        String html = templateEngine.process("admin/invoice-print", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            // 2. Витягуємо шрифт із JAR у тимчасовий файл на диску, щоб бібліотека могла його прочитати
            File tempFontFile;
            try (InputStream fontStream = new ClassPathResource("fonts/DejaVuSans.ttf").getInputStream()) {
                tempFontFile = File.createTempFile("dejavu", ".ttf");
                tempFontFile.deleteOnExit(); // Видалити при закритті програми
                Files.copy(fontStream, tempFontFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            builder.useFont(tempFontFile, "DejaVu Sans");

            builder.withHtmlContent(html, "/");
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Помилка генерації PDF: " + e.getMessage(), e);
        }
    }
}