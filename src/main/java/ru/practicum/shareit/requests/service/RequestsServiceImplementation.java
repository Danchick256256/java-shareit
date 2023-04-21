package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;
import ru.practicum.shareit.requests.exception.RequestsBadRequestException;
import ru.practicum.shareit.requests.exception.RequestsNotFoundException;
import ru.practicum.shareit.requests.model.Requests;
import ru.practicum.shareit.requests.repository.RequestsRepository;
import ru.practicum.shareit.requests.util.RequestMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestsServiceImplementation implements RequestsService {
    private final RequestsRepository requestsRepository;

    private final ItemRepository itemRepository;

    private final UserService userService;

    @Override
    public RequestsResponse createRequest(RequestsDto requestsDto, long userId) {
        if (userService.getUserById(userId) == null) throw new UserNotFoundException(userId);
        if (requestsDto.getDescription() == null || requestsDto.getDescription().isBlank())
            throw new RequestsBadRequestException(userId);
        Requests requests = requestsRepository.save(RequestMapper.dtoToRequest(requestsDto, userId));
        List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
        return RequestMapper.requestToResponse(requests, items);
    }

    @Override
    public List<RequestsResponse> getRequestByOwnerId(long userId) {
        if (userService.getUserById(userId) == null) throw new UserNotFoundException(userId);
        return requestsRepository.findAllByOwner(userId).stream().map(requests -> {
            List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
            return RequestMapper.requestToResponse(requests, items);
        }).collect(Collectors.toList());
    }

    @Override
    public RequestsResponse getRequestById(long id, long userId) {
        if (userService.getUserById(userId) == null) throw new UserNotFoundException(userId);
        return requestsRepository.findById(id).map(requests -> {
            List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
            return RequestMapper.requestToResponse(requests, items);
        }).orElseThrow(() -> new RequestsNotFoundException(id));
    }

    @Override
    public List<RequestsResponse> getRequestsByOwnerId(long userId) {
        List<RequestsResponse> responses = requestsRepository.findAllByOwner(userId).stream().map(requests -> {
            if (requests.getOwner() != userId) {
                List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
                return RequestMapper.requestToResponse(requests, items);
            } else {
                return null;
            }
        }).collect(Collectors.toList());
        return requestsRepository.findAllByOwner(userId).stream().filter(requests -> requests.getOwner() != userId).map(requests -> {
            List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
            return RequestMapper.requestToResponse(requests, items);
        }).collect(Collectors.toList());
    }

    @Override
    public List<RequestsResponse> getRequestsByOwnerIdWithSize(long userId, long from, long size) {
        if (from == 0 && size == 0) throw new RequestsBadRequestException(userId);
        if (from < 0 && size < 0) throw new RequestsBadRequestException(userId);
        List<RequestsResponse> requestsList = requestsRepository.findAllByOwner(userId).stream().filter(requests -> requests.getOwner() != userId).map(requests -> {
            List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
            return RequestMapper.requestToResponse(requests, items);
        }).collect(Collectors.toList());
        return requestsList.subList((int) (from), (int) (from + size > requestsList.size() ? requestsList.size() : from + size));
    }

    @Override
    public List<RequestsResponse> getAllRequests(long userId) {
        return requestsRepository.findAll().stream().filter(requests -> requests.getOwner() != userId).map(requests -> {
            List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
            return RequestMapper.requestToResponse(requests, items);
        }).collect(Collectors.toList());
    }

    @Override
    public List<RequestsResponse> getAllRequestsWithSize(long userId, long from, long size) {
        if (from == 0 && size == 0) throw new RequestsBadRequestException(userId);
        if (from < 0 && size < 0) throw new RequestsBadRequestException(userId);
        List<RequestsResponse> requestsList = requestsRepository.findAll().stream().filter(requests -> requests.getOwner() != userId).map(requests -> {
            List<Item> items = itemRepository.findAllByRequestOrderByIdAsc(requests.getId());
            return RequestMapper.requestToResponse(requests, items);
        }).collect(Collectors.toList());
        return requestsList.subList((int) (from), (int) (from + size > requestsList.size() ? requestsList.size() : from + size));
    }

    @Override
    public void deleteRequest(long id) {
        requestsRepository.deleteById(id);
    }
}
