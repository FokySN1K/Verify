package com.example.verify_backend.dto;

import java.util.List;

public record GetItemsResponse<T>(List<T> items) { }
