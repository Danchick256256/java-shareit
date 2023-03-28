package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemBadRequestException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.CommentMapper;
import ru.practicum.shareit.item.util.ItemMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImplementation implements ItemService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    @Override
    public CommentDto createComment(CommentDto dto, long authorId, long itemId) {
        bookingRepository.findAllByItemIdAndBookerId(itemId, authorId)
                .stream()
                .filter(booking -> booking.getStatus() == BookingState.APPROVED && booking.getStart().isBefore(LocalDateTime.now()))
                .findAny().orElseThrow(() -> new ItemBadRequestException(itemId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        User user = userService.getUserById(authorId);

        return CommentMapper.commentToDto(commentRepository.save(CommentMapper.dtoToComment(dto, item, user)));
    }

    @Override
    public ItemDto createItem(long ownerId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null) throw new ItemBadRequestException("Missed available");
        if (itemDto.getName() == null || itemDto.getName().isBlank()) throw new ItemBadRequestException("Missed name");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) throw new ItemBadRequestException("Missed description");
        if (userService.getUserById(ownerId) == null) throw new UserNotFoundException(ownerId);
        return ItemMapper.itemToDto(
                itemRepository.save(
                        ItemMapper.dtoToItem(
                                ownerId, itemDto)), null, null, null);
    }

    @Override
    public Stream<Item> getAllItemsByOwnerId(long ownerId) {
        return itemRepository.findAllByOwnerOrderByIdAsc(ownerId).stream();/*.map(item -> {
            Optional<Booking> lastBooking = bookingRepository.findById(item.getLastBooking());
            Optional<Booking> nextBooking = bookingRepository.findById(item.getNextBooking());
            return ItemMapper.itemToDto(item, lastBooking.get(), nextBooking.get()) //ADD COMMENTS
        });*/
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        BookingDtoToItem lastBooking = null;
        BookingDtoToItem nextBooking = null;
        Optional<Booking> lastBookingOptional = bookingRepository.findById(item.getLastBooking() == null ? -1 : item.getLastBooking());
        if (lastBookingOptional.isPresent()) lastBooking = BookingMapper.bookingDtoToItem(lastBookingOptional.get());
        Optional<Booking> nextBookingOptional = bookingRepository.findById(item.getNextBooking() == null ? -1 : item.getNextBooking());
        if (nextBookingOptional.isPresent()) nextBooking = BookingMapper.bookingDtoToItem(nextBookingOptional.get());
        return ItemMapper.itemToDto(item, lastBooking, nextBooking, commentRepository.findAllByItemIdOrderByIdAsc(itemId).stream().map(CommentMapper::commentToDto).collect(Collectors.toList()));
    }

    @Override
    public Item updateItem(long ownerId, long itemId, ItemDto itemDto) {
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        if (updatedItem.getOwner() != ownerId) throw new ItemNotFoundException(ownerId);
        if (itemDto.getDescription() != null) updatedItem.setDescription(itemDto.getDescription());
        if (itemDto.getName() != null) updatedItem.setName(itemDto.getName());
        if (itemDto.getAvailable() != null) updatedItem.setAvailable(itemDto.getAvailable());
        return itemRepository.save(updatedItem);
    }

    @Override
    public void removeItem(long ownerId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        if (item.getOwner() == ownerId) {
            itemRepository.deleteById(itemId);
        }
    }

    @Override
    public Stream<Item> searchItems(String text) {
        if (text.isBlank()) {
            return Stream.empty();
        }
        return itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text).stream();
    }
}
