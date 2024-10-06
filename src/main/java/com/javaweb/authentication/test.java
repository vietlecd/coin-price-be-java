package com.javaweb.authentication;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class test {

    private final Hello hello;

//    @GetMapping("/api")
    public Hello test() {
        hello.setMsg("fesfes");
        hello.setMsg2("joiwef");
        hello.setMsg3("hi");

        return hello;
    }
}
