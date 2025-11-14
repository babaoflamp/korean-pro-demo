package com.mk.common;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.mk.config.exception.ApiException;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.Exceptions;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Component
public class WebClientUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebClientUtil.class);

  private static WebClient webClient;

  static {
    // Network connect, read, write timeout 설정
    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 연결 타임아웃
        .responseTimeout(Duration.ofSeconds(10))              // 응답 타임아웃
        .doOnConnected(conn -> conn
            .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
            .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)))
        .secure(sslContextSpec -> {
          try {
            sslContextSpec.sslContext(
            SslContextBuilder.forClient()
              .trustManager((X509TrustManager) null) // 모든 인증서 신뢰
              .build());
          } catch (SSLException e) {
            e.printStackTrace();
          }
        });

    // WebClient 인스턴스 생성
    webClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))  // 연결 풀링 사용
        .build();
  }

  public static ApiResponse<String> makeRequest(String method, String url, Map<String, String> headers, Map<String, Object> body) {

    WebClient.RequestBodySpec requestSpec = webClient
        .method(HttpMethod.valueOf(method))
        .uri(url);

      // headers 추가
      headers.forEach(requestSpec::header);

      // HTTP request
      return requestSpec
          .bodyValue(body)
          .retrieve()
          .toEntity(String.class)
          .timeout(Duration.ofSeconds(12))  // request, response 처리 시간을 지정한 시간내에 완료하지 못했을 시 타임아웃
          .retryWhen(
              Retry.backoff(3, Duration.ofSeconds(5))  // 실패 시 5초 간격으로 최대 3번 재시도
              .doBeforeRetry(retrySignal -> {
                  LOGGER.info("재시도 발생, 시도 횟수: {}", retrySignal.totalRetriesInARow());
              })
          )
          .onErrorMap(e -> {
              Throwable unwrapped = Exceptions.unwrap(e);
              String className = unwrapped.getClass().getName();
              LOGGER.error("error 원인 >>>>>> {}", className);

              // 재시도가 모두 소진된 경우
              if ("reactor.core.Exceptions$RetryExhaustedException".equals(className))
                  LOGGER.error("Retries exhausted");

              return new ApiException(ApiResponse.of(HttpStatus.REQUEST_TIMEOUT, null));
          })
          .map(response -> {
            // 응답이 성공적이면 ApiResponse로 반환
            if ( response.getStatusCode().is2xxSuccessful() ) {
                return ApiResponse.of(HttpStatus.OK, response.getBody());
            } else {
                return ApiResponse.of(HttpStatus.BAD_REQUEST, response.getBody()); // 실패
            }
        })
        .defaultIfEmpty(ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "No response"))
        .block();
    }

}
