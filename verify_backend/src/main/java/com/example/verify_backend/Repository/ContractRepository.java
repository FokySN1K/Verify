package com.example.verify_backend.Repository;

import com.example.verify_backend.Entity.Contract;
import com.example.verify_backend.Enums.XmlStatus;
import com.example.verify_backend.Exception.NoAffectException;
import com.example.verify_backend.Repository.Query.ContractQuery;
import com.example.verify_backend.Repository.RowMapper.ContractRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void createContract(Contract contract) {
        jdbcTemplate.update(ContractQuery.CREATE_CONTRACT.getQuery(),
                new MapSqlParameterSource()
                        .addValue("name", contract.getName(), Types.VARCHAR)
                        .addValue("description", contract.getDescription(), Types.VARCHAR)
                        .addValue("client_id", contract.getCustomer().getClientId(), Types.VARCHAR));
    }

    // Todo здесь валидируемся по контракту
    public List<Contract> getContractList(String clientId) {
        return jdbcTemplate.query(ContractQuery.GET_CONTRACTS.getQuery(),
                new MapSqlParameterSource()
                        .addValue("client_id", clientId, Types.VARCHAR),
                new ContractRowMapper());
    }

    // TODO добавить проверку, что приявязки не существует
    /**
     * Устанавливаем на заказ подрядчика
     *
     * */
    public void setContractorForContract(Long contractId, Long contractorId) {

        int affectRow = jdbcTemplate.update(ContractQuery.SET_CONTRACTOR_FOR_CONTRACT.getQuery(),
                            new MapSqlParameterSource()
                                    .addValue("contract_id", contractId, Types.BIGINT)
                                    .addValue("client_id", contractorId, Types.BIGINT));
        if (affectRow == 0) {
            throw new NoAffectException("Ошибка установки подрядчика заказу");
        }

    }

    public void changeContractStatus(Long contractId, XmlStatus status) {
        int affectRow = jdbcTemplate.update(ContractQuery.CHANGE_CONTRACT_STATUS.getQuery(),
                new MapSqlParameterSource()
                        .addValue("contract_id", contractId, Types.BIGINT)
                        .addValue("status", status, Types.OTHER));

        if (affectRow == 0) {
            throw new NoAffectException("Ошибка при переводе контракта в другой статус");
        }
    }

}
