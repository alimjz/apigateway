package com.apigateway.token.dto;

import com.apigateway.token.util.TokenImpl;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.token.Token;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TokenDtoConvertor {


    TokenDtoConvertor instance = Mappers.getMapper(TokenDtoConvertor.class);

    @Mapping(source = "token.digest",target = "digestive")
    TokenImpDto convertTokenImplToDto(TokenImpl token);

    List<TokenImpDto> modelsToDtos(List<TokenImpl> tokenList);

    @InheritInverseConfiguration
    TokenImpl dtoToModel(TokenImpDto tokenImpDto);

}
