package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.requests.dto.RequestsDto;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestsController {
    private final RequestsClient requestsClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(Constants.userHeader) int ownerId, @RequestBody RequestsDto requestsDto) {
        return requestsClient.addRequest(ownerId, requestsDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequest(@RequestHeader(Constants.userHeader) int ownerId) {
        return requestsClient.getAll(ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(@RequestHeader(Constants.userHeader) int ownerId,
                                                @RequestParam(value = "from", defaultValue = "0") Long from,
                                                @RequestParam(value = "size", defaultValue = "10") Long size) {
        return requestsClient.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(Constants.userHeader) long userId,
                                             @PathVariable long requestId) {
        return requestsClient.getById(userId, requestId);
    }
}
