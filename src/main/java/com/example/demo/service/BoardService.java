package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

@Service // 서비스일을하는 component다.
// @Component // service객체를 만들어라 => component라고 써도되지만 서비스일을하는 component이기 때문에 명시적으로 알려주는게 좋음 
public class BoardService {

	@Autowired
	private BoardMapper mapper;
	
	public List<Board> listBoard() {
		List<Board> list = mapper.selectAll();
		return list;
	}

	public Board getBoard(Integer id) {
		// TODO Auto-generated method stub
		return mapper.selectById(id);
	}

	public boolean modify(Board board) { //잘수정됬는지 안됬는지 boolean타입으로 보겠다
		// TODO Auto-generated method stub
		int cnt = mapper.update(board);
		return cnt == 1;
	}

	public boolean remove(Integer id) {
		// TODO Auto-generated method stub
		int cnt = mapper.deleteById(id);
		return cnt == 1;
	}
}
