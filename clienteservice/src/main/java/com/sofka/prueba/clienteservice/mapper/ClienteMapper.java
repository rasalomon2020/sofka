package com.sofka.prueba.clienteservice.mapper;

import com.sofka.prueba.clienteservice.dto.ClienteDto;
import com.sofka.prueba.clienteservice.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDto toDto(Cliente cliente);

    Cliente toEntity(ClienteDto clienteDto);
}

