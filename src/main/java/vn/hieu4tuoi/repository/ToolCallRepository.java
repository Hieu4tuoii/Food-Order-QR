package vn.hieu4tuoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hieu4tuoi.model.ToolCall;

import java.util.List;

public interface ToolCallRepository extends JpaRepository<ToolCall, String> {
}
