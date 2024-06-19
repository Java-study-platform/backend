package com.study.core.service.impl;

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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ReactionRepository reactionRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public Message sendMessage(SendMessageModel sendMessageModel, UUID chatId, Jwt user) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        Message message = new Message();
        message.setContent(message.getContent());
        message.setSenderLogin(user.getClaim("preffered_username"));

        if (sendMessageModel.getParentMessageId() != null) {
            Message parentMessage = messageRepository.findById(sendMessageModel.getParentMessageId())
                    .orElseThrow(() -> new MessageNotFoundException(sendMessageModel.getParentMessageId()));
            message.setParentMessage(parentMessage);
            //TODO: Внедрить отправку уведомления тому, на чье сообщение ответили: parentMessage.senderLogin
        }

        message.setChat(chat);
        message.setEventType(MessageEventType.NEW);

        return messageRepository.save(message);
    }

    @Override
    public List<MessageDTO> getChatHistory(UUID id, Jwt user) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException(id));


        return chat.getMessages().stream().map(message -> {
            Reaction reaction = reactionRepository.findByAuthorLoginAndMessage(user.getClaim("preferred_username"), message)
                    .orElse(new Reaction());

            return messageMapper.toDTO(message, reaction.getReactions());
        }).toList();
    }

    @Override
    @Transactional
    public MessageDTO reactMessage(ReactMessageModel reactMessageModel, UUID id, Jwt user) {
        String authorLogin = user.getClaim("preferred_username");

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
    public MessageDTO unreactMessage(UnreactMessageModel unreactMessageModel, UUID id, Jwt user) {
        String authorLogin = user.getClaim("preferred_username");

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
