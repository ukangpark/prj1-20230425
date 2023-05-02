package com.example.demo.service;

import java.io.*;
import java.util.*;

import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

@Service // 서비스일을하는 component다.
// @Component // service객체를 만들어라 => component라고 써도되지만 서비스일을하는 component이기 때문에 명시적으로 알려주는게 좋음 
// 서비스에 작성된 메소드 하나하나가 다 트랜잭션이기 때문에 class레벨로 사용하면 됨
@Transactional (rollbackFor = Exception.class)
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
	
	public boolean insert(Board board, MultipartFile[] files) throws Exception {
		// 게시물 insert
		int cnt = mapper.insertAll(board);
		
		for (MultipartFile file : files) {
			if(file.getSize() > 0) {
				System.out.println(file.getOriginalFilename());
				System.out.println(file.getSize());
		//파일 저장  (파일 시스템에 저장)
		//폴더 만들기 
		String folder = "c:/study/upload/" + board.getId();
		File targetFolder = new File(folder);
		if (!targetFolder.exists()) { // 없을 때만 폴더 만들기
			targetFolder.mkdir();
		}
		String path = "c:/study/upload/" + board.getId() + "/" + file.getOriginalFilename();
		File target = new File(path);
		file.transferTo(target);
		
		// db에 관련 정보 저장 (insert) 
		Integer upload = mapper.insertFileName(board.getId(), file.getOriginalFilename());
			}
		}
		return cnt == 1;
	}

	public Map<String, Object> listBoard
			(Integer page, String search, String type) {
		// 페이지당 행의 개수
		Integer rowPerPage = 10;
		
		//페이지당 데이터 개수 구하기
		Integer startIndex = (page-1) * rowPerPage; 
		
		// 페이지네이션이 필요한 정보
		// 전체레코드수 구하기
		Integer numOfRecords = mapper.countAll(search, type);
		// 마지막페이지 번호 구하기
		Integer lastPageNumber = (numOfRecords - 1) / rowPerPage + 1;
		
		// 페이지네이션 왼쪽번호
		Integer leftPageNum = page - 5;
		// 1보다 작을 수 없음
		leftPageNum = Math.max(leftPageNum, 1);
		
		// 페이지네이션 오른쪽번호
		Integer rightPageNum = leftPageNum + 9;
		// 마지막 페이지보다 클 수 없음
		rightPageNum = Math.min(rightPageNum, lastPageNumber);
		
		// 해당 변수들을 한곳에 담아서 컨트롤러에게 줘야 컨트롤러가 jsp에 건내줌
		//map으로 담아보자
		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("rightPageNum", rightPageNum);
		pageInfo.put("leftPageNum", leftPageNum);
		pageInfo.put("currentPageNum", page);
		pageInfo.put("lastPageNum", lastPageNumber);
		
		// 게시물 목록
		List<Board> list = mapper.selectAllPage(startIndex, rowPerPage, search, type);
		return Map.of("pageInfo", pageInfo, "boardList", list);
	}
}
