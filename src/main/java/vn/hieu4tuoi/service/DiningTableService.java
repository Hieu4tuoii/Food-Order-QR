package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.diningtable.DiningTableRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.diningtable.DiningTableResponse;

import java.util.List;

public interface DiningTableService {
    List<DiningTableResponse> getDiningTableList(String keyword) ;
    DiningTableResponse getById(Long id);
    Long save(DiningTableRequest request);
    void update(Long id, DiningTableRequest request);
    void delete(Long id);
}
