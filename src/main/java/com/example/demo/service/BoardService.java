package com.example.demo.service;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

import software.amazon.awssdk.core.sync.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

@Service // 서비스일을하는 component다.
// @Component // service객체를 만들어라 => component라고 써도되지만 서비스일을하는 component이기 때문에 명시적으로 알려주는게 좋음 
// 서비스에 작성된 메소드 하나하나가 다 트랜잭션이기 때문에 class레벨로 사용하면 됨
@Transactional(rollbackFor = Exception.class)
public class BoardService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private BoardLikeMapper likeMapper;

	@Autowired
	private S3Client s3;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Autowired
	private BoardMapper mapper;

	public List<Board> listBoard() {
		List<Board> list = mapper.selectAll();
		return list;
	}

	public Board getBoard(Integer id, Authentication auth) {
		Board board = mapper.selectById(id);

		// 현재 로그인한 사람이 이게시물에 좋아요를 했는지? -> auth필요
		if (auth != null) {
			Like like = likeMapper.select(id, auth.getName());
			if (like != null) {
				board.setLiked(true);
			}

		}
		return board;
	}

	public boolean modify(Board board, List<String> removeFileNames, MultipartFile[] addFiles) throws Exception { // 잘수정됬는지
																													// 안됬는지
																													// boolean타입으로
																													// 보겠다

		// FileName 테이블 삭제
		if (removeFileNames != null && !removeFileNames.isEmpty()) {

			for (String fileName : removeFileNames) {
				// aws s3에서 삭제
				String objectKey = "board/" + board.getId() + "/" + fileName;
				DeleteObjectRequest dor = DeleteObjectRequest.builder()
						.bucket(bucketName)
						.key(objectKey)
						.build();

				s3.deleteObject(dor);

				/*
				 * // 하드디스크에서 삭제 String path = "c:/study/upload/" + board.getId() + "/" +
				 * fileName; File file = new File(path); if (file.exists()) { file.delete();
				 */

				// 테이블에서 삭제
				mapper.deleteFileNameByBoardIdAndFileName(board.getId(), fileName);
			}

		}

		// 새 파일 추가
		for (MultipartFile newFile : addFiles) {
			if (newFile.getSize() > 0) {

				// 테이블에 파일명 추가
				mapper.insertFileName(board.getId(), newFile.getOriginalFilename());
				// aws s3에 업로드
				String objectKey = "board/" + board.getId() + "/" + newFile.getOriginalFilename();
				PutObjectRequest por = PutObjectRequest.builder()
						.bucket(bucketName)
						.key(objectKey)
						.acl(ObjectCannedACL.PUBLIC_READ)
						.build();
				RequestBody rb = RequestBody.fromInputStream(newFile.getInputStream(), newFile.getSize());
				s3.putObject(por, rb);

				/*
				 * String fileName = newFile.getOriginalFilename(); String folder =
				 * "c:/study/upload/" + board.getId(); String path = folder + "/" + fileName; //
				 * 디렉토리 없으면 만들기 File dir = new File(folder); if (!dir.exists()) { dir.mkdir(); }
				 * // 파일을 하드디스크에 저장 File file = new File(path); newFile.transferTo(file);
				 */

			}
		}
		// 게시물(Board) 테이블 수정
		int cnt = mapper.update(board);
		return cnt == 1;
	}

	public boolean remove(Integer id) {
		
		// 댓글 테이블 지우기
		commentMapper.deleteByBoardId(id);
		
		// 좋아요 테이블 지우기
		likeMapper.deleteByBoardId(id);
		// 파일명 조회
		List<String> fileNames = mapper.selectFileNameByBoardId(id);
		
		// FileName 테이블의 데이터 지우기
		mapper.deleteFileNameByBoardId(id);

		// s3 bucket의 파일(객체) 지우기
		for (String fileName : fileNames) {
			String objectKey = "board/" + id + "/" + fileName;
			DeleteObjectRequest dor = DeleteObjectRequest.builder()
					.bucket(bucketName)
					.key(objectKey)
					.build();
			s3.deleteObject(dor);
		}

		/*
		 * //하드디스크의 파일 지우기 for (String fileName : fileNames) { String path =
		 * "c:/study/upload/" + id + "/" + fileName; File file = new File(path); if
		 * (file.exists()) { file.delete(); } }
		 */

		// 게시물 테이블의 데이터 지우기
		int cnt = mapper.deleteById(id);
		return cnt == 1;
	}

	public boolean insert(Board board, MultipartFile[] files) throws Exception {
		// 게시물 insert
		int cnt = mapper.insertAll(board);

		for (MultipartFile file : files) {
			if (file.getSize() > 0) {
				String objectKey = "board/" + board.getId() + "/" + file.getOriginalFilename();
				PutObjectRequest por = PutObjectRequest.builder()
						.bucket(bucketName)
						.key(objectKey)
						.acl(ObjectCannedACL.PUBLIC_READ)
						.build();
				RequestBody rb = RequestBody.fromInputStream(file.getInputStream(), file.getSize());

				s3.putObject(por, rb);

				/*
				 * //파일 저장 (파일 시스템에 저장) //폴더 만들기 String folder = "c:/study/upload/" +
				 * board.getId(); File targetFolder = new File(folder); if
				 * (!targetFolder.exists()) { // 없을 때만 폴더 만들기 targetFolder.mkdir(); } String
				 * path = "c:/study/upload/" + board.getId() + "/" + file.getOriginalFilename();
				 * File target = new File(path); file.transferTo(target);
				 */
				// db에 관련 정보 저장 (insert)
				Integer upload = mapper.insertFileName(board.getId(), file.getOriginalFilename());
			}
		}
		return cnt == 1;
	}

	public Map<String, Object> listBoard(Integer page, String search, String type) {
		// 페이지당 행의 개수
		Integer rowPerPage = 10;

		// 페이지당 데이터 개수 구하기
		Integer startIndex = (page - 1) * rowPerPage;

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
		// map으로 담아보자
		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("rightPageNum", rightPageNum);
		pageInfo.put("leftPageNum", leftPageNum);
		pageInfo.put("currentPageNum", page);
		pageInfo.put("lastPageNum", lastPageNumber);

		// 게시물 목록
		List<Board> list = mapper.selectAllPage(startIndex, rowPerPage, search, type);
		return Map.of("pageInfo", pageInfo, "boardList", list);
	}

	public void removeByWriter(String writer) {
		List<Integer> idList = mapper.selectIdByWriter(writer);

		for (Integer id : idList) {
			remove(id);
		}

	}

	public Map<String, Object> like(Like like, Authentication auth) {
		Map<String, Object> result = new HashMap<>();

		result.put("like", false);

		like.setMemberId(auth.getName());
		Integer deleteCnt = likeMapper.delete(like); // 지웠는데 지운게 없으면

		if (deleteCnt != 1) {
			Integer insertCnt = likeMapper.insert(like); // 새로 누른것으로 insert 해라
			result.put("like", true);
		}

		Integer count = likeMapper.countByBoardId(like.getBoardId());
		result.put("count", count);// 좋아요 개수 넘겨줌

		return result;
	}

}
