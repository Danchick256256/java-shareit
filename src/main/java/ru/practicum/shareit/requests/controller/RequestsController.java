package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.requests.dto.RequestsDto;
import ru.practicum.shareit.requests.dto.RequestsResponse;
import ru.practicum.shareit.requests.service.RequestsService;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestsController {
    private final RequestsService requestsService;

    @PostMapping
    public RequestsResponse createRequest(@RequestHeader(Constants.userHeader) int ownerId, @RequestBody RequestsDto requestsDto) {
        return requestsService.createRequest(requestsDto, ownerId);
    }

    @GetMapping
    public List<RequestsResponse> getRequest(@RequestHeader(Constants.userHeader) int ownerId) {
        return requestsService.getRequestByOwnerId(ownerId);
    }

    @GetMapping("/all")
    public List<RequestsResponse> getAllRequest(@RequestHeader(Constants.userHeader) int ownerId,
                                                @RequestParam(value = "from", defaultValue = "-256") Long from,
                                                @RequestParam(value = "size", defaultValue = "-256") Long size) {
        if (from == -256 || size == -256) {
            return requestsService.getAllRequests(ownerId);
        } else {
            return requestsService.getAllRequestsWithSize(ownerId, from, size);
        }

    }

    @GetMapping("{requestId}")
    public RequestsResponse getRequest(@RequestHeader(Constants.userHeader) long userId,
                                       @PathVariable long requestId) {
        return requestsService.getRequestById(requestId, userId);
    }
}
