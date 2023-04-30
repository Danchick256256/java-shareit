package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constants.Constants;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking add(@RequestHeader(Constants.userHeader) Long ownerId, @RequestBody BookingDto bookingDto) {
        return bookingService.add(ownerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable long bookingId,
                                      @RequestHeader(Constants.userHeader) long ownerId,
                                      @RequestParam("approved") boolean approved) {
        return bookingService.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking get(@RequestHeader(Constants.userHeader) int ownerId, @PathVariable int bookingId) {
        return bookingService.get(ownerId, bookingId);
    }

    @GetMapping
    public List<Booking> getAll(@RequestHeader(Constants.userHeader) int ownerId,
                                @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                @RequestParam(value = "from", defaultValue = "0") Long from,
                                @RequestParam(value = "size", defaultValue = "10") Long size) {
        return bookingService.getAll(ownerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwnerId(@RequestHeader(Constants.userHeader) int ownerId,
                                         @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                         @RequestParam(value = "from", defaultValue = "0") Long from,
                                         @RequestParam(value = "size", defaultValue = "10") Long size) {
        return bookingService.getAllByOwnerId(ownerId, state, from, size);
    }



}
