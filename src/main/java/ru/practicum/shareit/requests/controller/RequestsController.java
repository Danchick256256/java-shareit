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
        log.info(requestsDto.toString());
        return requestsService.createRequest(requestsDto, ownerId);
    }

    @GetMapping
    public List<RequestsResponse> getRequest(@RequestHeader(Constants.userHeader) int ownerId) {
        return requestsService.getRequestByOwnerId(ownerId);
    }

    @GetMapping("/all")
    public List<RequestsResponse> getAllRequest(@RequestHeader(Constants.userHeader) int ownerId,
                                                @RequestParam(value = "from", defaultValue = "0") Long from,
                                                @RequestParam(value = "size", defaultValue = "10") Long size) {
        return requestsService.getAllRequests(ownerId, from, size);
    }

    @GetMapping("{requestId}")
    public RequestsResponse getRequest(@RequestHeader(Constants.userHeader) long userId,
                                       @PathVariable long requestId) {
        return requestsService.getRequestById(requestId, userId);
    }
}
