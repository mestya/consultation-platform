package com.tju.consultationPlatform.repository;

import com.tju.consultationPlatform.domain.Message;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgRepository extends PagingAndSortingRepository<Message, Integer>,
        JpaSpecificationExecutor<Message> {
    /**
     * - T save(T entity);                  //保存单个实体
     * - T findOne(ID id);                  // 根据id查找实体
     * - void delete(ID/T/Iterable);        // 根据Id删除实体，删除实体，批量删除
     */
    List<Message> findByFrom(String from);

    List<Message> findByTo(String to);

    List<Message> findByType(int type);


    @Query("SELECT m FROM Message m WHERE m.to =?1 AND m.isTransport = ?2")
    List<Message> getMessageUnReceive(String to, int isTransport);

    @Modifying
    @Query("UPDATE Message m SET m.isTransport = :isTransport WHERE m.id = :id")
    int updateMsgFlag(@Param("isTransport") int isTransport, @Param("id") int id);
}
