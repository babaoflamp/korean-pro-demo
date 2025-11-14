package com.mk.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Base64ToFileConverter {

  /**
   * Base64 문자열을 디코딩하여 오디오 파일로 저장
   *
   * @param
   * @return
   */
  public MultipartFile base64ToFile(String base64Data, String originalFileName) throws IOException {

    String contentType = extractContentType(base64Data);
    String base64Content = extractBase64Content(base64Data);

    // Base64 데이터를 디코딩하여 바이트 배열로 변환
    byte[] decodedData = Base64.getDecoder().decode(base64Content);

    return new MultipartFile() {
      @Override
      public String getName() {
        return "file";
      }

      @Override
      public String getOriginalFilename() {
        return originalFileName;
      }

      @Override
      public String getContentType() {
        return contentType; // Base64에서 추출된 contentType 사용
      }

      @Override
      public boolean isEmpty() {
        return decodedData.length == 0;
      }

      @Override
      public long getSize() {
        return decodedData.length;
      }

      @Override
      public byte[] getBytes() throws IOException {
        return decodedData;
      }

      @Override
      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(decodedData);
      }

      @Override
      public void transferTo(File dest) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(dest)) {
          outputStream.write(decodedData);
        }
      }
    };
  }

  // Base64 문자열에서 contentType 추출하는 메소드
  private static String extractContentType(String base64Data) {
    // Base64가 data URL 형식일 경우 contentType을 추출 (예: data:image/png;base64,...)
    Pattern pattern = Pattern.compile("data:(.*?);base64,");
    Matcher matcher = pattern.matcher(base64Data);

    if (matcher.find()) {
      return matcher.group(1); // contentType 부분을 반환
    } else {
      return "application/octet-stream"; // 기본값
    }
  }

  // Base64 문자열에서 실제 콘텐츠만 추출하는 메소드
  private static String extractBase64Content(String base64Data) {
    // `data:<contentType>;base64,` 부분을 제거하고 순수 Base64 데이터만 반환
    int base64Index = base64Data.indexOf("base64,");
    if (base64Index != -1) {
      return base64Data.substring(base64Index + 7); // 'base64,' 이후 부분을 추출
    }
    return base64Data; // 기본적으로 Base64로 되어 있으면 그대로 반환
  }

}
