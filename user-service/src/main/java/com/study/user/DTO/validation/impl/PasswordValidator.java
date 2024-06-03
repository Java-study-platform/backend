package com.study.user.DTO.validation.impl;

import com.study.user.DTO.validation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.passay.*;
import com.google.common.base.Joiner;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        return isPasswordValid(password, context);
    }

    @SneakyThrows
    private boolean isPasswordValid(String password, ConstraintValidatorContext context) {
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 2),
                new CharacterRule(EnglishCharacterData.Digit, 4),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        ));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        Joiner.on(",").join(validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}