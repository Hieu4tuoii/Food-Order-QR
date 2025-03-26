package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.diningtable.DiningTableRequest;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.dto.respone.diningtable.DiningTableResponse;
import vn.hieu4tuoi.service.DiningTableService;

@RestController
@RequestMapping("/dining-tables")
@Tag(name = "Dining Table Controller")
@Slf4j(topic = "DINING-TABLE-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class DiningTableController {
    private final DiningTableService diningTableService;

    @Operation(summary = "Get dining table list with filtering and pagination",
            description = "keyword: search term (optional), sort: sorting criteria like 'name:asc' (optional), page (default 1), size (default 10)")
    @GetMapping("/")
    public ResponseData<?> getDiningTableList(@RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestParam(value = "sort", required = false) String sort) {
        log.info("Getting dining table list by keyword {}, sort: {}, page: {}, size: {}", keyword, sort, page, size);
        return new ResponseData<>(HttpStatus.OK.value(), "Get dining table list successfully", 
                diningTableService.getDiningTableList(keyword, sort, page, size));
    }

    @Operation(summary = "Find dining table by id")
    @GetMapping("/{id}")
    public ResponseData<?> getDiningTableById(@PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") Long id) {
        log.info("Getting dining table by id: {}", id);
        DiningTableResponse response = diningTableService.getById(id);
        return ResponseData.<DiningTableResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get dining table successfully")
                .data(response)
                .build();
    }

    @Operation(summary = "Create new dining table")
    @PostMapping("/")
    public ResponseData<?> createDiningTable(@Valid @RequestBody DiningTableRequest request) {
        log.info("Creating dining table with request: {}", request);
        Long tableId = diningTableService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Create dining table successfully", tableId);
    }

    @Operation(summary = "Update dining table")
    @PutMapping("/{id}")
    public ResponseData<?> updateDiningTable(@PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") Long id,
                                             @Valid @RequestBody DiningTableRequest request) {
        log.info("Updating dining table id: {} with request: {}", id, request);
        diningTableService.update(id, request);
        return new ResponseData<>(HttpStatus.OK.value(), "Update dining table successfully", null);
    }

    @Operation(summary = "Delete dining table")
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteDiningTable(@PathVariable @Min(value = 1, message = "ID must be greater than or equal to 1") Long id) {
        log.info("Deleting dining table id: {}", id);
        diningTableService.delete(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Delete dining table successfully", null);
    }
}
