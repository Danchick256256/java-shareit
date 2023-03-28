package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    @Autowired
    private BookingService bookingService;
    private final String userHeader = "X-Sharer-User-Id";
    @PostMapping
    public Booking createBooking(@RequestHeader(userHeader) int ownerId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("create.booking.request");
        return bookingService.createBooking(ownerId, bookingDto);
    }

    @GetMapping
    public List<Booking> getAllBookings(@RequestHeader(userHeader) int ownerId,
                                        @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        log.info("get.all.bookings.request");
        return bookingService.getAll(ownerId, state).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsByOwnerId(@RequestHeader(userHeader) int ownerId,
                                        @RequestParam(value = "state", defaultValue = "ALL") BookingState state) {
        log.info("get.all.bookings.request");
        return bookingService.getAllBookingsByOwnerId(ownerId, state).collect(Collectors.toList());
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(userHeader) int ownerId, @PathVariable int bookingId) {
        log.info("get.booking.by.id.request");
        return bookingService.getById(ownerId, bookingId);
    }
    @PatchMapping("/{bookingId}")
    public Booking updateItem(@PathVariable long bookingId,
                           @RequestHeader(userHeader) long ownerId,
                           @RequestParam("approved") boolean approved) {
        log.info("update.booking.request");
        return bookingService.updateBooking(ownerId, bookingId, approved);
    }
}
