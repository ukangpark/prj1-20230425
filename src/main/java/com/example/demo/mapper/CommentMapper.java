package com.example.demo.mapper;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.domain.*;

@Mapper
public interface CommentMapper {

	@Select("""
			SELECT *
			FROM Comment
			WHERE boardId = #{boardId}
			ORDER BY id DESC
			""")
	List<Comment> seletAllByBoardId(Integer boardId);

	@Insert("""
			INSERT INTO Comment (boardId, content, memberId)
			VALUES (#{boardId}, #{content}, #{memberId})
			""")
	Integer insert(Comment comment);

	@Delete("""
			DELETE FROM Comment
			WHERE Id = #{commentId}
			""")
	Integer delete(Integer commentId);

	@Select("""
			SELECT *
			FROM Comment
			WHERE Id = #{id}
			""")
	Comment seletById(Integer id);

	@Update("""
			UPDATE Comment
			SET 
				content = #{content}
			WHERE Id = #{id}
			""")
	Integer update(Comment comment);

}
