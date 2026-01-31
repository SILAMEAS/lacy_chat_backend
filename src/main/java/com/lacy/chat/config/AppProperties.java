package com.lacy.chat.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppProperties {
  private String jwt_secret;
  private String url_redirect;
  private Long jwt_exp;
  private String cors_origin;
}
