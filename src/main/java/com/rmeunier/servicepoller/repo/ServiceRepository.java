package com.rmeunier.servicepoller.repo;

import com.rmeunier.servicepoller.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByUserId(Long id);

    @Transactional
    void deleteByUserId(Long id);
}
