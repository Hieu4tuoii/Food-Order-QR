package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hieu4tuoi.common.TableStatus;
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
    public List<DiningTableResponse> getDiningTableList(String keyword) {
        log.info("Getting dining tables by keyword {}", keyword);
        List<DiningTable> tableList;
        if(StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            tableList = diningTableRepository.searchByKeyword(keyword);
        }else{
            tableList = diningTableRepository.findAll();
        }

        List<DiningTableResponse> tableResponses = tableList.stream()
                .map(table -> DiningTableResponse.builder()
                        .id(table.getId())
                        .name(table.getName())
                        .status(table.getStatus())
                        .status(table.getStatus())
                        .build())
                .collect(Collectors.toList());

        log.info("Got dining tables by keyword {} ", keyword);
        return tableResponses;
    }

    @Override
    public DiningTableResponse getById(String id) {
        log.info("Getting dining table by id {}", id);
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
        
        return DiningTableResponse.builder()
                .id(diningTable.getId())
                .name(diningTable.getName())
                .build();
    }

    @Override
    public String save(DiningTableRequest request) {
        log.info("Saving dining table with name: {}", request.getName());
        DiningTable diningTable = DiningTable.builder()
                .name(request.getName())
                .build();
                
        diningTableRepository.save(diningTable);
        return diningTable.getId();
    }

    @Override
    public void update(String id, DiningTableRequest request) {
        log.info("Updating dining table id {} with name: {}", id, request.getName());
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
                
        diningTable.setName(request.getName());
        diningTableRepository.save(diningTable);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting dining table id {}", id);
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
                
        diningTableRepository.delete(diningTable);
    }

}
