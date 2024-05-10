package com.example.mdmggreal.ingameinfo.repository;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InGameInfoRepository extends JpaRepository<InGameInfo, Long> {

}
