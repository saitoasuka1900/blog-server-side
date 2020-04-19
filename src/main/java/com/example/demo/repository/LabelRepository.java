package com.example.demo.repository;

import com.example.demo.entity.Label;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends CrudRepository<Label, Integer> {
    List<Label> findByBelong(String belong);
    List<Label> findByName(String name);
    List<Label> findByNameAndBelong(String name, String belong);
    Optional<Label> findById(Integer id);
    void deleteById(Integer id);
}
