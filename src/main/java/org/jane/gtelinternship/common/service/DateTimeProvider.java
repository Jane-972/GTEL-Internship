package org.jane.gtelinternship.common.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DateTimeProvider {
  public Instant getInstant() {
    return Instant.now();
  }
}
