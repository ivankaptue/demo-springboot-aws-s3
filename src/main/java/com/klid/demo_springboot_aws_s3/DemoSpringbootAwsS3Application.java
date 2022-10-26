package com.klid.demo_springboot_aws_s3;

import com.klid.demo_springboot_aws_s3.initializer.AWSS3Initializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DemoSpringbootAwsS3Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoSpringbootAwsS3Application.class)
            .initializers(new AWSS3Initializer())
            .run(args);
    }
}
