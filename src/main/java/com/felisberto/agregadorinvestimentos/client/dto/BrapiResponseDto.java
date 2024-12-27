package com.felisberto.agregadorinvestimentos.client.dto;

import java.util.LinkedList;
import java.util.List;

public record BrapiResponseDto(LinkedList<StockDto> results) {
}
