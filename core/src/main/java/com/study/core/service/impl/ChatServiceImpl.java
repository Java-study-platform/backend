package com.study.core.service.impl;

import com.study.common.DTO.UserDto;
import com.study.core.dto.Message.MessageDTO;
import com.study.core.dto.Message.ReactMessageModel;
import com.study.core.dto.Message.SendMessageModel;
import com.study.core.dto.Message.UnreactMessageModel;
import com.study.core.enums.MessageEventType;
import com.study.core.exceptions.Chat.ChatNotFoundException;
import com.study.core.exceptions.Message.MessageAndChatMissmatchException;
import com.study.core.exceptions.Message.MessageNotFoundException;
import com.study.core.exceptions.Message.ReactionAlreadyExistsException;
import com.study.core.exceptions.Message.ReactionNotFoundException;
import com.study.core.mapper.MessageMapper;
import com.study.core.models.Chat;
import com.study.core.models.Message;
import com.study.core.models.Reaction;
import com.study.core.repository.ChatRepository;
import com.study.core.repository.MessageRepository;
import com.study.core.repository.ReactionRepository;
import com.study.core.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.study.common.Constants.Consts.USERNAME_CLAIM;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ReactionRepository reactionRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public Message sendMessage(SendMessageModel sendMessageModel, UUID chatId, UserDto user) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
        log.info(user.toString());
        log.info(sendMessageModel.toString());
        Message message = new Message();
        message.setContent(sendMessageModel.getContent());
        message.setSenderLogin(user.getUsername());

        if (sendMessageModel.getParentMessageId() != null) {
            Message parentMessage = messageRepository.findById(sendMessageModel.getParentMessageId())
                    .orElseThrow(() -> new MessageNotFoundException(sendMessageModel.getParentMessageId()));
            if (parentMessage.getParentMessage() != null){
                message.setParentMessage(parentMessage.getParentMessage());
            }
            else{
                message.setParentMessage(parentMessage);
            }
        }

        message.setChat(chat);
        message.setEventType(MessageEventType.NEW);

        return messageRepository.save(message);
    }

    @Override
    public List<MessageDTO> getChatHistory(UUID id, Jwt user) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException(id));
        List<Message> messages = messageRepository.findByChatAndParentMessageIsNull(chat);
        List<MessageDTO> dtos = new ArrayList<>();
        for (Message message : messages) {
            MessageDTO dto = messageMapper.toDTO(message);
            for (Message reply : message.getReplies()){
                Reaction reaction = reactionRepository.findByAuthorLoginAndMessage(user.getClaim(USERNAME_CLAIM), message)
                        .orElse(new Reaction());
                dto.getReplies().add(messageMapper.toDTO(reply, reaction.getReactions()));
            }
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    @Transactional
    public MessageDTO reactMessage(ReactMessageModel reactMessageModel, UUID id, UserDto user) {
        String authorLogin = user.getUsername();

        Message message = messageRepository.findById(reactMessageModel.getMessageId())
                .orElseThrow(() -> new MessageNotFoundException(reactMessageModel.getMessageId()));

        if (!message.getChat().getId().equals(id)) {
            throw new MessageAndChatMissmatchException(reactMessageModel.getMessageId(), id);
        }

        Reaction reaction = reactionRepository.findByAuthorLoginAndMessage(authorLogin, message)
                .orElse(new Reaction());

        reaction.setMessage(message);
        reaction.setAuthorLogin(authorLogin);

        if (reaction.getReactions().contains(reactMessageModel.getReactionType())) {
            throw new ReactionAlreadyExistsException(message.getId(), reactMessageModel.getReactionType().getPrettyName());
        } else {
            reaction.getReactions().add(reactMessageModel.getReactionType());
        }

        message.getReactions().merge(reactMessageModel.getReactionType(), 1, Integer::sum);
        message.setEventType(MessageEventType.UPDATE);

        Reaction newReaction = reactionRepository.save(reaction);

        return messageMapper.toDTO(messageRepository.save(message), newReaction.getReactions());
    }

    @Override
    @Transactional
    public MessageDTO unreactMessage(UnreactMessageModel unreactMessageModel, UUID id, UserDto user) {
        String authorLogin = user.getUsername();

        Message message = messageRepository.findById(unreactMessageModel.getMessageId())
                .orElseThrow(() -> new MessageNotFoundException(unreactMessageModel.getMessageId()));

        if (!message.getChat().getId().equals(id)) {
            throw new MessageAndChatMissmatchException(unreactMessageModel.getMessageId(), id);
        }

        Reaction reaction = reactionRepository.findByAuthorLoginAndMessage(authorLogin, message)
                .orElseThrow(() -> new ReactionNotFoundException(message.getId(), unreactMessageModel.getReactionType().getPrettyName()));

        reaction.setMessage(message);
        reaction.setAuthorLogin(authorLogin);

        if (!reaction.getReactions().contains(unreactMessageModel.getReactionType())) {
            throw new ReactionNotFoundException(message.getId(), unreactMessageModel.getReactionType().getPrettyName());
        } else {
            reaction.getReactions().remove(unreactMessageModel.getReactionType());
        }

        message.getReactions().merge(unreactMessageModel.getReactionType(), -1, Integer::sum);
        message.setEventType(MessageEventType.UPDATE);

        Reaction newReaction = reactionRepository.save(reaction);

        return messageMapper.toDTO(messageRepository.save(message), newReaction.getReactions());
    }
}
