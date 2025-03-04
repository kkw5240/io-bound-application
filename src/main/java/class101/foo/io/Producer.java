package class101.foo.io;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Producer {
    private final RabbitTemplate rabbitTemplate;

    public void sendTo(String message) {
        this.rabbitTemplate.convertAndSend("CREATE_POST_QUEUE", message);
    }

}
