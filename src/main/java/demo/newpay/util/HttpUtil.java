package demo.newpay.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
    private final static String DEFAULT_ENCODING = "UTF-8";
    private static final String METHOD_POST = "POST";

    public static String postData(String urlStr, String data) throws IOException {
        String response = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(METHOD_POST);
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(CONNECT_TIMEOUT);

            conn.setRequestProperty("content-type", "application/json");

            conn.setRequestProperty("Accept", "text/plain,text/xml,text/javascript,text/html,text/json");
            conn.setRequestProperty("User-Agent", "aop-sdk-java");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
            if (data == null) data = "";
            writer.write(data);
            writer.flush();
            writer.close();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            response = sb.toString();
        } catch (IOException e) {
            logger.error("Error connecting to " + urlStr + ": " + e.getMessage());
            throw e;
        } finally {
            logger.info("请求接口地址:{} , 报文:{} , 返回响应:{}", urlStr, data, response);
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
            }
        }
        return response;
    }

}
