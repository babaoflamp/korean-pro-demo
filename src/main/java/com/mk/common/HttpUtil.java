package com.mk.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpUtil {

  private static Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

  private final static String GET = "GET";
  private final static String POST = "POST";
  private final static String PATCH = "PATCH";
  private final static String PUT = "PUT";

  /**
   * HTTP(S) 요청 실행
   *
   * @param method HTTP(S) 메서드 (GET, POST, PUT, PATCH, DELETE)
   * @param url 요청 URL
   * @param params 파라미터
   * @return String
   * @throws IOException
   * @throws NoSuchAlgorithmException
   * @throws KeyManagementException
   */
  public static String executeRequest(String method, String url, Map<String, Object> headers,
      Map<String, Object> params) throws IOException, NoSuchAlgorithmException, KeyManagementException {

    // url 확인
    if (url == null || url.isBlank())
      throw new IllegalArgumentException("[HttpUtil] URL cannot be null or empty");

    // url 설정
    StringBuilder requestUrl = new StringBuilder(url);

    ObjectMapper mapper = new ObjectMapper();
    String body = mapper.writeValueAsString(params);

    // connection 설정
    HttpURLConnection con = null;

    if (url.startsWith("https")) {
      // https일 시 SSL 검증 비활성화
      disableSSLVerification();
      con = (HttpsURLConnection) new URL(requestUrl.toString()).openConnection();

    } else {
      con = (HttpURLConnection) new URL(requestUrl.toString()).openConnection();
    }

    // header 설정
    if (headers != null && !headers.isEmpty()) {
      for (Entry<String, Object> entry : headers.entrySet())
        con.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
    }

    // method 설정
    con.setRequestMethod(method);

    if ( POST.equals(method) || PATCH.equals(method) || PUT.equals(method) ) {
      con.setDoOutput(true);    // 요청 본문을 전송할 수 있도록 설정
      try (OutputStream os = con.getOutputStream()) {
          byte[] input = body.getBytes("UTF-8");
          os.write(input, 0, input.length);
      }
    }

    // 파라미터 설정
    String parameter = buildString(params);
    // GET url에 파라미터 추가
    if (GET.equals(method) && !parameter.isBlank())
      requestUrl.append((url.contains("?")) ? "&" : "?").append(parameter);

    // TimeOut 설정 (5초)
    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);

    // 응답 설정
    StringBuilder response = new StringBuilder();
    BufferedReader br = null;

    try {
      br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;

      while ((inputLine = br.readLine()) != null)
        response.append(inputLine);

      return response.toString();

    } catch (IOException e) {
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      StringBuilder errorResponse = new StringBuilder();
      String errorInputLine;

      while ((errorInputLine = errorReader.readLine()) != null)
        errorResponse.append(errorInputLine);

      throw new IOException("HTTP Method: " + method + " request failed: " + con.getResponseCode()
          + " " + con.getResponseMessage() + "\nError response: " + errorResponse.toString(), e);

    } finally {
      if (br != null)
        br.close();

      if (con != null)
        con.disconnect();
    }


  }

  // SSL 검증 비활성화 메서드
  private static void disableSSLVerification() {

    try {
      // SSLContext를 TLS로 설정
      SSLContext sslContext = SSLContext.getInstance("TLS");

      // 모든 인증서를 신뢰하는 TrustManager 설정
      TrustManager[] trustAllCertificates = new TrustManager[] {new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null; // 모든 인증서를 허용
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
          // 클라이언트 인증서 검증을 하지 않음
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
          // 서버 인증서 검증을 하지 않음
        }
      }};

      // SSLContext 초기화
      sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

      // HttpsURLConnection에 SSL 소켓 팩토리 설정
      HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

      // 호스트 검증 비활성화 (hostname verifier)
      HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Map 파라미터 문자열 변환
   *
   * @param params 파라미터
   * @return String
   * @throws UnsupportedEncodingException
   */
  private static String buildString(Map<String, Object> params)
      throws UnsupportedEncodingException {

    StringBuilder parameter = new StringBuilder();

    if (params != null && !params.isEmpty()) {
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        if (entry.getValue() != null) {
          parameter.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          parameter.append("=");
          parameter.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
          parameter.append("&");
        }
      }
      parameter.deleteCharAt(parameter.length() - 1);
    }

    return parameter.toString();
  }

}
