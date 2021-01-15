package org.ruan4261.eiuc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static byte[] getByteData(String path, String cookieString) throws IOException {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(path);
            // cast to HttpUrlConnection
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // settings
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            conn.setRequestProperty("Cookie", cookieString);
            // open
            conn.connect();
            //通过conn取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {
                try (InputStream input = conn.getInputStream()) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream(8192);

                    int read;
                    byte[] bytes = new byte[8192];
                    while ((read = input.read(bytes)) != -1) {
                        output.write(bytes, 0, read);
                    }

                    return output.toByteArray();
                }
            } else {
                throw new IOException(conn.getResponseCode() + " :: " + conn.getResponseMessage());
            }
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

}
