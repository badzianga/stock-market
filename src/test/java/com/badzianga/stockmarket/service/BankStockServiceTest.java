package com.badzianga.stockmarket.service;

import com.badzianga.stockmarket.mapper.BankStockMapper;
import com.badzianga.stockmarket.model.dto.BankStockDto;
import com.badzianga.stockmarket.model.entity.BankStock;
import com.badzianga.stockmarket.repository.BankStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankStockServiceTest {

    @Mock
    private BankStockRepository bankStockRepository;

    @InjectMocks
    private BankStockService bankStockService;

    @Test
    void shouldDeleteAllAndSaveNewStocks() {
        // given
        List<BankStockDto> dtos = List.of(
                new BankStockDto("stock1", 99),
                new BankStockDto("stock2", 1)
        );

        BankStock entity1 = new BankStock("stock1", 99);
        BankStock entity2 = new BankStock("stock2", 1);

        try (MockedStatic<BankStockMapper> mapperMock = Mockito.mockStatic(BankStockMapper.class)) {
            mapperMock.when(() -> BankStockMapper.toEntity(dtos.getFirst())).thenReturn(entity1);
            mapperMock.when(() -> BankStockMapper.toEntity(dtos.get(1))).thenReturn(entity2);

            // when
            bankStockService.setStocks(dtos);

            // then
            verify(bankStockRepository).deleteAll();
            verify(bankStockRepository).saveAll(List.of(entity1, entity2));
        }
    }

    @Test
    void shouldReturnAllStocks() {
        // given
        List<BankStock> stocks = List.of(
                new BankStock("stock1", 99),
                new BankStock("stock2", 1)
        );

        when(bankStockRepository.findAll()).thenReturn(stocks);

        // when
        List<BankStock> result = bankStockService.getAllStocks();

        // then
        assertEquals(2, result.size());
        assertEquals(stocks, result);
        verify(bankStockRepository).findAll();
    }
}
