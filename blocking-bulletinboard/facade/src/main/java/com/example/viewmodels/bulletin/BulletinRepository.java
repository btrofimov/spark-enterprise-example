package com.example.viewmodels.bulletin;

import com.example.infrastructure.ReadOnlyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BulletinRepository extends ReadOnlyRepository<Bulletin, String> {
}
