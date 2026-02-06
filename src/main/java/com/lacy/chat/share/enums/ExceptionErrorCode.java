package com.lacy.chat.share.enums;

import lombok.Getter;

@Getter
public enum ExceptionErrorCode {

  // Generic errors
  UNKNOWN("An unknown error occurred"),
  UNAUTHORIZED("User is not authorized"),
  ACCESS_DENIED("Access denied"),
  RESOURCE_NOT_FOUND("Resource not found"),
  VALIDATION_FAILED("Validation failed"),
  USER_NOT_FOUND;

  private final String defaultMassage;

  ExceptionErrorCode(String defaultMassage) {
    this.defaultMassage = defaultMassage;
  }

  ExceptionErrorCode() {
    this.defaultMassage = null;
  }
}
