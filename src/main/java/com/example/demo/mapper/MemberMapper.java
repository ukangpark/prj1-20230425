package com.example.demo.mapper;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.domain.*;

@Mapper
public interface MemberMapper {

	@Insert("""
			INSERT INTO Member (id, password, nickName, email)
			VALUES (#{id}, #{password}, #{nickName} ,#{email})
			""")
	int insert(Member member);

	@Select("""
			SELECT * FROM Member
			ORDER BY Id DESC
			""")
	List<Member> selectAll();

	@Select("""
			SELECT * FROM Member
			WHERE Id = #{id}
			""")
	Member selectById(String id);

	@Delete("""
			DELETE FROM Member
			WHERE Id = #{id}
			""")
	Integer removeById(String id);

	@Update("""
			<script>
			
			UPDATE Member
			SET 
				<if test="password neq null and password neq ''">
				password = #{password},
				</if>
				
				nickName = #{nickName},
				email = #{email}
			WHERE Id = #{id}
			
			</script>
			""")
	Integer update(Member member);

	
}
