package com.ah.dao;

import com.ah.pojo.GameType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameTypeRespository extends JpaRepository<GameType, Integer> {
}
