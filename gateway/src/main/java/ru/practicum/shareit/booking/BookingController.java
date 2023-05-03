package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingUnknownStateException;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.constants.Constants;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(Constants.userHeader) int ownerId,
                                                @Valid @RequestBody BookingDto bookingDto) {
        log.info("create.booking.request");
        if (bookingDto.getStart().equals(bookingDto.getEnd()))
            throw new BookingBadRequestException("end equals start" + bookingDto.getItemId());
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()))
            throw new BookingBadRequestException("start is after end" + bookingDto.getItemId());
        return bookingClient.addBooking(ownerId, bookingDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsWithSize(@RequestHeader(Constants.userHeader) int ownerId,
                                                         @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                         @RequestParam(value = "from", defaultValue = "0") Long from,
                                                         @RequestParam(value = "size", defaultValue = "10") Long size) {
        log.info("get.all.bookings.request");
        if (state.equals(BookingState.UNSUPPORTED_STATUS)) throw new BookingUnknownStateException(state.name());
        if (from == 0 && size == 0) throw new BookingBadRequestException(ownerId);
        if (from < 0 && size < 0) throw new BookingBadRequestException(ownerId);
        return bookingClient.getAll(ownerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwnerIdWithSize(@RequestHeader(Constants.userHeader) int ownerId,
                                                                  @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                                  @RequestParam(value = "from", defaultValue = "0") Long from,
                                                                  @RequestParam(value = "size", defaultValue = "10") Long size) {
        log.info("get.all.bookings.request");
        if (state.equals(BookingState.UNSUPPORTED_STATUS)) throw new BookingUnknownStateException(state.name());
        if (from == 0 && size == 0) throw new BookingBadRequestException(ownerId);
        if (from < 0 && size < 0) throw new BookingBadRequestException(ownerId);
        return bookingClient.getAllByOwnerId(ownerId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(Constants.userHeader) int ownerId, @PathVariable int bookingId) {
        log.info("get.booking.by.id.request");
        return bookingClient.getById(ownerId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateItem(@PathVariable long bookingId,
                                             @RequestHeader(Constants.userHeader) long ownerId,
                                             @RequestParam("approved") boolean approved) {
        log.info("update.booking.request");
        return bookingClient.approve(ownerId, bookingId, approved);
    }
}
