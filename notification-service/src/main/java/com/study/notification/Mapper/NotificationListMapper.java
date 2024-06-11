package com.study.notification.Mapper;

import com.study.notification.DTO.NotificationForUserModel;
import com.study.notification.Entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {NotificationMapper.class})
public interface NotificationListMapper {
    List<NotificationForUserModel> toModelList(List<Notification> list);
}
