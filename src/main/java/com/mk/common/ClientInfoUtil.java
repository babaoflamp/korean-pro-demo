package com.mk.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;

public class ClientInfoUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientInfoUtil.class);

  /**
   * ip 취득
   * @param HttpServletRequest
   * @return String ip
   */
  public static String getIp(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");
    if ( ip == null ) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if ( ip == null ) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if ( ip == null ) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if ( ip == null ) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null ) {
      ip = request.getRemoteAddr();
    }
    if ( "0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip) ) {
      InetAddress local = null;
      try {
        local = InetAddress.getLocalHost();

      } catch (UnknownHostException e) {
        LOGGER.error(e.getMessage());
      }
      ip = local.getHostAddress();
    }

    return ip;
  }


  /**
   * 브라우저 접속 정보
   * @param HttpServletRequest
   * @return String browser
   */
  public static String getBrowser(HttpServletRequest request) {

    String userAgent = request.getHeader("User-Agent").toUpperCase();
    String browser = "";

    if ( userAgent.indexOf("TRIDENT") > -1 ) { // IE
      browser = "IE";
    } else if ( userAgent.indexOf("EDGE") > -1 || userAgent.indexOf("EDG") > -1) { // Egde
      browser = "Edge";
    } else if ( userAgent.indexOf("WHALE") > -1 ) { // Naver Whale
      browser = "Whale";
    } else if ( userAgent.indexOf("OPERA") > -1 || userAgent.indexOf("OPR") > -1 ) { // Opera
      browser = "Opera";
    } else if ( userAgent.indexOf("FIREFOX") > -1) { // Firefox
      browser = "Firefox";
    } else if ( userAgent.indexOf("SAFARI") > -1 && userAgent.indexOf("CHROME") == -1 ) { // Safari
      browser = "Safari";
    } else if ( userAgent.indexOf("CHROME") > -1 ) { // Chrome
      browser = "Chrome";
    }

    return browser;

  }

  /**
   * 운영체제 반환
   * @param HttpServletRequest
   * @return String os
   */
  public static String getOs(HttpServletRequest request) {

    String agent = request.getHeader("User-Agent");
    String os = "";

    /* [Window]
     *  - Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
     * [Mac]
     *  - Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4.1 Safari/605.1.15
     */
    if ( agent.indexOf("NT 6.0") > -1) {
      os = "Windows Vista/Server 2008";
    } else if ( agent.indexOf("NT 5.2") > -1) {
      os = "Windows Server 2003";
    } else if ( agent.indexOf("NT 5.1") > -1) {
      os = "Windows XP";
    } else if ( agent.indexOf("NT 5.0") > -1) {
      os = "Windows 2000";
    } else if ( agent.indexOf("NT") > -1) {
      os = "Windows NT";
    } else if ( agent.indexOf("9x 4.90") > -1) {
      os = "Windows Me";
    } else if ( agent.indexOf("98") > -1) {
      os = "Windows 98";
    } else if ( agent.indexOf("95") > -1) {
      os = "Windows 95";
    } else if ( agent.indexOf("Win16") > -1) {
      os = "Windows 3.x";
    } else if ( agent.indexOf("Windows") > -1) {
      os = "Windows";
    } else if ( agent.indexOf("Linux") > -1) {
      os = "Linux";
    } else if ( agent.indexOf("Macintosh") > -1) {
      os = "Macintosh";
    }

    return os;
  }

}
