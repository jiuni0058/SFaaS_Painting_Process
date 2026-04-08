package com.team1.sfaas.mapper;

import com.team1.sfaas.model.Hr;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HrMapper {

    @Select("SELECT name, user_id as id, email FROM HR WHERE user_id = #{id}")
    Hr findById(String id);

    @Select("SELECT name, user_id as id FROM HR WHERE (user_id = #{id} or email = #{id}) and pw = #{pw}")
    Hr findByName(@Param("id") String id, @Param("pw") String pw);

    @Select("SELECT * FROM HR")
    List<Hr> findAll();

    // id 중복 체크
    @Select("SELECT COUNT(*) FROM HR WHERE user_id = #{id}")
    int countById(String id);

    // email 중복 체크
    @Select("SELECT COUNT(*) FROM HR WHERE email = #{email}")
    int countByEmail(String email);

    @Insert("INSERT INTO HR (user_id, name, pw, email) VALUES (#{id}, #{name}, #{pw}, #{email})")
    int insert(Hr hr);

    @Update("UPDATE HR SET name=#{name}, pw=#{pw}, email=#{email} WHERE user_id=#{id}")
    int update(Hr hr);

    @Update("UPDATE HR SET pw=#{pw} WHERE email=#{email}")
    int updatePassword(@Param("email") String email, @Param("pw") String pw);

    @Delete("DELETE FROM HR WHERE id=#{id}")
    int delete(String id);
}