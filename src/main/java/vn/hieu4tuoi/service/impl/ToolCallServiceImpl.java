package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hieu4tuoi.model.ToolCall;
import vn.hieu4tuoi.repository.ToolCallRepository;
import vn.hieu4tuoi.service.ToolCallService;

@Service
@RequiredArgsConstructor
public class ToolCallServiceImpl implements ToolCallService {
    private final ToolCallRepository toolCallRepository;
    @Override
    public String save(ToolCall request) {
        toolCallRepository.save(request);
        return request.getId();
    }
}
