package com.mk.config.logging;

import org.springframework.util.StopWatch;

public class StopWatchContext {

  private static final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

  public static void start() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatchThreadLocal.set(stopWatch);
  }

  public static void stop() {
    StopWatch stopWatch = stopWatchThreadLocal.get();
    if (stopWatch != null) {
      stopWatch.stop();
    }
  }

  public static void clear() {
    stopWatchThreadLocal.remove();
  }

  public static double getTime() {
    StopWatch stopWatch = stopWatchThreadLocal.get();
    if (stopWatch != null) {
      return stopWatch.getTotalTimeSeconds();
    }
    return 0L;
  }
}
