package com.example.mdmggreal.ingameinfo.repository;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InGameInfoQueryRepository extends QuerydslRepositorySupport {

    public InGameInfoQueryRepository() {
        super(InGameInfo.class);
    }

}
