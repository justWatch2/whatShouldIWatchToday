package com.example.demo.repository;

import com.example.demo.entity.BeforeEntity;
import com.example.demo.repository.mapping.BeforeMapping;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeforeRepository extends JpaRepository<BeforeEntity, Long> {

}
