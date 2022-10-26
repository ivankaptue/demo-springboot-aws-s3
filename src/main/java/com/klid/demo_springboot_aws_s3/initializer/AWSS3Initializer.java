package com.klid.demo_springboot_aws_s3.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author Ivan Kaptue
 */
public class AWSS3Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(AWSS3Initializer.class);

    public static final String APP_AWS_S3_ACCESS_KEY = "app.aws.s3.access-key";
    public static final String APP_AWS_S3_SECRET_KEY = "app.aws.s3.secret";
    public static final String AWS_ACCESS_KEY_PROPERTY = "aws.accessKeyId";
    public static final String AWS_SECRET_ACCESS_KEY_PROPERTY = "aws.secretAccessKey";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var env = applicationContext.getEnvironment();
        var accessKey = getEnvProperty(env, APP_AWS_S3_ACCESS_KEY, String.class);
        var secret = getEnvProperty(env, APP_AWS_S3_SECRET_KEY, String.class);

        logger.info("access key : " + accessKey);
        logger.info("secret : " + secret);

        System.setProperty(AWS_ACCESS_KEY_PROPERTY, accessKey);
        System.setProperty(AWS_SECRET_ACCESS_KEY_PROPERTY, secret);
    }

    private <T> T getEnvProperty(Environment env, String key, Class<T> clazz) {
        var value = env.getProperty(key, clazz);
        return Objects.requireNonNull(value, String.format("%s not found in application.properties file", key));
    }
}
