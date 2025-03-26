package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hieu4tuoi.dto.request.diningtable.DiningTableRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.diningtable.DiningTableResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.DiningTable;
import vn.hieu4tuoi.repository.DiningTableRepository;
import vn.hieu4tuoi.service.DiningTableService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "DINING_TABLE_SERVICE")
public class DiningTableServiceImpl implements DiningTableService {
    private final DiningTableRepository diningTableRepository;

    @Override
    public PageResponse getDiningTableList(String keyword, String sort, int page, int size) {
        log.info("Getting dining tables by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columnName = matcher.group(1);
                order = matcher.group(3).equalsIgnoreCase("asc")
                        ? new Sort.Order(Sort.Direction.ASC, columnName)
                        : new Sort.Order(Sort.Direction.DESC, columnName);
            }
        }

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<DiningTable> tablePage;
        if(StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            tablePage = diningTableRepository.searchByKeyword(keyword, pageable);
        }else{
            tablePage = diningTableRepository.findAll(pageable);
        }
        
        List<DiningTableResponse> tableResponses = tablePage.getContent().stream()
                .map(table -> DiningTableResponse.builder()
                        .id(table.getId())
                        .name(table.getName())
                        .build())
                .collect(Collectors.toList());

        log.info("Got dining tables by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        return PageResponse.builder()
                .pageNo(page + 1)
                .pageSize(size)
                .totalPage(tablePage.getTotalPages())
                .items(tableResponses)
                .build();
    }

    @Override
    public DiningTableResponse getById(Long id) {
        log.info("Getting dining table by id {}", id);
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
        
        return DiningTableResponse.builder()
                .id(diningTable.getId())
                .name(diningTable.getName())
                .build();
    }

    @Override
    public Long save(DiningTableRequest request) {
        log.info("Saving dining table with name: {}", request.getName());
        DiningTable diningTable = DiningTable.builder()
                .name(request.getName())
                .build();
                
        diningTableRepository.save(diningTable);
        return diningTable.getId();
    }

    @Override
    public void update(Long id, DiningTableRequest request) {
        log.info("Updating dining table id {} with name: {}", id, request.getName());
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
                
        diningTable.setName(request.getName());
        diningTableRepository.save(diningTable);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting dining table id {}", id);
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
                
        diningTableRepository.delete(diningTable);
    }
}
