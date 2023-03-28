package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingUnknownStateException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingMapper;
import ru.practicum.shareit.item.exception.ItemBadRequestException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class BookingServiceImplementation implements BookingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Override
    public Booking createBooking(long ownerId, BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) throw new BookingBadRequestException(bookingDto.getItemId());
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) throw new BookingBadRequestException(bookingDto.getItemId());
        if (bookingDto.getStart().equals(bookingDto.getEnd())) throw new BookingBadRequestException(bookingDto.getItemId());
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) throw new BookingBadRequestException(bookingDto.getItemId());
        User user = userRepository.findById(ownerId).orElseThrow(()
                -> new UserNotFoundException(ownerId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .filter(item1 -> !Objects.equals(item1.getOwner(), user.getId()))
                .orElseThrow(() -> new ItemNotFoundException(bookingDto.getItemId()));
        if (!item.isAvailable()) throw new ItemBadRequestException(bookingDto.getItemId());
        Booking booking = bookingRepository.save(BookingMapper.dtoToBooking(bookingDto, user, item));
        item.setNextBooking(booking.getId());
        itemRepository.save(item);
        return booking;
    }

    @Override
    public Booking getById(long ownerId, long bookingId) {
        userRepository.findById(ownerId).orElseThrow(()
                -> new UserNotFoundException(ownerId));
        return bookingRepository.findById(bookingId)
                .filter(booking -> booking.getBooker().getId() == ownerId || booking.getItem().getOwner() == ownerId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public Stream<Booking> getAll(long bookerId, BookingState state) {
        if (state.equals(BookingState.UNSUPPORTED_STATUS)) throw new BookingUnknownStateException(state.name());
        userRepository.findById(bookerId).orElseThrow(()
                -> new UserNotFoundException(bookerId));
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId).stream();
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(bookerId, BookingState.WAITING).stream();
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(bookerId, BookingState.REJECTED).stream();
            case FUTURE: // не понимаю почему не выдает то что в тестах
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartAsc(bookerId, now).stream();
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(bookerId, now, now).stream();
            case PAST:
                return bookingRepository.findAllByBookerIdAndStartBeforeOrderByStartAsc(bookerId, now).stream();
            default:
                throw new BookingNotFoundException(state);
        }
    }

    @Override
    public Stream<Booking> getAllBookingsByOwnerId(long ownerId, BookingState state) {
        if (state.equals(BookingState.UNSUPPORTED_STATUS)) throw new BookingUnknownStateException(state.name());
        userRepository.findById(ownerId).orElseThrow(()
                -> new UserNotFoundException(ownerId));
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerOrderByStartDesc(ownerId).stream();
            case WAITING:
                return bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(ownerId, BookingState.WAITING).stream();
            case REJECTED:
                return bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(ownerId, BookingState.REJECTED).stream();
            case FUTURE:
                return bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartAsc(ownerId, now).stream();
            case CURRENT:
                return bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartAsc(ownerId, now, now).stream();
            case PAST:
                return bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartAsc(ownerId, now).stream();
            default:
                throw new BookingNotFoundException(state);
        }
    }

    @Override
    public BookingDtoResponse updateBooking(long ownerId, long bookingId, boolean approvedStatus) {
        Booking updatedBooking = bookingRepository.findById(bookingId)
                .filter(b -> b.getItem().getOwner() == ownerId)
                .orElseThrow(() -> new BookingNotFoundException(ownerId));
        if (updatedBooking.getStatus() != BookingState.WAITING) throw new BookingBadRequestException(bookingId);
        BookingState status = approvedStatus ? BookingState.APPROVED : BookingState.REJECTED;
        Item updatedItem = updatedBooking.getItem();
        updatedItem.setLastBooking(updatedBooking.getId());
        itemRepository.save(updatedItem);
        updatedBooking.setStatus(status);
        return BookingMapper.bookingToResponse(bookingRepository.save(updatedBooking));
    }
}
