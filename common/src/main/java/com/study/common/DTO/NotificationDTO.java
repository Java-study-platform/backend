package com.study.common.DTO;

import com.study.common.Enum.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private String title;
    private String content;
    private String userEmail;
    private LocalDateTime createTime;
    private List<NotificationChannel> channelList;
    private Boolean needInHistory;
}

