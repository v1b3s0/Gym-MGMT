package tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ui.AppStyle;

/**
 * Build-time tool (not shipped). Renders ui.AppStyle's dumbbell at several
 * sizes and packs them into a single Windows .ico for jpackage.
 * Run with: ./gradlew generateIcon
 */
public class IconGenerator {
    private static final int[] SIZES = { 16, 24, 32, 48, 64, 128, 256 };
    private static final Path OUTPUT = Path.of("packaging", "windows", "icon.ico");

    public static void main(String[] args) throws IOException {
        // 1. Render each size and PNG-encode it — reusing the app's REAL drawing.
        List<byte[]> pngs = new ArrayList<>();
        for (int size : SIZES) {
            BufferedImage img = AppStyle.createAppIcon(size);
            ByteArrayOutputStream png = new ByteArrayOutputStream();
            ImageIO.write(img, "png", png);
            pngs.add(png.toByteArray());
        }

        // 2. Wrap those PNGs in an .ico container.
        byte[] ico = buildIco(pngs);

        // 3. Write it out, creating packaging/windows/ if needed.
        Files.createDirectories(OUTPUT.getParent());
        Files.write(OUTPUT, ico);
        System.out.println("Wrote " + ico.length + " bytes to " + OUTPUT.toAbsolutePath());
    }

    // An .ico is: a 6-byte header, then one 16-byte directory entry per image,
    // then the raw image bytes. We store each image as PNG (Windows supports it).
    private static byte[] buildIco(List<byte[]> pngs) {
        int count = pngs.size();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // --- ICONDIR header (6 bytes) ---
        writeShortLE(out, 0); // reserved, always 0
        writeShortLE(out, 1); // image type, 1 = icon
        writeShortLE(out, count); // how many images follow

        // Image data starts right after the header + all directory entries.
        int offset = 6 + 16 * count;

        // --- one ICONDIRENTRY (16 bytes) per image ---
        for (int i = 0; i < count; i++) {
            byte[] png = pngs.get(i);
            int size = SIZES[i];
            out.write(size >= 256 ? 0 : size); // width (0 is the code for 256)
            out.write(size >= 256 ? 0 : size); // height (0 is the code for 256)
            out.write(0); // palette color count (0 = no palette)
            out.write(0); // reserved
            writeShortLE(out, 1); // color planes
            writeShortLE(out, 32); // bits per pixel
            writeIntLE(out, png.length); // byte size of this image
            writeIntLE(out, offset); // file offset where this image begins
            offset += png.length;
        }

        // --- the image bytes, in the same order as the entries ---
        for (byte[] png : pngs) {
            out.write(png, 0, png.length);
        }
        return out.toByteArray();
    }

    // .ico stores multi-byte numbers little-endian (lowest byte first).
    private static void writeShortLE(ByteArrayOutputStream o, int v) {
        o.write(v & 0xFF);
        o.write((v >> 8) & 0xFF);
    }

    private static void writeIntLE(ByteArrayOutputStream o, int v) {
        o.write(v & 0xFF);
        o.write((v >> 8) & 0xFF);
        o.write((v >> 16) & 0xFF);
        o.write((v >> 24) & 0xFF);
    }
}
