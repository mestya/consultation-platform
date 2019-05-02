package com.tju.consultationPlatform.repository;

import com.tju.consultationPlatform.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface UserRepository extends PagingAndSortingRepository<User, String>,
        JpaSpecificationExecutor<User> {
    /**
     * CrudRepository 接口提供了最基本的对实体类的添删改查操作
     * - T save(T entity);                  //保存单个实体
     * - T findOne(ID id);                  // 根据id查找实体
     * - void delete(ID/T/Iterable);        // 根据Id删除实体，删除实体，批量删除
     */

    User findByUserId(String userId);

    User findByUsername(String userName);

    List<User> findByUsernameContaining(String name);


    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :id")
    int updateUserPassword(@Param("id") String id, @Param("password") String password);

    @Modifying
    @Query("UPDATE User u SET u.phonenumber = :phonenumber WHERE u.userId = :id")
    int updateUserPhoneNumber(@Param("id") String id, @Param("phonenumber") String phonenumber);

    @Modifying
    @Query("UPDATE User u SET u.avatarUrl=?2 WHERE u.userId=?1")
    int updateAvatarUrl(String id,String avatarUrl);


}
