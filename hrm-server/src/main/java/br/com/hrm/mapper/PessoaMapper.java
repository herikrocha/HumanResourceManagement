package br.com.hrm.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import br.com.hrm.dto.PessoaDTO;
import br.com.hrm.entity.Pessoa;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PessoaMapper {

    @Mapping(source = "pessoa.cpf", target = "pessoa")
    public abstract PessoaDTO toDTO(Pessoa pessoa);

    @InheritInverseConfiguration
    public abstract Pessoa toEntity(PessoaDTO pessoaDTO);

}