package org.example.com.family.accounting.utils;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageSourceHelper {
    @Inject
    private MessageSource injectedMessageSource;

    private static MessageSource messageSource;

    @Value("${language}")
    private String userLanguage;

    private static Locale userLocale;

    /**
     * Retrieves message value corresponding to the provided message key.
     * This method is used for retrieving message that doesn't contain arguments.
     *
     * @param messageKey the provided message key
     * @return the message value
     */
    public static String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, userLocale);
    }

    /**
     * Retrieves message value corresponding to the provided message key.
     * This method is used for retrieving message that contains arguments.
     *
     * @param messageKey the provided message key
     * @param args the array of message arguments
     * @return the message value
     */
    public static String getMessage(String messageKey, Object[] args) {
        return messageSource.getMessage(messageKey, args, userLocale);
    }

    @PostConstruct
    public void postConstruct() {
        messageSource = injectedMessageSource;
        userLocale = Locale.of(userLanguage);
    }
}
