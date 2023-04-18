package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;

import java.util.List;

public interface RequestsService {
    RequestsResponse createRequest(RequestsDto requestsDto, long userId);

    List<RequestsResponse> getRequestByOwnerId(long userId);

    List<RequestsResponse> getRequestsByOwnerId(long userId);

    List<RequestsResponse> getRequestsByOwnerIdWithSize(long userId, long from, long size);

    RequestsResponse getRequestById(long id, long userId);

    void deleteRequest(long id);

    List<RequestsResponse> getAllRequests(long userId);

    List<RequestsResponse> getAllRequestsWithSize(long userId, long from, long size);

}
