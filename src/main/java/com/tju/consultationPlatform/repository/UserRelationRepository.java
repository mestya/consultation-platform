package com.tju.consultationPlatform.repository;


import com.tju.consultationPlatform.domain.UserRelation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRelationRepository extends PagingAndSortingRepository<UserRelation, Integer>,
        JpaSpecificationExecutor<UserRelation> {

    @Query("SELECT rel FROM UserRelation rel WHERE rel.userA = ?1  AND  rel.relationType = ?2")
    List<UserRelation> findRelationsByOwnerId(String id, int relationType);

    @Query("SELECT rel FROM UserRelation rel WHERE rel.userA = ?1  AND rel.userB= ?2 AND  rel.relationType = ?3")
    UserRelation findOne(String Aid, String Bid, int relationType);

    @Modifying
    @Query("DELETE FROM UserRelation rel WHERE rel.userA= ?1 AND rel.userB= ?2 AND rel.relationType=?3")
    int deleteUserRelation(String userA, String userB, int relationType);

}
