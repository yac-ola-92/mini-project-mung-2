package com.example.mung.repository;

import com.example.mung.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Integer> {
    // 필요한 메서드가 있다면 추가할 수 있습니다.
}
