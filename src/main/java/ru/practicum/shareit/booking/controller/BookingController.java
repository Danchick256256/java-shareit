package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constants.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader(Constants.userHeader) int ownerId, @Valid @RequestBody BookingDto bookingDto) {
        log.info("create.booking.request");
        return bookingService.createBooking(ownerId, bookingDto);
    }

    @GetMapping
    public List<Booking> getAllBookingsWithSize(@RequestHeader(Constants.userHeader) int ownerId,
                                                @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                @RequestParam(value = "from", defaultValue = "0") Long from,
                                                @RequestParam(value = "size", defaultValue = "10") Long size) {
        log.info("get.all.bookings.request");
        return bookingService.getAll(ownerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsByOwnerIdWithSize(@RequestHeader(Constants.userHeader) int ownerId,
                                                         @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                         @RequestParam(value = "from", defaultValue = "0") Long from,
                                                         @RequestParam(value = "size", defaultValue = "10") Long size) {
        log.info("get.all.bookings.request");
        return bookingService.getAllByOwnerId(ownerId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(Constants.userHeader) int ownerId, @PathVariable int bookingId) {
        log.info("get.booking.by.id.request");
        return bookingService.getById(ownerId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateItem(@PathVariable long bookingId,
                                         @RequestHeader(Constants.userHeader) long ownerId,
                                         @RequestParam("approved") boolean approved) {
        log.info("update.booking.request");
        return bookingService.updateBooking(ownerId, bookingId, approved);
    }
}
