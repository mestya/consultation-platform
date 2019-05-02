package com.tju.consultationPlatform.service;

import com.tju.consultationPlatform.domain.Message;
import com.tju.consultationPlatform.repository.MsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MsgRepository msgRepository;

    public List<Message> getMessageByFromId(String from) {
        return msgRepository.findByFrom(from);
    }

    public List<Message> getMessageByToId(String to) {
        return msgRepository.findByTo(to);
    }

    public List<Message> getMessageUnReceive(String to) {
        return msgRepository.getMessageUnReceive(to, 0);
    }

    public List<Message> getMessageByType(int type) {
        return msgRepository.findByType(type);
    }

    public void addMessage(Message message) {
        msgRepository.save(message);

    }

    public void deleteMessage(Message message) {
        msgRepository.deleteById(message.getId());
    }

    public int updateMessage(Message message) {
        return msgRepository.updateMsgFlag(message.getIsTransport(), message.getId());
    }


}
