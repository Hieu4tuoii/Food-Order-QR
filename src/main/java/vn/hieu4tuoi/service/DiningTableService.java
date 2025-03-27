package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.diningtable.DiningTableRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.diningtable.DiningTableResponse;

import java.util.List;

public interface DiningTableService {
    PageResponse<List<DiningTableResponse>> getDiningTableList(String keyword, String sort, int page, int size);
    DiningTableResponse getById(Long id);
    Long save(DiningTableRequest request);
    void update(Long id, DiningTableRequest request);
    void delete(Long id);
}
