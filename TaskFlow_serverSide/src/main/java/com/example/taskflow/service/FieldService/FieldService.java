package com.example.taskflow.service.FieldService;

import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.Field.FieldDTO;

@Service
public abstract class FieldService {

    abstract public FieldDTO createField(FieldDTO fieldDTO);

}
