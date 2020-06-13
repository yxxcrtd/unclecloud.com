package com.unclecloud.repository;

import com.unclecloud.domain.WorksheetReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorksheetReplyRepository extends JpaRepository<WorksheetReply, Long>, JpaSpecificationExecutor<WorksheetReply> {

}
