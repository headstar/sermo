package demo.web;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.headstartech.sermo.persist.CachePersist;
import com.headstartech.sermo.statemachine.StateMachineDeleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.kryo.MessageHeadersSerializer;
import org.springframework.statemachine.kryo.StateMachineContextSerializer;
import org.springframework.statemachine.kryo.UUIDSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class KryoCachePersist <S, E> implements StateMachinePersist<S, E, String>, StateMachineDeleter<String> {

    private static final Logger log = LoggerFactory.getLogger(CachePersist.class);

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {

        @SuppressWarnings("rawtypes")
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.addDefaultSerializer(StateMachineContext.class, new StateMachineContextSerializer());
            kryo.addDefaultSerializer(MessageHeaders.class, new MessageHeadersSerializer());
            kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
            return kryo;
        }
    };

    private final Cache cache;

    public KryoCachePersist(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void write(StateMachineContext<S, E> context, String contextObj) {
        log.trace("Writing state machine context: contextObj={}, context={}", contextObj, context);
        cache.put(contextObj, serialize(context));
    }

    @SuppressWarnings("unchecked")
    @Override
    public StateMachineContext<S, E> read(String contextObj) {
        log.debug("Reading state machine context: contextObj={}", contextObj);
        StateMachineContext<S, E> context = deserialize(cache.get(contextObj, byte[].class));
        log.trace("State machine context read: contextObj={}, context={}", contextObj, context);
        return context;

    }

    @Override
    public void delete(String contextObj) {
        log.debug("Deleting state machine context: contextObj={}", contextObj);
        cache.evictIfPresent(contextObj);
    }

    private byte[] serialize(StateMachineContext<S, E> context) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        kryo.writeObject(output, context);
        output.close();
        return out.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private StateMachineContext<S, E> deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Input input = new Input(in);
        return kryo.readObject(input, StateMachineContext.class);
    }
}
