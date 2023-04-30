package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    @Autowired
    public BookingClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                    .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/bookings"))
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                    .build()
        );
    }

    public ResponseEntity<Object> addBooking(long userId, BookingDto booking) {
        return post("", userId, null, booking);
    }

    public ResponseEntity<Object> getAll(long userId, BookingState state, Long from, Long size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwnerId(long userId, BookingState state, Long from, Long size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(long userId, long bookingId) {
        return get("/" + bookingId, userId, null);
    }

    public ResponseEntity<Object> approve(long userId, long bookingId, boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null, null);
    }
}
