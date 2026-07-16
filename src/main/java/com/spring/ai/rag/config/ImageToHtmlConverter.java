package com.spring.ai.rag.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageToHtmlConverter {

    public static void main(String[] args) {
        // ── Configure these two paths ────────────────────────────────────
        String inputImagePath = "C:\\Users\\mural\\workspace\\spring-ai-RAG-app\\src\\main\\resources\\photos\\doctor-photo.jpg";      // source image file
        String outputHtmlPath = "output.html";      // generated HTML file
        // ──────────────────────────────────────────────────────────────────

        try {
            File imageFile = new File(inputImagePath);

            if (!imageFile.exists()) {
                System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                return;
            }

            // 1. Read raw image bytes
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

            // 2. Encode to Base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // 3. Detect MIME type from file extension (png/jpg/gif/etc.)
            String mimeType = detectMimeType(inputImagePath);

            // 4. Build the HTML content with the image embedded as a data URI
            String html = buildHtml(base64Image, mimeType);

            // 5. Write the HTML file
            try (FileWriter writer = new FileWriter(outputHtmlPath)) {
                writer.write(html);
            }

            System.out.println("HTML file generated: " + new File(outputHtmlPath).getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to convert image to HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String detectMimeType(String filePath) {
        String lower = filePath.toLowerCase();
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".svg"))  return "image/svg+xml";
        return "application/octet-stream"; // fallback
    }

    private static String buildHtml(String base64Image, String mimeType) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>Embedded Image</title>\n" +
                "  <style>\n" +
                "    body { font-family: sans-serif; background:#0d1117; color:#e6edf3; " +
                "display:flex; flex-direction:column; align-items:center; padding:40px; }\n" +
                "    img { max-width:600px; border-radius:8px; border:1px solid #30363d; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <h2>Rendered Image (Base64 Embedded)</h2>\n" +
                "  <img id=\"embeddedImage\" src=\"data:" + mimeType + ";base64," + base64Image + "\">\n" +
                "</body>\n" +
                "</html>";
    }
}
