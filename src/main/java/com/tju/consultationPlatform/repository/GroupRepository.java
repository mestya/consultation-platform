package com.tju.consultationPlatform.repository;

import com.tju.consultationPlatform.domain.Group;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GroupRepository extends PagingAndSortingRepository<Group, String>,
        JpaSpecificationExecutor<Group> {
    Group findByGroupId(String id);
    List<Group> findByGroupNameContaining(String name);

    @Modifying
    @Query("UPDATE Group g SET g.groupName = :groupname,g.groupCreaterId =:creator," +
            "g.groupIntroduction=:intro,g.groupUserCount=:cnt WHERE g.groupId = :id")
    int updateGroup(@Param("id") String id, @Param("groupname") String groupname, @Param("creator") String creator,
                    @Param("intro") String intro, @Param("cnt") int cnt);

    @Modifying
    @Query("UPDATE Group g SET g.groupUserCount= ?2 WHERE g.groupId = ?1 ")
    int updateUserCount(String groupId, int groupCount);

    @Query(value = "SELECT g FROM Group g WHERE g.groupName LIKE %?1%")
    List<Group> findGroupsByName(String name);


}
