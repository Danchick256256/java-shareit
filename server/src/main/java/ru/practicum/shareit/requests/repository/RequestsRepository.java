package ru.practicum.shareit.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.requests.model.Requests;

import java.util.List;

public interface RequestsRepository extends JpaRepository<Requests, Long> {
    List<Requests> findAllByOwner(long claimantId);
}
