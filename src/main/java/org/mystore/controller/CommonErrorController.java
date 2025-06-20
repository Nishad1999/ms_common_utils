package org.mystore.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import org.mystore.advisor.ControllerAdvisor;
import org.mystore.dto.RequestWrapperDTO;
import org.mystore.dto.StatusResponse;
import org.mystore.util.CommonRequestWrapperUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonErrorController implements ErrorController {

    @GetMapping("/error")
    public ResponseEntity<RequestWrapperDTO<Object, Object>> errorHandler(HttpServletRequest request) {
        StatusResponse errorResponse = CommonRequestWrapperUtil.buildErrorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(ControllerAdvisor.getRequestWrapperDto(HttpStatus.NOT_FOUND, Collections.singletonList(errorResponse)),
                HttpStatus.NOT_FOUND);
    }

}