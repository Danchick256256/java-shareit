package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;

import java.util.List;

public interface RequestsService {
    RequestsResponse createRequest(RequestsDto requestsDto, long userId);

    List<RequestsResponse> getRequestByOwnerId(long userId);

    List<RequestsResponse> getRequestsByOwnerId(long userId);

    RequestsResponse getRequestById(long id, long userId);

    void deleteRequest(long id);

    List<RequestsResponse> getAllRequests(long userId, long from, long size);
}
