package com.example.demo.service;

import com.example.demo.entity.Position;
import com.example.demo.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PositionServiceImplTest {

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionServiceImpl positionService;

    private Position position1;
    private Position position2;

    @BeforeEach
    public void setUp() {
        position1 = new Position();
        position1.setId(1L);
        position1.setName("Position 1");

        position2 = new Position();
        position2.setId(2L);
        position2.setName("Position 2");
    }

    @Test
    public void testFindAll() {
        when(positionRepository.findAll()).thenReturn(Arrays.asList(position1, position2));

        List<Position> result = positionService.findAll();

        assertEquals(2, result.size());
        assertEquals("Position 1", result.getFirst().getName());
        assertEquals("Position 2", result.get(1).getName());
    }
}
