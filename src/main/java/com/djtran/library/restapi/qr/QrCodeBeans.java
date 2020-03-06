package com.djtran.library.restapi.qr;

import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class QrCodeBeans {

    @Bean
    QRCodeWriter defaultQrCodeWriter() {
        return new QRCodeWriter();
    }

    @Bean
    QRCodeReader defaultQrCodeReader() {
        return new QRCodeReader();
    }
}
