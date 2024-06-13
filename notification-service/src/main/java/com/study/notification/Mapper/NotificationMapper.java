package com.study.notification.Mapper;

import com.study.common.DTO.NotificationDTO;
import com.study.notification.DTO.NotificationForUserModel;
import com.study.notification.Entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationForUserModel toDTO(Notification notification);
}
