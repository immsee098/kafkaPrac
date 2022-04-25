package com.bithumb.msacommunity.service;

import com.bithumb.msacommunity.domain.Reply;
import com.bithumb.msacommunity.repository.ReplyRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    //댓글 저장
    @Override
    public Mono<Reply> saveReply(Reply reply) {
        return this.replyRepository.save(reply);
    }

    //댓글 숨김
    //TODO:: 에러면 없는 댓입니다~ 하고 뱉어내게 하는 부분
    //https://kogle.tistory.com/285
    public Mono hideReply(Integer replyId) {
        return replyRepository.findById(replyId)
                .switchIfEmpty(Mono.error(new RuntimeException(">>>>not found exception")))
                .log()
//                .filter(item -> item.getReplyvisibleyn()==0) //show상태일떄
                .doOnNext(item -> item.setReplyvisibleyn(1))
                .flatMap(item -> replyRepository.save(item))
                .log()
                .onErrorResume(tr -> {
                    return Mono.just(new Reply(-1, -1, -1, "", -1, LocalDateTime.now(), LocalDateTime.now()));
                });
    }

}
