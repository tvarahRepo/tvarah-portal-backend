package com.tvarah.model.converter;

import com.tvarah.model.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : UserStatus.fromDbValue(dbValue);
    }
}
