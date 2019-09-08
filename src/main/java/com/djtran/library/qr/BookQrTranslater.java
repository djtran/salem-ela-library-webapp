package com.djtran.library.qr;

import com.amazonaws.util.Base64;
import com.djtran.library.dom.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BookQrTranslater {

    private static final Logger log = LoggerFactory.getLogger(BookQrTranslater.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private final int sideLength;
    private final QRCodeWriter writer;
    private final QRCodeReader reader;

    public BookQrTranslater(int sideLength,
                            QRCodeWriter writer,
                            QRCodeReader reader) {
        this.sideLength = sideLength;
        this.writer = writer;
        this.reader = reader;
    }

    public String generateQrCode(Book b) {
        try {
            String encodeToQr = mapper.writeValueAsString(b);
            BitMatrix bitMatrix = writer.encode(encodeToQr, BarcodeFormat.QR_CODE, sideLength, sideLength);
            ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOut);
            return Base64.encodeAsString(pngOut.toByteArray());
        } catch (WriterException e) {
            log.error("Could not generate QR code for {} : {}", b, e);
        } catch (IOException e) {
            log.error("Could not write QR bitMatrix to bytestream");
        }
        return null;
    }

    public Book readQrCode(String encodedImg) throws IOException, FormatException, ChecksumException, NotFoundException {
        byte[] decoded = Base64.decode(encodedImg);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decoded));

        //debug
        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(bufferedImage)));
        frame.pack();
        frame.setVisible(true);


        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = reader.decode(bitmap);
        return mapper.readValue(result.getText(), Book.class);
    }
}
