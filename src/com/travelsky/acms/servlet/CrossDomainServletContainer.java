package com.travelsky.acms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class CrossDomainServletContainer extends ServletContainer {
  private static final long serialVersionUID = -227602337644961064L;

  public void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    response.setHeader("Access-Control-Allow-Origin", "*");
    super.service(request, response);
  }
}
