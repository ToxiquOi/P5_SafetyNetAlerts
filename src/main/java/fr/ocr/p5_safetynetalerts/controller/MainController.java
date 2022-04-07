package fr.ocr.p5_safetynetalerts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    private static final String actuatorEndpoint = "/actuator";
    private static final String swaggerEndpoint = "/swagger-ui/index.html#/";

    @ResponseBody
    @GetMapping(path = "/")
    public String home(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String host = request.getServerName();
        String url = "http://" + host + ":9091" + contextPath;

        return "<h2>SafetyNetAlert Endpoints</h2>" + "<ul>" +
                "<li><a href='" + url + actuatorEndpoint + "'>" + url + actuatorEndpoint + "</a></li>" +
                "<li><a href='" + "http://localhost:9090/swagger-ui/index.html" + "'>" + "http://localhost:9090/swagger-ui/index.html" + "</a></li>" +
                "</ul>";
    }
}
