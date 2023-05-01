package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Booking add(long ownerId, BookingDto bookingDto) {
        User user = userRepository.findById(ownerId).orElseThrow(()
                -> new UserNotFoundException(ownerId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .filter(item1 -> !Objects.equals(item1.getOwner(), user.getId()))
                .orElseThrow(() -> new ItemNotFoundException(bookingDto.getItemId()));
        if (!item.isAvailable()) throw new ItemBadRequestException(bookingDto.getItemId());
        return bookingRepository.save(BookingMapper.dtoToBooking(bookingDto, user, item));
    }

    @Override
    public Booking get(long ownerId, long bookingId) {
        userRepository.findById(ownerId).orElseThrow(()
                -> new UserNotFoundException(ownerId));
        return bookingRepository.findById(bookingId)
                .filter(booking -> booking.getBooker().getId() == ownerId || booking.getItem().getOwner() == ownerId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public List<Booking> getAll(long bookerId, BookingState state, Long from, Long size) {
        userRepository.findById(bookerId).orElseThrow(()
                -> new UserNotFoundException(bookerId));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(bookerId, BookingState.WAITING);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(bookerId, BookingState.REJECTED);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, now);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, now, now);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            default:
                throw new BookingNotFoundException(state);
        }
    }

    @Override
    public List<Booking> getAllByOwnerId(long ownerId, BookingState state, Long from, Long size) {
        userRepository.findById(ownerId).orElseThrow(()
                -> new UserNotFoundException(ownerId));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerOrderByStartDesc(ownerId);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(ownerId, BookingState.WAITING);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartAsc(ownerId, BookingState.REJECTED);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(ownerId, now);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartAsc(ownerId, now, now);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(ownerId, now);
                return bookingList.subList(Math.toIntExact(from), (int) (from + size > bookingList.size() ? bookingList.size() : from + size));
            default:
                throw new BookingNotFoundException(state);
        }
    }

    @Override
    public BookingDtoResponse approve(long ownerId, long bookingId, boolean approvedStatus) {
        Booking updatedBooking = bookingRepository.findById(bookingId)
                .filter(b -> b.getItem().getOwner() == ownerId)
                .orElseThrow(() -> new BookingNotFoundException(ownerId));
        if (updatedBooking.getStatus() != BookingState.WAITING) throw new BookingBadRequestException(bookingId);
        BookingState status = approvedStatus ? BookingState.APPROVED : BookingState.REJECTED;
        updatedBooking.setStatus(status);
        return BookingMapper.bookingToResponse(bookingRepository.save(updatedBooking));
    }
}
